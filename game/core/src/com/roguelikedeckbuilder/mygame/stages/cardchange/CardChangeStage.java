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
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.cards.CardTypeName;
import com.roguelikedeckbuilder.mygame.helpers.*;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import java.util.Random;


public class CardChangeStage extends GenericStage {
    private final ScrollPane.ScrollPaneStyle scrollPaneStyle;
    private final ClickListener clickListenerToGoBackToTreasure;
    private final Random random;
    private final Label textAtTop;
    private Group cardChoiceGroup;

    public CardChangeStage() {
        super("gray background");
        super.getStageBackgroundActor().setPosition(270, 80);
        resetCardChoiceGroup();
        this.clickListenerToGoBackToTreasure = ClickListenerManager.triggeringMenuState(MenuState.TREASURE, MenuSoundType.OPEN);
        random = new Random();

        textAtTop = new Label("", LabelMaker.getLarge());
        textAtTop.setPosition(346, 800);
        addActor(textAtTop);

        scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        Image vScrollKnob = new Image(new Texture(Gdx.files.internal("OTHER UI/scrollbar.png")));
        scrollPaneStyle.vScrollKnob = vScrollKnob.getDrawable();
        scrollPaneStyle.vScrollKnob.setMinWidth(vScrollKnob.getWidth());
        scrollPaneStyle.vScrollKnob.setMinHeight(4);

        Image vScrollBackground = new Image(new Texture(Gdx.files.internal("OTHER UI/scrollbar background.png")));
        scrollPaneStyle.vScroll = vScrollBackground.getDrawable();
        scrollPaneStyle.vScroll.setMinWidth(vScrollBackground.getWidth());
        scrollPaneStyle.vScroll.setMinHeight(4);

        ImageButton backButton = ClickListenerManager.getImageButton("back");
        backButton.addListener(getClickListenerForBackButton());
        backButton.setPosition(940, 732);
        addActor(backButton);
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

        card1.setPosition(346, 360);
        card2.setPosition(606, 360);
        card3.setPosition(866, 360);

        prepareCardChoiceCards(card1);
        prepareCardChoiceCards(card2);
        prepareCardChoiceCards(card3);

        addActor(cardChoiceGroup);
    }

    private void prepareCardChoiceCards(Card card) {
        int CHANCE_OUT_OF_100_FOR_UPGRADED_CARD = 20;
        int number = random.nextInt(100);
        
        if (number > 100 - CHANCE_OUT_OF_100_FOR_UPGRADED_CARD) {
            card.setUpgraded(true);
        }

        card.addCaptureListener(clickListenerToGoBackToTreasure);
        card.addCaptureListener(ClickListenerManager.obtainingCard(card.getCardTypeName(), card.isUpgraded()));
        cardChoiceGroup.addActor(card);
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
        table.add(scroller).size(800, 600);
        table.setPosition(0, -40);

        table.setUserObject(UserObjectOptions.TREASURE_GROUP);

        addActor(table);
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
                cardWithClickListener.addCaptureListener(ClickListenerManager.upgradingCard(i, card.getCardTypeName()));
            } else {
                cardWithClickListener.setUpgraded(card.isUpgraded());
            }
            if (addRemovingClick) {
                cardWithClickListener.addCaptureListener(ClickListenerManager.removingCard(i));
            }

            scrollTable.add(cardWithClickListener);

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
            scrollTable.add(cardCopy);

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
                AudioManager.playMenuCloseSound();
            }
        };
    }
}
