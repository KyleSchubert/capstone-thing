package com.roguelikedeckbuilder.mygame.tracking.statistics;

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
