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
import com.roguelikedeckbuilder.mygame.combat.Ability;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class Card {
    private final CardData cardType;
    private final int cardValue;
    private final Group group;
    private boolean isUpgraded = false;
    private final float width;
    private final float height;

    public Card(CardData cardType, boolean showValue) {
        this.cardType = cardType;

        Image background = new Image(new Texture(Gdx.files.internal("CARDS/background.png")));
        background.setPosition(0, 0);

        Image image = new Image(new Texture(Gdx.files.internal(cardType.imagePath)));
        image.setScale(2);
        XYPair<Float> imagePosition = new XYPair<>(
                (background.getWidth() - image.getWidth() * 2) / 2,
                200 - image.getWidth() / 2
        );
        image.setPosition(imagePosition.x(), imagePosition.y());


        cardValue = cardType.value;

        Label cardName = LabelMaker.newLabel(cardType.name, LabelMaker.getMedium());
        cardName.setPosition(72, 266);

        Label cardEffectDescription = LabelMaker.newLabel(cardType.description, LabelMaker.getSmall());
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

            Label cardValueLabel = LabelMaker.newLabel("Price: " + cardValue, LabelMaker.getSmall());
            cardValueLabel.setPosition(background.getWidth() / 2 - 60, 10);
            group.addActor(cardValueLabel);
        }

        // Energy
        Image energyImage = new Image(new Texture(Gdx.files.internal("CARDS/energy on card.png")));
        energyImage.setPosition(4, 234);
        group.addActor(energyImage);

        Label energyCostLabel = LabelMaker.newLabel(String.valueOf(cardType.energyCost), LabelMaker.getMediumHpAndDamage());
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

    public Group getGroup() {
        return group;
    }

    public int getCardValue() {
        return cardValue;
    }

    public Ability.AbilityTypeName getAbilityTypeName() {
        return cardType.abilityTypeName;
    }

    public int getAbilityEnergyCost() {
        return cardType.energyCost;
    }

    public CardData getCardType() {
        return cardType;
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
                Player.setPotentialAbilityTargetType(AbilityData.getTargetType(Card.this.getAbilityTypeName()));
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
    }

    public enum CardData {
        VORTEX(
                Ability.AbilityTypeName.VORTEX,
                110,
                2
        ),
        FLAME(
                Ability.AbilityTypeName.FLAME,
                70,
                1
        ),
        FIRE_STRIKE(
                Ability.AbilityTypeName.FIRE_STRIKE,
                80,
                1
        );

        private final String name;
        private final String description;
        private final int value;
        private final String imagePath;
        private final Ability.AbilityTypeName abilityTypeName;
        private final int energyCost;

        CardData(Ability.AbilityTypeName abilityTypeName, int value, int energyCost) {
            this.name = AbilityData.getName(abilityTypeName);
            this.description = AbilityData.getDescription(abilityTypeName);
            this.value = value;
            this.imagePath = AbilityData.getCardIconPath(abilityTypeName);
            this.abilityTypeName = abilityTypeName;
            this.energyCost = energyCost;
        }
    }
}
