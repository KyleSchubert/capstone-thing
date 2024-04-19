package com.roguelikedeckbuilder.mygame.combat.buffordebuff;

import com.badlogic.gdx.utils.Array;

public class BuffOrDebuffData {
    private static Array<IndividualBuffOrDebuffData> data;

    public static void initialize() {
        data = new Array<>();

        for (BuffOrDebuffName name : BuffOrDebuffName.values()) {
            data.add(new IndividualBuffOrDebuffData(name));
        }
    }

    public static String getImagePath(BuffOrDebuffName buffOrDebuffName) {
        return data.get(buffOrDebuffName.ordinal()).getImagePath();
    }

    public static String getName(BuffOrDebuffName buffOrDebuffName) {
        return data.get(buffOrDebuffName.ordinal()).getName();
    }

    public static String getDescription(BuffOrDebuffName buffOrDebuffName) {
        return data.get(buffOrDebuffName.ordinal()).getDescription();
    }

    private static class IndividualBuffOrDebuffData {
        private final String imagePath;
        private String name;
        private String description;
        // NOTE: the buffs / debuffs don't CAUSE effects. They are visual reminders of what is active and how it works

        public IndividualBuffOrDebuffData(BuffOrDebuffName buffOrDebuffName) {
            String iconFileName = "default.png";

            switch (buffOrDebuffName) {
                case STRENGTH -> {
                    iconFileName = "strength.png";
                    name = "Strength";
                    description = "Every [GREEN]1 Strength[] grants [CYAN]+1 Damage[] when you Attack enemies.";
                }
                case CONSTITUTION -> {
                    iconFileName = "constitution.png";
                    name = "Constitution";
                    description = "Every [GREEN]1 Constitution[] grants [CYAN]+1 Defense[] when you gain Defense.";
                }
            }

            imagePath = "BUFFS DEBUFFS/" + iconFileName;
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
