package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;

import java.util.Random;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class CardChoiceStage extends GenericStage {
    private Group cardChoiceGroup;
    private final ClickListener clickListenerToGoBackToTreasure;
    private final Array<Card.CardData> allCards;
    private final Random random;
    private final Label textAtTop;

    public CardChoiceStage(ScreenViewport viewportForStage, ClickListener clickListenerToGoBackToTreasure) {
        super(viewportForStage, "gray background");
        super.getStageBackgroundActor().setPosition(13.5f, 4);
        resetCardChoiceGroup();
        this.clickListenerToGoBackToTreasure = clickListenerToGoBackToTreasure;
        allCards = new Array<>(Card.CardData.values());
        random = new Random();

        textAtTop = new Label("", LabelMaker.getLarge());
        textAtTop.setFontScale(SCALE_FACTOR);
        textAtTop.setPosition(17.3f, 40);
    }

    private void resetCardChoiceGroup() {
        cardChoiceGroup = new Group();
        cardChoiceGroup.setUserObject(UserObjectOptions.TREASURE_GROUP);
    }

    public void prepareThreeCardChoice() {
        if (cardChoiceGroup.getUserObject().equals(UserObjectOptions.TREASURE_GROUP)) {
            resetCardChoiceGroup();
        }

        textAtTop.setText("Choose 1 Card to Obtain");
        cardChoiceGroup.addActor(textAtTop);

        allCards.shuffle();
        Card card1 = new Card(allCards.get(0), false);
        Card card2 = new Card(allCards.get(1), false);
        Card card3 = new Card(allCards.get(2), false);

        card1.getGroup().setPosition(17.3f, 18);
        card2.getGroup().setPosition(30.3f, 18);
        card3.getGroup().setPosition(43.3f, 18);

        prepareCardChoiceCards(card1);
        prepareCardChoiceCards(card2);
        prepareCardChoiceCards(card3);

        this.getStage().addActor(cardChoiceGroup);
    }

    private void prepareCardChoiceCards(Card card) {
        int number = random.nextInt(50);
        if (number > 40) {
            card.setUpgraded(true);
        }

        card.getGroup().addCaptureListener(clickListenerToGoBackToTreasure);
        card.getGroup().addCaptureListener(getClickListenerForObtainingCard(card.getCardType(), card.isUpgraded()));
        cardChoiceGroup.addActor(card.getGroup());
    }

    private ClickListener getClickListenerForObtainingCard(Card.CardData cardData, boolean isUpgraded) {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Player.obtainCard(cardData, isUpgraded);
            }
        };
    }
}
