package com.roguelikedeckbuilder.mygame.stages.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;


public class Slider extends Group {
    private final Label valueLabel = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Image knob;
    private float value; // 0.0 to 1.0
    private boolean isChanged = false;

    public Slider(float value, String title, float x, float y) {
        this.setPosition(x, y);

        Image sliderBackground = new Image(new Texture(Gdx.files.internal("OTHER UI/slider.png")));
        this.addActor(sliderBackground);
        this.setSize(sliderBackground.getWidth(), sliderBackground.getHeight());

        this.value = value;
        knob = new Image(new Texture(Gdx.files.internal("OTHER UI/knob.png")));

        loadPositionFromValue(value);
        knob.setY(getCenter() - knob.getHeight() / 2);

        knob.addCaptureListener(ClickListenerManager.getDragListener());
        knob.setUserObject(UserObjectOptions.SLIDER_KNOB);
        this.addActor(knob);

        Label titleLabel = LabelMaker.newLabel(title, LabelMaker.getMedium());
        titleLabel.setPosition(0, getHeight() + 8);
        this.addActor(titleLabel);

        valueLabel.setPosition(this.getWidth() + 24, getCenter());
        this.addActor(valueLabel);

        updateText();
    }

    public void loadPositionFromValue(float someValue) {
        updateKnobPosition(this.getWidth() * someValue - knob.getWidth() / 2);
    }

    private void updateText() {
        valueLabel.setText(String.format("%.0f%%", value * 100));
    }

    private float getCenter() {
        return this.getHeight() / 2;
    }

    public void updateKnobPosition(float x) {
        float minimumX = -1 * knob.getWidth() / 2;
        float maximumX = this.getWidth() + minimumX;

        if (x < minimumX) {
            x = minimumX;
        } else if (x > maximumX) {
            x = maximumX;
        }

        if (x != knob.getX()) {
            knob.setX(x);
            value = (knob.getX() + knob.getWidth() / 2) / this.getWidth();
            updateText();

            isChanged = true;
        }
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setIsChanged(boolean changed) {
        this.isChanged = changed;
    }

    public float getValue() {
        return value;
    }
}
