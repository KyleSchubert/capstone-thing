package com.roguelikedeckbuilder.mygame.treasure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;

import java.util.Random;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Treasure {
    private final Group treasureGroup;
    private final Random random;
    private final ClickListener cardChoiceClickListener;

    public Treasure(ClickListener cardChoiceClickListener) {
        treasureGroup = new Group();
        treasureGroup.setUserObject(UserObjectOptions.TREASURE_GROUP);
        treasureGroup.setScale(SCALE_FACTOR);

        random = new Random();

        this.cardChoiceClickListener = cardChoiceClickListener;
    }

    public void addTreasure(TreasureType treasureType) {
        int amount = 1;
        Label label;
        switch (treasureType) {
            case CURRENCY -> {
                amount = generateRandomCurrencyAmount();
                label = LabelMaker.newLabel(amount + " Coins", LabelMaker.getLarge());
            }
            case PERSISTENT_CURRENCY -> {
                amount = generateRandomPersistentCurrencyAmount();
                label = LabelMaker.newLabel(amount + " SUPER Coins", LabelMaker.getLarge());
            }
            case CARDS -> label = LabelMaker.newLabel("1 of 3 Cards", LabelMaker.getLarge());
            default -> label = LabelMaker.newLabel("$100 (REAL)", LabelMaker.getMediumHpAndDamage());
        }

        Group group = prepareGenericTreasureWrapper(treasureType);
        group.addActor(iconMaker(treasureType));

        label.setPosition(120, 20);
        group.addActor(label);
        group.addCaptureListener(ClickListenerManager.triggerTreasure(treasureType, amount, group));

        if (treasureType == TreasureType.CARDS) {
            group.addCaptureListener(cardChoiceClickListener);
        }
    }

    private Group prepareGenericTreasureWrapper(TreasureType treasureType) {
        Group group = new Group();

        String imagePath = "TREASURE/treasure wrapper.png";
        if (treasureType == TreasureType.PERSISTENT_CURRENCY) {
            imagePath = "TREASURE/treasure wrapper good.png";
        }
        Image treasureWrapper = new Image(new Texture(Gdx.files.internal(imagePath)));
        treasureWrapper.setPosition(0, 0);

        group.addActor(treasureWrapper);
        treasureGroup.addActor(group);

        return group;
    }

    private Image iconMaker(TreasureType treasureType) {
        String imagePath = "";

        switch (treasureType) {
            case CURRENCY -> imagePath = "ITEMS/doubloon.png";
            case PERSISTENT_CURRENCY -> imagePath = "ITEMS/persistent coin.png";
            case CARDS -> imagePath = "TREASURE/card choice.png";
        }

        Image icon = new Image(new Texture(Gdx.files.internal(imagePath)));
        if (treasureType != TreasureType.CARDS) {
            icon.setScale(2);
            icon.setPosition(18, 9);
        } else {
            icon.setPosition(10, 0);
        }

        return icon;
    }

    private int generateRandomCurrencyAmount() {
        return random.nextInt(40) + 65;
    }

    private int generateRandomPersistentCurrencyAmount() {
        return random.nextInt(2) + 2;
    }

    public static void triggerTreasure(TreasureType treasureType, int amount) {
        switch (treasureType) {
            case CURRENCY -> Player.changeMoney(amount);
            case PERSISTENT_CURRENCY -> Player.changePersistentMoney(amount);
            case CARDS -> {
            }
        }
    }

    public Group getGroup() {
        return treasureGroup;
    }

    public enum TreasureType {
        CURRENCY, PERSISTENT_CURRENCY, CARDS
    }
}
