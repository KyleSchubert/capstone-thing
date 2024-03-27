package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.utils.Array;

public class DelayScheduler {
    private static final Array<Delay> delays = new Array<>();

    public static Delay scheduleNewDelay(float remainingTime, String additionalInformation) {
        Delay delay = new Delay(remainingTime, additionalInformation);
        delays.add(delay);
        return delays.get(delays.size - 1);
    }

    public static void deleteDelay(Delay delay) {
        delays.removeValue(delay, true);
    }

    public static void changeAllDelays(float timeChange) {
        for (Delay delay : delays) {
            delay.changeRemainingTime(timeChange);
        }
    }

    public static class Delay {
        private float remainingTime = 0;
        private boolean isDone = true;
        private final String additionalInformation;


        public Delay(float remainingTime, String additionalInformation) {
            this.remainingTime = remainingTime;
            this.isDone = false;
            this.additionalInformation = additionalInformation;
        }

        public boolean isDone() {
            return isDone;
        }

        public void changeRemainingTime(float change) {
            if (!isDone) {
                remainingTime += change;
                isDone = remainingTime < 0;
            }
        }

        public String getAdditionalInformation() {
            return additionalInformation;
        }
    }
}
