package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardTypeName;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectData;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectType;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffect;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffectTypeName;
import com.roguelikedeckbuilder.mygame.helpers.AudioManager;
import com.roguelikedeckbuilder.mygame.helpers.SaveLoad;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.items.ItemTier;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuController;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    public static final int MAXIMUM_CARDS_IN_HAND = 10;
    public static final ArrayList<String> allUpgradeNames = new ArrayList<>();
    public static boolean combatMenuStageMustUpdatePileText;
    public static boolean combatMenuStageMustAddCard;
    private static CharacterTypeName characterTypeName;
    private static CombatInformation combatInformation;
    private static int money;
    private static int persistentMoney;
    private static int spentPersistentMoney;
    private static Map<String, Integer> upgrades;
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
        positionOnStage = new XYPair<>(360f, 576f);
        persistentMoney = 0;
        spentPersistentMoney = 0;
        setCharacterTypeName(CharacterTypeName.HELMET_PENGUIN);
        upgrades = new HashMap<>();
        ownedCards = new Array<>();
        combatInformation = new CombatInformation();
        combatInformation.setPlayerInformation(true);
        flagGoBackToPreviousMenuState = false;
        ownedItems = new Array<>();
        drawPileContents = new Array<>();
        shufflePileContents = new Array<>();
        handContents = new Array<>();
        combatMenuStageMustUpdatePileText = false;
        initializeUpgradeNames();
        reset();
    }

    private static void initializeUpgradeNames() {
        allUpgradeNames.add("upgrade-maxHP");
        allUpgradeNames.add("upgrade-draw");
        allUpgradeNames.add("upgrade-str");
        allUpgradeNames.add("upgrade-con");
        allUpgradeNames.add("upgrade-preCure");
        allUpgradeNames.add("upgrade-coins");
        allUpgradeNames.add("upgrade-x2SUPER");
        allUpgradeNames.add("upgrade-item");
        allUpgradeNames.add("upgrade-energy");
        allUpgradeNames.add("upgrade-x2Damage");
        allUpgradeNames.add("upgrade-bypassImmunity");
    }

    public static void referenceMenuController(MenuController _menuController) {
        menuController = _menuController;
    }

    public static void reset() {
        money = Player.getUpgrades().getOrDefault("upgrade-coins", 0) * 300;
        flagGoBackToPreviousMenuState = false;

        combatInformation.loadPlayerStats();

        resetToDefaultDeck();

        ownedItems.clear();

        Array<Actor> mustRemove = new Array<>();

        if (menuController != null) {
            for (Actor actor : menuController.getTopBarStage().getStage().getActors()) {
                UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
                if (actorType == UserObjectOptions.ITEM) {
                    mustRemove.add(actor);
                }
            }
        }

        for (Actor actor : mustRemove) {
            actor.remove();
        }

        if (menuController != null) {
            int bonusItems = Player.getUpgrades().getOrDefault("upgrade-item", 0);
            for (int i = 0; i < bonusItems; i++) {
                ItemTypeName itemTypeName = ItemData.getSomeRandomItemNamesByTier(ItemTier.COMMON, 1, false).first();

                // Because Millionth Dollar gives 100 SUPER Coins. So this leaves it as an exploit, but it's much smaller (fun)
                if (itemTypeName == ItemTypeName.MILLIONTH_DOLLAR) {
                    itemTypeName = ItemTypeName.JUNK;
                }

                Player.obtainItem(itemTypeName);
            }
        }
    }

    private static void resetToDefaultDeck() {
        ownedCards.clear();

        // 5 Slash cards
        for (int i = 0; i < 5; i++) {
            Card card = new Card(CardTypeName.SLASH, false);
            card.addCaptureListener(card.getClickListener());
            ownedCards.add(card);
        }
        // Upgrade the last Slash card
        ownedCards.get(ownedCards.size - 1).setUpgraded(true);

        // 5 Defend cards
        for (int i = 0; i < 5; i++) {
            Card card = new Card(CardTypeName.DEFEND, false);
            card.addCaptureListener(card.getClickListener());
            ownedCards.add(card);
        }
        // Upgrade the last Defend card
        ownedCards.get(ownedCards.size - 1).setUpgraded(true);

        ownedCards.shuffle();
    }

    public static void changeMoney(int change) {
        money += change;
        if (change > 0) {
            AudioManager.playGetCoinsSound();
            Statistics.gainedCoins(change);
        } else {
            Statistics.spentCoins(Math.abs(change));
        }
    }

    public static void changePersistentMoney(int change) {
        if (Player.getUpgrades().getOrDefault("upgrade-x2SUPER", 0) > 0) {
            change *= (int) Math.pow(2, Player.getUpgrades().get("upgrade-x2SUPER"));
        }
        persistentMoney += change;
        if (change > 0) {
            AudioManager.playGetCoinsSound();
            Statistics.gainedPersistentCoins(change);
        }
        SaveLoad.savePersistentMoney();
    }

    public static int getMoney() {
        return money;
    }

    public static int getPersistentMoney() {
        return persistentMoney;
    }

    public static void setPersistentMoney(int persistentMoney) {
        Player.persistentMoney = persistentMoney;
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
        card.addCaptureListener(card.getClickListener());
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
        Character character = new Character(characterTypeName, positionOnStage.x(), positionOnStage.y());
        character.setUserObject(UserObjectOptions.PLAYER);
        character.faceRight();
        combatInformation.setPositions(character.getCharacterCenter());
        return character;
    }

    public static void setCharacterTypeName(CharacterTypeName newCharacterTypeName) {
        characterTypeName = newCharacterTypeName;
    }

    public static Array<Card> getOwnedCards() {
        return ownedCards;
    }

    public static CombatInformation getCombatInformation() {
        return combatInformation;
    }

    public static void combatStart() {
        combatInformation.getTemporaryItems().clear();
        combatInformation.resetStatusEffects();
        combatInformation.setHpBarVisibility(true);

        drawPileContents.clear();
        for (Card card : ownedCards) {
            card.setToGoToShufflePile(false);
            card.setUsed(false);
            card.setToBeAddedToCombatMenuStage(false);
        }
        drawPileContents.addAll(ownedCards);
        drawPileContents.shuffle();

        shufflePileContents.clear();

        handContents.clear();

        startTurn();
    }

    public static void startTurn() {
        Statistics.turnStarted();
        int oldEnergyAmount = energy;
        drawCards(5 + Player.getUpgrades().getOrDefault("upgrade-draw", 0));
        energy = 3;

        if (Player.hasItem(ItemTypeName.LED_BULB)) {
            energy += 1;
        }

        energy += Player.getUpgrades().getOrDefault("upgrade-energy", 0);

        Statistics.restoredEnergy(energy - oldEnergyAmount);
        combatInformation.clearDefense();

        combatInformation.activateStartTurnStatusEffects();

        int bonusStrength = Player.getUpgrades().getOrDefault("upgrade-str", 0);
        int bonusConstitution = Player.getUpgrades().getOrDefault("upgrade-con", 0);
        int bonusPreCure = Player.getUpgrades().getOrDefault("upgrade-preCure", 0);

        if (bonusStrength > 0) {
            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.STRENGTH, bonusStrength));
        }
        if (bonusConstitution > 0) {
            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.CONSTITUTION, bonusConstitution));
        }
        if (bonusPreCure > 0) {
            combatInformation.addStatusEffect(new StatusEffect(StatusEffectTypeName.PRE_CURE, bonusPreCure));
        }
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

    public static Array<Item> getOwnedItems() {
        return ownedItems;
    }

    public static void obtainItem(ItemTypeName itemTypeName) {
        Item item = new Item(itemTypeName);
        System.out.println("Player gained item: " + itemTypeName);

        switch (ItemData.getName(itemTypeName)) {
            case "Cash Injection" -> changeMoney(500);
            case "Millionth Dollar" -> changePersistentMoney(100);
            case "Heart V2.0" -> {
                combatInformation.changeMaxHp(50);
                combatInformation.changeHp(99999);
            }
        }

        item.setPosition(10 + ownedItems.size * 60, 802);

        for (Item ownedItem : ownedItems) {
            if (itemTypeName == ItemTypeName.JUNK) {
                changePersistentMoney(5);
                AudioManager.playFunnyTadaSound();
            } else if (ownedItem.getItemTypeName() == itemTypeName) {
                item.clear();
                changePersistentMoney(5);
                AudioManager.playFunnyTadaSound();
                return;
            }
        }

        menuController.getTopBarStage().addActor(item);

        ownedItems.add(item);
        Statistics.gainedItem();
        AudioManager.playGetItemSound();
    }

    public static boolean hasItem(ItemTypeName itemTypeName) {
        for (Item ownedItem : ownedItems) {
            if (ownedItem.getItemTypeName() == itemTypeName) {
                return true;
            }
        }
        return false;
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

    public static boolean potentiallyDiscardCards() {
        boolean discardedSomething = false;
        for (Card card : handContents) {
            if (card.isToGoToShufflePile()) {
                card.setToGoToShufflePile(false);

                handContents.removeValue(card, true);
                card.remove();
                Statistics.discardedCard();
                combatMenuStageMustUpdatePileText = true;
                discardedSomething = true;

                // Temporary_item type cards normally are deleted after use, instead of being sent to the shuffle pile.
                // For all other types or if a temporary_item type card is discarded another way, it's sent to the shuffle pile like this
                if (!card.isDiscardedByUse() || EffectData.getEffectType(AbilityData.getEffect(card.getUsedAbilityTypeName())) != EffectType.TEMPORARY_ITEM) {
                    shufflePileContents.add(card);
                }
            }
        }
        return discardedSomething;
    }

    public static void endTurn() {
        shufflePileContents.addAll(handContents);
        for (int i = 0; i < handContents.size; i++) {
            Statistics.discardedCard();
        }
        handContents.clear();

        combatInformation.activateEndTurnStatusEffects();
        combatInformation.tickDownDebuffStatusEffects();
    }

    public static void drawCards(int amount) {
        amount = Math.min(amount, drawPileContents.size + shufflePileContents.size);
        amount = Math.min(amount, MAXIMUM_CARDS_IN_HAND - handContents.size);
        if (amount <= 0) {
            return;
        }

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
            drawnCard.clearActions();
            drawnCard.setPosition(-4000, 0);

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

    public static void discardOneRandomCard() {
        // If there are no cards to discard, that is OK
        Array<Integer> optionsByIndex = new Array<>();
        for (int i = 0; i < handContents.size; i++) {
            optionsByIndex.add(i);
        }
        optionsByIndex.shuffle();

        for (int index : optionsByIndex) {
            Card card = handContents.get(index);
            if (!card.isUsed()) {
                card.setToGoToShufflePile(true);
                card.setDiscardedByUse(false);
                break;
            }
        }
    }

    public static XYPair<Float> getPositionOnStage() {
        return positionOnStage;
    }

    public static boolean buyItem(int itemValue, ItemTypeName itemTypeName) {
        if (money > itemValue) {
            changeMoney(-itemValue);
            obtainItem(itemTypeName);
            return true;
        }
        return false;
    }

    public static Map<String, Integer> getUpgrades() {
        return upgrades;
    }

    public static int getSpentPersistentMoney() {
        return spentPersistentMoney;
    }

    public static void setSpentPersistentMoney(int spentPersistentMoney) {
        Player.spentPersistentMoney = spentPersistentMoney;
    }

    public static void changeSpentPersistentMoney(int change) {
        if (change < 0) {
            System.out.println("SOMETHING TRIED TO CHANGE THE SPENT SUPER COIN AMOUNT with - instead of +");
            return;
        }
        spentPersistentMoney += change;
    }
}
