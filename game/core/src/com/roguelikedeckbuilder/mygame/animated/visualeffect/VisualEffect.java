package com.roguelikedeckbuilder.mygame.animated.visualeffect;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class VisualEffect extends Group {
    private final VisualEffectName visualEffectName;
    private final int totalFrameCount;
    private float frameTime = 0;
    private int frame = 0;
    private boolean looping = false;

    public VisualEffect(VisualEffectName visualEffectName, float x, float y, float scaleAmount) {
        this.visualEffectName = visualEffectName;
        this.totalFrameCount = VisualEffectData.getTotalFrameCount(visualEffectName);

        x -= VisualEffectData.getOrigin(visualEffectName).x() * scaleAmount;
        y -= (VisualEffectData.getDimensions(visualEffectName).y() - VisualEffectData.getOrigin(visualEffectName).y()) * scaleAmount;

        this.setPosition(x, y);
        this.setSize(VisualEffectData.getDimensions(visualEffectName).x(), VisualEffectData.getDimensions(visualEffectName).y());
        this.setScale(scaleAmount);

        this.setTouchable(Touchable.disabled);

        setBounds(x, y,
                VisualEffectData.getDimensions(visualEffectName).x(),
                VisualEffectData.getDimensions(visualEffectName).y());
    }

    @Override
    public void act(float elapsedTime) {
        this.frameTime += elapsedTime;
        float delayAmount = VisualEffectData.getAnimationFrameDelays(visualEffectName).get(this.frame);

        if (this.frameTime > delayAmount) {
            this.frameTime -= delayAmount;
            if (this.frame == totalFrameCount - 1) {
                if (looping) {
                    this.frame = 0;
                } else {
                    this.remove();
                    return;
                }
            } else {
                this.frame++;
            }
        }

        for (Action action : this.getActions()) {
            action.act(elapsedTime);
        }
    }

    public void setLooping() {
        this.looping = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(VisualEffectData.getAnimationFrames(visualEffectName)[this.frame],
                this.getX(),
                this.getY(),
                this.getOriginX(),
                this.getOriginY(),
                this.getWidth(),
                this.getHeight(),
                this.getScaleX(), this.getScaleY(), this.getRotation()
        );
    }
}
