package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.cards.Card;

import java.util.Random;


public class ShopMenuStage extends GenericStage {
    private int numberOfCards = 0;

    public ShopMenuStage(ScreenViewport viewportForStage, ImageButton exitButton) {
        super(viewportForStage, "shop");
        super.getStageBackgroundActor().setPosition(4.5f, 1.5f);

        exitButton.setPosition(56.8f, 38);
        getStage().addActor(exitButton);
    }

    public void batch(float elapsedTime) {
        super.batch(elapsedTime);
    }

    public void generateShop() {
        numberOfCards = 0;
        Random random = new Random();
        int randomNumber;

        for (int i = 0; i < 8; i++) {
            randomNumber = random.nextInt(Card.CardData.values().length);
            Card card = new Card(Card.CardData.values()[randomNumber], true);
            card.setInShop(true);
            addCard(card);
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
