package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.helpers.GenericHelpers;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

public class Player {
    private static Character character;
    private static CombatInformation combatInformation;
    private static int money;
    private static int persistentMoney;
    private static Array<Card> ownedCards;
    private static int energy;
    private static TargetType potentialAbilityTargetType;
    private static boolean flagGoBackToPreviousMenuState;
    private static XYPair<Float> positionOnStage;

    public static void initialize() {
        positionOnStage = new XYPair<>(18f, 22.8f);
        persistentMoney = 200;
        character = new Character(Character.CharacterTypeName.HELMET_PENGUIN, positionOnStage.x(), positionOnStage.y());
        character.faceRight();
        ownedCards = new Array<>();
        combatInformation = new CombatInformation();
        flagGoBackToPreviousMenuState = false;
        reset();
    }

    public static void reset() {
        money = 1500;
        combatInformation.setPositions(character.getCharacterCenter());
        combatInformation.loadPlayerStats();
        flagGoBackToPreviousMenuState = false;

        ownedCards.clear();
        for (int i = 0; i < 3; i++) {
            Card card = new Card(Card.CardData.ENERGY_SLICES, false);
            card.getGroup().addCaptureListener(card.getClickListener());
            ownedCards.add(card);
        }
        Card card = new Card(Card.CardData.FLAME, false);
        card.getGroup().addCaptureListener(card.getClickListener());
        ownedCards.add(card);

        card = new Card(Card.CardData.FIRE_STRIKE, false);
        card.getGroup().addCaptureListener(card.getClickListener());
        ownedCards.add(card);

        card = new Card(Card.CardData.FIRE_STRIKE, false);
        card.getGroup().addCaptureListener(card.getClickListener());
        card.setUpgraded(true);
        ownedCards.add(card);

        card = new Card(Card.CardData.DEFEND, false);
        card.getGroup().addCaptureListener(card.getClickListener());
        ownedCards.add(card);

        ownedCards.shuffle();
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

            obtainCard(cardData, isUpgraded);
        }
    }

    public static void obtainCard(Card.CardData cardData, boolean isUpgraded) {
        Card card = new Card(cardData, false);
        card.setUpgraded(isUpgraded);
        card.getGroup().addCaptureListener(card.getClickListener());
        ownedCards.add(card);
    }

    public static void removeCard(int cardIndex) {
        ownedCards.removeIndex(cardIndex);
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

    public static void combatStart() {
        combatInformation.setPositions(character.getCharacterCenter());
        combatInformation.setHpBarVisibility(true);
        startTurn();
    }

    public static void startTurn() {
        energy = 3;
        Player.getCombatInformation().clearDefense();
    }

    public static int getEnergy() {
        return energy;
    }

    public static boolean tryToSpendEnergy(int amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        } else {
            return false;
        }
    }

    public static void setPotentialAbilityTargetType(TargetType potentialAbilityTargetType) {
        Player.potentialAbilityTargetType = potentialAbilityTargetType;
    }

    public static TargetType getPotentialAbilityTargetType() {
        return potentialAbilityTargetType;
    }

    public static void setFlagGoBackToPreviousMenuState(boolean flag) {
        flagGoBackToPreviousMenuState = flag;
    }

    public static boolean isFlagGoBackToPreviousMenuState() {
        return flagGoBackToPreviousMenuState;
    }

    public static boolean isPointWithinRange(XYPair<Float> point) {
        return GenericHelpers.isPointWithinRange(point, positionOnStage);
    }
}
