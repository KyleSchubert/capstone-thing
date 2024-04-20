package com.roguelikedeckbuilder.mygame.combat.ability;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectData;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectName;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectType;

import java.util.Objects;

public class AbilityData {
    private static Array<IndividualAbilityData> data;

    public static void initialize() {
        data = new Array<>();

        for (AbilityTypeName name : AbilityTypeName.values()) {
            data.add(new IndividualAbilityData(name));
        }
    }

    public static String getName(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getName();
    }

    public static int getEnergyCost(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getEnergyCost();
    }

    public static TargetType getTargetType(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getTargetType();
    }

    public static EffectName getPreEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getPreEffect();
    }

    public static EffectName getEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getEffect();
    }

    public static EffectName getPostEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getPostEffect();
    }

    public static String getDescription(AbilityTypeName typeName) {
        String preEffectText = "";
        String effectText = "";
        String postEffectText = "";

        EffectName preEffect = getPreEffect(typeName);
        if (preEffect != EffectName.NOTHING) {
            preEffectText = prepareOneEffectDescription(typeName, preEffect);
        }

        EffectName effect = getEffect(typeName);
        if (effect != EffectName.NOTHING) {
            effectText = prepareOneEffectDescription(typeName, effect);
        }

        EffectName postEffect = getPostEffect(typeName);
        if (postEffect != EffectName.NOTHING) {
            postEffectText = prepareOneEffectDescription(typeName, postEffect);
        }

        return String.join("", preEffectText, effectText, postEffectText);
    }

    private static String prepareOneEffectDescription(AbilityTypeName typeName, EffectName effectName) {
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

        TargetType targetType = getTargetType(typeName);
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

    public static void useAbility(CombatInformation theAttacker, AbilityTypeName abilityTypeName, Array<CombatInformation> targets) {
        EffectData.useEffect(theAttacker, AbilityData.getPreEffect(abilityTypeName), targets);
        EffectData.useEffect(theAttacker, AbilityData.getEffect(abilityTypeName), targets);
        EffectData.useEffect(theAttacker, AbilityData.getPostEffect(abilityTypeName), targets);
    }

    private static class IndividualAbilityData {
        private String name;
        private int energyCost;
        private TargetType targetType;
        private EffectName preEffect;
        private EffectName effect;
        private EffectName postEffect;

        public IndividualAbilityData(AbilityTypeName abilityTypeName) {
            preEffect = EffectName.NOTHING;
            postEffect = EffectName.NOTHING;

            switch (abilityTypeName) {
                case DISCARD_DRAW -> {
                    name = "Risky Draw";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    preEffect = EffectName.DISCARD_RANDOM_CARD_ONE;
                    effect = EffectName.DRAW_CARD_ONE;
                    postEffect = EffectName.DRAW_CARD_ONE;
                }
                case DRAW -> {
                    name = "You Draw 2 Cards.";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    preEffect = EffectName.DRAW_CARD_ONE;
                    effect = EffectName.DRAW_CARD_ONE;
                }
                case ENERGY_SLICES -> {
                    name = "Energy Slices";
                    energyCost = 2;
                    targetType = TargetType.ALL;
                    effect = EffectName.DAMAGE_MANY_TIMES;
                }
                case ENERGY_SLICES_UPGRADED -> {
                    name = "Energy Slices+";
                    energyCost = 1;
                    targetType = TargetType.ALL;
                    effect = EffectName.DAMAGE_MANY_TIMES;
                }
                case FLAME -> {
                    name = "Flame";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                }
                case FLAME_UPGRADED -> {
                    name = "Double Flame";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectName.DAMAGE_MANY_TIMES;
                    postEffect = EffectName.DAMAGE_A_BIT;
                }
                case FIRE_STRIKE -> {
                    name = "Fire Strike";
                    energyCost = 1;
                    targetType = TargetType.ONE;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                }
                case FIRE_STRIKE_UPGRADED -> {
                    name = "Fire Strike+";
                    energyCost = 1;
                    targetType = TargetType.ONE;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                    postEffect = EffectName.DEFEND_SOME;
                }
                case AMPLIFY -> {
                    name = "Amplify";
                    energyCost = 3;
                    targetType = TargetType.SELF;
                    preEffect = EffectName.STRENGTH_ONE;
                    effect = EffectName.CONSTITUTION_ONE;
                }
                case AMPLIFY_UPGRADED -> {
                    name = "Amplify+";
                    energyCost = 2;
                    targetType = TargetType.SELF;
                    preEffect = EffectName.STRENGTH_ONE;
                    effect = EffectName.CONSTITUTION_ONE;
                }
                case DEFEND -> {
                    name = "Defend";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectName.DEFEND_SOME;
                }
                case DEFEND_UPGRADED -> {
                    name = "Defend+";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectName.DEFEND_TWICE_A_BIT;
                }
                case ITEM_SWORD_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    targetType = TargetType.ALL;
                    effect = EffectName.DAMAGE_A_BIT;
                }
                case ITEM_SWORD_2_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    targetType = TargetType.ONE;
                    effect = EffectName.DAMAGE_A_BIT;
                }
                case ITEM_SHIELD_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    targetType = TargetType.SELF;
                    effect = EffectName.DEFEND_SOME;
                    postEffect = EffectName.HEAL_SMALL;
                }
                case NOTHING -> {
                    name = "Sold";
                    energyCost = 0;
                    targetType = TargetType.SELF;
                    effect = EffectName.NOTHING;
                }
                case PERCENTAGE_PUNCH -> {
                    name = "% Punch";
                    energyCost = 1;
                    targetType = TargetType.ONE;
                    effect = EffectName.TRUE_DAMAGE_PERCENT_SMALL;
                }
                case PERCENTAGE_PUNCH_UPGRADED -> {
                    name = "% Punch+";
                    energyCost = 1;
                    targetType = TargetType.ONE;
                    effect = EffectName.TRUE_DAMAGE_PERCENT_A_LITTLE_MORE;
                }
                default ->
                        System.out.println("Why was an ability almost generated with no matching type name? abilityTypeName:  " + abilityTypeName);
            }
        }

        public String getName() {
            return name;
        }

        public int getEnergyCost() {
            return energyCost;
        }

        public TargetType getTargetType() {
            return targetType;
        }

        public EffectName getPreEffect() {
            return preEffect;
        }

        public EffectName getEffect() {
            return effect;
        }

        public EffectName getPostEffect() {
            return postEffect;
        }
    }
}
