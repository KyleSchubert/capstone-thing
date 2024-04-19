package com.roguelikedeckbuilder.mygame.stages.treasure;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;
import com.roguelikedeckbuilder.mygame.treasure.Treasure;
import com.roguelikedeckbuilder.mygame.treasure.TreasureType;

public class TreasureMenuStage extends GenericStage {
    private Group treasureGroup;
    private final ClickListener cardChoiceClickListener;
    private final ClickListener cardChoicePreparerClickListener;

    public TreasureMenuStage(ScreenViewport viewportForStage, ImageButton exitButton, ClickListener clickListener, ClickListener cardChoicePreparerClickListener) {
        super(viewportForStage, "treasure background");
        this.getStage().getActors().get(0).setPosition(16, 1.6f);
        exitButton.setPosition(42, 3);
        this.getStage().addActor(exitButton);
        treasureGroup = new Group();
        treasureGroup.setUserObject("");

        cardChoiceClickListener = clickListener;
        this.cardChoicePreparerClickListener = cardChoicePreparerClickListener;
    }

    public void aLotOfTreasure() {
        if (treasureGroup.getUserObject().equals(UserObjectOptions.TREASURE_GROUP)) {
            treasureGroup.remove();
        }
        Treasure treasure = new Treasure(cardChoiceClickListener);
        addCardTreasure(treasure);
        treasure.addTreasure(TreasureType.PERSISTENT_CURRENCY);
        addCardTreasure(treasure);
        treasure.addTreasure(TreasureType.CURRENCY);
        addCardTreasure(treasure);
        addCardTreasure(treasure);
        treasure.addTreasure(TreasureType.CURRENCY);
        treasure.addTreasure(TreasureType.PERSISTENT_CURRENCY);

        treasureGroup = treasure.getGroup();
        this.getStage().addActor(treasureGroup);
        positionTreasures();
    }

    public void addGenericWinTreasureSet() {
        if (treasureGroup.getUserObject().equals(UserObjectOptions.TREASURE_GROUP)) {
            treasureGroup.remove();
        }
        Treasure treasure = new Treasure(cardChoiceClickListener);
        treasure.addTreasure(TreasureType.CURRENCY);
        addCardTreasure(treasure);

        treasureGroup = treasure.getGroup();
        this.getStage().addActor(treasureGroup);
        positionTreasures();
    }

    private void addCardTreasure(Treasure treasure) {
        treasure.addTreasure(TreasureType.CARDS);
        treasure.getGroup().getChild(treasure.getGroup().getChildren().size - 1).addCaptureListener(cardChoicePreparerClickListener);
    }

    private void positionTreasures() {
        int yOffset = 50;
        for (Actor group : treasureGroup.getChildren()) {
            group.setPosition(334, 700 - yOffset);
            yOffset += 79;
        }
    }

    public void batch(float elapsedTime) {
        super.batch(elapsedTime);
    }
}
