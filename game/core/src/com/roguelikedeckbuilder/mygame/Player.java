package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.animated.Character;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.helpers.GenericHelpers;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.tracking.Statistics;

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
    private static Array<Item> ownedItems;
    private static MenuController menuController;

    public static void initialize() {
        positionOnStage = new XYPair<>(18f, 22.8f);
        persistentMoney = 200;
        character = new Character(Character.CharacterTypeName.HELMET_PENGUIN, positionOnStage.x(), positionOnStage.y());
        character.setTouchable(Touchable.disabled);
        character.faceRight();
        ownedCards = new Array<>();
        combatInformation = new CombatInformation();
        combatInformation.setPlayerInformation(true);
        flagGoBackToPreviousMenuState = false;
        ownedItems = new Array<>();
        reset();
    }

    public static void referenceMenuController(MenuController _menuController) {
        menuController = _menuController;
    }

    public static void reset() {
        money = 1500;
        combatInformation.setPositions(character.getCharacterCenter());
        combatInformation.loadPlayerStats();
        flagGoBackToPreviousMenuState = false;

        ownedCards.clear();

        for (CardData.CardTypeName cardTypeName : CardData.CardTypeName.values()) {
            Card card = new Card(cardTypeName, false);
            card.getGroup().addCaptureListener(card.getClickListener());
            ownedCards.add(card);

            Card cardUpgraded = new Card(cardTypeName, false);
            cardUpgraded.setUpgraded(true);
            cardUpgraded.getGroup().addCaptureListener(cardUpgraded.getClickListener());
            ownedCards.add(cardUpgraded);
        }

        ownedCards.shuffle();

        ownedItems.clear();

        Array<Actor> mustRemove = new Array<>();

        if (menuController != null) {
            for (Actor actor : menuController.getTopBarStage().getActors()) {
                UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
                if (actorType == UserObjectOptions.ITEM) {
                    mustRemove.add(actor);
                }
            }
        }

        for (Actor actor : mustRemove) {
            actor.remove();
        }
    }

    public static void changeMoney(int change) {
        money += change;
        if (change > 0) {
            SoundManager.playGetCoinsSound();
            Statistics.gainedCoins(change);
        } else {
            Statistics.spentCoins(Math.abs(change));
        }
    }

    public static void changePersistentMoney(int change) {
        persistentMoney += change;
        if (change > 0) {
            SoundManager.playGetCoinsSound();
            Statistics.gainedPersistentCoins(change);
        }
    }

    public static int getMoney() {
        return money;
    }

    public static int getPersistentMoney() {
        return persistentMoney;
    }

    public static boolean buyCard(int cardValue, CardData.CardTypeName cardTypeName, boolean isUpgraded) {
        if (money > cardValue) {
            changeMoney(-cardValue);

            obtainCard(cardTypeName, isUpgraded);
            return true;
        }
        return false;
    }

    public static void obtainCard(CardData.CardTypeName cardTypeName, boolean isUpgraded) {
        Card card = new Card(cardTypeName, false);
        card.setUpgraded(isUpgraded);
        if (isUpgraded) {
            Statistics.upgradedCard();
        }
        card.getGroup().addCaptureListener(card.getClickListener());
        Statistics.gainedCard();
        ownedCards.add(card);
    }

    public static void removeCard(int cardIndex, boolean track) {
        if (track) {
            ownedCards.removeIndex(cardIndex);
            Statistics.removedCard();
        } else {
            ownedCards.removeIndex(cardIndex);
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

    public static void combatStart() {
        combatInformation.setPositions(character.getCharacterCenter());
        combatInformation.setHpBarVisibility(true);
        startTurn();
    }

    public static void startTurn() {
        int oldEnergyAmount = energy;
        energy = 3;
        Statistics.restoredEnergy(energy - oldEnergyAmount);
        Player.getCombatInformation().clearDefense();
    }

    public static int getEnergy() {
        return energy;
    }

    public static boolean tryToSpendEnergy(int amount) {
        if (energy >= amount) {
            energy -= amount;
            Statistics.spentEnergy(amount);
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

    public static Array<Item> getOwnedItems() {
        return ownedItems;
    }

    public static void obtainItem(ItemData.ItemTypeName itemTypeName) {
        Item item = new Item(itemTypeName);
        System.out.println("Player gained item: " + itemTypeName);

        item.getGroup().setPosition(0.5f + ownedItems.size * 3, 40.1f);

        for (Item ownedItem : ownedItems) {
            if (ownedItem.getItemTypeName() == itemTypeName) {
                item.getGroup().clear();
                if (itemTypeName == ItemData.ItemTypeName.JUNK) {
                    Player.changePersistentMoney(5);
                }
                SoundManager.playFunnyTadaSound();
                return;
            }
        }

        menuController.getTopBarStage().addActor(item.getGroup());

        ownedItems.add(item);
        Statistics.gainedItem();
        SoundManager.playGetItemSound();
    }
}
