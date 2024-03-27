package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class SoundManager {
    private static Sound hitSound;
    private static Sound getCoinsSound;
    private static Sound defendSound;
    private static float masterVolume;
    private static final float BASE_VOLUME = 0.25f;
    private static final Array<DelayScheduler.Delay> delays = new Array<>();

    public static void initialize() {
        hitSound = newSound("hit_freesound.org");
        getCoinsSound = newSound("get_coins_freesound.org");
        defendSound = newSound("defend_freesound.org");
        masterVolume = 0.5f;
    }

    private static Sound newSound(String fileName) {
        return Gdx.audio.newSound(Gdx.files.internal("SOUNDS/" + fileName + ".mp3"));
    }

    private static void playSound(Sound sound) {
        sound.play(BASE_VOLUME * masterVolume);
    }

    private static void playSound(Sound sound, float volumeMultiplier) {
        sound.play(BASE_VOLUME * masterVolume * volumeMultiplier);
    }

    private static boolean noSoundOfSameTypeIsPlaying(String typeName) {
        Array<DelayScheduler.Delay> delaysCopy = new Array<>();
        delaysCopy.addAll(delays);

        for (DelayScheduler.Delay delay : delaysCopy) {
            if (delay.isDone()) {
                delays.removeValue(delay, true);
                DelayScheduler.deleteDelay(delay);
            }
        }

        for (DelayScheduler.Delay delay : delays) {
            if (delay.getAdditionalInformation().equals(typeName)) {
                return false;
            }
        }

        delays.add(DelayScheduler.scheduleNewDelay(0.1f, typeName));
        return true;
    }

    public static void playHitSound() {
        if (noSoundOfSameTypeIsPlaying("hitSound")) {
            playSound(hitSound);
        }
    }

    public static void playGetCoinsSound() {
        if (noSoundOfSameTypeIsPlaying("getCoinsSound")) {
            playSound(getCoinsSound, 4);
        }
    }

    public static void playDefendSound() {
        if (noSoundOfSameTypeIsPlaying("defendSound")) {
            playSound(defendSound);
        }
    }
}
