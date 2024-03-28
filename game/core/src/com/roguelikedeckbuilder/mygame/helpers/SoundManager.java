package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class SoundManager {
    private static Sound hitSound;
    private static Sound getCoinsSound;
    private static Sound defendSound;
    private static Sound funnyTadaSound;
    private static Sound getItemSound;
    private static Sound notSureWhatThisCouldBeUsedForSound;
    private static Sound buyInShopSound;
    private static Sound getCardSound;
    private static Sound menuCloseSound;
    private static Sound menuOpenSound;
    private static Sound travelSound;
    private static float masterVolume;
    private static final float BASE_VOLUME = 0.25f;
    private static final Array<DelayScheduler.Delay> delays = new Array<>();

    public static void initialize() {
        hitSound = newSound("hit_freesound.org");
        getCoinsSound = newSound("get_coins_freesound.org");
        defendSound = newSound("defend_freesound.org");
        funnyTadaSound = newSound("funny_tada_freesound.org");
        getItemSound = newSound("get_item_freesound.org");
        notSureWhatThisCouldBeUsedForSound = newSound("not sure");
        buyInShopSound = newSound("buy_in_shop_freesound.org");
        getCardSound = newSound("get_card_freesound.org");
        menuCloseSound = newSound("menu_close");
        menuOpenSound = newSound("menu_open");
        travelSound = newSound("travel");
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

    public static void playFunnyTadaSound() {
        if (noSoundOfSameTypeIsPlaying("funnyTadaSound")) {
            playSound(funnyTadaSound);
        }
    }

    public static void playGetItemSound() {
        if (noSoundOfSameTypeIsPlaying("getItemSound")) {
            playSound(getItemSound);
        }
    }

    public static void playNotSureWhatThisCouldBeUsedForSound() {
        if (noSoundOfSameTypeIsPlaying("notSureWhatThisCouldBeUsedForSound")) {
            playSound(notSureWhatThisCouldBeUsedForSound);
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
        if (noSoundOfSameTypeIsPlaying("menuCloseSound")) {
            playSound(menuCloseSound);
        }
    }

    public static void playMenuOpenSound() {
        if (noSoundOfSameTypeIsPlaying("menuOpenSound")) {
            playSound(menuOpenSound);
        }
    }

    public static void playTravelSound() {
        if (noSoundOfSameTypeIsPlaying("travelSound")) {
            playSound(travelSound, 1.5f);
        }
    }
}
