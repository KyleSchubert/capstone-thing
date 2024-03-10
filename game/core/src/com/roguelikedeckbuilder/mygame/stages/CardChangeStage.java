package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;

import java.util.Random;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class CardChangeStage extends GenericStage {
    private Group cardChoiceGroup;
    private Group cardUpgradeGroup;
    private final ClickListener clickListenerToGoBackToTreasure;
    private final Array<Card.CardData> allCards;
    private final Random random;
    private final Label textAtTop;

    public CardChangeStage(ScreenViewport viewportForStage, ClickListener clickListenerToGoBackToTreasure) {
        super(viewportForStage, "gray background");
        super.getStageBackgroundActor().setPosition(13.5f, 4);
        resetCardChoiceGroup();
        cardUpgradeGroup = new Group();
        this.clickListenerToGoBackToTreasure = clickListenerToGoBackToTreasure;
        allCards = new Array<>(Card.CardData.values());
        random = new Random();

        textAtTop = new Label("", LabelMaker.getLarge());
        textAtTop.setFontScale(SCALE_FACTOR);
        textAtTop.setPosition(17.3f, 40);
        this.getStage().addActor(textAtTop);
    }

    private void resetCardChoiceGroup() {
        cardChoiceGroup = new Group();
        cardChoiceGroup.setUserObject(UserObjectOptions.TREASURE_GROUP);
    }

    public void prepareThreeCardChoice() {
        removePlayerCards();
        if (cardChoiceGroup.getUserObject().equals(UserObjectOptions.TREASURE_GROUP)) {
            resetCardChoiceGroup();
        }

        textAtTop.setText("Choose 1 Card to Obtain");

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

    public void prepareUpgradePlayerCards() {
        removePlayerCards();
        textAtTop.setText("Upgrade 1 of Your Cards");
        prepareShowPlayerCards();
    }

    private void removePlayerCards() {
        Array<Actor> mustRemove = new Array<>();

        for (Actor actor : this.getStage().getActors()) {
            UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
            if (actorType == UserObjectOptions.TREASURE_GROUP) {
                mustRemove.add(actor);
            }
        }
        for (Actor actor : mustRemove) {
            actor.remove();
        }
    }

    private void prepareShowPlayerCards() {
        cardUpgradeGroup = new Group();
        cardUpgradeGroup.setUserObject(UserObjectOptions.TREASURE_GROUP);

        final Table scrollTable = new Table();
        int counter = 0;
        for (Card card : Player.getOwnedCards()) {
            if (!card.isUpgraded()) {
                Card cardWithClickListener = new Card(card.getCardType(), false);
                cardWithClickListener.setUpgraded(true);

                cardWithClickListener.getGroup().addCaptureListener(getClickListenerForUpgradingCard(card.getCardType()));
                scrollTable.add(cardWithClickListener.getGroup());

                counter++;
                if (counter == 3) {
                    scrollTable.row();
                    counter = 0;
                }
            }
        }

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        Image vScrollKnob = new Image(new Texture(Gdx.files.internal("OTHER UI/scrollbar.png")));
        scrollPaneStyle.vScrollKnob = vScrollKnob.getDrawable();
        scrollPaneStyle.vScrollKnob.setMinWidth(1);
        scrollPaneStyle.vScrollKnob.setMinHeight(0.2f);

        Image vScrollBackground = new Image(new Texture(Gdx.files.internal("OTHER UI/scrollbar background.png")));
        scrollPaneStyle.vScroll = vScrollBackground.getDrawable();
        scrollPaneStyle.vScroll.setMinWidth(1);
        scrollPaneStyle.vScroll.setMinHeight(0.2f);

        final ScrollPane scroller = new ScrollPane(scrollTable);
        scroller.setStyle(scrollPaneStyle);
        scroller.setFadeScrollBars(false);
        scroller.setScrollbarsVisible(true);

        final Table table = new Table();
        table.setFillParent(true);
        table.add(scroller).size(40, 30);
        table.setPosition(0, -2);

        table.setUserObject(UserObjectOptions.TREASURE_GROUP);

        this.getStage().addActor(table);

        this.getStage().addActor(cardUpgradeGroup);
    }

    private ClickListener getClickListenerForObtainingCard(Card.CardData cardData, boolean isUpgraded) {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Player chose card: " + cardData.name());
                Player.obtainCard(cardData, isUpgraded);
            }
        };
    }

    private ClickListener getClickListenerForUpgradingCard(Card.CardData cardData) {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Player upgraded card: " + cardData.name());
                Player.removeCard(cardData);
                Player.obtainCard(cardData, true);
                Player.setFlagGoBackToPreviousMenuState(true);
            }
        };
    }
}
