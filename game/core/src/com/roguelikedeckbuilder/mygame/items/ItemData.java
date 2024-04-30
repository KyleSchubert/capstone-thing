package com.roguelikedeckbuilder.mygame.items;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.tracking.trigger.TriggerData;
import com.roguelikedeckbuilder.mygame.tracking.trigger.TriggerName;

public class ItemData {
    private static Array<IndividualItemData> data;
    private static Array<ItemTypeName> allItemNames;

    public static void initialize() {
        data = new Array<>();

        for (ItemTypeName name : ItemTypeName.values()) {
            data.add(new IndividualItemData(name));
        }

        allItemNames = new Array<>(ItemTypeName.values());

    }

    public static Array<ItemTypeName> getSomeRandomItemNamesByTier(ItemTier itemTier, int amount, boolean allowDuplicates) {
        Array<ItemTypeName> results = new Array<>();
        Array<ItemTypeName> copy = new Array<>();
        copy.addAll(allItemNames);
        copy.shuffle();

        for (int i = copy.size - 1; i >= 0; i--) {
            if (getItemTier(copy.get(i)) == ItemTier.TEMPORARY_ITEM) {
                copy.removeIndex(i);
            }
        }

        if (!allowDuplicates) {
            for (Item item : Player.getOwnedItems()) {
                copy.removeValue(item.getItemTypeName(), true);
            }
        }

        for (ItemTypeName itemTypeName : copy) {
            if (itemTier == ItemTier.ANY || getItemTier(itemTypeName).equals(itemTier)) {
                if (getItemTier(itemTypeName) != ItemTier.JUNK) {
                    results.add(itemTypeName);
                    if (results.size == amount) {
                        break;
                    }
                }
            }
        }

        while (results.size < amount) {
            results.add(ItemTypeName.JUNK);
        }
        results.shuffle();

        return results;
    }

    public static String getPartialDescription(ItemTypeName itemTypeName) {
        String descriptionOverride = getDescriptionOverride(itemTypeName);
        if (descriptionOverride.isEmpty()) {
            return String.format("    %s\n    %s", getEffectExplanation(itemTypeName), getTriggerExplanation(itemTypeName));
        } else {
            return descriptionOverride;
        }
    }

    public static String getEffectExplanation(ItemTypeName itemTypeName) {
        String color = "ORANGE";
        return "[" + color + "]Effect[]: " + AbilityData.getDescription(getAbilityTypeName(itemTypeName));
    }

    public static String getTriggerExplanation(ItemTypeName itemTypeName) {
        String color = "ORANGE";
        return "[" + color + "]Activation[]: " + TriggerData.getExplanationString(getTriggerName(itemTypeName));
    }

    public static String getFullDescription(ItemTypeName itemTypeName) {
        String descriptionOverride = getDescriptionOverride(itemTypeName);
        if (descriptionOverride.isEmpty()) {
            return getName(itemTypeName) + "\n" + getPartialDescription(itemTypeName);
        } else {
            return descriptionOverride;
        }
    }

    public static String getImagePath(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getImagePath();
    }

    public static String getName(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getName();
    }

    public static AbilityTypeName getAbilityTypeName(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getAbilityTypeName();
    }

    public static ItemTier getItemTier(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getItemTier();
    }

    public static TriggerName getTriggerName(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getTriggerName();
    }

    public static String getDescriptionOverride(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getDescriptionOverride();
    }

    public static int getValue(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getValue();
    }

    private static class IndividualItemData {
        private final String imagePath;
        private String name;
        private AbilityTypeName abilityTypeName;
        private ItemTier itemTier; // Does nearly nothing, because elites give 1 item and bosses give 2, instead.
        private TriggerName triggerName;
        private int value;
        private String descriptionOverride = "";

