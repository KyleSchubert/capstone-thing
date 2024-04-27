package com.roguelikedeckbuilder.mygame.stages.rest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;


public class RestMenuStage extends GenericStage {
    public RestMenuStage() {
        super("gray background");
        float backgroundXPosition = 270;
        super.getStageBackgroundActor().setPosition(backgroundXPosition, 80);

        ClickListener clickListener = ClickListenerManager.triggeringMenuState(MenuState.MAP, MenuSoundType.CLOSE);
        ClickListener cardChangeStageTrigger = ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN);
        ClickListener cardUpgradePreparerClickListener = ClickListenerManager.preparingCardUpgradeMenu();

        Image restButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/rest area/Rest.png")));
        float restButtonXPosition = backgroundXPosition + (getStageBackgroundActor().getWidth() - restButton.getWidth()) / 2;
        restButton.setPosition(restButtonXPosition, 520);
        restButton.addListener(clickListener);
        restButton.addListener(ClickListenerManager.healing());
        getStage().addActor(restButton);

        Image upgradeButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/rest area/Upgrade.png")));
        float upgradeButtonXPosition = backgroundXPosition + (getStageBackgroundActor().getWidth() - upgradeButton.getWidth()) / 2;
        upgradeButton.setPosition(upgradeButtonXPosition, 120);
        upgradeButton.addListener(clickListener);
        upgradeButton.addListener(cardChangeStageTrigger);
        upgradeButton.addListener(cardUpgradePreparerClickListener);
        getStage().addActor(upgradeButton);
    }
}
