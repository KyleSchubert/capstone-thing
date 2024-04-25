package com.roguelikedeckbuilder.mygame.tracking.statistics;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.CombatMenuStage;

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

    public static int countOccurrencesInCurrentRun(StatisticsType statisticsType) {
        int total = 0;

        for (int i = fullStatistics.size - 1; i >= 0; i--) {
            if (fullStatistics.get(i).statisticsType() == StatisticsType.RUN_STARTED) {
                break;
            }
            if (fullStatistics.get(i).statisticsType() == statisticsType) {
                total++;
            }
        }
        return total;
    }

    public static int sumValuesOfOccurrencesInCurrentRun(StatisticsType statisticsType) {
        int total = 0;

        for (int i = fullStatistics.size - 1; i >= 0; i--) {
            if (fullStatistics.get(i).statisticsType() == StatisticsType.RUN_STARTED) {
                break;
            }
            if (fullStatistics.get(i).statisticsType() == statisticsType) {
                total += fullStatistics.get(i).value();
            }
        }
        return total;
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
        int indexOfThisNewRow = fullStatistics.size;
        fullStatistics.add(new StatisticsRow(statisticsType, value, getSecondsIntoRun(), getZoneNumber(), getStageNumber(), getNodeNumber(), getTurnNumber()));
        recheckAllTriggers(fullStatistics.get(indexOfThisNewRow), indexOfThisNewRow);
    }

    private static void recheckAllTriggers(Statistics.StatisticsRow newRow, int indexOfThisNewRow) {
        int amountOfItems = Player.getOwnedItems().size;
        for (int i = 0; i < amountOfItems; i++) {
            Player.getOwnedItems().get(i).checkTrigger(newRow, indexOfThisNewRow);
        }

        // Temporary items (during combat only)
        int amountOfPlayerTemporaryItems = Player.getCombatInformation().getTemporaryItems().size;
        for (int i = 0; i < amountOfPlayerTemporaryItems; i++) {
            Player.getCombatInformation().getTemporaryItems().get(i).checkTrigger(newRow, indexOfThisNewRow);
        }
        Player.getCombatInformation().removeUsedUpTemporaryItems();

        for (int i = 0; i < CombatMenuStage.getCombatInformationForLivingEnemies().size; i++) {
            Array<Item> temporaryItems = CombatMenuStage.getCombatInformationForLivingEnemies().get(i).getTemporaryItems();
            int amountOfTemporaryItems = temporaryItems.size;
            for (int j = 0; j < amountOfTemporaryItems; j++) {
                temporaryItems.get(j).checkTrigger(newRow, indexOfThisNewRow);
            }
            CombatMenuStage.getCombatInformationForLivingEnemies().get(i).removeUsedUpTemporaryItems();
        }
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

    public record StatisticsRow(StatisticsType statisticsType, int value, float secondsIntoRun, int zoneNumber,
                                int stageNumber, int nodeNumber, int turnNumber) {
    }
}
