package com.roguelikedeckbuilder.mygame.stages.pause;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class PauseMenuStage extends GenericStage {
    public PauseMenuStage() {
        super("pause background");

        getStageBackgroundActor().setPosition(29.5f, 20);

        ImageButton resumeButton = ClickListenerManager.getMenuSwitchingButton(
                "resume", MenuState.RESUME, MenuSoundType.CLOSE, 31, 29.2f);
        getStage().addActor(resumeButton);

        ImageButton settingsButton = ClickListenerManager.getMenuSwitchingButton(
                "settings", MenuState.SETTINGS, MenuSoundType.OPEN, 31, 25.1f);
        settingsButton.addCaptureListener(ClickListenerManager.reloadSettingsMenu());
        getStage().addActor(settingsButton);

        ImageButton giveUpButton = ClickListenerManager.getMenuSwitchingButton(
                "give up", MenuState.RESULTS, MenuSoundType.SILENT, 31.5f, 20.5f);
        getStage().addActor(giveUpButton);
    }
}
