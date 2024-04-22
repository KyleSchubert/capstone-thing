package com.roguelikedeckbuilder.mygame.stages.settings;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.helpers.AudioManager;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class SettingsMenuStage extends GenericStage {
    private static Slider overallVolumeSlider;
    private static Slider musicVolumeSlider;
    private static Slider soundVolumeSlider;

    public SettingsMenuStage(ScreenViewport viewportForStage) {
        super(viewportForStage, "settings background");

        getStageBackgroundActor().setPosition(23.7f, 3.7f);

        ImageButton backButton = ClickListenerManager.getMenuSwitchingButton(
                "back", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE, 25, 5);
        backButton.addCaptureListener(ClickListenerManager.notSavingSettings());
        getStage().addActor(backButton);

        ImageButton confirmButton = ClickListenerManager.getMenuSwitchingButton(
                "confirm", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE, 49, 5);
        confirmButton.addCaptureListener(ClickListenerManager.savingSettings());
        getStage().addActor(confirmButton);

        Group titleArea = new Group();
        titleArea.setScale(SCALE_FACTOR);
        getStage().addActor(titleArea);

        Label title = LabelMaker.newLabel("Settings", LabelMaker.getLarge());
        titleArea.addActor(title);
        titleArea.setPosition(25, 40);

        overallVolumeSlider = new Slider(0.5f, "Overall Volume", 26, 35);
        getStage().addActor(overallVolumeSlider);

        musicVolumeSlider = new Slider(0.5f, "Music Volume", 26, 30);
        getStage().addActor(musicVolumeSlider);

        soundVolumeSlider = new Slider(0.5f, "Sound Volume", 26, 25);
        getStage().addActor(soundVolumeSlider);
    }

    public static void repositionSliders() {
        overallVolumeSlider.loadPositionFromValue(AudioManager.getOverallVolume());
        musicVolumeSlider.loadPositionFromValue(AudioManager.getMusicVolume());
        soundVolumeSlider.loadPositionFromValue(AudioManager.getSoundVolume());
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        if (overallVolumeSlider.isChanged()) {
            AudioManager.setOverallVolume(overallVolumeSlider.getValue());
            overallVolumeSlider.setIsChanged(false);
            AudioManager.playHitSound();
        }
        if (musicVolumeSlider.isChanged()) {
            AudioManager.setMusicVolume(musicVolumeSlider.getValue());
            musicVolumeSlider.setIsChanged(false);
        }
        if (soundVolumeSlider.isChanged()) {
            AudioManager.setSoundVolume(soundVolumeSlider.getValue());
            soundVolumeSlider.setIsChanged(false);
            AudioManager.playHitSound();
        }
    }
}
