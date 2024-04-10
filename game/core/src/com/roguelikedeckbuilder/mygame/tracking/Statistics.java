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
    public static float secondsIntoRun = 0.0f;
    private static int zoneNumber = 1;
    private static int stageNumber = 1;
    private static int nodeNumber = 1;
    private static int turnNumber = 1;

    public static void resetVariables() {
        // Does not reset fullStatistics

        secondsIntoRun = 0.0f;
        zoneNumber = 1;
        stageNumber = 1;
        nodeNumber = 1;
        turnNumber = 1;
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

    private void add(StatisticsType statisticsType, Object value) {
        fullStatistics.add(new StatisticsRow(statisticsType, value, getSecondsIntoRun(), getZoneNumber(), getStageNumber(), getNodeNumber(), getTurnNumber()));
    }

    public void combatStarted(Array<Enemy> initialEnemies) {
        add(StatisticsType.COMBAT_STARTED, initialEnemies);
    }

    public void combatEnded(int turns) {
        add(StatisticsType.COMBAT_ENDED, turns);
    }

    public void turnStarted() {
        add(StatisticsType.TURN_STARTED, "");
    }

    public void turnEnded() {
        add(StatisticsType.TURN_ENDED, "");
    }

    public void playedCard(Card usedCard) {
        add(StatisticsType.PLAYED_CARD, usedCard);
    }

    public void discardedCard(DiscardReason discardReason) {
        add(StatisticsType.DISCARDED_CARD, discardReason);
    }

    public void drewCard(Card drawnCard) {
        add(StatisticsType.DREW_CARD, drawnCard);
    }

    public void spentEnergy(int amount) {
        add(StatisticsType.SPENT_ENERGY, amount);
    }

    public void restoredEnergy(int amount) {
        add(StatisticsType.RESTORED_ENERGY, amount);
    }

    public void playerTookDamage(int amount) {
        add(StatisticsType.PLAYER_TOOK_DAMAGE, amount);
    }

    public void playerDied(CauseOfDeath causeOfDeath) {
        add(StatisticsType.PLAYER_DIED, causeOfDeath);
    }

    public void playerHealed(int amount) {
        add(StatisticsType.PLAYER_HEALED, amount);
    }

    public void playerMaxHpChanged(int amount) {
        add(StatisticsType.PLAYER_MAX_HP_CHANGED, amount);
    }

    public void buffOrDebuffTriggered(BuffOrDebuffData.BuffOrDebuffName buffOrDebuffName) {
        add(StatisticsType.BUFF_OR_DEBUFF_TRIGGERED, buffOrDebuffName);
    }

    public void itemTriggered(ItemData.ItemName itemName) {
        add(StatisticsType.ITEM_TRIGGERED, itemName);
    }

    public void enemyUsedAbility(AbilityData.AbilityTypeName usedAbilityTypeName) {
        add(StatisticsType.ENEMY_USED_ABILITY, usedAbilityTypeName);
    }

    public void gainedPersistentCoins(int amount) {
        add(StatisticsType.GAINED_PERSISTENT_COINS, amount);
    }

    public void gainedCoins(int amount) {
        add(StatisticsType.GAINED_COINS, amount);
    }

    public void gainedItem(ItemData.ItemName itemName) {
        add(StatisticsType.GAINED_ITEM, itemName);
    }

    public void spentCoins(int amount) {
        add(StatisticsType.SPENT_COINS, amount);
    }

    public void boughtCard(Card boughtCard) {
        add(StatisticsType.BOUGHT_CARD, boughtCard);
    }

    public void upgradedCard(Card cardThatWasUpgraded) {
        add(StatisticsType.UPGRADED_CARD, cardThatWasUpgraded);
    }

    public void removedCard(Card cardThatWasRemoved) {
        add(StatisticsType.REMOVED_CARD, cardThatWasRemoved);
    }

    public void runStarted(int runNumber) {
        add(StatisticsType.RUN_STARTED, runNumber);
    }

    public void runEnded(int runNumber) {
        add(StatisticsType.RUN_ENDED, runNumber);
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
        BUFF_OR_DEBUFF_TRIGGERED, // value: BuffOrDebuffName (the triggered buff or debuff)
        ITEM_TRIGGERED, // value: ItemName (the triggered item)
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

    public enum DiscardReason {
        OTHER_CARD, ITEM, END_TURN
    }

    public enum CauseOfDeath {
        ENEMY, ITEM, CARD, DEBUFF
    }

    private record StatisticsRow(StatisticsType statisticsType, Object value, float secondsIntoRun, int zoneNumber,
                                 int stageNumber, int nodeNumber, int turnNumber) {
    }
}
