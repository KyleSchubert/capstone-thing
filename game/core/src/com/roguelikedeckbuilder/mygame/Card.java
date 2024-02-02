package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Card {
    private final CardData cardType;
    private final int cardValue;
    private final Group group;
    private final Label.LabelStyle labelStyle = new Label.LabelStyle();

    public Card(CardData cardType, boolean showValue) {
        this.cardType = cardType;

        Image background = new Image(new Texture(Gdx.files.internal("CARDS/background.png")));
        background.setPosition(0, 0);

        Image image = new Image(new Texture(Gdx.files.internal(cardType.imagePath)));
        float imageXPosition = (background.getWidth() - image.getWidth()) / 2;
        image.setPosition(imageXPosition, 230 - image.getWidth() / 2);

        labelStyle.font = new BitmapFont(Gdx.files.internal("font2.fnt"));
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font.getData().markupEnabled = true;

        cardValue = cardType.value;

        Label cardName = newLabel(cardType.name);
        cardName.setPosition(14, 274);

        Label cardEffectDescription = newLabel(cardType.effectDescription);
        cardEffectDescription.setPosition(14, 130);


        group = new Group();

        group.addActor(background);
        group.addActor(image);
        group.addActor(cardName);
        group.addActor(cardEffectDescription);

        if (showValue) {
            Image coinImage = new Image(new Texture(Gdx.files.internal("ITEMS/doubloon.png")));
            coinImage.setPosition(144, 6);
            group.addActor(coinImage);

            Label cardValueLabel = newLabel("Price: " + cardValue);
            cardValueLabel.setPosition(background.getWidth() / 2 - 60, 10);
            group.addActor(cardValueLabel);
        }

        group.setScale(SCALE_FACTOR);

        group.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Player.buyCard(cardValue, cardType.name);
            }
        });
    }

    private Label newLabel(String text) {
        Label label = new Label(text, labelStyle);
        label.setWidth(200);
        label.setWrap(true);
        return label;
    }

    public Group getGroup() {
        return group;
    }

    public int getCardValue() {
        return cardValue;
    }

    public CardData getCardType() {
        return cardType;
    }

    public enum CardData {
        DEFAULT(
                "Default",
                "I am [ORANGE]testing[] out a card and seeing if I want to format the file this way. Deals [RED]9 Damage[][PURPLE], which is [][CYAN]cool[]",
                110,
                "ITEMS/default.png"
        );

        private final String name;
        private final String effectDescription;
        private final int value;
        private final String imagePath;

        CardData(String name, String description, int value, String imagePath) {
            this.name = name;
            this.effectDescription = description;
            this.value = value;
            this.imagePath = imagePath;
        }
    }
}
