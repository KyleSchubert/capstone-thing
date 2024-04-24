package com.roguelikedeckbuilder.mygame.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.UseLine;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class Card {
    private final CardTypeName cardTypeName;
    private final Group group;
    private final float width;
    private final float height;
    private final Label cardName;
    private final Label cardEffectDescription;
    private final Label energyCostLabel;
    private boolean isUpgraded = false;
    private boolean toGoToShufflePile = false;
    private boolean toBeAddedToCombatMenuStage;
    private boolean used;

    public Card(CardTypeName cardTypeName, boolean showValue) {
        this.cardTypeName = cardTypeName;

        Image background = new Image(new Texture(Gdx.files.internal("CARDS/background.png")));
        background.setPosition(0, 0);

        Image image = new Image(new Texture(Gdx.files.internal(CardData.getImagePath(cardTypeName))));
        image.setScale(3);
        XYPair<Float> imagePosition = new XYPair<>(
                (background.getWidth() - image.getWidth() * 3) / 2,
                180 - image.getWidth() / 3
        );
        image.setPosition(imagePosition.x(), imagePosition.y());

        cardName = LabelMaker.newLabel(AbilityData.getName(getUsedAbilityTypeName()), LabelMaker.getMedium());
        cardName.setAlignment(Align.center);
        cardName.setWidth(background.getWidth());
        cardName.setPosition(0, 266);

        cardEffectDescription = LabelMaker.newLabel(AbilityData.getDescription(getUsedAbilityTypeName()), LabelMaker.getSmall());
        cardEffectDescription.setAlignment(Align.topLeft);
        cardEffectDescription.setPosition(16, 140);


        group = new Group();

        group.addActor(background);
        group.addActor(image);
        group.addActor(cardName);
        group.addActor(cardEffectDescription);

        if (showValue) {
            Image coinImage = new Image(new Texture(Gdx.files.internal("ITEMS/doubloon.png")));
            coinImage.setPosition(148, 18);
            group.addActor(coinImage);

            Label cardValueLabel = LabelMaker.newLabel("" + CardData.getValue(cardTypeName), LabelMaker.getMedium());
            cardValueLabel.setPosition(184, 18);
            cardValueLabel.setTouchable(Touchable.disabled);
            group.addActor(cardValueLabel);
        }

        // Energy
        Image energyImage = new Image(new Texture(Gdx.files.internal("CARDS/energy on card.png")));
        energyImage.setPosition(170, 200);
        group.addActor(energyImage);

        energyCostLabel = LabelMaker.newLabel(
                String.valueOf(AbilityData.getEnergyCost(getUsedAbilityTypeName())),
                LabelMaker.getMediumHpAndDamage());
        energyCostLabel.setPosition(186, 206);
        energyCostLabel.setWidth(background.getWidth());
        energyCostLabel.setTouchable(Touchable.disabled);
        group.addActor(energyCostLabel);

        // Final things
        width = background.getWidth() * SCALE_FACTOR;
        height = background.getHeight() * SCALE_FACTOR;

        group.setScale(SCALE_FACTOR);
        group.setUserObject(UserObjectOptions.CARD);

        group.setWidth(width);
        group.setHeight(height);
    }

    public CardTypeName getCardTypeName() {
        return cardTypeName;
    }

    public AbilityTypeName getUsedAbilityTypeName() {
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

    public void setUpgraded(boolean upgraded) {
        if (upgraded) {
            // A star symbol, to show it is upgraded
            Image upgradedImage = new Image(new Texture(Gdx.files.internal("CARDS/upgraded star.png")));
            upgradedImage.setPosition(25, 212);
            group.addActor(upgradedImage);
        }

        isUpgraded = upgraded;

        cardName.setText(AbilityData.getName(getUsedAbilityTypeName()));
        cardEffectDescription.setText(AbilityData.getDescription(getUsedAbilityTypeName()));
        energyCostLabel.setText(AbilityData.getEnergyCost(getUsedAbilityTypeName()));
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
                Player.setPotentialAbilityTargetType(AbilityData.getTargetTypeForHoveringAndHighlighting(getUsedAbilityTypeName()));
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

    public boolean isToGoToShufflePile() {
        return toGoToShufflePile;
    }

    public void setToGoToShufflePile(boolean toGoToShufflePile) {
        this.toGoToShufflePile = toGoToShufflePile;
    }

    public boolean isToBeAddedToCombatMenuStage() {
        return toBeAddedToCombatMenuStage;
    }

    public void setToBeAddedToCombatMenuStage(boolean toBeAddedToCombatMenuStage) {
        this.toBeAddedToCombatMenuStage = toBeAddedToCombatMenuStage;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
