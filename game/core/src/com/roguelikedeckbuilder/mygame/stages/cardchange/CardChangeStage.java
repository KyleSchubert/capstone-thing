package com.roguelikedeckbuilder.mygame.stages.cardchange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.cards.CardTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import java.util.Random;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.menucontroller.MenuController.getImageButton;

public class CardChangeStage extends GenericStage {
    private final ScrollPane.ScrollPaneStyle scrollPaneStyle;
    private final ClickListener clickListenerToGoBackToTreasure;
    private final Random random;
    private final Label textAtTop;
    private Group cardChoiceGroup;

    public CardChangeStage(ScreenViewport viewportForStage, ClickListener clickListenerToGoBackToTreasure) {
        super(viewportForStage, "gray background");
        super.getStageBackgroundActor().setPosition(13.5f, 4);
        resetCardChoiceGroup();
        this.clickListenerToGoBackToTreasure = clickListenerToGoBackToTreasure;
        random = new Random();

        textAtTop = new Label("", LabelMaker.getLarge());
        textAtTop.setPosition(346, 800);

        Group groupForTextAtTop = new Group();
        groupForTextAtTop.setScale(SCALE_FACTOR);

        groupForTextAtTop.addActor(textAtTop);
        this.getStage().addActor(groupForTextAtTop);

        scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        Image vScrollKnob = new Image(new Texture(Gdx.files.internal("OTHER UI/scrollbar.png")));
        scrollPaneStyle.vScrollKnob = vScrollKnob.getDrawable();
        scrollPaneStyle.vScrollKnob.setMinWidth(vScrollKnob.getWidth() * SCALE_FACTOR);
        scrollPaneStyle.vScrollKnob.setMinHeight(0.2f);

        Image vScrollBackground = new Image(new Texture(Gdx.files.internal("OTHER UI/scrollbar background.png")));
        scrollPaneStyle.vScroll = vScrollBackground.getDrawable();
        scrollPaneStyle.vScroll.setMinWidth(vScrollBackground.getWidth() * SCALE_FACTOR);
        scrollPaneStyle.vScroll.setMinHeight(0.2f);

        ImageButton backButton = getImageButton("back");
        backButton.addListener(getClickListenerForBackButton());
        backButton.setPosition(47, 36.6f);
        this.getStage().addActor(backButton);
    }

    private void resetCardChoiceGroup() {
        cardChoiceGroup = new Group();
        cardChoiceGroup.setUserObject(UserObjectOptions.TREASURE_GROUP);
    }

    public void prepareThreeCardChoice() {
        clearStage();
        if (cardChoiceGroup.getUserObject().equals(UserObjectOptions.TREASURE_GROUP)) {
            resetCardChoiceGroup();
        }

        textAtTop.setText("Choose 1 Card to Obtain");

        Array<CardTypeName> cardTypeNames = CardData.getSomeRandomCards(3, false);
        Card card1 = new Card(cardTypeNames.get(0), false);
        Card card2 = new Card(cardTypeNames.get(1), false);
        Card card3 = new Card(cardTypeNames.get(2), false);

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
        card.getGroup().addCaptureListener(ClickListenerManager.obtainingCard(card.getCardTypeName(), card.isUpgraded()));
        cardChoiceGroup.addActor(card.getGroup());
    }

    public void prepareUpgradePlayerCards() {
        clearStage();
        textAtTop.setText("Upgrade 1 of Your Cards");
        prepareShowPlayerCards(getScrollTableFromPlayerCards(true, true, false));
    }

    public void prepareRemovePlayerCards() {
        clearStage();
        textAtTop.setText("Remove 1 of Your Cards");
        prepareShowPlayerCards(getScrollTableFromPlayerCards(false, false, true));
    }

    public void prepareViewPlayerCards() {
        clearStage();
        textAtTop.setText("Your Cards");
        prepareShowPlayerCards(getScrollTableFromPlayerCards(false, false, false));
    }

    public void prepareViewDrawPile(Array<Card> drawPileCards) {
        clearStage();
        textAtTop.setText("Cards in Draw Pile");
        prepareShowPlayerCards(getScrollTableFromDrawPile(drawPileCards));
    }

    private void clearStage() {
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

    private void prepareShowPlayerCards(Table scrollTable) {
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
    }

    private Table getScrollTableFromPlayerCards(boolean excludeUpgraded, boolean addUpgradingClick, boolean addRemovingClick) {
        final Table scrollTable = new Table();
        int counter = 0;
        for (int i = 0; i < Player.getOwnedCards().size; i++) {
            Card card = Player.getOwnedCards().get(i);

            if (excludeUpgraded && card.isUpgraded()) {
                continue;
            }

            Card cardWithClickListener = new Card(card.getCardTypeName(), false);
            if (addUpgradingClick) {
                cardWithClickListener.setUpgraded(true);
                cardWithClickListener.getGroup().addCaptureListener(ClickListenerManager.upgradingCard(i, card.getCardTypeName()));
            } else {
                cardWithClickListener.setUpgraded(card.isUpgraded());
            }
            if (addRemovingClick) {
                cardWithClickListener.getGroup().addCaptureListener(ClickListenerManager.removingCard(i));
            }

            scrollTable.add(cardWithClickListener.getGroup());

            counter++;
            if (counter == 3) {
                scrollTable.row();
                counter = 0;
            }
        }
        return scrollTable;
    }

    private Table getScrollTableFromDrawPile(Array<Card> drawPileCards) {
        final Table scrollTable = new Table();
        int counter = 0;
        for (int i = 0; i < drawPileCards.size; i++) {
            Card card = drawPileCards.get(i);

            Card cardCopy = new Card(card.getCardTypeName(), false);
            cardCopy.setUpgraded(card.isUpgraded());
            scrollTable.add(cardCopy.getGroup());

            counter++;
            if (counter == 3) {
                scrollTable.row();
                counter = 0;
            }
        }
        return scrollTable;
    }

    private InputListener getClickListenerForBackButton() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Player.setFlagGoBackToPreviousMenuState(true);
                SoundManager.playMenuCloseSound();
            }
        };
    }
}
