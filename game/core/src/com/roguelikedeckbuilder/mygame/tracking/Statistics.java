package com.roguelikedeckbuilder.mygame.tracking;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.BuffOrDebuffData;
import com.roguelikedeckbuilder.mygame.combat.Enemy;
import com.roguelikedeckbuilder.mygame.items.ItemData;

public class Statistics {
    private static final Array<StatisticsRow> fullStatistics = new Array<>();
    // 😏 I think I'll just write this class, profile the performance, and if it's bad then I'll change it.
    // But it'll be in this very-likely naively-done way until then.
    // TODO: Also, wouldn't some of these like the ones that use Card need to make a copy and not just use the original?
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

    private static void add(StatisticsType statisticsType, Object value) {
        fullStatistics.add(new StatisticsRow(statisticsType, value, getSecondsIntoRun(), getZoneNumber(), getStageNumber(), getNodeNumber(), getTurnNumber()));
    }

    public static void combatStarted(Array<Enemy> initialEnemies) {
        add(StatisticsType.COMBAT_STARTED, initialEnemies);
    }

    public static void combatEnded() {
        add(StatisticsType.COMBAT_ENDED, getTurnNumber());
    }

    public static void turnStarted() {
        add(StatisticsType.TURN_STARTED, "");
    }

    public static void turnEnded() {
        add(StatisticsType.TURN_ENDED, "");
    }

    public static void playedCard(Card usedCard) {
        add(StatisticsType.PLAYED_CARD, usedCard);
    }

    public static void discardedCard(DiscardReason discardReason) {
        add(StatisticsType.DISCARDED_CARD, discardReason);
    }

    public static void drewCard(Card drawnCard) {
        add(StatisticsType.DREW_CARD, drawnCard);
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

    public static void enemyWasTargeted(Enemy enemy) {
        add(StatisticsType.ENEMY_WAS_TARGETED, enemy);
    }

    public static void enemyTookDamage(int amount) {
        add(StatisticsType.ENEMY_TOOK_DAMAGE, amount);
    }

    public static void enemyDied() {
        add(StatisticsType.ENEMY_DIED, "");
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
        add(StatisticsType.PLAYER_WAS_TARGETED, "");
    }

    public static void playerTookDamage(int amount) {
        add(StatisticsType.PLAYER_TOOK_DAMAGE, amount);
    }

    public static void playerDied() {
        add(StatisticsType.PLAYER_DIED, "");
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

    public static void buffOrDebuffTriggered(BuffOrDebuffData.BuffOrDebuffName buffOrDebuffName) {
        add(StatisticsType.BUFF_OR_DEBUFF_TRIGGERED, buffOrDebuffName);
    }

    public static void itemTriggered(ItemData.ItemName itemName) {
        add(StatisticsType.ITEM_TRIGGERED, itemName);
    }

    public static void enemyUsedAbility(AbilityData.AbilityTypeName usedAbilityTypeName) {
        add(StatisticsType.ENEMY_USED_ABILITY, usedAbilityTypeName);
    }

    public static void gainedPersistentCoins(int amount) {
        add(StatisticsType.GAINED_PERSISTENT_COINS, amount);
    }

    public static void gainedCoins(int amount) {
        add(StatisticsType.GAINED_COINS, amount);
    }

    public static void gainedItem(ItemData.ItemName itemName) {
        add(StatisticsType.GAINED_ITEM, itemName);
    }

    public static void spentCoins(int amount) {
        add(StatisticsType.SPENT_COINS, amount);
    }

    public static void gainedCard(Card gainedCard) {
        add(StatisticsType.GAINED_CARD, gainedCard);
    }

    public static void upgradedCard(Card cardThatWasUpgraded) {
        add(StatisticsType.UPGRADED_CARD, cardThatWasUpgraded);
    }

    public static void removedCard(Card cardThatWasRemoved) {
        add(StatisticsType.REMOVED_CARD, cardThatWasRemoved);
    }

    public static void runStarted() {
        add(StatisticsType.RUN_STARTED, getRunNumber());
    }

    public static void runEnded() {
        add(StatisticsType.RUN_ENDED, getRunNumber());
    }

    public enum StatisticsType {
        COMBAT_STARTED, // value: Array<Enemy> (initial enemies)
        COMBAT_ENDED, // value: int (turns)
        TURN_STARTED, // value: None
        TURN_ENDED, // value: None
        PLAYED_CARD, // value: Card (the used card)
        DISCARDED_CARD, // value: reason it was discarded | played card - other card - item - end turn
        DREW_CARD, // value: Card (the drawn card)
        SHUFFLED_IN, // value: int (amount of cards that were in the shuffle pile)
        SPENT_ENERGY, // value: int (amount)
        RESTORED_ENERGY, // value: int (amount)
        ENEMY_WAS_TARGETED, // value: Enemy TODO: I wonder if doing this makes it never be able to be garbage-collected
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
        BUFF_OR_DEBUFF_TRIGGERED, // value: BuffOrDebuffName (the triggered buff or debuff)
        ITEM_TRIGGERED, // value: ItemName (the triggered item)
        ENEMY_USED_ABILITY, // value: AbilityTypeName (the used ability)
        GAINED_PERSISTENT_COINS, // value: int (amount)
        GAINED_COINS, // value: int (amount)
        GAINED_ITEM, // value: ItemName (the gained item)
        SPENT_COINS, // value: int (amount)
        GAINED_CARD, // value: Card (the gained card)
        UPGRADED_CARD, // value: Card (the card that was upgraded)
        REMOVED_CARD, // value: Card (the removed card)
        RUN_STARTED, // value: int (run number)
        RUN_ENDED, // value: int (run number)
    }

    public enum DiscardReason {
        OTHER_CARD, ITEM, PLAYED_CARD, END_TURN
    }

    private record StatisticsRow(StatisticsType statisticsType, Object value, float secondsIntoRun, int zoneNumber,
                                 int stageNumber, int nodeNumber, int turnNumber) {
    }
}
