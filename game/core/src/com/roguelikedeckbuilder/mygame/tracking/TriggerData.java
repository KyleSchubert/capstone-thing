package com.roguelikedeckbuilder.mygame.tracking;

import com.badlogic.gdx.utils.Array;

public class TriggerData {
    private static Array<IndividualTriggerData> data;

    public static void initialize() {
        data = new Array<>();

        for (TriggerName name : TriggerName.values()) {
            data.add(new IndividualTriggerData(name));
        }
    }

    public static Statistics.StatisticsType getTypeOfTrackedStatistic(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getTypeOfTrackedStatistic();
    }

    public static Trigger.WhatToLookAt getWhatToLookAt(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getWhatToLookAt();
    }

    public static Trigger.ActivationComparison getActivationComparison(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getActivationComparison();
    }

    public static int getActivationValue(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getActivationValue();
    }

    public static Statistics.StatisticsType getWhenToCheck(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getWhenToCheck();
    }

    public static Statistics.StatisticsType getWhenToReset(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getWhenToReset();
    }

    public static boolean isAlwaysResetWhenPossible(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).isAlwaysResetWhenPossible();
    }

    public static int getHowManyMostRecentToConsider(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getHowManyMostRecentToConsider();
    }

    public static String getExplanationString(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getExplanationString();
    }

    private static class IndividualTriggerData {
        private Statistics.StatisticsType typeOfTrackedStatistic;
        private Trigger.WhatToLookAt whatToLookAt;
        private Trigger.ActivationComparison activationComparison;
        private int activationValue;
        private Statistics.StatisticsType whenToCheck;
        private Statistics.StatisticsType whenToReset;
        private boolean alwaysResetWhenPossible = false;
        private int howManyMostRecentToConsider = 999999999; // Unused if looking at OCCURRENCES
        private String explanationString; // It would not be easier/faster to make this automatically written

        public IndividualTriggerData(TriggerName triggerName) {
            switch (triggerName) {
                case EVERY_START_OF_BATTLE -> {
                    typeOfTrackedStatistic = Statistics.StatisticsType.COMBAT_STARTED;
                    whatToLookAt = Trigger.WhatToLookAt.OCCURRENCES;
                    activationComparison = Trigger.ActivationComparison.EQUAL;
                    activationValue = 1;
                    whenToCheck = Statistics.StatisticsType.COMBAT_STARTED;
                    whenToReset = Statistics.StatisticsType.COMBAT_ENDED;
                    explanationString = "At the start of combat";
                }
                case ONCE_PER_TURN_AFTER_ENEMY_USES_ABILITY -> {
                    typeOfTrackedStatistic = Statistics.StatisticsType.ENEMY_USED_ABILITY;
                    whatToLookAt = Trigger.WhatToLookAt.OCCURRENCES;
                    activationComparison = Trigger.ActivationComparison.EQUAL;
                    activationValue = 1;
                    whenToCheck = Statistics.StatisticsType.ENEMY_USED_ABILITY;
                    whenToReset = Statistics.StatisticsType.TURN_STARTED;
                    explanationString = "After the first time any enemy uses an ability in a turn";
                }
                case EVERY_TURN_AFTER_TAKING_20_DAMAGE -> {
                    typeOfTrackedStatistic = Statistics.StatisticsType.PLAYER_TOOK_DAMAGE;
                    whatToLookAt = Trigger.WhatToLookAt.VALUE;
                    activationComparison = Trigger.ActivationComparison.GREATER_THAN_OR_EQUAL;
                    activationValue = 20;
                    whenToCheck = Statistics.StatisticsType.PLAYER_TOOK_DAMAGE;
                    whenToReset = Statistics.StatisticsType.TURN_STARTED;
                    alwaysResetWhenPossible = true;
                    explanationString = "After you have taken at least 20 damage in a turn";
                }
                case LAST_THREE_DAMAGE_DEALT_GREATER_THAN_30 -> {
                    typeOfTrackedStatistic = Statistics.StatisticsType.ENEMY_TOOK_DAMAGE;
                    whatToLookAt = Trigger.WhatToLookAt.VALUE;
                    activationComparison = Trigger.ActivationComparison.GREATER_THAN_OR_EQUAL;
                    activationValue = 30;
                    whenToCheck = Statistics.StatisticsType.TURN_ENDED;
                    whenToReset = Statistics.StatisticsType.TURN_STARTED;
                    howManyMostRecentToConsider = 3;
                    explanationString = "If you have dealt 30+ damage in the last 3 hits of your turn";
                }
            }
        }

        public Statistics.StatisticsType getTypeOfTrackedStatistic() {
            return typeOfTrackedStatistic;
        }

        public Trigger.WhatToLookAt getWhatToLookAt() {
            return whatToLookAt;
        }

        public Trigger.ActivationComparison getActivationComparison() {
            return activationComparison;
        }

        public int getActivationValue() {
            return activationValue;
        }

        public Statistics.StatisticsType getWhenToCheck() {
            return whenToCheck;
        }

        public Statistics.StatisticsType getWhenToReset() {
            return whenToReset;
        }

        public int getHowManyMostRecentToConsider() {
            return howManyMostRecentToConsider;
        }

        public boolean isAlwaysResetWhenPossible() {
            return alwaysResetWhenPossible;
        }

        public String getExplanationString() {
            return explanationString;
        }
    }

    public enum TriggerName {
        EVERY_START_OF_BATTLE, ONCE_PER_TURN_AFTER_ENEMY_USES_ABILITY, EVERY_TURN_AFTER_TAKING_20_DAMAGE,
        LAST_THREE_DAMAGE_DEALT_GREATER_THAN_30
    }
}
