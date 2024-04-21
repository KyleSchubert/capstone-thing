package com.roguelikedeckbuilder.mygame.stages.settings;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class SettingsMenuStage extends GenericStage {
    public SettingsMenuStage(ScreenViewport viewportForStage, ImageButton backButton, ImageButton confirmButton) {
        super(viewportForStage, "settings background");

        getStageBackgroundActor().setPosition(23.7f, 3.7f);

        backButton.setPosition(25, 5);
        getStage().addActor(backButton);

        confirmButton.setPosition(49, 5);
        getStage().addActor(confirmButton);
    }
}
