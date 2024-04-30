package com.roguelikedeckbuilder.mygame.tracking.statistics;

// When it says  "None"  that means it'll always return `Integer.MIN_VALUE`
public enum StatisticsType {
    BOSS_DIED, // value: None
    COMBAT_ENDED, // value: int (turns)
    COMBAT_STARTED, // value: None
    DISCARDED_CARD, // value: None
    DREW_CARD, // value: None
    ELITE_DIED, // value: None
    ENEMY_DIED, // value: None
    ENEMY_GAINED_DEFENSE, // value: int (amount)
    ENEMY_HEALED, // value: int (amount)
    ENEMY_MAX_HP_CHANGED, // value: int (amount)
    ENEMY_TOOK_DAMAGE, // value: int (amount)
    ENEMY_USED_ABILITY, // value: None
    ENEMY_WAS_TARGETED, // value: None
    GAINED_CARD, // value: None
    GAINED_COINS, // value: int (amount)
    GAINED_ITEM, // value: None
    GAINED_PERSISTENT_COINS, // value: int (amount)
    ITEM_TRIGGERED, // value: None
    PLAYED_CARD, // value: None
    PLAYER_DIED, // value: None
    PLAYER_GAINED_DEFENSE, // value: int (amount)
    PLAYER_HEALED, // value: int (amount)
    PLAYER_MAX_HP_CHANGED, // value: int (amount)
    PLAYER_TOOK_DAMAGE, // value: int (amount)
    PLAYER_WAS_TARGETED, // value: None
    REMOVED_CARD, // value: None
    RESTORED_ENERGY, // value: int (amount)
    RUN_ENDED, // value: int (run number)
    RUN_STARTED, // value: int (run number)
    SHUFFLED_IN, // value: int (amount of cards that were in the shuffle pile)
    SPENT_COINS, // value: int (amount)
    SPENT_ENERGY, // value: int (amount)
    TURN_ENDED, // value: None
    TURN_STARTED, // value: None
    UPGRADED_CARD // value: None

}
