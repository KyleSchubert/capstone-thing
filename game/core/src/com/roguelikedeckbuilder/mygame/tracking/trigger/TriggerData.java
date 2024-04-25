package com.roguelikedeckbuilder.mygame.tracking.trigger;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.tracking.statistics.ActivationComparison;
import com.roguelikedeckbuilder.mygame.tracking.statistics.StatisticsType;

public class TriggerData {
    private static Array<IndividualTriggerData> data;

    public static void initialize() {
        data = new Array<>();

        for (TriggerName name : TriggerName.values()) {
            data.add(new IndividualTriggerData(name));
        }
    }

    public static StatisticsType getTypeOfTrackedStatistic(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getTypeOfTrackedStatistic();
    }

    public static WhatToLookAt getWhatToLookAt(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getWhatToLookAt();
    }

    public static ActivationComparison getActivationComparison(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getActivationComparison();
    }

    public static int getActivationValue(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getActivationValue();
    }

    public static StatisticsType getWhenToCheck(TriggerName triggerName) {
        return data.get(triggerName.ordinal()).getWhenToCheck();
    }

    public static StatisticsType getWhenToReset(TriggerName triggerName) {
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
        private StatisticsType typeOfTrackedStatistic;
        private WhatToLookAt whatToLookAt;
        private ActivationComparison activationComparison;
        private int activationValue;
        private StatisticsType whenToCheck;
        private StatisticsType whenToReset;
        private boolean alwaysResetWhenPossible = false;
        private int howManyMostRecentToConsider = 999999999; // Unused if looking at OCCURRENCES
        private String explanationString; // It would not be easier/faster to make this automatically written

        public IndividualTriggerData(TriggerName triggerName) {
            switch (triggerName) {
                case EVERY_START_OF_BATTLE -> {
                    typeOfTrackedStatistic = StatisticsType.COMBAT_STARTED;
                    whatToLookAt = WhatToLookAt.OCCURRENCES;
                    activationComparison = ActivationComparison.EQUAL;
                    activationValue = 1;
                    whenToCheck = StatisticsType.COMBAT_STARTED;
                    whenToReset = StatisticsType.COMBAT_ENDED;
                    explanationString = "At the start of combat";
                }
                case ONCE_PER_TURN_AFTER_ENEMY_USES_ABILITY -> {
                    typeOfTrackedStatistic = StatisticsType.ENEMY_USED_ABILITY;
                    whatToLookAt = WhatToLookAt.OCCURRENCES;
                    activationComparison = ActivationComparison.EQUAL;
                    activationValue = 1;
                    whenToCheck = StatisticsType.ENEMY_USED_ABILITY;
                    whenToReset = StatisticsType.TURN_STARTED;
                    explanationString = "After the first time any enemy uses an ability in a turn";
                }
                case EVERY_TURN_AFTER_TAKING_20_DAMAGE -> {
                    typeOfTrackedStatistic = StatisticsType.PLAYER_TOOK_DAMAGE;
                    whatToLookAt = WhatToLookAt.VALUE;
                    activationComparison = ActivationComparison.GREATER_THAN_OR_EQUAL;
                    activationValue = 20;
                    whenToCheck = StatisticsType.PLAYER_TOOK_DAMAGE;
                    whenToReset = StatisticsType.TURN_STARTED;
                    alwaysResetWhenPossible = true;
                    explanationString = "After you have taken at least 20 damage in a turn";
                }
                case LAST_THREE_DAMAGE_DEALT_GREATER_THAN_30 -> {
                    typeOfTrackedStatistic = StatisticsType.ENEMY_TOOK_DAMAGE;
                    whatToLookAt = WhatToLookAt.VALUE;
                    activationComparison = ActivationComparison.GREATER_THAN_OR_EQUAL;
                    activationValue = 30;
                    whenToCheck = StatisticsType.TURN_ENDED;
                    whenToReset = StatisticsType.TURN_STARTED;
                    howManyMostRecentToConsider = 3;
                    explanationString = "If you have dealt 30+ damage in the last 3 hits of your turn";
                }
                case NOTHING -> {
                    typeOfTrackedStatistic = StatisticsType.RUN_ENDED;
                    whatToLookAt = WhatToLookAt.OCCURRENCES;
                    activationComparison = ActivationComparison.GREATER_THAN_OR_EQUAL;
                    activationValue = 1;
                    whenToCheck = StatisticsType.RUN_ENDED;
                    whenToReset = StatisticsType.RUN_ENDED;
                    explanationString = "Does nothing.";
                }
                case DRAW_CARD -> {
                    typeOfTrackedStatistic = StatisticsType.DREW_CARD;
                    whatToLookAt = WhatToLookAt.OCCURRENCES;
                    activationComparison = ActivationComparison.EQUAL;
                    activationValue = 1;
                    whenToCheck = StatisticsType.DREW_CARD;
                    whenToReset = StatisticsType.DREW_CARD;
                    explanationString = "When you draw a card.";
                }
                case END_OF_TURN -> {
                    typeOfTrackedStatistic = StatisticsType.TURN_ENDED;
                    whatToLookAt = WhatToLookAt.OCCURRENCES;
                    activationComparison = ActivationComparison.EQUAL;
                    activationValue = 1;
                    whenToCheck = StatisticsType.TURN_ENDED;
                    whenToReset = StatisticsType.TURN_STARTED;
                    explanationString = "At the end of your turn.";
                }
                case START_OF_TURN -> {
                    typeOfTrackedStatistic = StatisticsType.TURN_STARTED;
                    whatToLookAt = WhatToLookAt.OCCURRENCES;
                    activationComparison = ActivationComparison.EQUAL;
                    activationValue = 1;
                    whenToCheck = StatisticsType.TURN_STARTED;
                    whenToReset = StatisticsType.TURN_ENDED;
                    explanationString = "At the start of your turn.";
                }
            }
        }

        public StatisticsType getTypeOfTrackedStatistic() {
            return typeOfTrackedStatistic;
        }

        public WhatToLookAt getWhatToLookAt() {
            return whatToLookAt;
        }

        public ActivationComparison getActivationComparison() {
            return activationComparison;
        }

        public int getActivationValue() {
            return activationValue;
        }

        public StatisticsType getWhenToCheck() {
            return whenToCheck;
        }

        public StatisticsType getWhenToReset() {
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

}
