package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.treasure.Treasure;

public class TreasureMenuStage extends GenericStage {
    private Group treasureGroup;

    public TreasureMenuStage(ScreenViewport viewportForStage, ImageButton exitButton) {
        super(viewportForStage, "treasure background");
        this.getStage().getActors().get(0).setPosition(16, 1.6f);
        exitButton.setPosition(42, 3);
        this.getStage().addActor(exitButton);
        treasureGroup = new Group();
        treasureGroup.setUserObject("");
    }

    public void testing() {
        if (treasureGroup.getUserObject().equals(UserObjectOptions.TREASURE_GROUP)) {
            treasureGroup.remove();
        }
        Treasure treasure = new Treasure();
        treasure.addTreasure(Treasure.TreasureType.CARDS);
        treasure.addTreasure(Treasure.TreasureType.PERSISTENT_CURRENCY);
        treasure.addTreasure(Treasure.TreasureType.CARDS);
        treasure.addTreasure(Treasure.TreasureType.CURRENCY);
        treasure.addTreasure(Treasure.TreasureType.CARDS);
        treasure.addTreasure(Treasure.TreasureType.CARDS);
        treasure.addTreasure(Treasure.TreasureType.CURRENCY);
        treasure.addTreasure(Treasure.TreasureType.PERSISTENT_CURRENCY);

        treasureGroup = treasure.getGroup();
        this.getStage().addActor(treasureGroup);
        positionTreasures();
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
