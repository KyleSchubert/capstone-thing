package com.roguelikedeckbuilder.mygame.stages.results;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class ResultsMenuStage extends GenericStage {
    public ResultsMenuStage(ScreenViewport viewportForStage, ImageButton mainMenuButton) {
        super(viewportForStage, "results background");

        getStageBackgroundActor().setPosition(48, 0.7f);

        mainMenuButton.setPosition(52, 2);
        getStage().addActor(mainMenuButton);
    }
}
