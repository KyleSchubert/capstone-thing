package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.roguelikedeckbuilder.mygame.Player;

public class SaveLoad {
    private static Preferences prefs;

    public static void initialize() {
        prefs = Gdx.app.getPreferences("MyPreferences");

        loadVolumeSettings();
        Player.setPersistentMoney(prefs.getInteger("persistentMoney", 0));
    }

    public static void loadVolumeSettings() {
        SoundManager.setOverallVolume(prefs.getFloat("overallVolume", 0.5f));
        SoundManager.setMusicVolume(prefs.getFloat("musicVolume", 0.5f));
        SoundManager.setSoundVolume(prefs.getFloat("soundVolume", 0.5f));

        if (SoundManager.getOverallVolume() > 1 || SoundManager.getOverallVolume() < 0) {
            SoundManager.setOverallVolume(0.5f);
        }
        if (SoundManager.getMusicVolume() > 1 || SoundManager.getMusicVolume() < 0) {
            SoundManager.setMusicVolume(0.5f);
        }
        if (SoundManager.getSoundVolume() > 1 || SoundManager.getSoundVolume() < 0) {
            SoundManager.setSoundVolume(0.5f);
        }
    }

    public static void saveVolumeSettings() {
        prefs.putFloat("overallVolume", SoundManager.getOverallVolume());
        prefs.putFloat("musicVolume", SoundManager.getMusicVolume());
        prefs.putFloat("soundVolume", SoundManager.getSoundVolume());
        prefs.flush();
    }

    public static void savePersistentMoney() {
        prefs.putInteger("persistentMoney", Player.getPersistentMoney());
        prefs.flush();
    }

    public static void clearSave() {
        prefs.clear();
        prefs.flush();
    }
}
