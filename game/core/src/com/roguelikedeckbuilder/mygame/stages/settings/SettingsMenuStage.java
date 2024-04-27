package com.roguelikedeckbuilder.mygame.stages.settings;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.roguelikedeckbuilder.mygame.helpers.AudioManager;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;


public class SettingsMenuStage extends GenericStage {
    private static Slider overallVolumeSlider;
    private static Slider musicVolumeSlider;
    private static Slider soundVolumeSlider;
    private static boolean justLoaded = false;
    private static boolean mustPlaySound = false;

    public SettingsMenuStage() {
        super("settings background");

        getStageBackgroundActor().setPosition(474, 74);

        ImageButton backButton = ClickListenerManager.getMenuSwitchingButton(
                "back", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE, 500, 100);
        backButton.addCaptureListener(ClickListenerManager.notSavingSettings());
        addActor(backButton);

        ImageButton confirmButton = ClickListenerManager.getMenuSwitchingButton(
                "confirm", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE, 980, 100);
        confirmButton.addCaptureListener(ClickListenerManager.savingSettings());
        addActor(confirmButton);

        Label title = LabelMaker.newLabel("Settings", LabelMaker.getLarge());
        title.setPosition(500, 800);
        addActor(title);

        overallVolumeSlider = new Slider(0.5f, "Overall Volume", 520, 700);
        addActor(overallVolumeSlider);

        musicVolumeSlider = new Slider(0.5f, "Music Volume", 520, 600);
        addActor(musicVolumeSlider);

        soundVolumeSlider = new Slider(0.5f, "Sound Volume", 520, 500);
        addActor(soundVolumeSlider);
    }

    public static void repositionSliders() {
        overallVolumeSlider.loadPositionFromValue(AudioManager.getOverallVolume());
        musicVolumeSlider.loadPositionFromValue(AudioManager.getMusicVolume());
        soundVolumeSlider.loadPositionFromValue(AudioManager.getSoundVolume());

        justLoaded = true;
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        if (overallVolumeSlider.isChanged()) {
            AudioManager.setOverallVolume(overallVolumeSlider.getValue());
            overallVolumeSlider.setIsChanged(false);
            mustPlaySound = true;
        }
        if (musicVolumeSlider.isChanged()) {
            AudioManager.setMusicVolume(musicVolumeSlider.getValue());
            musicVolumeSlider.setIsChanged(false);
        }
        if (soundVolumeSlider.isChanged()) {
            AudioManager.setSoundVolume(soundVolumeSlider.getValue());
            soundVolumeSlider.setIsChanged(false);
            mustPlaySound = true;
        }

        if (justLoaded) {
            mustPlaySound = false;
            justLoaded = false;
        }

        if (mustPlaySound) {
            mustPlaySound = false;
            AudioManager.playHitSound();
        }
    }
}
