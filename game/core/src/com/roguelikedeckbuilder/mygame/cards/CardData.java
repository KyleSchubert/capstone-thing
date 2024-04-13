package com.roguelikedeckbuilder.mygame.cards;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;

public class CardData {
    private static Array<IndividualCardData> data;
    private static Array<CardTypeName> allCardTypeNames;

    public static void initialize() {
        data = new Array<>();

        for (CardTypeName name : CardTypeName.values()) {
            data.add(new IndividualCardData(name));
        }

        allCardTypeNames = new Array<>(CardTypeName.values());
    }

    public static Array<CardTypeName> getSomeRandomCards(int amount, boolean allowDuplicates) {
        Array<CardTypeName> results = new Array<>();
        Array<CardTypeName> copy = new Array<>();
        copy.addAll(allCardTypeNames);
        copy.shuffle();

        while (results.size < amount) {
            if (allowDuplicates) {
                if (copy.get(0) != CardTypeName.OUT_OF_STOCK) {
                    results.add(copy.get(0));
                } else {
                    results.add(copy.get(1));
                }
                copy.shuffle();
            } else {
                results.add(copy.pop());
                if (copy.isEmpty()) {
                    copy.addAll(allCardTypeNames);
                }
            }
        }

        return results;
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
                case OUT_OF_STOCK -> {
                    abilityTypeName = AbilityData.AbilityTypeName.NOTHING;
                    upgradedAbilityTypeName = AbilityData.AbilityTypeName.NOTHING;
                    iconFileName = "sold.png";
                    value = 999999;
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
        DEFEND,
        OUT_OF_STOCK
    }
}
