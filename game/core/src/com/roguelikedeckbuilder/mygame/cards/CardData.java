package com.roguelikedeckbuilder.mygame.cards;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;

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
        copy.removeValue(CardTypeName.OUT_OF_STOCK, true);

        while (results.size < amount) {
            if (allowDuplicates) {
                results.add(copy.get(0));
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

    public static AbilityTypeName getAbilityTypeName(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getAbilityTypeName();
    }

    public static AbilityTypeName getUpgradedAbilityTypeName(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getUpgradedAbilityTypeName();
    }

    public static int getValue(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getValue();
    }

    public static String getImagePath(CardTypeName cardTypeName) {
        return data.get(cardTypeName.ordinal()).getImagePath();
    }

    public static class IndividualCardData {
        private final String imagePath;
        private AbilityTypeName abilityTypeName;
        private AbilityTypeName upgradedAbilityTypeName;
        private int value;

        public IndividualCardData(CardTypeName cardTypeName) {
            String iconFileName = "1.png";

            switch (cardTypeName) {
                case ENERGY_SLICES -> {
                    abilityTypeName = AbilityTypeName.ENERGY_SLICES;
                    upgradedAbilityTypeName = AbilityTypeName.ENERGY_SLICES_UPGRADED;
                    value = 110;
                    iconFileName = "1.png";
                }
                case FLAME -> {
                    abilityTypeName = AbilityTypeName.FLAME;
                    upgradedAbilityTypeName = AbilityTypeName.FLAME_UPGRADED;
                    value = 70;
                    iconFileName = "2.png";
                }
                case FIRE_STRIKE -> {
                    abilityTypeName = AbilityTypeName.FIRE_STRIKE;
                    upgradedAbilityTypeName = AbilityTypeName.FIRE_STRIKE_UPGRADED;
                    value = 80;
                    iconFileName = "3.png";
                }
                case DEFEND -> {
                    abilityTypeName = AbilityTypeName.DEFEND;
                    upgradedAbilityTypeName = AbilityTypeName.DEFEND_UPGRADED;
                    value = 80;
                    iconFileName = "4.png";
                }
                case OUT_OF_STOCK -> {
                    abilityTypeName = AbilityTypeName.NOTHING;
                    upgradedAbilityTypeName = AbilityTypeName.NOTHING;
                    iconFileName = "sold.png";
                    value = 999999;
                }
                case DISCARD_DRAW -> {
                    abilityTypeName = AbilityTypeName.DISCARD_DRAW;
                    upgradedAbilityTypeName = AbilityTypeName.DRAW;
                    iconFileName = "not done.png";
                    value = 65;
                }
                case WEIRD_PUNCH -> {
                    abilityTypeName = AbilityTypeName.PERCENTAGE_PUNCH;
                    upgradedAbilityTypeName = AbilityTypeName.PERCENTAGE_PUNCH_UPGRADED;
                    iconFileName = "not done.png";
                    value = 105;
                }
                case AMPLIFY -> {
                    abilityTypeName = AbilityTypeName.AMPLIFY;
                    upgradedAbilityTypeName = AbilityTypeName.AMPLIFY_UPGRADED;
                    iconFileName = "not done.png";
                    value = 250;
                }
                case ROUTINE_POKING -> {
                    abilityTypeName = AbilityTypeName.SMALL_DAMAGE_EVERY_TURN;
                    upgradedAbilityTypeName = AbilityTypeName.SMALL_DAMAGE_EVERY_TURN_UPGRADED;
                    iconFileName = "not done.png";
                    value = 280;
                }
                case SHIELDS_UP -> {
                    abilityTypeName = AbilityTypeName.SHIELDS_UP;
                    upgradedAbilityTypeName = AbilityTypeName.SHIELDS_UP_UPGRADED;
                    iconFileName = "not done.png";
                    value = 280;
                }
                case SPEED_DIAL -> {
                    abilityTypeName = AbilityTypeName.SPEED_DIAL;
                    upgradedAbilityTypeName = AbilityTypeName.SPEED_DIAL_UPGRADED;
                    iconFileName = "not done.png";
                    value = 350;
                }
                case PINS_AND_NEEDLES -> {
                    abilityTypeName = AbilityTypeName.PINS_AND_NEEDLES;
                    upgradedAbilityTypeName = AbilityTypeName.PINS_AND_NEEDLES_UPGRADED;
                    iconFileName = "not done.png";
                    value = 350;
                }
                case PUMP_IRON -> {
                    abilityTypeName = AbilityTypeName.PUMP_IRON;
                    upgradedAbilityTypeName = AbilityTypeName.SHIELDS_UP_UPGRADED;
                    iconFileName = "not done.png";
                    value = 550;
                }
                default ->
                        System.out.println("Why was an ability almost generated with no matching type name? abilityTypeName:  " + cardTypeName);
            }

            imagePath = "ABILITIES/" + iconFileName;
        }

        public AbilityTypeName getAbilityTypeName() {
            return abilityTypeName;
        }

        public AbilityTypeName getUpgradedAbilityTypeName() {
            return upgradedAbilityTypeName;
        }

        public int getValue() {
            return value;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}
