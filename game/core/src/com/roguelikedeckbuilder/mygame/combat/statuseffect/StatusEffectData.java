package com.roguelikedeckbuilder.mygame.combat.statuseffect;

import com.badlogic.gdx.utils.Array;

public class StatusEffectData {
    private static Array<IndividualStatusEffectData> data;

    public static void initialize() {
        data = new Array<>();

        for (StatusEffectTypeName name : StatusEffectTypeName.values()) {
            data.add(new IndividualStatusEffectData(name));
        }
    }

    public static String getImagePath(StatusEffectTypeName statusEffectTypeName) {
        return data.get(statusEffectTypeName.ordinal()).getImagePath();
    }

    public static String getName(StatusEffectTypeName statusEffectTypeName) {
        return data.get(statusEffectTypeName.ordinal()).getName();
    }

    public static String getDescription(StatusEffectTypeName statusEffectTypeName) {
        return data.get(statusEffectTypeName.ordinal()).getDescription();
    }

    private static class IndividualStatusEffectData {
        private final String imagePath;
        private String name;
        private String description;

        public IndividualStatusEffectData(StatusEffectTypeName statusEffectTypeName) {
            String iconFileName = "default.png";

            switch (statusEffectTypeName) {
                case STRENGTH -> {
                    iconFileName = "strength.png";
                    name = "Strength";
                    description = "Every [GREEN]1 Strength[] grants [CYAN]+1 Damage[] when you Attack enemies.\n\nStrength does not apply to True Damage or % Damage.";
                }
                case CONSTITUTION -> {
                    iconFileName = "constitution.png";
                    name = "Constitution";
                    description = "Every [GREEN]1 Constitution[] grants [CYAN]+1 Defense[] when you gain Defense.";
                }
            }

            imagePath = "STATUS EFFECTS/" + iconFileName;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
