package com.roguelikedeckbuilder.mygame.cards;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;

public class CardData {
    private static Array<IndividualCardData> data;

    public static void initialize() {
        data = new Array<>();

        for (CardTypeName name : CardTypeName.values()) {
            data.add(new IndividualCardData(name));
        }
    }

    public static AbilityData.AbilityTypeName getAbilityTypeName(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getAbilityTypeName();
    }

    public static AbilityData.AbilityTypeName getUpgradedAbilityTypeName(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getUpgradedAbilityTypeName();
    }

    public static int getValue(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getValue();
    }

    public static String getImagePath(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getImagePath();
    }

    public static class IndividualCardData {
        private AbilityData.AbilityTypeName abilityTypeName;
        private AbilityData.AbilityTypeName upgradedAbilityTypeName;
        private int value;
        private final String imagePath;

        public IndividualCardData(CardTypeName cardTypeName) {
            String iconFileName = "1.png";

            switch (cardTypeName) {
                case ENERGY_SLICES -> {
                    abilityTypeName = AbilityData.AbilityTypeName.ENERGY_SLICES;
                    upgradedAbilityTypeName = AbilityData.AbilityTypeName.ENERGY_SLICES_UPGRADED;
                    value = 110;
                    iconFileName = "1.png";
                }
                case FLAME -> {
                    abilityTypeName = AbilityData.AbilityTypeName.FLAME;
                    upgradedAbilityTypeName = AbilityData.AbilityTypeName.FLAME_UPGRADED;
                    value = 70;
                    iconFileName = "2.png";
                }
                case FIRE_STRIKE -> {
                    abilityTypeName = AbilityData.AbilityTypeName.FIRE_STRIKE;
                    upgradedAbilityTypeName = AbilityData.AbilityTypeName.FIRE_STRIKE_UPGRADED;
                    value = 80;
                    iconFileName = "3.png";
                }
                case DEFEND -> {
                    abilityTypeName = AbilityData.AbilityTypeName.DEFEND;
                    upgradedAbilityTypeName = AbilityData.AbilityTypeName.DEFEND_UPGRADED;
                    value = 80;
                    iconFileName = "4.png";
                }
                default ->
                        System.out.println("Why was an ability almost generated with no matching type name? abilityTypeName:  " + cardTypeName);
            }

            imagePath = "ABILITIES/" + iconFileName;
        }

        public AbilityData.AbilityTypeName getAbilityTypeName() {
            return abilityTypeName;
        }

        public AbilityData.AbilityTypeName getUpgradedAbilityTypeName() {
            return upgradedAbilityTypeName;
        }

        public int getValue() {
            return value;
        }

        public String getImagePath() {
            return imagePath;
        }
    }


    public enum CardTypeName {
        ENERGY_SLICES,
        FLAME,
        FIRE_STRIKE,
        DEFEND
    }
}
