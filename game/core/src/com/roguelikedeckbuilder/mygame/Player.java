package com.roguelikedeckbuilder.mygame;

import com.roguelikedeckbuilder.mygame.characters.Character;

public class Player {
    private static Character character;
    private static int maxHp;
    private static int hp;
    private static int money;
    private static int persistentMoney;

    public static void initialize() {
        persistentMoney = 200;
        reset();
        character = new Character(Character.CharacterTypeName.HELMET_PENGUIN, 24, 4);
        character.faceRight();
    }

    public static void reset() {
        maxHp = 70;
        hp = maxHp;
        money = 1500;
    }

    public static void changeMoney(int change) {
        money += change;
    }

    public static void changeHp(int change) {
        if (hp + change >= 1) {
            hp = Math.min(hp + change, maxHp);
        } else {
            hp = 0;
        }
    }

    public static void changeMaxHp(int change) {
        if (maxHp + change >= 1) {
            maxHp += change;
            changeHp(change);
        } else {
            maxHp = 1;
            hp = 1;
        }
    }

    public static void changePersistentMoney(int change) {
        persistentMoney += change;
    }

    public static int getMaxHp() {
        return maxHp;
    }

    public static int getHp() {
        return hp;
    }

    public static int getMoney() {
        return money;
    }

    public static int getPersistentMoney() {
        return persistentMoney;
    }

    public static void buyCard(int cardValue, String cardName) {
        if (money > cardValue) {
            changeMoney(-cardValue);
        }
    }
}