        public IndividualItemData(ItemTypeName itemTypeName) {
            String iconFileName = "default.png";

            switch (itemTypeName) {
                case TEST_SWORD -> {
                    iconFileName = "sword1.png";
                    name = "Test Sword";
                    abilityTypeName = AbilityTypeName.ITEM_SWORD_ABILITY;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 400;
                }
                case TEST_SHIELD -> {
                    iconFileName = "shield1.png";
                    name = "Test Shield";
                    abilityTypeName = AbilityTypeName.ITEM_SHIELD_ABILITY;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.LAST_THREE_DAMAGE_DEALT_GREATER_THAN_30;
                    value = 250;
                }
                case TEST_SWORD_2 -> {
                    iconFileName = "sword2.png";
                    name = "Test Sword 2";
                    abilityTypeName = AbilityTypeName.ITEM_SWORD_2_ABILITY;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.ONCE_PER_TURN_AFTER_ENEMY_USES_ABILITY;
                    value = 350;
                }
                case JUNK -> {
                    iconFileName = "junk.png";
                    name = "Junk \n\n\n+5 SUPER Coins";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.JUNK;
                    triggerName = TriggerName.NOTHING;
                    value = 999;
                }
                case FOR_CARD_DAMAGE_EVERY_TURN -> {
                    iconFileName = "ability4.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_SMALL_DAMAGE;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.END_OF_TURN;
                    value = 1;
                }
                case FOR_CARD_DEFENSE_EVERY_TURN -> {
                    iconFileName = "ability5.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_SMALL_DEFENSE;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.END_OF_TURN;
                    value = 1;
                }
                case FOR_CARD_DRAW_EVERY_TURN -> {
                    iconFileName = "ability3.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_DRAW_CARD;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.START_OF_TURN;
                    value = 1;
                }
                case FOR_CARD_STRENGTH_EVERY_TURN -> {
                    iconFileName = "ability2.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_GAIN_STRENGTH;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.START_OF_TURN;
                    value = 1;
                }
                case FOR_CARD_DAMAGE_ON_DRAW -> {
                    iconFileName = "ability1.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_SMALL_TRUE_DAMAGE_ONE;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.DRAW_CARD;
                    value = 1;
                }
                case FOR_CARD_DAMAGE_WHEN_ATTACKED -> {
                    iconFileName = "thorns.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ENEMY_BURGER_LARGER_HIT;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.PLAYER_TOOK_DAMAGE;
                    value = 1;
                }
                case FOR_CARD_POISON_WHEN_ATTACKED -> {
                    iconFileName = "poison attacked.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ENEMY_SAD_DOLLAR_POISON;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.PLAYER_TOOK_DAMAGE;
                    value = 1;
                }
                case FOR_CARD_BURNING_WHEN_ATTACKED -> {
                    iconFileName = "burn when attacked.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_BURN_ONE;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.PLAYER_TOOK_DAMAGE;
                    value = 1;
                }
                case FOR_CARD_BURNING_EVERY_TURN -> {
                    iconFileName = "burn on turn.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_BURN_ONE;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.START_OF_TURN;
                    value = 1;
                }
                case ANTI_CURSE -> {
                    iconFileName = "anti curse.png";
                    name = "Anti-Curse";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 700;
                    descriptionOverride = "Immunity to Weakness.";
                }
                case ANTI_HEX -> {
                    iconFileName = "anti hex.png";
                    name = "Anti-Hex";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 700;
                    descriptionOverride = "Immunity to Vulnerability.";
                }
                case DAILY_MULTIVITAMIN -> {
                    iconFileName = "daily multivitamin.png";
                    name = "Daily Multivitamin";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 1500;
                    descriptionOverride = "Immunity to Poison.";
                }
                case ANTI_FIRE_WHICH_IS_WATER -> {
                    iconFileName = "anti fire.png";
                    name = "Anti-Fire (Water)";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 1500;
                    descriptionOverride = "Immunity to Burning.";
                }
                case AUTO_SHIELD -> {
                    iconFileName = "auto shield.png";
                    name = "Auto Shield";
                    abilityTypeName = AbilityTypeName.ITEM_SMALL_DEFENSE;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.ENEMY_USED_ABILITY;
                    value = 600;
                }
                case CUP_OF_POISON -> {
                    iconFileName = "cup of poison.png";
                    name = "Cup of Poison";
                    abilityTypeName = AbilityTypeName.ITEM_1_POISON;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.DAMAGE_IN_ONE_HIT_GTE_12;
                    value = 700;
                }
                case CUP_OF_MATCHES -> {
                    iconFileName = "cup of matches.png";
                    name = "Cup of Matches";
                    abilityTypeName = AbilityTypeName.ITEM_3_BURNING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.DAMAGE_IN_ONE_HIT_GTE_24;
                    value = 700;
                }
                case WEIGHTED_BANDS -> {
                    iconFileName = "weighted bands.png";
                    name = "Weighted Bands";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 700;
                    descriptionOverride = "Gain 1 Strength when gaining Constitution.";
                }
                case EVIL_NOTE -> {
                    iconFileName = "evil note note.png";
                    name = "Evil Note (Note)";
                    abilityTypeName = AbilityTypeName.ITEM_TWO_VULNERABILITY_ALL;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 500;
                }
                case EVIL_NOTE_ACTUAL -> {
                    iconFileName = "evil note letter.png";
                    name = "Evil Note (Letter)";
                    abilityTypeName = AbilityTypeName.ENEMY_CHIPS_INITIAL;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 500;
                }
                case SPECIAL_MATCHES -> {
                    iconFileName = "special matches.png";
                    name = "Special Matches";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 800;
                    descriptionOverride = "Inflict 1 additional Burning when inflicting Burning.";
                }
                case POISONOUS_POISON -> {
                    iconFileName = "poisonous poison.png";
                    name = "Poisonous Poison";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 800;
                    descriptionOverride = "Inflict 1 additional Poison when inflicting Poison.";
                }
                case BAND_AID_BOX -> {
                    iconFileName = "band aid box.png";
                    name = "Band-Aid Box";
                    abilityTypeName = AbilityTypeName.ITEM_HEAL_5;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.END_OF_COMBAT;
                    value = 400;
                }
                case NORMAL_MONOCLE -> {
                    iconFileName = "normal monocle.png";
                    name = "Normal Monocle";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 620;
                    descriptionOverride = "+1 Card Reward after combat.";
                }
                case GOLD_MONOCLE -> {
                    iconFileName = "gold monocle.png";
                    name = "Gold Monocle";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 620;
                    descriptionOverride = "+1 Coin Reward after combat.";
                }
                case LUCKY_TROWEL -> {
                    iconFileName = "lucky trowel.png";
                    name = "Lucky Trowel";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 740;
                    descriptionOverride = "??? Map Nodes are always Treasure.";
                }
                case GAME_FACE -> {
                    iconFileName = "game face.png";
                    name = "Game Face";
                    abilityTypeName = AbilityTypeName.ITEM_GAIN_STRENGTH;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 500;
                }
                case A_SHIELD_SHIELD -> {
                    iconFileName = "a shield shield.png";
                    name = "A Shield Shield";
                    abilityTypeName = AbilityTypeName.ITEM_GAIN_CONSTITUTION;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 500;
                }
                case EASY_LUNCH -> {
                    iconFileName = "easy lunch.png";
                    name = "An Easy Lunch";
                    abilityTypeName = AbilityTypeName.PRE_CURE;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 550;
                }
                case PHASING_SHIELD -> {
                    iconFileName = "phasing shield.png";
                    name = "Phasing Shield";
                    abilityTypeName = AbilityTypeName.ITEM_DEFEND_30;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.TURN_NUMBER_4;
                    value = 350;
                }
                case CASH_INJECTION -> {
                    iconFileName = "cash injection.png";
                    name = "Cash Injection";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 90;
                    descriptionOverride = "+500 Coins immediately.";
                }
                case MILLIONTH_DOLLAR -> {
                    iconFileName = "millionth dollar.png";
                    name = "Millionth Dollar";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 1000;
                    descriptionOverride = "+100 SUPER Coins immediately.";
                }
                case HEART_V2 -> {
                    iconFileName = "heart v2.png";
                    name = "Heart V2.0";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 900;
                    descriptionOverride = "Max HP +50. Heal to full.";
                }
                case PENCIL_ERASER -> {
                    iconFileName = "pencil eraser.png";
                    name = "Pencil Eraser";
                    abilityTypeName = AbilityTypeName.ITEM_REDUCE_MAX_HP_5_PERCENT;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 500;
                }
                case GOOD_ERASER -> {
                    iconFileName = "good eraser.png";
                    name = "Good Eraser";
                    abilityTypeName = AbilityTypeName.ITEM_REDUCE_MAX_HP_10_PERCENT;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 700;
                }
                case COMICALLY_LARGE_ERASER -> {
                    iconFileName = "comically large eraser.png";
                    name = "Too-Large Eraser";
                    abilityTypeName = AbilityTypeName.ITEM_REDUCE_MAX_HP_15_PERCENT;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                    value = 1000;
                }
                case HURRY_UP_ALARM -> {
                    iconFileName = "hurry up alarm.png";
                    name = "Hurry-Up Alarm";
                    abilityTypeName = AbilityTypeName.ITEM_TRUE_DAMAGE_35;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.TURN_NUMBER_7;
                    value = 700;
                }
                case HOUSE_RULES -> {
                    iconFileName = "house rules.png";
                    name = "House Rules";
                    abilityTypeName = AbilityTypeName.ITEM_DRAW_CARD;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.START_OF_TURN;
                    value = 700;
                }
                case LED_BULB -> {
                    iconFileName = "LED bulb.png";
                    name = "LED Bulb";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 700;
                    descriptionOverride = "+1 Energy.";
                }
                case A_REMINDER -> {
                    iconFileName = "a reminder.png";
                    name = "A Reminder";
                    abilityTypeName = AbilityTypeName.ITEM_DEFEND_14;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.USED_4_CARDS_IN_TURN;
                    value = 450;
                }
                case SOUP_SPOON -> {
                    iconFileName = "soup spoon.png";
                    name = "Soup Spoon";
                    abilityTypeName = AbilityTypeName.ITEM_SMALL_HEAL;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.ENEMY_DIED;
                    value = 450;
                }
                case THE_PLASTIC_CUP -> {
                    iconFileName = "the plastic cup.png";
                    name = "The Plastic Cup";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.NOTHING;
                    value = 700;
                    descriptionOverride = "Heal for 1 when you gain a stack of Pre-Cure.";
                }
            }

            imagePath = "ITEMS/" + iconFileName;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getName() {
            return name;
        }

        public AbilityTypeName getAbilityTypeName() {
            return abilityTypeName;
        }

        public ItemTier getItemTier() {
            return itemTier;
        }

        public TriggerName getTriggerName() {
            return triggerName;
        }

        public int getValue() {
            return value;
        }

        public String getDescriptionOverride() {
            return descriptionOverride;
        }
    }
}
