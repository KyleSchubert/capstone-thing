package com.roguelikedeckbuilder.mygame.stages.treasure;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;
import com.roguelikedeckbuilder.mygame.treasure.Treasure;
import com.roguelikedeckbuilder.mygame.treasure.TreasureType;

public class TreasureMenuStage extends GenericStage {
    private final ClickListener cardChoiceClickListener;
    private final ClickListener cardChoicePreparerClickListener;
    private Group treasureGroup;

    public TreasureMenuStage() {
        super("treasure background");
        getStageBackgroundActor().setPosition(16, 1.6f);

        ImageButton exitButton = ClickListenerManager.getMenuSwitchingButton(
                "exit", MenuState.MAP, MenuSoundType.CLOSE, 42, 3);
        this.getStage().addActor(exitButton);

        treasureGroup = new Group();
        treasureGroup.setUserObject("");

        this.cardChoiceClickListener = ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN);
        this.cardChoicePreparerClickListener = ClickListenerManager.preparingCardChoiceMenu();
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
}
