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
        String color = "ORANGE";

        String effect = "[" + color + "]Effect[]: " + AbilityData.getDescription(getAbilityTypeName(itemTypeName));
        String triggerExplanation = "[" + color + "]Activation[]: " + TriggerData.getExplanationString(getTriggerName(itemTypeName));

        return String.format("    %s\n    %s", effect, triggerExplanation);
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

    private static class IndividualItemData {
        private final String imagePath;
        private String name;
        private AbilityTypeName abilityTypeName;
        private ItemTier itemTier;
        private TriggerName triggerName;

        public IndividualItemData(ItemTypeName itemTypeName) {
            String iconFileName = "default.png";

            switch (itemTypeName) {
                case TEST_SWORD -> {
                    iconFileName = "sword1.png";
                    name = "Test Sword";
                    abilityTypeName = AbilityTypeName.ITEM_SWORD_ABILITY;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.EVERY_START_OF_BATTLE;
                }
                case TEST_SHIELD -> {
                    iconFileName = "shield1.png";
                    name = "Test Shield";
                    abilityTypeName = AbilityTypeName.ITEM_SHIELD_ABILITY;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.LAST_THREE_DAMAGE_DEALT_GREATER_THAN_30;
                }
                case TEST_SWORD_2 -> {
                    iconFileName = "sword2.png";
                    name = "Test Sword 2";
                    abilityTypeName = AbilityTypeName.ITEM_SWORD_2_ABILITY;
                    itemTier = ItemTier.COMMON;
                    triggerName = TriggerName.ONCE_PER_TURN_AFTER_ENEMY_USES_ABILITY;
                }
                case JUNK -> {
                    iconFileName = "junk.png";
                    name = "Junk";
                    abilityTypeName = AbilityTypeName.NOTHING;
                    itemTier = ItemTier.JUNK;
                    triggerName = TriggerName.NOTHING;
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
    }

}
