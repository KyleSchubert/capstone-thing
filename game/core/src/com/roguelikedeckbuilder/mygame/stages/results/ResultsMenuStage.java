package com.roguelikedeckbuilder.mygame.stages.results;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class ResultsMenuStage extends GenericStage {
    public ResultsMenuStage(ScreenViewport viewportForStage) {
        super(viewportForStage, "results background");

        getStageBackgroundActor().setPosition(48, 0.7f);

        ImageButton mainMenuButton = ClickListenerManager.getMenuSwitchingButton(
                "main menu", MenuState.MAIN_MENU, MenuSoundType.CLOSE, 52, 2);
        getStage().addActor(mainMenuButton);
    }
}
