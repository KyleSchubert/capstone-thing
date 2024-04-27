package com.roguelikedeckbuilder.mygame.stages.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.cards.CardTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.items.ItemTier;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import java.util.Random;


public class ShopMenuStage extends GenericStage {
    private final Label upgradeCostLabel;
    private final Group nonCardShopUI;
    private final Image upgradeButton;
    private final Image upgradeButtonNoInteraction;
    private final Image removeCardButton;
    private final Image removeCardButtonNoInteraction;
    private final Label removeCardCostLabel;
    private final Integer[] upgradeCost = new Integer[]{150};
    private final Integer[] removeCardCost = new Integer[]{200};
    private int numberOfCards = 0;
    private int numberOfItems = 0;

    public ShopMenuStage() {
        super("shop");
        super.getStageBackgroundActor().setPosition(100, 60);

        ImageButton exitButton = ClickListenerManager.getMenuSwitchingButton(
                "exit", MenuState.MAP, MenuSoundType.CLOSE, 1136, 640);
        addActor(exitButton);

        nonCardShopUI = new Group();
        addActor(nonCardShopUI);

        upgradeCostLabel = LabelMaker.newLabel("Price: 150", LabelMaker.getMedium());
        upgradeCostLabel.setPosition(1160, 520);
        nonCardShopUI.addActor(upgradeCostLabel);

        upgradeButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Upgrade.png")));
        upgradeButton.setPosition(1110, 550);
        upgradeButton.addListener(ClickListenerManager.increasingCostInShop(upgradeCost, upgradeCostLabel));
        upgradeButton.addListener(ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.SILENT));
        upgradeButton.addListener(ClickListenerManager.preparingCardUpgradeMenu());

        upgradeButtonNoInteraction = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Upgrade.png")));
        upgradeButtonNoInteraction.setPosition(1110, 550);

        removeCardCostLabel = LabelMaker.newLabel("Price: 200", LabelMaker.getMedium());
        removeCardCostLabel.setPosition(1160, 420);
        nonCardShopUI.addActor(removeCardCostLabel);

        removeCardButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Remove card.png")));
        removeCardButton.setPosition(1120, 450);
        removeCardButton.addListener(ClickListenerManager.increasingCostInShop(removeCardCost, removeCardCostLabel));
        removeCardButton.addListener(ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.SILENT));
        removeCardButton.addListener(ClickListenerManager.preparingCardRemoveMenu());

        removeCardButtonNoInteraction = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Remove card.png")));
        removeCardButtonNoInteraction.setPosition(1120, 450);
    }

    public void generateShop() {
        reset();

        numberOfItems = 0;
        numberOfCards = 0;
        Random random = new Random();

        int howManyCardsWillBeUpgraded = random.nextInt(3) + 2;
        Array<Boolean> whichAreUpgraded = new Array<>();
        for (int i = 0; i < howManyCardsWillBeUpgraded; i++) {
            whichAreUpgraded.add(true);
        }
        while (whichAreUpgraded.size < 8) {
            whichAreUpgraded.add(false);
        }
        whichAreUpgraded.shuffle();

        Array<CardTypeName> cardTypeNames = CardData.getSomeRandomCards(8, true);
        for (int i = 0; i < 8; i++) {
            Card card = new Card(cardTypeNames.get(i), true);
            card.setUpgraded(whichAreUpgraded.get(i));
            card.getGroup().addCaptureListener(ClickListenerManager.buyingCard(card));
            addCard(card);
        }

        Array<ItemTypeName> itemTypeNames = ItemData.getSomeRandomItemNamesByTier(ItemTier.ANY, 3, false);
        for (int i = 0; i < 3; i++) {
            Item item = new Item(itemTypeNames.get(i));
            item.setShowPriceLabel(true);
            item.getGroup().addCaptureListener(ClickListenerManager.buyingItem(item));
            addItem(item);
        }

        upgradeCost[0] = 150;
        upgradeCostLabel.setText("Price: " + upgradeCost[0]);

        removeCardCost[0] = 200;
        removeCardCostLabel.setText("Price: " + removeCardCost[0]);

        useCorrectButtons();
    }

    private void reset() {
        for (int i = getStage().getActors().size - 1; i >= 0; i--) {
            Actor someActor = getStage().getActors().get(i);
            if (someActor.getUserObject() == UserObjectOptions.CARD || someActor.getUserObject() == UserObjectOptions.ITEM) {
                someActor.remove();
            }
        }
    }

    public void addCard(Card card) {
        Group cardGroup = card.getGroup();

        ShopPositions position = switch (numberOfCards) {
            case 0 -> ShopPositions.CARD1;
            case 1 -> ShopPositions.CARD2;
            case 2 -> ShopPositions.CARD3;
            case 3 -> ShopPositions.CARD4;
            case 4 -> ShopPositions.CARD5;
            case 5 -> ShopPositions.CARD6;
            case 6 -> ShopPositions.CARD7;
            case 7 -> ShopPositions.CARD8;
            default -> throw new IllegalStateException("Unexpected value: " + numberOfCards);
        };

        cardGroup.setPosition(position.x, position.y);
        numberOfCards += 1;

        addActor(cardGroup);
    }

    public void useCorrectButtons() {
        // Depending on the amount of money the player has, switch the buttons out for the ones that don't work
        upgradeButton.remove();
        upgradeButtonNoInteraction.remove();

        if (Player.getMoney() >= upgradeCost[0]) {
            nonCardShopUI.addActor(upgradeButton);
        } else {
            nonCardShopUI.addActor(upgradeButtonNoInteraction);
        }

        removeCardButton.remove();
        removeCardButtonNoInteraction.remove();

        if (Player.getMoney() >= removeCardCost[0]) {
            nonCardShopUI.addActor(removeCardButton);
        } else {
            nonCardShopUI.addActor(removeCardButtonNoInteraction);
        }
    }

    public void setCardSold(Card card) {
        float posX = card.getGroup().getX();
        float posY = card.getGroup().getY();
        getStage().getActors().removeValue(card.getGroup(), true);
        Card newCard = new Card(CardTypeName.OUT_OF_STOCK, false);
        newCard.getGroup().setPosition(posX, posY);
        addActor(newCard.getGroup());
    }

    public void setItemSold(Item item) {
        float posX = item.getGroup().getX();
        float posY = item.getGroup().getY();
        getStage().getActors().removeValue(item.getGroup(), true);
        Item newItem = new Item(ItemTypeName.JUNK);
        newItem.setShowPriceLabel(true);
        newItem.getGroup().addCaptureListener(ClickListenerManager.buyingItem(newItem));
        newItem.getGroup().setPosition(posX, posY);
        addActor(newItem.getGroup());
    }

    public void addItem(Item item) {
        Group itemGroup = item.getGroup();

        ShopPositions position = switch (numberOfItems) {
            case 0 -> ShopPositions.ITEM1;
            case 1 -> ShopPositions.ITEM2;
            case 2 -> ShopPositions.ITEM3;
            default -> throw new IllegalStateException("Unexpected value: " + numberOfItems);
        };

        itemGroup.setPosition(position.x, position.y);
        numberOfItems += 1;

        addActor(itemGroup);
    }

    private enum ShopPositions {
        CARD1(120, 412),
        CARD2(360, 412),
        CARD3(600, 412),
        CARD4(840, 412),
        CARD5(120, 82),
        CARD6(360, 82),
        CARD7(600, 82),
        CARD8(840, 82),
        ITEM1(1120, 300),
        ITEM2(1120, 200),
        ITEM3(1120, 100);

        private final float x;
        private final float y;

        ShopPositions(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
