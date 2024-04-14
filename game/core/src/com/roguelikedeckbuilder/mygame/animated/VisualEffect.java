package com.roguelikedeckbuilder.mygame.animated;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class VisualEffect extends Group {
    private float frameTime = 0;
    private int frame = 0;
    private final VisualEffectData.VisualEffectName visualEffectName;
    private final int totalFrameCount;

    public VisualEffect(VisualEffectData.VisualEffectName visualEffectName, float x, float y) {
        this.visualEffectName = visualEffectName;
        this.totalFrameCount = VisualEffectData.getTotalFrameCount(visualEffectName);

        x -= VisualEffectData.getOrigin(visualEffectName).x() * SCALE_FACTOR;
        y -= (VisualEffectData.getDimensions(visualEffectName).y() - VisualEffectData.getOrigin(visualEffectName).y()) * SCALE_FACTOR;

        this.setPosition(x, y);
        this.setSize(VisualEffectData.getDimensions(visualEffectName).x(), VisualEffectData.getDimensions(visualEffectName).y());
        this.setScale(SCALE_FACTOR);

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
                this.remove();
                return;
            } else {
                this.frame++;
            }
        }

        for (Action action : this.getActions()) {
            action.act(elapsedTime);
        }
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
