package com.roguelikedeckbuilder.mygame.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.UseLine;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class Card {
    private final CardData.CardTypeName cardTypeName;
    private final Group group;
    private boolean isUpgraded = false;
    private final float width;
    private final float height;
    private final Label cardName;
    private final Label cardEffectDescription;
    private final Label energyCostLabel;

    public Card(CardData.CardTypeName cardTypeName, boolean showValue) {
        this.cardTypeName = cardTypeName;

        Image background = new Image(new Texture(Gdx.files.internal("CARDS/background.png")));
        background.setPosition(0, 0);

        Image image = new Image(new Texture(Gdx.files.internal(CardData.getImagePath(cardTypeName))));
        image.setScale(2);
        XYPair<Float> imagePosition = new XYPair<>(
                (background.getWidth() - image.getWidth() * 2) / 2,
                200 - image.getWidth() / 2
        );
        image.setPosition(imagePosition.x(), imagePosition.y());

        cardName = LabelMaker.newLabel(AbilityData.getName(getUsedAbilityTypeName()), LabelMaker.getMedium());
        cardName.setPosition(72, 266);

        cardEffectDescription = LabelMaker.newLabel(AbilityData.getDescription(getUsedAbilityTypeName()), LabelMaker.getSmall());
        cardEffectDescription.setPosition(16, 120);


        group = new Group();

        group.addActor(background);
        group.addActor(image);
        group.addActor(cardName);
        group.addActor(cardEffectDescription);

        if (showValue) {
            Image coinImage = new Image(new Texture(Gdx.files.internal("ITEMS/doubloon.png")));
            coinImage.setPosition(144, 6);
            group.addActor(coinImage);

            Label cardValueLabel = LabelMaker.newLabel("Price: " + CardData.getValue(cardTypeName), LabelMaker.getSmall());
            cardValueLabel.setPosition(background.getWidth() / 2 - 60, 10);
            group.addActor(cardValueLabel);
        }

        // Energy
        Image energyImage = new Image(new Texture(Gdx.files.internal("CARDS/energy on card.png")));
        energyImage.setPosition(4, 234);
        group.addActor(energyImage);

        energyCostLabel = LabelMaker.newLabel(
                String.valueOf(AbilityData.getEnergyCost(getUsedAbilityTypeName())),
                LabelMaker.getMediumHpAndDamage());
        energyCostLabel.setPosition(28, 248);
        energyCostLabel.setWidth(90);
        group.addActor(energyCostLabel);

        // Final things
        width = background.getWidth() * SCALE_FACTOR;
        height = background.getHeight() * SCALE_FACTOR;

        group.setScale(SCALE_FACTOR);
        group.setUserObject(UserObjectOptions.CARD);

        group.setWidth(width);
        group.setHeight(height);
    }

    public CardData.CardTypeName getCardTypeName() {
        return cardTypeName;
    }

    public AbilityData.AbilityTypeName getUsedAbilityTypeName() {
        if (Card.this.isUpgraded) {
            return CardData.getUpgradedAbilityTypeName(Card.this.getCardTypeName());
        } else {
            return CardData.getAbilityTypeName(Card.this.getCardTypeName());
        }
    }


    public Group getGroup() {
        return group;
    }

    public boolean isUpgraded() {
        return isUpgraded;
    }

    public ClickListener getClickListener() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                UseLine.setVisibility(true);
                Player.setPotentialAbilityTargetType(AbilityData.getTargetType(getUsedAbilityTypeName()));
                UseLine.setPosition(
                        new XYPair<>(
                                getGroup().getX() + width / 2,
                                getGroup().getY() + height / 2),
                        getMousePosition());
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                UseLine.setVisibility(false);
                CombatHandler.playerUsesCard(Card.this);
            }
        };
    }


    public void setUpgraded(boolean upgraded) {
        isUpgraded = upgraded;

        if (isUpgraded) {
            // A star symbol, to show it is upgraded
            Image upgradedImage = new Image(new Texture(Gdx.files.internal("CARDS/upgraded star.png")));
            upgradedImage.setPosition(25, 190);
            group.addActor(upgradedImage);
        }

        cardName.setText(AbilityData.getName(getUsedAbilityTypeName()));
        cardEffectDescription.setText(AbilityData.getDescription(getUsedAbilityTypeName()));
        energyCostLabel.setText(AbilityData.getEnergyCost(getUsedAbilityTypeName()));
    }
}
