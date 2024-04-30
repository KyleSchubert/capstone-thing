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
        Player.setSpentPersistentMoney(prefs.getInteger("spentPersistentMoney", 0));

        for (String upgradeName : Player.allUpgradeNames) {
            Player.getUpgrades().put(upgradeName, prefs.getInteger(upgradeName, 0));
        }
    }

    public static void loadVolumeSettings() {
        AudioManager.setOverallVolume(prefs.getFloat("overallVolume", 0.5f));
        AudioManager.setMusicVolume(prefs.getFloat("musicVolume", 0.5f));
        AudioManager.setSoundVolume(prefs.getFloat("soundVolume", 0.5f));

        if (AudioManager.getOverallVolume() > 1 || AudioManager.getOverallVolume() < 0) {
            AudioManager.setOverallVolume(0.5f);
        }
        if (AudioManager.getMusicVolume() > 1 || AudioManager.getMusicVolume() < 0) {
            AudioManager.setMusicVolume(0.5f);
        }
        if (AudioManager.getSoundVolume() > 1 || AudioManager.getSoundVolume() < 0) {
            AudioManager.setSoundVolume(0.5f);
        }
    }

    public static void saveVolumeSettings() {
        prefs.putFloat("overallVolume", AudioManager.getOverallVolume());
        prefs.putFloat("musicVolume", AudioManager.getMusicVolume());
        prefs.putFloat("soundVolume", AudioManager.getSoundVolume());
        prefs.flush();
    }

    public static void savePersistentMoney() {
        prefs.putInteger("persistentMoney", Player.getPersistentMoney());
        prefs.flush();
    }

    public static void saveUpgrades() {
        for (String upgradeName : Player.allUpgradeNames) {
            prefs.putInteger(upgradeName, Player.getUpgrades().get(upgradeName));
        }

        prefs.putInteger("spentPersistentMoney", Player.getSpentPersistentMoney());
        prefs.flush();
    }

    public static void resetUpgrades() {
        for (String upgradeName : Player.allUpgradeNames) {
            Player.getUpgrades().put(upgradeName, 0);
        }

        Player.setSpentPersistentMoney(0);
        saveUpgrades();
    }

    public static void clearSave() {
        prefs.clear();
        prefs.flush();

        loadVolumeSettings();
        Player.setPersistentMoney(prefs.getInteger("persistentMoney", 0));
    }
}
