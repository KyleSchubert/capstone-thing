package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class AudioManager {
    private static final float BASE_VOLUME = 0.5f;
    private static final Array<DelayScheduler.Delay> delays = new Array<>();
    private static final int MUSIC_FADE_IN_INITIAL_SILENCE_ITERATIONS = 5;
    private static final int MUSIC_FADE_IN_FADE_ITERATIONS = 15;
    private static final float MUSIC_FADE_IN_TOTAL_DELAY_SECONDS = 12;
    private static Music menuMusic;
    private static Music bossMusic;
    private static Music mapMusic;
    private static Music normalCombatMusic;
    private static Sound hitSound;
    private static Sound getCoinsSound;
    private static Sound defendSound;
    private static Sound funnyTadaSound;
    private static Sound getItemSound;
    private static Sound healSound;
    private static Sound buyInShopSound;
    private static Sound getCardSound;
    private static Sound menuCloseSound;
    private static Sound menuOpenSound;
    private static Sound travelSound;
    private static float overallVolume;
    private static float musicVolume;
    private static float soundVolume;
    private static Music currentMusic;
    private static String currentMusicInternalName;
    private static float currentMusicVolumeMultiplier;
    private static float currentMusicFinalVolume;
    private static int currentMusicCurrentIteration;

    public static void initialize() {
        menuMusic = newMusic("Local Forecast - Elevator");
        bossMusic = newMusic("Blood-Reaper-Kane-Boss-The");
        mapMusic = newMusic("Autumn-Warrior-Remastered");
        normalCombatMusic = newMusic("Ordinary");

        hitSound = newSound("hit_freesound.org");
        getCoinsSound = newSound("get_coins_freesound.org");
        defendSound = newSound("defend_freesound.org");
        funnyTadaSound = newSound("funny_tada_freesound.org");
        getItemSound = newSound("get_item_freesound.org");
        healSound = newSound("heal_sound");
        buyInShopSound = newSound("buy_in_shop_freesound.org");
        getCardSound = newSound("get_card_freesound.org");
        menuCloseSound = newSound("menu_close");
        menuOpenSound = newSound("menu_open");
        travelSound = newSound("travel");
        overallVolume = 0.5f;
        musicVolume = 0.5f;
        soundVolume = 0.5f;

        currentMusicInternalName = "";
        currentMusicVolumeMultiplier = 0;
        currentMusicFinalVolume = 0;
        currentMusicCurrentIteration = 0;
        currentMusic = menuMusic;
    }

    private static Music newMusic(String fileName) {
        return Gdx.audio.newMusic(Gdx.files.internal("MUSIC/" + fileName + ".mp3"));
    }

    private static Sound newSound(String fileName) {
        return Gdx.audio.newSound(Gdx.files.internal("SOUNDS/" + fileName + ".mp3"));
    }

    private static void playMusic(Music music, String internalName) {
        if (currentMusicInternalName.equals(internalName)) {
            return;
        }
        currentMusicInternalName = internalName;

        currentMusic.setVolume(0);
        currentMusic.stop();
        cancelFadeIn();

        music.setVolume(0);
        currentMusic = music;
        currentMusic.setLooping(true);
        currentMusic.pause();

        updateCurrentMusicVolume();
        currentMusicCurrentIteration = 0;

        delays.add(DelayScheduler.scheduleNewDelay(fadeInIterationDelay(), "fadeInMusic"));
    }

    private static void fadeInMusic() {
        if (currentMusicCurrentIteration == MUSIC_FADE_IN_INITIAL_SILENCE_ITERATIONS) {
            currentMusic.play();
        }

        if (currentMusicCurrentIteration >= MUSIC_FADE_IN_INITIAL_SILENCE_ITERATIONS) {
            int stepNumber = currentMusicCurrentIteration - MUSIC_FADE_IN_INITIAL_SILENCE_ITERATIONS;
            float percentage = (float) stepNumber / MUSIC_FADE_IN_FADE_ITERATIONS;
            currentMusic.setVolume(percentage * currentMusicFinalVolume);
        }

        if (isFadeInDone()) {
            return;
        }

        currentMusicCurrentIteration++;
        delays.add(DelayScheduler.scheduleNewDelay(fadeInIterationDelay(), "fadeInMusic"));
    }

    public static void checkDelays() {
        Array<DelayScheduler.Delay> delaysCopy = new Array<>();
        delaysCopy.addAll(delays);

        for (DelayScheduler.Delay delay : delaysCopy) {
            if (delay.isDone()) {
                if (delay.getAdditionalInformation().equals("fadeInMusic")) {
                    fadeInMusic();
                }

                delays.removeValue(delay, true);
                DelayScheduler.deleteDelay(delay);
            }
        }
    }

    private static void playSound(Sound sound) {
        sound.play(BASE_VOLUME * overallVolume * soundVolume);
    }

    private static void playSound(Sound sound, float volumeMultiplier) {
        sound.play(BASE_VOLUME * overallVolume * soundVolume * volumeMultiplier);
    }

    private static boolean noSoundOfSameTypeIsPlaying(String typeName) {
        for (DelayScheduler.Delay delay : delays) {
            if (delay.getAdditionalInformation().equals(typeName)) {
                return false;
            }
        }

        delays.add(DelayScheduler.scheduleNewDelay(0.1f, typeName));
        return true;
    }

    public static void playMenuMusic() {
        currentMusicVolumeMultiplier = 0.65f;
        playMusic(menuMusic, "elevator");
    }

    public static void playBossMusic() {
        currentMusicVolumeMultiplier = 0.7f;
        playMusic(bossMusic, "boss");
    }

    public static void playMapMusic() {
        currentMusicVolumeMultiplier = 0.5f;
        playMusic(mapMusic, "map");
    }

    public static void playNormalCombatMusic() {
        currentMusicVolumeMultiplier = 0.5f;
        playMusic(normalCombatMusic, "normalCombat");
    }

    public static void playHitSound() {
        if (noSoundOfSameTypeIsPlaying("hitSound")) {
            playSound(hitSound, 1.3f);
        }
    }

    public static void playGetCoinsSound() {
        if (noSoundOfSameTypeIsPlaying("getCoinsSound")) {
            playSound(getCoinsSound, 6);
        }
    }

    public static void playDefendSound() {
        if (noSoundOfSameTypeIsPlaying("defendSound")) {
            playSound(defendSound, 1.6f);
        }
    }

    public static void playFunnyTadaSound() {
        if (noSoundOfSameTypeIsPlaying("funnyTadaSound")) {
            playSound(funnyTadaSound, 1.5f);
        }
    }

    public static void playGetItemSound() {
        if (noSoundOfSameTypeIsPlaying("getItemSound")) {
            playSound(getItemSound, 0.5f);
        }
    }

    public static void playHealSound() {
        if (noSoundOfSameTypeIsPlaying("healSound")) {
            playSound(healSound, 1.3f);
        }
    }

    public static void playBuyInShopSound() {
        if (noSoundOfSameTypeIsPlaying("buyInShopSound")) {
            playSound(buyInShopSound);
        }
    }

    public static void playGetCardSound() {
        if (noSoundOfSameTypeIsPlaying("getCardSound")) {
            playSound(getCardSound);
        }
    }

    public static void playMenuCloseSound() {
        playSound(menuCloseSound, 2f);
    }

    public static void playMenuOpenSound() {
        playSound(menuOpenSound, 2f);
    }

    public static void playTravelSound() {
        if (noSoundOfSameTypeIsPlaying("travelSound")) {
            playSound(travelSound, 1.5f);
        }
    }

    public static float getOverallVolume() {
        return overallVolume;
    }

    public static void setOverallVolume(float overallVolume) {
        AudioManager.overallVolume = overallVolume;
        updateCurrentMusicVolume();
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        AudioManager.musicVolume = musicVolume;
        updateCurrentMusicVolume();
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static void setSoundVolume(float soundVolume) {
        AudioManager.soundVolume = soundVolume;
    }

    private static float fadeInIterationDelay() {
        int totalIterations = MUSIC_FADE_IN_INITIAL_SILENCE_ITERATIONS + MUSIC_FADE_IN_FADE_ITERATIONS;
        return MUSIC_FADE_IN_TOTAL_DELAY_SECONDS / totalIterations;
    }

    private static boolean isFadeInDone() {
        return currentMusicCurrentIteration == MUSIC_FADE_IN_INITIAL_SILENCE_ITERATIONS + MUSIC_FADE_IN_FADE_ITERATIONS;
    }

    private static void updateCurrentMusicVolume() {
        currentMusicFinalVolume = BASE_VOLUME * overallVolume * musicVolume * currentMusicVolumeMultiplier;

        if (isFadeInDone()) {
            currentMusic.setVolume(currentMusicFinalVolume);
        }
    }

    private static void cancelFadeIn() {
        if (!isFadeInDone()) {
            Array<DelayScheduler.Delay> delaysCopy = new Array<>();
            delaysCopy.addAll(delays);

            for (DelayScheduler.Delay delay : delaysCopy) {
                if (delay.getAdditionalInformation().equals("fadeInMusic")) {
                    delays.removeValue(delay, true);
                    DelayScheduler.deleteDelay(delay);
                }
            }
        }
    }
}
