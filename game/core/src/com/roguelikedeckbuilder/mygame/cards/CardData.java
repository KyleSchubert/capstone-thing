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
            String iconFileName = "not done.png";

            switch (cardTypeName) {
                case SLASH -> {
                    abilityTypeName = AbilityTypeName.DEFAULT_ATTACK;
                    upgradedAbilityTypeName = AbilityTypeName.DEFAULT_ATTACK_UPGRADED;
                    value = 40;
                    iconFileName = "not done.png";
                }
                case ENERGY_SLICES -> {
                    abilityTypeName = AbilityTypeName.ENERGY_SLICES;
                    upgradedAbilityTypeName = AbilityTypeName.ENERGY_SLICES_UPGRADED;
                    value = 110;
                    iconFileName = "not done.png";
                }
                case STRIKE -> {
                    abilityTypeName = AbilityTypeName.STRIKE;
                    upgradedAbilityTypeName = AbilityTypeName.STRIKE_UPGRADED;
                    value = 130;
                    iconFileName = "not done.png";
                }
                case DEFEND -> {
                    abilityTypeName = AbilityTypeName.DEFEND;
                    upgradedAbilityTypeName = AbilityTypeName.DEFEND_UPGRADED;
                    value = 80;
                    iconFileName = "not done.png";
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
                    iconFileName = "ability4.png";
                    value = 280;
                }
                case SHIELDS_UP -> {
                    abilityTypeName = AbilityTypeName.SHIELDS_UP;
                    upgradedAbilityTypeName = AbilityTypeName.SHIELDS_UP_UPGRADED;
                    iconFileName = "ability5.png";
                    value = 280;
                }
                case SPEED_DIAL -> {
                    abilityTypeName = AbilityTypeName.SPEED_DIAL;
                    upgradedAbilityTypeName = AbilityTypeName.SPEED_DIAL_UPGRADED;
                    iconFileName = "ability3.png";
                    value = 350;
                }
                case PINS_AND_NEEDLES -> {
                    abilityTypeName = AbilityTypeName.PINS_AND_NEEDLES;
                    upgradedAbilityTypeName = AbilityTypeName.PINS_AND_NEEDLES_UPGRADED;
                    iconFileName = "ability1.png";
                    value = 350;
                }
                case PUMP_IRON -> {
                    abilityTypeName = AbilityTypeName.PUMP_IRON;
                    upgradedAbilityTypeName = AbilityTypeName.PUMP_IRON_UPGRADED;
                    iconFileName = "ability2.png";
                    value = 550;
                }
                case PRE_CURE -> {
                    abilityTypeName = AbilityTypeName.PRE_CURE;
                    upgradedAbilityTypeName = AbilityTypeName.PRE_CURE_UPGRADED;
                    iconFileName = "not done.png";
                    value = 110;
                }
                case MAGIC_BARRIER -> {
                    abilityTypeName = AbilityTypeName.MAGIC_BARRIER;
                    upgradedAbilityTypeName = AbilityTypeName.MAGIC_BARRIER_UPGRADED;
                    iconFileName = "not done.png";
                    value = 270;
                }
                case FRYING_PAN -> {
                    abilityTypeName = AbilityTypeName.FRYING_PAN;
                    upgradedAbilityTypeName = AbilityTypeName.FRYING_PAN_UPGRADED;
                    iconFileName = "not done.png";
                    value = 150;
                }
                case TETANUS -> {
                    abilityTypeName = AbilityTypeName.TETANUS;
                    upgradedAbilityTypeName = AbilityTypeName.TETANUS_UPGRADED;
                    iconFileName = "not done.png";
                    value = 190;
                }
                case NAILS_ON_CHALKBOARD -> {
                    abilityTypeName = AbilityTypeName.NAILS_ON_CHALKBOARD;
                    upgradedAbilityTypeName = AbilityTypeName.NAILS_ON_CHALKBOARD_UPGRADED;
                    iconFileName = "not done.png";
                    value = 210;
                }
                case CURSE -> {
                    abilityTypeName = AbilityTypeName.CURSE;
                    upgradedAbilityTypeName = AbilityTypeName.CURSE_UPGRADED;
                    iconFileName = "not done.png";
                    value = 250;
                }
                case HEX -> {
                    abilityTypeName = AbilityTypeName.HEX;
                    upgradedAbilityTypeName = AbilityTypeName.HEX_UPGRADED;
                    iconFileName = "not done.png";
                    value = 250;
                }
                case DEBILITATE -> {
                    abilityTypeName = AbilityTypeName.DEBILITATE;
                    upgradedAbilityTypeName = AbilityTypeName.DEBILITATE_UPGRADED;
                    iconFileName = "not done.png";
                    value = 300;
                }
                case SPREAD_GERMS -> {
                    abilityTypeName = AbilityTypeName.SPREAD_GERMS;
                    upgradedAbilityTypeName = AbilityTypeName.SPREAD_GERMS_UPGRADED;
                    iconFileName = "not done.png";
                    value = 200;
                }
                case CRACK_KNUCKLES -> {
                    abilityTypeName = AbilityTypeName.CRACK_KNUCKLES;
                    upgradedAbilityTypeName = AbilityTypeName.CRACK_KNUCKLES_UPGRADED;
                    iconFileName = "not done.png";
                    value = 290;
                }
                case THORNS -> {
                    abilityTypeName = AbilityTypeName.THORNS;
                    upgradedAbilityTypeName = AbilityTypeName.THORNS_UPGRADED;
                    iconFileName = "thorns.png";
                    value = 360;
                }
                case WEAR_POISON -> {
                    abilityTypeName = AbilityTypeName.WEAR_POISON;
                    upgradedAbilityTypeName = AbilityTypeName.WEAR_POISON_UPGRADED;
                    iconFileName = "poison attacked.png";
                    value = 350;
                }
                case FIRE_WHIRL -> {
                    abilityTypeName = AbilityTypeName.FIRE_WHIRL;
                    upgradedAbilityTypeName = AbilityTypeName.FIRE_WHIRL_UPGRADED;
                    iconFileName = "burn when attacked.png";
                    value = 340;
                }
                case SPIKED_SHIELD -> {
                    abilityTypeName = AbilityTypeName.SPIKED_SHIELD;
                    upgradedAbilityTypeName = AbilityTypeName.SPIKED_SHIELD_UPGRADED;
                    iconFileName = "not done.png";
                    value = 180;
                }
                case MULTI_STAB -> {
                    abilityTypeName = AbilityTypeName.MULTI_STAB;
                    upgradedAbilityTypeName = AbilityTypeName.MULTI_STAB_UPGRADED;
                    iconFileName = "not done.png";
                    value = 170;
                }
                case RAMPING -> {
                    abilityTypeName = AbilityTypeName.RAMPING;
                    upgradedAbilityTypeName = AbilityTypeName.RAMPING_UPGRADED;
                    iconFileName = "not done.png";
                    value = 230;
                }
                case SWIFT_BLOCK -> {
                    abilityTypeName = AbilityTypeName.SWIFT_BLOCK;
                    upgradedAbilityTypeName = AbilityTypeName.SWIFT_BLOCK_UPGRADED;
                    iconFileName = "not done.png";
                    value = 220;
                }
                case BURN_STEEL_WOOL -> {
                    abilityTypeName = AbilityTypeName.BURN_STEEL_WOOL;
                    upgradedAbilityTypeName = AbilityTypeName.BURN_STEEL_WOOL_UPGRADED;
                    iconFileName = "not done.png";
                    value = 260;
                }
                case VERY_ILL -> {
                    abilityTypeName = AbilityTypeName.VERY_ILL;
                    upgradedAbilityTypeName = AbilityTypeName.VERY_ILL_UPGRADED;
                    iconFileName = "not done.png";
                    value = 260;
                }
                case REGENERATE -> {
                    abilityTypeName = AbilityTypeName.REGENERATE;
                    upgradedAbilityTypeName = AbilityTypeName.REGENERATE_UPGRADED;
                    iconFileName = "not done.png";
                    value = 180;
                }
                case SCATTERSHOT -> {
                    abilityTypeName = AbilityTypeName.SCATTERSHOT;
                    upgradedAbilityTypeName = AbilityTypeName.SCATTERSHOT_UPGRADED;
                    iconFileName = "not done.png";
                    value = 215;
                }
                case CULL -> {
                    abilityTypeName = AbilityTypeName.CULL;
                    upgradedAbilityTypeName = AbilityTypeName.CULL_UPGRADED;
                    iconFileName = "not done.png";
                    value = 330;
                }
                case LIFE_LEECHING -> {
                    abilityTypeName = AbilityTypeName.LIFE_LEECHING;
                    upgradedAbilityTypeName = AbilityTypeName.LIFE_LEECHING_UPGRADED;
                    iconFileName = "not done.png";
                    value = 220;
                }
                case AFFLICTION -> {
                    abilityTypeName = AbilityTypeName.AFFLICTION;
                    upgradedAbilityTypeName = AbilityTypeName.AFFLICTION_UPGRADED;
                    iconFileName = "not done.png";
                    value = 160;
                }
                case HOT_HOLY_WATER -> {
                    abilityTypeName = AbilityTypeName.HOT_HOLY_WATER;
                    upgradedAbilityTypeName = AbilityTypeName.HOT_HOLY_WATER_UPGRADED;
                    iconFileName = "not done.png";
                    value = 190;
                }
                case TWO_SHIELDS -> {
                    abilityTypeName = AbilityTypeName.TWO_SHIELDS;
                    upgradedAbilityTypeName = AbilityTypeName.TWO_SHIELDS_UPGRADED;
                    iconFileName = "not done.png";
                    value = 180;
                }
                case BOULDER_THROW -> {
                    abilityTypeName = AbilityTypeName.BOULDER_THROW;
                    upgradedAbilityTypeName = AbilityTypeName.BOULDER_THROW_UPGRADED;
                    iconFileName = "not done.png";
                    value = 300;
                }
                case SYMPATHETIC_PAIN -> {
                    abilityTypeName = AbilityTypeName.SYMPATHETIC_PAIN;
                    upgradedAbilityTypeName = AbilityTypeName.SYMPATHETIC_PAIN_UPGRADED;
                    iconFileName = "not done.png";
                    value = 220;
                }
                case DUST -> {
                    abilityTypeName = AbilityTypeName.DUST;
                    upgradedAbilityTypeName = AbilityTypeName.DUST_UPGRADED;
                    iconFileName = "not done.png";
                    value = 270;
                }
                case DISTRACTION -> {
                    abilityTypeName = AbilityTypeName.DISTRACTION;
                    upgradedAbilityTypeName = AbilityTypeName.DISTRACTION_UPGRADED;
                    iconFileName = "not done.png";
                    value = 210;
                }
                case FIRE_BREATH -> {
                    abilityTypeName = AbilityTypeName.FIRE_BREATH;
                    upgradedAbilityTypeName = AbilityTypeName.FIRE_BREATH_UPGRADED;
                    iconFileName = "burn on turn.png";
                    value = 400;
                }
                case COIN_WALL -> {
                    abilityTypeName = AbilityTypeName.COIN_WALL;
                    upgradedAbilityTypeName = AbilityTypeName.COIN_WALL_UPGRADED;
                    iconFileName = "not done.png";
                    value = 370;
                }
                case FOIL_ARMOR -> {
                    abilityTypeName = AbilityTypeName.FOIL_ARMOR;
                    upgradedAbilityTypeName = AbilityTypeName.FOIL_ARMOR_UPGRADED;
                    iconFileName = "not done.png";
                    value = 130;
                }
                case BLOCK_AND_STUFF -> {
                    abilityTypeName = AbilityTypeName.BLOCK_AND_STUFF;
                    upgradedAbilityTypeName = AbilityTypeName.BLOCK_AND_STUFF_UPGRADED;
                    iconFileName = "not done.png";
                    value = 220;
                }
                case NOTHING_BUT_GOOD -> {
                    abilityTypeName = AbilityTypeName.NOTHING_BUT_GOOD;
                    upgradedAbilityTypeName = AbilityTypeName.NOTHING_BUT_GOOD_UPGRADED;
                    iconFileName = "not done.png";
                    value = 600;
                }
                case NOTHING_BUT_GOLD -> {
                    abilityTypeName = AbilityTypeName.NOTHING_BUT_GOLD;
                    upgradedAbilityTypeName = AbilityTypeName.NOTHING_BUT_GOLD_UPGRADED;
                    iconFileName = "not done.png";
                    value = 600;
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
