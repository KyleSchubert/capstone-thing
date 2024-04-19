package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardTypeName;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.helpers.GenericHelpers;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuController;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

public class Player {
    public static boolean combatMenuStageMustUpdatePileText;
    public static boolean combatMenuStageMustAddCard;
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
    private static Array<Card> drawPileContents;
    private static Array<Card> shufflePileContents;
    private static Array<Card> handContents;

    public static void initialize() {
        positionOnStage = new XYPair<>(18f, 22.8f);
        persistentMoney = 200;
        character = new Character(CharacterTypeName.HELMET_PENGUIN, positionOnStage.x(), positionOnStage.y());
        character.setTouchable(Touchable.disabled);
        character.faceRight();
        ownedCards = new Array<>();
        combatInformation = new CombatInformation();
        combatInformation.setPlayerInformation(true);
        flagGoBackToPreviousMenuState = false;
        ownedItems = new Array<>();
        drawPileContents = new Array<>();
        shufflePileContents = new Array<>();
        handContents = new Array<>();
        combatMenuStageMustUpdatePileText = false;
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

        for (CardTypeName cardTypeName : CardTypeName.values()) {
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

    public static boolean buyCard(int cardValue, CardTypeName cardTypeName, boolean isUpgraded) {
        if (money > cardValue) {
            changeMoney(-cardValue);

            obtainCard(cardTypeName, isUpgraded);
            return true;
        }
        return false;
    }

    public static void obtainCard(CardTypeName cardTypeName, boolean isUpgraded) {
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

        drawPileContents.clear();
        for (Card card : ownedCards) {
            card.setToGoToShufflePile(false);
        }
        drawPileContents.addAll(ownedCards);
        drawPileContents.shuffle();

        shufflePileContents.clear();

        handContents.clear();

        drawCards(5);
    }

    public static void startTurn() {
        int oldEnergyAmount = energy;
        drawCards(5);
        energy = 3;
        Statistics.restoredEnergy(energy - oldEnergyAmount);
        combatInformation.clearDefense();
    }

    public static int getEnergy() {
        return energy;
    }

    public static void setEnergy(int energy) {
        Player.energy = energy;
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

    public static TargetType getPotentialAbilityTargetType() {
        return potentialAbilityTargetType;
    }

    public static void setPotentialAbilityTargetType(TargetType potentialAbilityTargetType) {
        Player.potentialAbilityTargetType = potentialAbilityTargetType;
    }

    public static boolean isFlagGoBackToPreviousMenuState() {
        return flagGoBackToPreviousMenuState;
    }

    public static void setFlagGoBackToPreviousMenuState(boolean flag) {
        flagGoBackToPreviousMenuState = flag;
    }

    public static boolean isPointWithinRange(XYPair<Float> point) {
        return GenericHelpers.isPointWithinRange(point, positionOnStage);
    }

    public static Array<Item> getOwnedItems() {
        return ownedItems;
    }

    public static void obtainItem(ItemTypeName itemTypeName) {
        Item item = new Item(itemTypeName);
        System.out.println("Player gained item: " + itemTypeName);

        item.getGroup().setPosition(0.5f + ownedItems.size * 3, 40.1f);

        for (Item ownedItem : ownedItems) {
            if (ownedItem.getItemTypeName() == itemTypeName) {
                item.getGroup().clear();
                if (itemTypeName == ItemTypeName.JUNK) {
                    changePersistentMoney(5);
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

    public static Array<Card> getDrawPileContents() {
        return drawPileContents;
    }

    public static Array<Card> getShufflePileContents() {
        return shufflePileContents;
    }

    public static Array<Card> getHandContents() {
        return handContents;
    }

    public static void potentiallyDiscardCards() {
        for (Card card : handContents) {
            if (card.isToGoToShufflePile()) {
                card.setToGoToShufflePile(false);
                shufflePileContents.add(card);
                handContents.removeValue(card, true);
                card.getGroup().remove();
                Statistics.discardedCard();
                combatMenuStageMustUpdatePileText = true;
            }
        }
    }

    public static void endTurn() {
        shufflePileContents.addAll(handContents);
        for (int i = 0; i < handContents.size; i++) {
            Statistics.discardedCard();
        }
        handContents.clear();
    }

    public static void drawCards(int amount) {
        amount = Math.min(amount, drawPileContents.size + shufflePileContents.size);

        for (int i = 0; i < amount; i++) {
            if (drawPileContents.size == 0) {
                // Shuffle the shuffle pile into the draw pile
                System.out.println("Shuffled in.");
                System.out.println("AMOUNT: " + amount + " sizes: draw: " + drawPileContents.size + " shuffle: " + shufflePileContents.size);

                Statistics.shuffledIn(shufflePileContents.size);
                drawPileContents.addAll(shufflePileContents);
                drawPileContents.shuffle();
                shufflePileContents.clear();
            }
            Card drawnCard = drawPileContents.get(0);
            drawnCard.getGroup().setPosition(12 + handContents.size * 7, 0);

            combatMenuStageMustUpdatePileText = true;
            combatMenuStageMustAddCard = true;
            drawnCard.setToBeAddedToCombatMenuStage(true);

            handContents.add(drawnCard);
            Statistics.drewCard();
            drawPileContents.removeIndex(0);
        }
    }

    public static void setCombatMenuStageMustUpdatePileText(boolean combatMenuStageMustUpdatePileText) {
        Player.combatMenuStageMustUpdatePileText = combatMenuStageMustUpdatePileText;
    }

    public static boolean isCombatMenuStageMustAddCard() {
        return combatMenuStageMustAddCard;
    }

    public static void setCombatMenuStageMustAddCard(boolean combatMenuStageMustAddCard) {
        Player.combatMenuStageMustAddCard = combatMenuStageMustAddCard;
    }
}
