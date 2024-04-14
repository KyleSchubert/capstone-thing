package com.roguelikedeckbuilder.mygame.animated;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import java.util.ArrayList;
import java.util.Collections;

public class VisualEffectData {
    private static Array<IndividualVisualEffect> data;

    public static void initialize() {
        data = new Array<>();

        for (VisualEffectName name : VisualEffectName.values()) {
            data.add(new IndividualVisualEffect(name));
        }
    }

    public static ArrayList<Float> getAnimationFrameDelays(VisualEffectName visualEffectName) {
        return data.get(visualEffectName.ordinal()).getAnimationFrameDelays();
    }

    public static int getTotalFrameCount(VisualEffectName visualEffectName) {
        return data.get(visualEffectName.ordinal()).getTotalFrameCount();
    }

    public static TextureRegion[] getAnimationFrames(VisualEffectName visualEffectName) {
        return data.get(visualEffectName.ordinal()).getAnimationFrames();
    }

    public static XYPair<Integer> getOrigin(VisualEffectName visualEffectName) {
        return data.get(visualEffectName.ordinal()).getOrigin();
    }

    public static XYPair<Integer> getDimensions(VisualEffectName visualEffectName) {
        return data.get(visualEffectName.ordinal()).getDimensions();
    }

    private static class IndividualVisualEffect {
        private final ArrayList<Float> animationFrameDelays;
        private final int totalFrameCount;
        private final TextureRegion[] animationFrames;
        private final XYPair<Integer> origin;
        private final XYPair<Integer> dimensions;

        public IndividualVisualEffect(VisualEffectName visualEffectName) {
            String internalName;

            switch (visualEffectName) {
                case ITEM_TRIGGERED -> {
                    internalName = "item triggered";
                    this.totalFrameCount = 8;
                    this.animationFrameDelays = new ArrayList<>(Collections.nCopies(totalFrameCount, 0.040f));
                    this.dimensions = new XYPair<>(242, 242);
                    this.origin = new XYPair<>(121, 121);
                }
                default ->
                        throw new IllegalStateException("Unexpected value for `visualEffectName`: " + visualEffectName);
            }

            this.animationFrames = new TextureRegion[totalFrameCount];
            TextureRegion[][] frames = TextureRegion.split(new Texture("VISUALS/" + internalName + ".png"), this.dimensions.x(), this.dimensions.y());
            System.arraycopy(frames[0], 0, animationFrames, 0, totalFrameCount);
        }

        public ArrayList<Float> getAnimationFrameDelays() {
            return animationFrameDelays;
        }

        public TextureRegion[] getAnimationFrames() {
            return animationFrames;
        }

        public int getTotalFrameCount() {
            return totalFrameCount;
        }

        public XYPair<Integer> getOrigin() {
            return origin;
        }

        public XYPair<Integer> getDimensions() {
            return dimensions;
        }
    }

    public enum VisualEffectName {
        ITEM_TRIGGERED
    }
}
