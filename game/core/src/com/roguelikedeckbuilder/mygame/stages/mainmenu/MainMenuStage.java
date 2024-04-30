package com.roguelikedeckbuilder.mygame.stages.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import static com.roguelikedeckbuilder.mygame.MyGame.batch;
import static com.roguelikedeckbuilder.mygame.MyGame.font;

public class MainMenuStage extends GenericStage {
    public MainMenuStage() {
        super();

        addActor(new CharacterSelection(getStage()));

        Image persistentCurrencyCounterImage = new Image(new Texture(Gdx.files.internal("ITEMS/persistent coin.png")));
        persistentCurrencyCounterImage.setPosition(310, 280);
        addActor(persistentCurrencyCounterImage);

        ImageButton playButton = ClickListenerManager.getMenuSwitchingButton(
                "play", MenuState.START_REWARDS, MenuSoundType.SILENT, 40, 360);
        addActor(playButton);

        ImageButton upgradesButton = ClickListenerManager.getMenuSwitchingButton(
                "upgrades", MenuState.UPGRADES, MenuSoundType.OPEN, 40, 260);
        addActor(upgradesButton);

        ImageButton settingsButton = ClickListenerManager.getMenuSwitchingButton(
                "settings", MenuState.SETTINGS, MenuSoundType.OPEN, 40, 160);
        settingsButton.addCaptureListener(ClickListenerManager.reloadSettingsMenu());
        addActor(settingsButton);

        ImageButton exitButton = ClickListenerManager.getImageButton("exit");
        exitButton.setPosition(40, 60);
        exitButton.addListener(ClickListenerManager.exitingGame());
        addActor(exitButton);

        addActor(new Credits());
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        font.draw(batch, "x " + (Player.getPersistentMoney() - Player.getSpentPersistentMoney()), 346, 306); // text for currency counter
    }
}
