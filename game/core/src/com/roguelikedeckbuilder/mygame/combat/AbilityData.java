package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;

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

    public static EffectData.EffectName getPreEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getPreEffect();
    }

    public static EffectData.EffectName getEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getEffect();
    }

    public static EffectData.EffectName getPostEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getPostEffect();
    }

    public static String getDescription(AbilityTypeName typeName) {
        String preEffectText = "";
        String effectText = "";
        String postEffectText = "";

        EffectData.EffectName preEffect = getPreEffect(typeName);
        if (preEffect != EffectData.EffectName.NOTHING) {
            preEffectText = prepareOneEffectDescription(typeName, preEffect);
        }

        EffectData.EffectName effect = getEffect(typeName);
        if (effect != EffectData.EffectName.NOTHING) {
            effectText = prepareOneEffectDescription(typeName, effect);
        }

        EffectData.EffectName postEffect = getPostEffect(typeName);
        if (postEffect != EffectData.EffectName.NOTHING) {
            postEffectText = prepareOneEffectDescription(typeName, postEffect);
        }

        return String.join("", preEffectText, effectText, postEffectText);
    }

    private static String prepareOneEffectDescription(AbilityTypeName typeName, EffectData.EffectName effectName) {
        //"Deals [RED]1 Damage[] to an enemy [CYAN]4 times[]."
        EffectData.EffectType effectType = EffectData.getEffectType(effectName);
        int effectiveness = EffectData.getEffectiveness(effectName);

        String effectText;
        switch (effectType) {
            case ATTACK -> effectText = String.format("Deals [RED]%d Damage[]", effectiveness);
            case DEFEND -> effectText = String.format("Grants [YELLOW]%d Defense[]", effectiveness);
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
        if (repetitions == 1) {
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

    public static void useAbility(AbilityTypeName abilityTypeName, Array<CombatInformation> targets) {
        EffectData.useEffect(AbilityData.getPreEffect(abilityTypeName), targets);
        EffectData.useEffect(AbilityData.getEffect(abilityTypeName), targets);
        EffectData.useEffect(AbilityData.getPostEffect(abilityTypeName), targets);
    }

    private static class IndividualAbilityData {
        private String name;
        private int energyCost;
        private TargetType targetType;
        private EffectData.EffectName preEffect;
        private EffectData.EffectName effect;
        private EffectData.EffectName postEffect;

        public IndividualAbilityData(AbilityTypeName abilityTypeName) {
            preEffect = EffectData.EffectName.NOTHING;
            postEffect = EffectData.EffectName.NOTHING;

            switch (abilityTypeName) {
                case ENERGY_SLICES -> {
                    name = "Energy Slices";
                    energyCost = 2;
                    targetType = TargetType.ALL;
                    effect = EffectData.EffectName.DAMAGE_MANY_TIMES;
                }
                case ENERGY_SLICES_UPGRADED -> {
                    name = "Energy Slices+";
                    energyCost = 1;
                    targetType = TargetType.ALL;
                    effect = EffectData.EffectName.DAMAGE_MANY_TIMES;
                }
                case FLAME -> {
                    name = "Flame";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectData.EffectName.HIGH_DAMAGE_MANY_TIMES;
                }
                case FLAME_UPGRADED -> {
                    name = "Double Flame";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectData.EffectName.DAMAGE_MANY_TIMES;
                    postEffect = EffectData.EffectName.DAMAGE_A_BIT;
                }
                case FIRE_STRIKE -> {
                    name = "Fire Strike";
                    energyCost = 1;
                    targetType = TargetType.ONE;
                    effect = EffectData.EffectName.HIGH_DAMAGE_MANY_TIMES;
                }
                case FIRE_STRIKE_UPGRADED -> {
                    name = "Fire Strike+";
                    energyCost = 1;
                    targetType = TargetType.ONE;
                    effect = EffectData.EffectName.HIGH_DAMAGE_MANY_TIMES;
                    postEffect = EffectData.EffectName.DEFEND_SOME;
                }
                case DEFEND -> {
                    name = "Defend";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectData.EffectName.DEFEND_SOME;
                }
                case DEFEND_UPGRADED -> {
                    name = "Defend+";
                    energyCost = 1;
                    targetType = TargetType.SELF;
                    effect = EffectData.EffectName.DEFEND_TWICE_A_BIT;
                }
                case ITEM_SWORD_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    targetType = TargetType.ALL;
                    effect = EffectData.EffectName.DAMAGE_A_BIT;
                }
                case ITEM_SWORD_2_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    targetType = TargetType.ONE;
                    effect = EffectData.EffectName.DAMAGE_A_BIT;
                }
                case ITEM_SHIELD_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    targetType = TargetType.SELF;
                    effect = EffectData.EffectName.DEFEND_SOME;
                }
                case NOTHING -> {
                    name = "Sold";
                    energyCost = 0;
                    targetType = TargetType.SELF;
                    effect = EffectData.EffectName.NOTHING;
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

        public EffectData.EffectName getPreEffect() {
            return preEffect;
        }

        public EffectData.EffectName getEffect() {
            return effect;
        }

        public EffectData.EffectName getPostEffect() {
            return postEffect;
        }
    }

    public enum AbilityTypeName {
        ENERGY_SLICES, ENERGY_SLICES_UPGRADED,
        FLAME, FLAME_UPGRADED,
        FIRE_STRIKE, FIRE_STRIKE_UPGRADED,
        DEFEND, DEFEND_UPGRADED,
        ITEM_SWORD_ABILITY,
        ITEM_SWORD_2_ABILITY,
        ITEM_SHIELD_ABILITY,
        NOTHING
    }
}
