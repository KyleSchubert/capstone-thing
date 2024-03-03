package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;

public class Player {
    private static Character character;
    private static CombatInformation combatInformation;
    private static int money;
    private static int persistentMoney;
    private static Array<Card> ownedCards;

    public static void initialize() {
        persistentMoney = 200;
        character = new Character(Character.CharacterTypeName.HELMET_PENGUIN, 46, 22.8f);
        character.faceRight();
        ownedCards = new Array<>();
        combatInformation = new CombatInformation();
        reset();
    }

    public static void reset() {
        money = 1500;
        combatInformation.loadPlayerStats();

        ownedCards.clear();
        for (int i = 0; i < 5; i++) {
            Card card = new Card(Card.CardData.TEST2, false);
            card.getGroup().addCaptureListener(card.getClickListener());
            ownedCards.add(card);
        }
        for (int i = 0; i < 3; i++) {
            Card card = new Card(Card.CardData.TEST3, false);
            card.getGroup().addCaptureListener(card.getClickListener());
            ownedCards.add(card);
        }
        for (int i = 0; i < 3; i++) {
            Card card = new Card(Card.CardData.DEFAULT, false);
            card.getGroup().addCaptureListener(card.getClickListener());
            ownedCards.add(card);
        }
    }

    public static void changeMoney(int change) {
        money += change;
    }

    public static void changePersistentMoney(int change) {
        persistentMoney += change;
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
            boughtCard.getGroup().addCaptureListener(boughtCard.getClickListener());
            ownedCards.add(boughtCard);
        }
    }

    public static Character getCharacter() {
        return character;
    }

    public static Array<Card> getOwnedCards() {
        return ownedCards;
    }

    public static CombatInformation getCombatInformation() {
        return combatInformation;
    }
}
