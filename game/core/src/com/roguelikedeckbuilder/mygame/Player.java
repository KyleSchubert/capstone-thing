package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;

public class Player {
    private static Character character;
    private static int maxHp;
    private static int hp;
    private static int money;
    private static int persistentMoney;
    private static Array<Card> ownedCards;

    public static void initialize() {
        persistentMoney = 200;
        character = new Character(Character.CharacterTypeName.HELMET_PENGUIN, 46, 22.8f);
        character.faceRight();
        ownedCards = new Array<>();
        reset();
    }

    public static void reset() {
        maxHp = 70;
        hp = maxHp;
        money = 1500;

        ownedCards.clear();
        for (int i = 0; i < 5; i++) {
            Card card = new Card(Card.CardData.TEST2, false);
            ownedCards.add(card);
        }
        for (int i = 0; i < 3; i++) {
            Card card = new Card(Card.CardData.TEST3, false);
            ownedCards.add(card);
        }
        for (int i = 0; i < 3; i++) {
            Card card = new Card(Card.CardData.DEFAULT, false);
            ownedCards.add(card);
        }
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

    public static void buyCard(int cardValue, Card.CardData cardData, boolean isUpgraded) {
        if (money > cardValue) {
            changeMoney(-cardValue);

            Card boughtCard = new Card(cardData, false);
            boughtCard.setUpgraded(isUpgraded);
            ownedCards.add(boughtCard);
        }
    }

    public static Character getCharacter() {
        return character;
    }

    public static Array<Card> getOwnedCards() {
        return ownedCards;
    }
}
