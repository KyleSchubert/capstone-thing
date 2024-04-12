package com.roguelikedeckbuilder.mygame.tracking;

import com.badlogic.gdx.utils.Array;

public class Statistics {
    private static final Array<StatisticsRow> fullStatistics = new Array<>();
    // 😏 I think I'll just write this class, profile the performance, and if it's bad then I'll change it.
    // But it'll be in this very-likely naively-done way until then.
    public static float secondsIntoRun = 0.0f;
    private static int zoneNumber = 1;
    private static int stageNumber = 1;
    private static int nodeNumber = 1;
    private static int turnNumber = 1;
    private static int runNumber = 0;

    public static void resetVariables() {
        // Does not reset fullStatistics

        secondsIntoRun = 0.0f;
        zoneNumber = 1;
        stageNumber = 1;
        nodeNumber = 1;
        turnNumber = 1;
    }

    public static void printAll() {
        // For debugging
        for (StatisticsRow row : fullStatistics) {
            System.out.printf("[%s] Value: %s, %s seconds, zone %s, stage %s, node %s, turn %s %n",
                    row.statisticsType(),
                    row.value(),
                    row.secondsIntoRun(),
                    row.zoneNumber(),
                    row.stageNumber(),
                    row.nodeNumber(),
                    row.turnNumber());
        }
    }

    public static int getSizeOfFullStatistics() {
        return fullStatistics.size;
    }

    public static int getValue(int index) {
        return fullStatistics.get(index).value();
    }

    public static StatisticsType getType(int index) {
        return fullStatistics.get(index).statisticsType();
    }

    public static int getStageNumber() {
        return stageNumber;
    }

    public static void setStageNumber(int stageNumber) {
        Statistics.stageNumber = stageNumber;
    }

    public static float getSecondsIntoRun() {
        return secondsIntoRun;
    }

    public static void setSecondsIntoRun(float secondsIntoRun) {
        Statistics.secondsIntoRun = secondsIntoRun;
    }

    public static int getZoneNumber() {
        return zoneNumber;
    }

    public static void setZoneNumber(int zoneNumber) {
        Statistics.zoneNumber = zoneNumber;
    }

    public static int getNodeNumber() {
        return nodeNumber;
    }

    public static void setNodeNumber(int nodeNumber) {
        Statistics.nodeNumber = nodeNumber;
    }

    public static int getTurnNumber() {
        return turnNumber;
    }

    public static void setTurnNumber(int turnNumber) {
        Statistics.turnNumber = turnNumber;
    }

    public static int getRunNumber() {
        return runNumber;
    }

    public static void setRunNumber(int runNumber) {
        Statistics.runNumber = runNumber;
    }

    private static void add(StatisticsType statisticsType, int value) {
        fullStatistics.add(new StatisticsRow(statisticsType, value, getSecondsIntoRun(), getZoneNumber(), getStageNumber(), getNodeNumber(), getTurnNumber()));
    }

    public static void combatStarted() {
        add(StatisticsType.COMBAT_STARTED, Integer.MIN_VALUE);
    }

    public static void combatEnded() {
        add(StatisticsType.COMBAT_ENDED, getTurnNumber());
    }

    public static void turnStarted() {
        add(StatisticsType.TURN_STARTED, Integer.MIN_VALUE);
    }

    public static void turnEnded() {
        add(StatisticsType.TURN_ENDED, Integer.MIN_VALUE);
    }

    public static void playedCard() {
        add(StatisticsType.PLAYED_CARD, Integer.MIN_VALUE);
    }

    public static void discardedCard() {
        add(StatisticsType.DISCARDED_CARD, Integer.MIN_VALUE);
    }

    public static void drewCard() {
        add(StatisticsType.DREW_CARD, Integer.MIN_VALUE);
    }

    public static void shuffledIn(int amountOfCardsThatWereInTheShufflePile) {
        add(StatisticsType.SHUFFLED_IN, amountOfCardsThatWereInTheShufflePile);
    }

    public static void spentEnergy(int amount) {
        add(StatisticsType.SPENT_ENERGY, amount);
    }

    public static void restoredEnergy(int amount) {
        add(StatisticsType.RESTORED_ENERGY, amount);
    }

    public static void enemyWasTargeted() {
        add(StatisticsType.ENEMY_WAS_TARGETED, Integer.MIN_VALUE);
    }

    public static void enemyTookDamage(int amount) {
        add(StatisticsType.ENEMY_TOOK_DAMAGE, amount);
    }

    public static void enemyDied() {
        add(StatisticsType.ENEMY_DIED, Integer.MIN_VALUE);
    }

    public static void enemyHealed(int amount) {
        add(StatisticsType.ENEMY_HEALED, amount);
    }

