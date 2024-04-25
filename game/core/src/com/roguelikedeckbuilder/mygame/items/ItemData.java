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
        return String.format("    %s\n    %s", getEffectExplanation(itemTypeName), getTriggerExplanation(itemTypeName));
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
        return getName(itemTypeName) + "\n" + getPartialDescription(itemTypeName);
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

    public static int getValue(ItemTypeName itemTypeName) {
        return data.get(itemTypeName.ordinal()).getValue();
    }

    private static class IndividualItemData {
        private final String imagePath;
        private String name;
        private AbilityTypeName abilityTypeName;
        private ItemTier itemTier;
        private TriggerName triggerName;
        private int value;

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
                    iconFileName = "default.png";
                    name = "Temporary Buff";
                    abilityTypeName = AbilityTypeName.ITEM_SMALL_DAMAGE;
                    itemTier = ItemTier.TEMPORARY_ITEM;
                    triggerName = TriggerName.END_OF_TURN;
                    value = 1;
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
    }
}
