package com.roguelikedeckbuilder.mygame.tracking;

public class Trigger {
    private final Statistics.StatisticsType typeOfTrackedStatistic;
    private final WhatToLookAt whatToLookAt;
    private final ActivationComparison activationComparison;
    private final int activationValue;
    private final Statistics.StatisticsType whenToCheck;
    private final Statistics.StatisticsType whenToReset;
    private final int howManyMostRecentToConsider;
    private final boolean alwaysResetWhenPossible;
    private boolean needsToReset = false;
    private int indexOfCutoff; // Inclusive. Everything before it is ignored

    public Trigger(TriggerData.TriggerName triggerName) {
        this.typeOfTrackedStatistic = TriggerData.getTypeOfTrackedStatistic(triggerName);
        this.whatToLookAt = TriggerData.getWhatToLookAt(triggerName);
        this.activationComparison = TriggerData.getActivationComparison(triggerName);
        this.activationValue = TriggerData.getActivationValue(triggerName);
        this.whenToCheck = TriggerData.getWhenToCheck(triggerName);
        this.whenToReset = TriggerData.getWhenToReset(triggerName);
        this.alwaysResetWhenPossible = TriggerData.isAlwaysResetWhenPossible(triggerName);

        if (whatToLookAt == WhatToLookAt.OCCURRENCES) {
            // The point is to see how many there are, so it should get them all.
            this.howManyMostRecentToConsider = 999999999;
        } else {
            this.howManyMostRecentToConsider = TriggerData.getHowManyMostRecentToConsider(triggerName);
        }

        reset();
    }

    public void reset() {
        this.indexOfCutoff = Math.max(Statistics.getSizeOfFullStatistics() - 1, 0);
    }

    public boolean check(Statistics.StatisticsRow newRow, int indexOfThisNewRow) {
        // Resetting and triggering is possible at the same time
        boolean activated = false;
        if (newRow.statisticsType() == whenToCheck) {
            activated = activationCheck();
            if (activated) {
                needsToReset = true;
            }
        }
        if (alwaysResetWhenPossible || (needsToReset && newRow.statisticsType() == whenToReset)) {
            indexOfCutoff = indexOfThisNewRow;
            needsToReset = false;
        }
        return activated;
    }

    private boolean activationCheck() {
        int total = 0;
        int amountSeen = 0;

        switch (whatToLookAt) {
            case VALUE -> {
                for (int i = Statistics.getSizeOfFullStatistics() - 1; i > indexOfCutoff; i--) {
                    int value = Statistics.getValue(i);

                    if (value == Integer.MIN_VALUE) {
                        // Then it was one of the `None` values
                        // Treat it as an occurrence check and return 1 so it can add 1
                        System.out.println("[!] TRIGGER LOOKED AT Value WHEN IT SHOULD'VE LOOKED AT Occurrences. --> "
                                + typeOfTrackedStatistic + " " + whatToLookAt + " " + whenToCheck + " " + whenToReset);
                        value = 1;
                    }

                    total += value;
                    amountSeen++;
                    if (amountSeen == howManyMostRecentToConsider) {
                        break;
                    }
                }
            }
            case OCCURRENCES -> {
                for (int i = Statistics.getSizeOfFullStatistics() - 1; i > indexOfCutoff; i--) {
                    if (Statistics.getType(i) == typeOfTrackedStatistic) {
                        total++;
                    }
                }
            }
        }

        switch (activationComparison) {
            case GREATER_THAN_OR_EQUAL -> {
                return total >= activationValue;
            }
            case EQUAL -> {
                return total == activationValue;
            }
            case LESS_THAN_OR_EQUAL -> {
                return total <= activationValue;
            }
            case GREATER_THAN -> {
                return total > activationValue;
            }
            case LESS_THAN -> {
                return total < activationValue;
            }
            case UNEQUAL -> {
                return total != activationValue;
            }
            default ->
                    throw new IllegalStateException("Unexpected value for `activationComparison`: " + activationComparison);
        }
    }

    public enum ActivationComparison {
        GREATER_THAN_OR_EQUAL, EQUAL, LESS_THAN_OR_EQUAL, GREATER_THAN, LESS_THAN, UNEQUAL
    }

    public enum WhatToLookAt {
        VALUE, OCCURRENCES
    }
}
