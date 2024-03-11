package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import java.util.ArrayList;
import java.util.Arrays;

public class AbilityData {
    private static Array<IndividualAbilityData> data;

    public static void initialize() {
        data = new Array<>();

        for (Ability.AbilityTypeName name : Ability.AbilityTypeName.values()) {
            data.add(new IndividualAbilityData(name));
        }
    }

    public static String getCardIconPath(Ability.AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getCardIconPath();
    }

    public static String getName(Ability.AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getName();
    }

    public static String getDescription(Ability.AbilityTypeName typeName) {
        //"Deals [RED]1 Damage[] to an enemy [CYAN]4 times[]."
        int damage = getDamage(typeName);
        String damageText = String.format("[RED]%d Damage[]", damage);

        TargetType targetType = getTargetType(typeName);
        String hitTypeText;
        switch (targetType) {
            case ONE -> hitTypeText = "to an enemy";
            case ALL -> hitTypeText = "to [LIME]all[] enemies";
            default ->
                    throw new IllegalStateException("Unexpected value for hitType in getDescription(): " + targetType);
        }

        int hits = getHits(typeName);
        String hitsText;
        if (hits == 1) {
            hitsText = String.format("[CYAN]%d time[]", hits);
        } else {
            hitsText = String.format("[CYAN]%d times[]", hits);
        }

        return String.format("Deals %s %s %s.", damageText, hitTypeText, hitsText);
    }

    public static int getDamage(Ability.AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getEffectiveness();
    }

    public static int getHits(Ability.AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getRepetitions();
    }

    public static TargetType getTargetType(Ability.AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getTargetType();
    }

    private static class IndividualAbilityData {
        private String cardIconPath;
        private String name;
        private int effectiveness;
        private int repetitions;
        private TargetType targetType;
        private XYPair<Float> dimensions;
        private XYPair<Float> origin;
        private ArrayList<Float> animationFrameDelays;
        private float lifetime;

        public IndividualAbilityData(Ability.AbilityTypeName abilityTypeName) {
            String cardIconFileName;
            switch (abilityTypeName) {
                case ENERGY_SLICES:
                    cardIconFileName = "1.png";
                    name = "Energy Slices";
                    effectiveness = 1;
                    repetitions = 8;
                    targetType = TargetType.ALL;
                    dimensions = new XYPair<>(759f, 339f);
                    origin = new XYPair<>(425f, 254f);
                    animationFrameDelays = new ArrayList<>(Arrays.asList(0.06f, 0.09f, 0.06f, 0.09f, 0.06f, 0.06f, 0.06f, 0.09f, 0.06f, 0.06f, 0.06f, 0.06f));
                    lifetime = 0.81f;
                    /*
                    // HIT STUFF:
                    dimensions = new XYPair<>(417f, 370f);
                    origin = new XYPair<>(208.5f, 185.0f);
                    animationFrameDelays = new ArrayList<>(Arrays.asList(0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f));
                    lifetime = 0.54f;
                     */
                    break;
                case FLAME:
                    cardIconFileName = "2.png";
                    name = "Flame";
                    effectiveness = 5;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                    dimensions = new XYPair<>(104f, 63f);
                    origin = new XYPair<>(40f, 32f);
                    animationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f));
                    lifetime = 0.360f;
                    /*
                    // HIT INFO:
                    dimensions = new XYPair<>(160f, 130f);
                    origin = new XYPair<>(80f, 65f);
                    animationFrameDelays = new ArrayList<>(Arrays.asList(0.090f, 0.090f, 0.090f, 0.090f, 0.090f));
                    lifetime = 0.450f;
                     */
                    break;
                case FIRE_STRIKE:
                    cardIconFileName = "3.png";
                    name = "Fire Strike";
                    effectiveness = 9;
                    repetitions = 1;
                    targetType = TargetType.ONE;
                    dimensions = new XYPair<>(227f, 331f);
                    origin = new XYPair<>(129f, 194f);
                    animationFrameDelays = new ArrayList<>(Arrays.asList(0.18f, 0.09f, 0.09f, 0.09f, 0.09f));
                    lifetime = 0.54f;
                    /*
                    // HIT INFO:
                    dimensions = new XYPair<>(151f, 178f);
                    origin = new XYPair<>(75.5f, 89.0f);
                    animationFrameDelays = new ArrayList<>(Arrays.asList(0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f));
                    lifetime = 0.36f;
                     */
                    break;
                case DEFEND:
                    cardIconFileName = "4.png";
                    name = "Defend";
                    effectiveness = 5;
                    repetitions = 1;
                    targetType = TargetType.SELF;
                    dimensions = new XYPair<>(186f, 272f);
                    origin = new XYPair<>(94f, 220f);
                    animationFrameDelays = new ArrayList<>(Arrays.asList(0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f, 0.12f));
                    lifetime = 1.8f;
                default:
                    System.out.println("Why was an ability almost generated with no matching type name? abilityTypeName:  " + abilityTypeName);
                    return;
            }

            cardIconPath = "ABILITIES/" + cardIconFileName;
        }

        public String getCardIconPath() {
            return cardIconPath;
        }

        public String getName() {
            return name;
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
