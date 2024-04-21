package com.roguelikedeckbuilder.mygame.stages.pause;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class PauseMenuStage extends GenericStage {
    public PauseMenuStage(ScreenViewport viewportForStage, ImageButton resumeButton, ImageButton settingsButton, ImageButton giveUpButton) {
        super(viewportForStage, "pause background");

        getStageBackgroundActor().setPosition(29.5f, 20);

        resumeButton.setPosition(31, 29.2f);
        getStage().addActor(resumeButton);

        settingsButton.setPosition(31, 25.1f);
        getStage().addActor(settingsButton);

        giveUpButton.setPosition(31.5f, 20.5f);
        getStage().addActor(giveUpButton);
    }
}
