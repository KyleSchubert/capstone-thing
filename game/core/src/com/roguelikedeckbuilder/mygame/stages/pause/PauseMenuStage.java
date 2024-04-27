package com.roguelikedeckbuilder.mygame.stages.pause;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class PauseMenuStage extends GenericStage {
    public PauseMenuStage() {
        super("pause background");

        getStageBackgroundActor().setPosition(590, 400);

        ImageButton resumeButton = ClickListenerManager.getMenuSwitchingButton(
                "resume", MenuState.RESUME, MenuSoundType.CLOSE, 625, 584);
        addActor(resumeButton);

        ImageButton settingsButton = ClickListenerManager.getMenuSwitchingButton(
                "settings", MenuState.SETTINGS, MenuSoundType.OPEN, 625, 502);
        settingsButton.addCaptureListener(ClickListenerManager.reloadSettingsMenu());
        addActor(settingsButton);

        ImageButton giveUpButton = ClickListenerManager.getMenuSwitchingButton(
                "give up", MenuState.RESULTS, MenuSoundType.SILENT, 630, 410);
        addActor(giveUpButton);
    }
}
