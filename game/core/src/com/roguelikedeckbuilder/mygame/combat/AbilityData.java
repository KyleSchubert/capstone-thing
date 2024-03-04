package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;

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

        HitType hitType = getHitType(typeName);
        String hitTypeText;
        switch (hitType) {
            case ONE -> hitTypeText = "to an enemy";
            case ALL -> hitTypeText = "to [LIME]all[] enemies";
            default -> throw new IllegalStateException("Unexpected value for hitType in getDescription(): " + hitType);
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
        return data.get(typeName.ordinal()).getDamage();
    }

    public static int getHits(Ability.AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getHits();
    }

    public static HitType getHitType(Ability.AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getHitType();
    }

    private static class IndividualAbilityData {
        private String cardIconPath;
        private String name;
        private int damage;
        private int hits;
        private HitType hitType;

        public IndividualAbilityData(Ability.AbilityTypeName abilityTypeName) {
            String cardIconFileName;
            switch (abilityTypeName) {
                case VORTEX:
                    cardIconFileName = "1.png";
                    name = "Vortex";
                    damage = 1;
                    hits = 8;
                    hitType = HitType.ALL;
                    break;
                case FLAME:
                    cardIconFileName = "2.png";
                    name = "Flame";
                    damage = 5;
                    hits = 1;
                    hitType = HitType.ONE;
                    break;
                case FIRE_STRIKE:
                    cardIconFileName = "3.png";
                    name = "Fire Strike";
                    damage = 9;
                    hits = 1;
                    hitType = HitType.ONE;
                    break;
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

        public int getDamage() {
            return damage;
        }

        public int getHits() {
            return hits;
        }

        public HitType getHitType() {
            return hitType;
        }
    }
}
