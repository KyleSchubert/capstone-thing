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

    public static boolean isDebuff(StatusEffectTypeName statusEffectTypeName) {
        return data.get(statusEffectTypeName.ordinal()).isDebuff();
    }

    private static class IndividualStatusEffectData {
        private final String imagePath;
        private String name;
        private String description;
        private boolean isDebuff;

        public IndividualStatusEffectData(StatusEffectTypeName statusEffectTypeName) {
            String iconFileName = "default.png";

            switch (statusEffectTypeName) {
                case BURNING -> {
                    iconFileName = "burning.png";
                    name = "Burning!";
                    description = "[CYAN]Take damage for each stack[] [RED]when turn starts[].\n\nStacks reduce by 1 when turn starts, removing the effect at 0.";
                    isDebuff = true;
                }
                case CONSTITUTION -> {
                    iconFileName = "constitution.png";
                    name = "Constitution";
                    description = "Every [GREEN]1 Constitution[] grants [CYAN]+1 Defense[] when you gain Defense.";
                    isDebuff = false;
                }
                case POISON -> {
                    iconFileName = "poison.png";
                    name = "Poisoned!";
                    description = "[CYAN]Take damage for each stack[] [RED]when turn ends[].\n\nStacks reduce by 1 when turn starts, removing the effect at 0.";
                    isDebuff = true;
                }
                case STRENGTH -> {
                    iconFileName = "strength.png";
                    name = "Strength";
                    description = "Every [GREEN]1 Strength[] grants [CYAN]+1 Damage[] when you Attack enemies.\n\nStrength does not apply to True Damage or % Damage.";
                    isDebuff = false;
                }
                case VULNERABILITY -> {
                    iconFileName = "vulnerability.png";
                    name = "Vulnerability";
                    description = "Increases [CYAN]damage taken to 150%[].\n\nStacks reduce by 1 when turn starts, removing the effect at 0.";
                    isDebuff = true;
                }
                case WEAKNESS -> {
                    iconFileName = "weakness.png";
                    name = "Weakness";
                    description = "Reduces [CYAN]damage output to 75%[].\n\nStacks reduce by 1 when turn starts, removing the effect at 0.";
                    isDebuff = true;
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

        public boolean isDebuff() {
            return isDebuff;
        }
    }
}
