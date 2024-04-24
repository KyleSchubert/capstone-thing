package com.roguelikedeckbuilder.mygame.combat.effect;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffect;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffectTypeName;
import com.roguelikedeckbuilder.mygame.helpers.AudioManager;

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
                        stopEarly = combatInformation.takeDamage(effectiveness + strengthAmount, false);
                        AudioManager.playHitSound();
                    }
                    case CONSTITUTION ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.CONSTITUTION, effectiveness));
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
                    case STRENGTH ->
                            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.STRENGTH, effectiveness * repetitions));
                    case TRUE_DAMAGE_FLAT -> {
                        combatInformation.takeDamage(effectiveness, true);
                        AudioManager.playHitSound();
                    }
                    case TRUE_DAMAGE_PERCENT -> {
                        float percent = (float) effectiveness / 100;
                        int change = Math.round(combatInformation.getMaxHp() * percent);
                        combatInformation.takeDamage(change, true);
                        AudioManager.playHitSound();
                    }
                }

                if (stopEarly) {
                    break;
                }
            }
        }
    }

    public static String prepareOneEffectDescription(EffectName effectName) {
        //"Deals [RED]1 Damage[] to an enemy [CYAN]4 times[]."
        EffectType effectType = EffectData.getEffectType(effectName);
        int effectiveness = EffectData.getEffectiveness(effectName);

        String effectText;
        switch (effectType) {
            case ATTACK -> effectText = String.format("Deal [RED]%d Damage[]", effectiveness);
            case CONSTITUTION -> effectText = String.format("Grant [YELLOW]%d Constitution[]", effectiveness);
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
            case STRENGTH -> effectText = String.format("Grant [YELLOW]%d Strength[]", effectiveness);
            case TRUE_DAMAGE_FLAT ->
                    effectText = String.format("Ignore defense to deal [RED]%d Damage[]", effectiveness);
            case TRUE_DAMAGE_PERCENT ->
                    effectText = String.format("Ignore defense to deal [RED]%d%% Damage[]", effectiveness);
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
                case HIGH_DAMAGE_ONCE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 20;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                }
                case CONSTITUTION_ONE -> {
                    effectType = EffectType.CONSTITUTION;
                    effectiveness = 1;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                }
                case DAMAGE_A_BIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 9;
                    repetitions = 1;
                    targetType = TargetType.ALL;
                }
                case DEFEND_SOME -> {
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
                case STRENGTH_ONE -> {
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
    }
}
