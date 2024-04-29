package com.roguelikedeckbuilder.mygame.combat.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffect;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffectTypeName;
import com.roguelikedeckbuilder.mygame.helpers.AudioManager;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;

import java.util.Objects;

public class EffectData {
    private static Array<IndividualEffectData> data;

    public static void initialize() {
        data = new Array<>();

        for (EffectName name : EffectName.values()) {
            data.add(new IndividualEffectData(name));
        }
    }

    public static EffectType getEffectType(EffectName effectName) {
        return data.get(effectName.ordinal()).getEffectType();
    }

    public static int getEffectiveness(EffectName effectName) {
        return data.get(effectName.ordinal()).getEffectiveness();
    }

    public static int getRepetitions(EffectName effectName) {
        return data.get(effectName.ordinal()).getRepetitions();
    }

    public static TargetType getTargetType(EffectName effectName) {
        return data.get(effectName.ordinal()).getTargetType();
    }

    public static ItemTypeName getTemporaryItem(EffectName effectName) {
        return data.get(effectName.ordinal()).getTemporaryItem();
    }

    public static boolean isSingleUseItem(EffectName effectName) {
        return data.get(effectName.ordinal()).isSingleUseItem();
    }

    public static void useEffect(CombatInformation theAttacker, EffectName effectName, Array<CombatInformation> targets) {
        EffectType effectType = getEffectType(effectName);

        if (effectType == EffectType.NOTHING) {
            return;
        }

        int repetitions = getRepetitions(effectName);
        int effectiveness = getEffectiveness(effectName);

        boolean stopEarly = false;

        for (CombatInformation combatInformation : targets) {
            for (int i = 0; i < repetitions; i++) {
                switch (effectType) {
                    case ATTACK -> {
                        int strengthAmount = theAttacker.getStatusEffectValue(StatusEffectTypeName.STRENGTH);
                        double totalDamage = effectiveness + strengthAmount;

                        if (theAttacker.getStatusEffectValue(StatusEffectTypeName.WEAKNESS) > 0) {
                            totalDamage *= 0.75;
                        }

                        stopEarly = combatInformation.takeDamage(totalDamage, false);
                        AudioManager.playHitSound();
                    }
                    case BURNING ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.BURNING, effectiveness));
                    case CONSTITUTION ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.CONSTITUTION, effectiveness));
                    case PRE_CURE ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.PRE_CURE, effectiveness));
                    case DEFEND -> {
                        int constitutionAmount = theAttacker.getStatusEffectValue(StatusEffectTypeName.CONSTITUTION);
                        combatInformation.grantDefense(effectiveness + constitutionAmount);
                        AudioManager.playDefendSound();
                    }
                    case DISCARD_RANDOM_CARD -> Player.discardOneRandomCard();
                    case DRAW_CARD -> Player.drawCards(effectiveness);
                    case GAIN_ENERGY -> Player.setEnergy(Player.getEnergy() + 1);
                    case GOLD_CHANGE -> Player.changeMoney(effectiveness);
                    case HEAL -> {
                        combatInformation.changeHp(effectiveness);
                        AudioManager.playHealSound();
                    }
                    case MAX_HP_CHANGE -> combatInformation.changeMaxHp(effectiveness);
                    case POISON ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.POISON, effectiveness));
                    case STRENGTH ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.STRENGTH, effectiveness * repetitions));
                    case TEMPORARY_ITEM ->
                            combatInformation.obtainTemporaryItem(getTemporaryItem(effectName), isSingleUseItem(effectName));
                    case TRUE_DAMAGE_FLAT -> {
                        double totalDamage = effectiveness;

                        if (theAttacker.getStatusEffectValue(StatusEffectTypeName.WEAKNESS) > 0) {
                            totalDamage *= 0.75;
                        }

                        combatInformation.takeDamage(totalDamage, true);
                        AudioManager.playHitSound();
                    }
                    case TRUE_DAMAGE_PERCENT -> {
                        if (theAttacker.getStatusEffectValue(StatusEffectTypeName.WEAKNESS) > 0) {
                            double newEffectiveness = effectiveness * 0.75;
                            effectiveness = (int) Math.round(newEffectiveness);
                        }

                        float percent = (float) effectiveness / 100;
                        double change = combatInformation.getMaxHp() * percent;

                        combatInformation.takeDamage(change, true);
                        AudioManager.playHitSound();
                    }
                    case VULNERABILITY ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.VULNERABILITY, effectiveness));
                    case WEAKNESS ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.WEAKNESS, effectiveness));
                }

                if (stopEarly) {
                    break;
                }
            }
        }
    }

    public static Image getIntentIcon(EffectName effectName) {
        EffectType effectType = getEffectType(effectName);
        String fileName = "";

        // Not all types have a unique icon

        switch (effectType) {
            case ATTACK -> fileName = "attack";
            case BURNING, POISON, VULNERABILITY, WEAKNESS -> fileName = "debuff";
            case CONSTITUTION, STRENGTH, PRE_CURE -> fileName = "buff";
            case DEFEND -> fileName = "defend";
            case DRAW_CARD, DISCARD_RANDOM_CARD, GAIN_ENERGY, GOLD_CHANGE, MAX_HP_CHANGE, NOTHING, TEMPORARY_ITEM ->
                    fileName = "other";
            case HEAL -> fileName = "heal";
            case TRUE_DAMAGE_FLAT, TRUE_DAMAGE_PERCENT -> fileName = "true";
        }

        String filePath = "ABILITIES/INTENT ICONS/" + fileName + ".png";
        return new Image(new Texture(Gdx.files.internal(filePath)));
    }

    public static String prepareOneEffectDescription(EffectName effectName) {
        //"Deals [RED]1 Damage[] to an enemy [CYAN]4 times[]."
        EffectType effectType = EffectData.getEffectType(effectName);
        int effectiveness = EffectData.getEffectiveness(effectName);

        String effectText;
        switch (effectType) {
            case ATTACK -> effectText = String.format("Deal [RED]%d Damage[]", effectiveness);
            case BURNING -> effectText = String.format("Inflict [ORANGE]%d Burning[]", effectiveness);
            case CONSTITUTION -> effectText = String.format("Grant [YELLOW]%d Constitution[]", effectiveness);
            case PRE_CURE -> effectText = String.format("Grant [YELLOW]%d Pre-Cure[]", effectiveness);
            case DEFEND -> effectText = String.format("Grant [YELLOW]%d Defense[]", effectiveness);
            case DISCARD_RANDOM_CARD -> {
                String singularOrPlural = "cards";
                if (effectiveness == 1) {
                    singularOrPlural = "card";
                }
                return String.format("[RED]Discard[] up to %d random %s from your hand. ", effectiveness, singularOrPlural);
            }
            case DRAW_CARD -> {
                String singularOrPlural = "cards";
                if (effectiveness == 1) {
                    singularOrPlural = "card";
                }
                return String.format("[YELLOW]Draw[] %d %s. ", effectiveness, singularOrPlural);
            }
            case GAIN_ENERGY -> {
                return String.format("Gain [YELLOW]%d Energy[]. ", effectiveness);
            }
            case GOLD_CHANGE -> {
                return String.format("Gain [ORANGE]%d Gold[]. ", effectiveness);
            }
            case HEAL -> effectText = String.format("Grant [GREEN]%d Immediate HP Recovery[]", effectiveness);
            case MAX_HP_CHANGE -> effectText = String.format("Permanently grant [RED]%d Max HP[]", effectiveness);
            case NOTHING -> effectText = "Do nothing";
            case POISON -> effectText = String.format("Inflict [ORANGE]%d Poison[]", effectiveness);
            case STRENGTH -> effectText = String.format("Grant [YELLOW]%d Strength[]", effectiveness);
            case TEMPORARY_ITEM -> {
                boolean isSingleUse = isSingleUseItem(effectName);
                if (isSingleUse) {
                    return String.format("Buff lasts [PURPLE]1 activation[]: %s%s",
                            ItemData.getEffectExplanation(getTemporaryItem(effectName)),
                            ItemData.getTriggerExplanation(getTemporaryItem(effectName)));
                } else {
                    return String.format("Buff lasts [PURPLE]the entire combat[]: %s%s",
                            ItemData.getEffectExplanation(getTemporaryItem(effectName)),
                            ItemData.getTriggerExplanation(getTemporaryItem(effectName)));
                }
            }
            case TRUE_DAMAGE_FLAT ->
                    effectText = String.format("Ignore defense to deal [RED]%d Damage[]", effectiveness);
            case TRUE_DAMAGE_PERCENT ->
                    effectText = String.format("Ignore defense to deal [RED]%d%% Damage[]", effectiveness);
            case VULNERABILITY -> effectText = String.format("Inflict [ORANGE]%d Vulnerability[]", effectiveness);
            case WEAKNESS -> effectText = String.format("Inflict [ORANGE]%d Weakness[]", effectiveness);
            default ->
                    throw new IllegalStateException("Unexpected value for effectType in getDescription(): " + effectType);
        }

        TargetType targetType = EffectData.getTargetType(effectName);
        String targetTypeText;
        switch (targetType) {
            case ONE -> targetTypeText = "to an enemy";
            case ALL -> targetTypeText = "to [LIME]all[] enemies";
            case SELF -> targetTypeText = "to yourself";
            default ->
                    throw new IllegalStateException("Unexpected value for hitType in getDescription(): " + targetType);
        }

        int repetitions = EffectData.getRepetitions(effectName);
        String repetitionsText;
        if (repetitions <= 1) {
            repetitionsText = "";
        } else {
            repetitionsText = String.format("[CYAN]%d times[]", repetitions);
        }

        if (Objects.equals(repetitionsText, "")) {
            return String.format("%s %s. ", effectText, targetTypeText);
        } else {
            return String.format("%s %s %s. ", effectText, targetTypeText, repetitionsText);
        }
    }

    private static class IndividualEffectData {
        private EffectType effectType;
        private int effectiveness;
        private int repetitions;
        private TargetType targetType;
        private ItemTypeName temporaryItem;
        private boolean isSingleUseItem;

        public IndividualEffectData(EffectName effectName) {
            switch (effectName) {
                case NOTHING -> {
                    effectType = EffectType.NOTHING;
                    effectiveness = 0;
                    repetitions = 0;
                    targetType = TargetType.SELF;
                }
                case DAMAGE_A_BIT_TO_ONE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 9;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case DAMAGE_MANY_TIMES -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 8;
                    targetType = TargetType.ALL;
                }
                case HEAL_MODERATE_ENEMY, ENEMY_KING_OF_THE_BURROW_HEAL -> {
                    effectType = EffectType.HEAL;
                    effectiveness = 15;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case HIGH_DAMAGE_ONCE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 20;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case CONSTITUTION_ONE, ENEMY_KING_OF_THE_BURROW_CONSTITUTION, ENEMY_SAD_DOLLAR_CONSTITUTION_SELF -> {
                    effectType = EffectType.CONSTITUTION;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case DAMAGE_ALL_VERY_SMALL -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case DAMAGE_A_BIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 9;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case DEFEND_SOME, ENEMY_HAM_AND_FIST_DEFEND, ENEMY_STARER_DEFEND_SELF -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 5;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case DEFEND_TWICE_A_BIT -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 4;
                    repetitions = 2;
                    targetType = TargetType.SELF;
                }
                case DISCARD_RANDOM_CARD_ONE -> {
                    effectType = EffectType.DISCARD_RANDOM_CARD;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case DRAW_CARD_ONE -> {
                    effectType = EffectType.DRAW_CARD;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_ALIEN_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 3;
                    repetitions = 3;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ALIEN_BURNING -> {
                    effectType = EffectType.BURNING;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ANTEATER_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_ANTEATER_HALF_HEALTH_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 3;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ANTEATER_HIT_ONCE, ENEMY_BURGER_SMALLER_HIT, ENEMY_CHIPS_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ANTEATER_HIT_TWICE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 2;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ANTEATER_INITIAL_POISON_TWO, ENEMY_PEANUT_BEE_POISON_TWICE, ENEMY_SAD_DOLLAR_POISON -> {
                    effectType = EffectType.POISON;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ANTEATER_POISON_ONE, ENEMY_PEANUT_BEE_POISON_ONCE -> {
                    effectType = EffectType.POISON;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ANTEATER_POISON_THREE, ENEMY_SOCK_POISON -> {
                    effectType = EffectType.POISON;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_ANTEATER_WEAKNESS, ENEMY_HELMET_PENGUIN_WEAKNESS -> {
                    effectType = EffectType.WEAKNESS;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_BURGER_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 8;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_BURGER_LARGER_HIT, ENEMY_SWORD_FISH_HIT_SMALL -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 6;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_CHIPS_WEAKNESS -> {
                    effectType = EffectType.WEAKNESS;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_CHIPS_STRENGTH_TEAM, ENEMY_HOT_DOG_STRENGTH_TEAM, ENEMY_UNIMPRESSED_FISH_STRENGTH_TEAM -> {
                    effectType = EffectType.STRENGTH;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_EVIL_HH_HEAL, ENEMY_HAM_SHAMWITCH_HEAL_TEAM -> {
                    effectType = EffectType.HEAL;
                    effectiveness = 4;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_EVIL_HH_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_EVIL_HH_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 4;
                    targetType = TargetType.ONE;
                }
                case ENEMY_EVIL_HH_INITIAL_POISON -> {
                    effectType = EffectType.POISON;
                    effectiveness = 4;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_EVIL_HH_INITIAL_CONSTITUTION -> {
                    effectType = EffectType.CONSTITUTION;
                    effectiveness = 7;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_EVIL_HH_INITIAL_STRENGTH, ENEMY_HELMET_PENGUIN_STRENGTH -> {
                    effectType = EffectType.STRENGTH;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_HAMMIE_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_HAMMIE_DOUBLE_HIT, ENEMY_PEANUT_BEE_HIT_TWICE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 2;
                    repetitions = 2;
                    targetType = TargetType.ONE;
                }
                case ENEMY_HAMMIE_SINGLE_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_HAM_AND_FIST_DOUBLE_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 3;
                    repetitions = 2;
                    targetType = TargetType.ONE;
                }
                case ENEMY_HAM_AND_FIST_WEAKNESS_SELF -> {
                    effectType = EffectType.WEAKNESS;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_HAM_SHAMWITCH_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 6;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_HAM_SHAMWITCH_HIT, ENEMY_HELMET_PENGUIN_SMALLER_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 5;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_HELMET_PENGUIN_CONSTITUTION_TEAM, ENEMY_MONOLITH_CONSTITUTION, ENEMY_SAD_DOLLAR_CONSTITUTION_TEAM -> {
                    effectType = EffectType.CONSTITUTION;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_HELMET_PENGUIN_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 9;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_HELMET_PENGUIN_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 7;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_HELMET_PENGUIN_VULNERABILITY, ENEMY_SWORD_FISH_VULNERABILITY -> {
                    effectType = EffectType.VULNERABILITY;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_HOT_DOG_VULNERABILITY_TEAM -> {
                    effectType = EffectType.VULNERABILITY;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_KING_OF_THE_BURROW_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 7;
                    repetitions = 3;
                    targetType = TargetType.SELF;
                }
                case ENEMY_KING_OF_THE_BURROW_HIT_EIGHT_TIMES -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 8;
                    targetType = TargetType.ONE;
                }
                case ENEMY_KING_OF_THE_BURROW_HIT_FOUR_TIMES -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 6;
                    repetitions = 4;
                    targetType = TargetType.ONE;
                }
                case ENEMY_KING_OF_THE_BURROW_VULNERABILITY -> {
                    effectType = EffectType.VULNERABILITY;
                    effectiveness = 99;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_KING_OF_THE_BURROW_MORE_STRENGTH -> {
                    effectType = EffectType.STRENGTH;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_KING_OF_THE_BURROW_MORE_CONSTITUTION -> {
                    effectType = EffectType.CONSTITUTION;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_KNIGHT_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 9;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_KNIGHT_HIT, ENEMY_POINTER_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 8;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_KNIGHT_HIT_LARGE, ENEMY_POINTER_INITIAL -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 12;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_MONOLITH_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 2;
                    repetitions = 4;
                    targetType = TargetType.ALL;
                }
                case ENEMY_MONOLITH_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 30;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_MONOLITH_TRUE_HIT -> {
                    effectType = EffectType.TRUE_DAMAGE_FLAT;
                    effectiveness = 5;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PEANUT_BEE_HIT_ONCE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PEANUT_BEE_WEAKNESS -> {
                    effectType = EffectType.WEAKNESS;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PEANUT_BEE_VULNERABILITY -> {
                    effectType = EffectType.VULNERABILITY;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_POINTER_TRUE_HIT -> {
                    effectType = EffectType.TRUE_DAMAGE_FLAT;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_BIG_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 45;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_BIG_HEAL -> {
                    effectType = EffectType.HEAL;
                    effectiveness = 45;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_PUFF_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 72;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_PUFF_HEAL_BIGGER_THAN_GIGANTIC -> {
                    effectType = EffectType.HEAL;
                    effectiveness = 72;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_PUFF_GIGANTIC_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 60;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_GIGANTIC_HEAL -> {
                    effectType = EffectType.HEAL;
                    effectiveness = 60;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_PUFF_HALF_HEALTH_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 200;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_HALF_HEALTH_CONSTITUTION -> {
                    effectType = EffectType.CONSTITUTION;
                    effectiveness = 50;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_PUFF_HALF_HEALTH_STRENGTH -> {
                    effectType = EffectType.STRENGTH;
                    effectiveness = 50;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case ENEMY_PUFF_INITIAL -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 80;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_POISON -> {
                    effectType = EffectType.POISON;
                    effectiveness = 10;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_BURNING -> {
                    effectType = EffectType.BURNING;
                    effectiveness = 10;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_TRUE_DAMAGE -> {
                    effectType = EffectType.TRUE_DAMAGE_FLAT;
                    effectiveness = 24;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_VULNERABILITY -> {
                    effectType = EffectType.VULNERABILITY;
                    effectiveness = 10;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_PUFF_WEAKNESS -> {
                    effectType = EffectType.WEAKNESS;
                    effectiveness = 10;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_SAD_DOLLAR_DEFEND -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 2;
                    repetitions = 4;
                    targetType = TargetType.SELF;
                }
                case ENEMY_SAD_DOLLAR_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 5;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_SOCK_DEFEND_TEAM -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 8;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_STARER_DEFEND_TEAM -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 3;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case ENEMY_SWORD_FISH_HIT_LARGE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 8;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case ENEMY_UNIMPRESSED_FISH_HIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 5;
                    targetType = TargetType.ONE;
                }
                case GAIN_ENERGY_ONE -> {
                    effectType = EffectType.GAIN_ENERGY;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case GOLD_BONUS_BIG -> {
                    effectType = EffectType.GOLD_CHANGE;
                    effectiveness = 30;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case HEAL_SMALL -> {
                    effectType = EffectType.HEAL;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case MAX_HP_CHANGE_DOWN_ONE -> {
                    effectType = EffectType.MAX_HP_CHANGE;
                    effectiveness = -1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case MAX_HP_CHANGE_UP_ONE -> {
                    effectType = EffectType.MAX_HP_CHANGE;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case STRENGTH_ONE, ENEMY_KING_OF_THE_BURROW_STRENGTH -> {
                    effectType = EffectType.STRENGTH;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case TRUE_DAMAGE_FLAT_MODERATE -> {
                    effectType = EffectType.TRUE_DAMAGE_FLAT;
                    effectiveness = 12;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case TRUE_DAMAGE_ONE_SMALL -> {
                    effectType = EffectType.TRUE_DAMAGE_FLAT;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case TRUE_DAMAGE_PERCENT_SMALL -> {
                    effectType = EffectType.TRUE_DAMAGE_PERCENT;
                    effectiveness = 5;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case TRUE_DAMAGE_PERCENT_A_LITTLE_MORE -> {
                    effectType = EffectType.TRUE_DAMAGE_PERCENT;
                    effectiveness = 8;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case DEFEND_AT_END_OF_TURN -> {
                    effectType = EffectType.TEMPORARY_ITEM;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                    temporaryItem = ItemTypeName.FOR_CARD_DEFENSE_EVERY_TURN;
                    isSingleUseItem = false;
                }
                case ATTACK_AT_END_OF_TURN -> {
                    effectType = EffectType.TEMPORARY_ITEM;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                    temporaryItem = ItemTypeName.FOR_CARD_DAMAGE_EVERY_TURN;
                    isSingleUseItem = false;
                }
                case ATTACK_ON_DRAW -> {
                    effectType = EffectType.TEMPORARY_ITEM;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                    temporaryItem = ItemTypeName.FOR_CARD_DAMAGE_ON_DRAW;
                    isSingleUseItem = false;
                }
                case PRE_CURE_ONE -> {
                    effectType = EffectType.PRE_CURE;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case STRENGTH_AT_START_OF_TURN -> {
                    effectType = EffectType.TEMPORARY_ITEM;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                    temporaryItem = ItemTypeName.FOR_CARD_STRENGTH_EVERY_TURN;
                    isSingleUseItem = false;
                }
                case DRAW_CARD_AT_START_OF_TURN -> {
                    effectType = EffectType.TEMPORARY_ITEM;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                    temporaryItem = ItemTypeName.FOR_CARD_DRAW_EVERY_TURN;
                    isSingleUseItem = false;
                }
                case DEFEND_TWO -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 2;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
            }
        }

        public EffectType getEffectType() {
            return effectType;
        }

        public int getEffectiveness() {
            return effectiveness;
        }

        public int getRepetitions() {
            return repetitions;
        }

        public TargetType getTargetType() {
            return targetType;
        }

        public ItemTypeName getTemporaryItem() {
            return temporaryItem;
        }

        public boolean isSingleUseItem() {
            return isSingleUseItem;
        }
    }
}
