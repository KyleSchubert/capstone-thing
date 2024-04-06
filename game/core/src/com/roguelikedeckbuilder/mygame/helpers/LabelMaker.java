package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelMaker {
    private static final Label.LabelStyle large = new Label.LabelStyle();
    private static final Label.LabelStyle medium = new Label.LabelStyle();
    private static final Label.LabelStyle small = new Label.LabelStyle();
    private static final Label.LabelStyle hpAndDamage = new Label.LabelStyle();
    private static final Label.LabelStyle mediumHpAndDamage = new Label.LabelStyle();

    public static void initialize() {
        large.font = new BitmapFont(Gdx.files.internal("font.fnt"));
        large.font.setUseIntegerPositions(false);
        large.font.getData().markupEnabled = true;

        medium.font = new BitmapFont(Gdx.files.internal("font3.fnt"));
        medium.font.setUseIntegerPositions(false);
        medium.font.getData().markupEnabled = true;

        small.font = new BitmapFont(Gdx.files.internal("font2.fnt"));
        small.font.setUseIntegerPositions(false);
        small.font.getData().markupEnabled = true;

        hpAndDamage.font = new BitmapFont(Gdx.files.internal("hp_and_damage.fnt"));
        hpAndDamage.font.setUseIntegerPositions(false);
        hpAndDamage.font.getData().markupEnabled = true;

        mediumHpAndDamage.font = new BitmapFont(Gdx.files.internal("hp_and_damage.fnt"));
        mediumHpAndDamage.font.setUseIntegerPositions(false);
        mediumHpAndDamage.font.getData().markupEnabled = true;
        mediumHpAndDamage.font.getData().setScale(0.4f);
    }

    public static Label.LabelStyle getLarge() {
        return large;
    }

    public static Label.LabelStyle getMedium() {
        return medium;
    }

    public static Label.LabelStyle getSmall() {
        return small;
    }

    public static Label.LabelStyle getHpAndDamage() {
        return hpAndDamage;
    }

    public static Label.LabelStyle getMediumHpAndDamage() {
        return mediumHpAndDamage;
    }

    public static Label newLabel(String text, Label.LabelStyle labelStyle) {
        Label label = new Label(text, labelStyle);
        label.setWidth(200);
        label.setWrap(true);
        return label;
    }
}