    public static void enemyMaxHpChanged(int amount) {
        add(StatisticsType.ENEMY_MAX_HP_CHANGED, amount);
    }

    public static void enemyGainedDefense(int amount) {
        add(StatisticsType.ENEMY_GAINED_DEFENSE, amount);
    }

    public static void playerWasTargeted() {
        add(StatisticsType.PLAYER_WAS_TARGETED, Integer.MIN_VALUE);
    }

    public static void playerTookDamage(int amount) {
        add(StatisticsType.PLAYER_TOOK_DAMAGE, amount);
    }

    public static void playerDied() {
        add(StatisticsType.PLAYER_DIED, Integer.MIN_VALUE);
    }

    public static void playerHealed(int amount) {
        add(StatisticsType.PLAYER_HEALED, amount);
    }

    public static void playerMaxHpChanged(int amount) {
        add(StatisticsType.PLAYER_MAX_HP_CHANGED, amount);
    }

    public static void playerGainedDefense(int amount) {
        add(StatisticsType.PLAYER_GAINED_DEFENSE, amount);
    }

    public static void buffOrDebuffTriggered() {
        add(StatisticsType.BUFF_OR_DEBUFF_TRIGGERED, Integer.MIN_VALUE);
    }

    public static void itemTriggered() {
        add(StatisticsType.ITEM_TRIGGERED, Integer.MIN_VALUE);
    }

    public static void enemyUsedAbility() {
        add(StatisticsType.ENEMY_USED_ABILITY, Integer.MIN_VALUE);
    }

    public static void gainedPersistentCoins(int amount) {
        add(StatisticsType.GAINED_PERSISTENT_COINS, amount);
    }

    public static void gainedCoins(int amount) {
        add(StatisticsType.GAINED_COINS, amount);
    }

    public static void gainedItem() {
        add(StatisticsType.GAINED_ITEM, Integer.MIN_VALUE);
    }

    public static void spentCoins(int amount) {
        add(StatisticsType.SPENT_COINS, amount);
    }

    public static void gainedCard() {
        add(StatisticsType.GAINED_CARD, Integer.MIN_VALUE);
    }

    public static void upgradedCard() {
        add(StatisticsType.UPGRADED_CARD, Integer.MIN_VALUE);
    }

    public static void removedCard() {
        add(StatisticsType.REMOVED_CARD, Integer.MIN_VALUE);
    }

    public static void runStarted() {
        add(StatisticsType.RUN_STARTED, getRunNumber());
    }

    public static void runEnded() {
        add(StatisticsType.RUN_ENDED, getRunNumber());
    }

    // When it says  "None"  that means it'll always return `Integer.MIN_VALUE`
    public enum StatisticsType {
        COMBAT_STARTED, // value: None
        COMBAT_ENDED, // value: int (turns)
        TURN_STARTED, // value: None
        TURN_ENDED, // value: None
        PLAYED_CARD, // value: None
        DISCARDED_CARD, // value: None
        DREW_CARD, // value: None
        SHUFFLED_IN, // value: int (amount of cards that were in the shuffle pile)
        SPENT_ENERGY, // value: int (amount)
        RESTORED_ENERGY, // value: int (amount)
        ENEMY_WAS_TARGETED, // value: None
        ENEMY_TOOK_DAMAGE, // value: int (amount)
        ENEMY_DIED, // value: None
        ENEMY_HEALED, // value: int (amount)
        ENEMY_MAX_HP_CHANGED, // value: int (amount)
        ENEMY_GAINED_DEFENSE, // value: int (amount)
        PLAYER_WAS_TARGETED, // value: None
        PLAYER_TOOK_DAMAGE, // value: int (amount)
        PLAYER_DIED, // value: None
        PLAYER_HEALED, // value: int (amount)
        PLAYER_MAX_HP_CHANGED, // value: int (amount)
        PLAYER_GAINED_DEFENSE, // value: int (amount)
        BUFF_OR_DEBUFF_TRIGGERED, // value: None
        ITEM_TRIGGERED, // value: None
        ENEMY_USED_ABILITY, // value: None
        GAINED_PERSISTENT_COINS, // value: int (amount)
        GAINED_COINS, // value: int (amount)
        GAINED_ITEM, // value: None
        SPENT_COINS, // value: int (amount)
        GAINED_CARD, // value: None
        UPGRADED_CARD, // value: None
        REMOVED_CARD, // value: None
        RUN_STARTED, // value: int (run number)
        RUN_ENDED, // value: int (run number)
    }

    public record StatisticsRow(StatisticsType statisticsType, int value, float secondsIntoRun, int zoneNumber,
                                int stageNumber, int nodeNumber, int turnNumber) {
    }
}
