package com.roguelikedeckbuilder.mygame.tracking;

import com.badlogic.gdx.utils.Array;

public class Statistics {
    // 😏 I think I'll just write this class, profile the performance, and if it's bad then I'll change it.
    // But it'll be in this very-likely naively-done way until then.
    public static float secondsIntoRun = 0.0f;
    private static int zoneNumber = 1;
    private static int stageNumber = 1;
    private static int nodeNumber = 1;
    private static int turnNumber = 1;
    private static final Array<Array<StatisticsRow>> fullStatistics = new Array<>();

    public static void resetVariables() {
        // Does not reset fullStatistics

        secondsIntoRun = 0.0f;
        zoneNumber = 1;
        stageNumber = 1;
        nodeNumber = 1;
        turnNumber = 1;
    }

    private record StatisticsRow(StatisticsType statisticsType, Object value, float secondsIntoRun, int zoneNumber,
                                 int stageNumber, int nodeNumber, int turnNumber) {
    }

    private void add(StatisticsType statisticsType, Object value) {
        StatisticsRow row = new StatisticsRow(statisticsType, value, getSecondsIntoRun(), getZoneNumber(), getStageNumber(), getNodeNumber(), getTurnNumber());
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

    public enum StatisticsType {
        COMBAT_STARTED, // value: Array<Enemy> (initial enemies)
        COMBAT_ENDED, // value: int (turns)
        TURN_STARTED, // value: None
        TURN_ENDED, // value: None
        PLAYED_CARD, // value: Card (the used card)
        DISCARDED_CARD, // value: reason it was discarded | card - item - end turn
        DREW_CARD, // value: Card (the drawn card)
        SPENT_ENERGY, // value: int (amount)
        RESTORED_ENERGY, // value: int (amount)
        PLAYER_TOOK_DAMAGE, // value: int (amount)
        PLAYER_DIED, // value: cause of death | enemy - item - card - debuff
        PLAYER_HEALED, // value: int (amount)
        PLAYER_MAX_HP_CHANGED, // value: int (amount)
        ENEMY_USED_ABILITY, // value: AbilityTypeName (the used ability)
        GAINED_PERSISTENT_COINS, // value: int (amount)
        GAINED_COINS, // value: int (amount)
        GAINED_ITEM, // value: ItemName (the gained item)
        SPENT_COINS, // value: int (amount)
        BOUGHT_CARD, // value: Card (the bought card)
        UPGRADED_CARD, // value: Card (the card that was upgraded)
        REMOVED_CARD, // value: Card (the removed card)
        RUN_STARTED, // value: int (run number)
        RUN_ENDED, // value: int (run number)
    }
}
