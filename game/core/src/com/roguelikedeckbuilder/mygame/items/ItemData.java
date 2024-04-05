package com.roguelikedeckbuilder.mygame.items;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;

public class ItemData {
    private static Array<IndividualItemData> data;
    private static Array<ItemData.ItemName> allItemNames;

    public static void initialize() {
        data = new Array<>();

        for (ItemName name : ItemName.values()) {
            data.add(new IndividualItemData(name));
        }

        allItemNames = new Array<>(ItemData.ItemName.values());

    }

    public static Array<ItemName> getSomeRandomItemNamesByTier(ItemTier itemTier, int amount) {
        Array<ItemName> results = new Array<>();
        allItemNames.shuffle();

        for (ItemName itemName : allItemNames) {
            if (getItemTier(itemName).equals(itemTier)) {
                results.add(itemName);
                if (results.size == amount) {
                    break;
                }
            }
        }

        while (results.size < amount) {
            results.add(results.first());
            results.shuffle();
        }

        return results;
    }

    public static String getImagePath(ItemName itemName) {
        return data.get(itemName.ordinal()).getImagePath();
    }

    public static String getName(ItemName itemName) {
        return data.get(itemName.ordinal()).getName();
    }

    public static AbilityData.AbilityTypeName getAbilityTypeName(ItemName itemName) {
        return data.get(itemName.ordinal()).getAbilityTypeName();
    }

    public static ItemTier getItemTier(ItemName itemName) {
        return data.get(itemName.ordinal()).getItemTier();
    }

    private static class IndividualItemData {
        private final String imagePath;
        private String name;
        private AbilityData.AbilityTypeName abilityTypeName;
        private ItemTier itemTier;

        public IndividualItemData(ItemName itemName) {
            String iconFileName = "default.png";

            switch (itemName) {
                case TEST_SWORD -> {
                    iconFileName = "sword1.png";
                    name = "Test Sword";
                    abilityTypeName = AbilityData.AbilityTypeName.ITEM_SWORD_ABILITY;
                    itemTier = ItemTier.COMMON;
                }
                case TEST_SHIELD -> {
                    iconFileName = "shield1.png";
                    name = "Test Shield";
                    abilityTypeName = AbilityData.AbilityTypeName.ITEM_SHIELD_ABILITY;
                    itemTier = ItemTier.COMMON;
                }
                case TEST_SWORD_2 -> {
                    iconFileName = "sword2.png";
                    name = "Test Sword 2";
                    abilityTypeName = AbilityData.AbilityTypeName.ITEM_SWORD_2_ABILITY;
                    itemTier = ItemTier.COMMON;
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

        public AbilityData.AbilityTypeName getAbilityTypeName() {
            return abilityTypeName;
        }

        public ItemTier getItemTier() {
            return itemTier;
        }
    }

    public enum ItemName {
        TEST_SWORD, TEST_SHIELD, TEST_SWORD_2
    }

    public enum ItemTier {
        COMMON, UNCOMMON, RARE, BOSS
    }
}
