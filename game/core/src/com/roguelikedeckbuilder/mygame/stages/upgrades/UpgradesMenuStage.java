package com.roguelikedeckbuilder.mygame.stages.upgrades;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class UpgradesMenuStage extends GenericStage {

    public UpgradesMenuStage(ScreenViewport viewportForStage, ImageButton backButton) {
        super(viewportForStage, "upgrades background");

        getStageBackgroundActor().setPosition(23, 3.7f);

        backButton.setPosition(25, 5);
        getStage().addActor(backButton);
    }
}
