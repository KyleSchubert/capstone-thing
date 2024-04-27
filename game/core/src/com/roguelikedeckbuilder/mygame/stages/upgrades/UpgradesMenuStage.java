package com.roguelikedeckbuilder.mygame.stages.upgrades;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

public class UpgradesMenuStage extends GenericStage {

    public UpgradesMenuStage() {
        super("upgrades background");

        getStageBackgroundActor().setPosition(23, 3.7f);

        ImageButton backButton = ClickListenerManager.getMenuSwitchingButton(
                "back", MenuState.MAIN_MENU, MenuSoundType.CLOSE, 25, 5);
        getStage().addActor(backButton);
    }
}
