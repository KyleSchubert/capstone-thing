package com.roguelikedeckbuilder.mygame.stages.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;


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

    public ShopMenuStage(ScreenViewport viewportForStage) {
        super(viewportForStage, "shop");
        super.getStageBackgroundActor().setPosition(5, 3);

        ImageButton exitButton = ClickListenerManager.getMenuSwitchingButton(
                "exit", MenuState.MAP, MenuSoundType.CLOSE, 56.8f, 32);
        exitButton.setPosition(56.8f, 32);
        getStage().addActor(exitButton);

        nonCardShopUI = new Group();
        nonCardShopUI.setScale(SCALE_FACTOR);
        getStage().addActor(nonCardShopUI);

        upgradeCostLabel = LabelMaker.newLabel("Price: 150", LabelMaker.getMedium());
        upgradeCostLabel.setPosition(1160, 520);
        nonCardShopUI.addActor(upgradeCostLabel);

        upgradeButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Upgrade.png")));
        upgradeButton.setPosition(1110, 550);
        upgradeButton.addListener(ClickListenerManager.increasingCostInShop(upgradeCost, upgradeCostLabel));
        upgradeButton.addListener(ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN));
        upgradeButton.addListener(ClickListenerManager.preparingCardUpgradeMenu());

        upgradeButtonNoInteraction = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Upgrade.png")));
        upgradeButtonNoInteraction.setPosition(1110, 550);

        removeCardCostLabel = LabelMaker.newLabel("Price: 200", LabelMaker.getMedium());
        removeCardCostLabel.setPosition(1160, 420);
        nonCardShopUI.addActor(removeCardCostLabel);

        removeCardButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Remove card.png")));
        removeCardButton.setPosition(1120, 450);
        removeCardButton.addListener(ClickListenerManager.increasingCostInShop(removeCardCost, removeCardCostLabel));
        removeCardButton.addListener(ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN));
        removeCardButton.addListener(ClickListenerManager.preparingCardRemoveMenu());

        removeCardButtonNoInteraction = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Remove card.png")));
        removeCardButtonNoInteraction.setPosition(1120, 450);
    }

    public void generateShop() {
        reset();

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
            if (someActor.getUserObject() == UserObjectOptions.CARD) {
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

        getStage().addActor(cardGroup);
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
        getStage().addActor(newCard.getGroup());
    }

    public void setItemSold(Item item) {
        float posX = item.getGroup().getX();
        float posY = item.getGroup().getY();
        getStage().getActors().removeValue(item.getGroup(), true);
        Item newItem = new Item(ItemTypeName.JUNK);
        newItem.setShowPriceLabel(true);
        newItem.getGroup().addCaptureListener(ClickListenerManager.buyingItem(newItem));
        newItem.getGroup().setPosition(posX, posY);
        getStage().addActor(newItem.getGroup());
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

        getStage().addActor(itemGroup);
    }

    private enum ShopPositions {
        CARD1(6, 20.6f),
        CARD2(18, 20.6f),
        CARD3(30, 20.6f),
        CARD4(42, 20.6f),
        CARD5(6, 4.1f),
        CARD6(18, 4.1f),
        CARD7(30, 4.1f),
        CARD8(42, 4.1f),
        ITEM1(56, 15),
        ITEM2(56, 10),
        ITEM3(56, 5);

        private final float x;
        private final float y;

        ShopPositions(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
