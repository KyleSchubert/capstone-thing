package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;

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
    private int upgradeCost = 150;
    private int removeCardCost = 200;
    private int numberOfCards = 0;

    public ShopMenuStage(ScreenViewport viewportForStage, ImageButton exitButton, ClickListener cardChangeStageTrigger, ClickListener cardUpgradePreparerClickListener, ClickListener cardRemovePreparerClickListener) {
        super(viewportForStage, "shop");
        super.getStageBackgroundActor().setPosition(4.5f, 1.5f);

        exitButton.setPosition(56.8f, 38);
        getStage().addActor(exitButton);

        nonCardShopUI = new Group();
        nonCardShopUI.setScale(SCALE_FACTOR);
        getStage().addActor(nonCardShopUI);

        upgradeButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Upgrade.png")));
        upgradeButton.setPosition(1110, 650);
        upgradeButton.addListener(getClickListenerForPayingUpgradeCost());
        upgradeButton.addListener(cardChangeStageTrigger);
        upgradeButton.addListener(cardUpgradePreparerClickListener);

        upgradeButtonNoInteraction = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Upgrade.png")));
        upgradeButtonNoInteraction.setPosition(1110, 650);

        upgradeCostLabel = LabelMaker.newLabel("Price: 150", LabelMaker.getMedium());
        upgradeCostLabel.setPosition(1160, 620);
        nonCardShopUI.addActor(upgradeCostLabel);

        removeCardButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Remove card.png")));
        removeCardButton.setPosition(1120, 450);
        removeCardButton.addListener(getClickListenerForPayingRemoveCost());
        removeCardButton.addListener(cardChangeStageTrigger);
        removeCardButton.addListener(cardRemovePreparerClickListener);

        removeCardButtonNoInteraction = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/shop area/Remove card.png")));
        removeCardButtonNoInteraction.setPosition(1120, 450);

        removeCardCostLabel = LabelMaker.newLabel("Price: 200", LabelMaker.getMedium());
        removeCardCostLabel.setPosition(1160, 420);
        nonCardShopUI.addActor(removeCardCostLabel);
    }

    public void batch(float elapsedTime) {
        super.batch(elapsedTime);
    }

    public void generateShop() {
        numberOfCards = 0;
        Random random = new Random();
        int randomNumber;

        for (int i = 0; i < 8; i++) {
            randomNumber = random.nextInt(CardData.CardTypeName.values().length);
            Card card = new Card(CardData.CardTypeName.values()[randomNumber], true);
            card.getGroup().addCaptureListener(getClickListenerForBuyingCard(card));
            addCard(card);
        }

        upgradeCost = 150;
        upgradeCostLabel.setText("Price: " + upgradeCost);

        removeCardCost = 200;
        removeCardCostLabel.setText("Price: " + removeCardCost);

        useCorrectButtons();
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

        if (Player.getMoney() >= upgradeCost) {
            nonCardShopUI.addActor(upgradeButton);
        } else {
            nonCardShopUI.addActor(upgradeButtonNoInteraction);
        }

        removeCardButton.remove();
        removeCardButtonNoInteraction.remove();

        if (Player.getMoney() >= removeCardCost) {
            nonCardShopUI.addActor(removeCardButton);
        } else {
            nonCardShopUI.addActor(removeCardButtonNoInteraction);
        }
    }

    private ClickListener getClickListenerForPayingUpgradeCost() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Player.getMoney() >= upgradeCost) {
                    Player.changeMoney(-upgradeCost);
                    upgradeCost += 50;
                    upgradeCostLabel.setText("Price: " + upgradeCost);
                    SoundManager.playBuyInShopSound();
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    private ClickListener getClickListenerForPayingRemoveCost() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Player.getMoney() >= removeCardCost) {
                    Player.changeMoney(-removeCardCost);
                    removeCardCost += 50;
                    removeCardCostLabel.setText("Price: " + removeCardCost);
                    SoundManager.playBuyInShopSound();
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    private ClickListener getClickListenerForBuyingCard(Card card) {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Player.buyCard(CardData.getValue(card.getCardTypeName()), card.getCardTypeName(), card.isUpgraded());
                SoundManager.playBuyInShopSound();
                useCorrectButtons();
            }
        };
    }

    public enum ShopPositions {
        CARD1(6, 25),
        CARD2(18, 25),
        CARD3(30, 25),
        CARD4(42, 25),
        CARD5(6, 4.5f),
        CARD6(18, 4.5f),
        CARD7(30, 4.5f),
        CARD8(42, 4.5f);

        private final float x;
        private final float y;

        ShopPositions(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
