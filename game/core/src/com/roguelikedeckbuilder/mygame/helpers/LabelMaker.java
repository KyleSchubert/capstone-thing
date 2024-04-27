package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelMaker {
    private static final Label.LabelStyle large = new Label.LabelStyle();
    private static final Label.LabelStyle medium = new Label.LabelStyle();
    private static final Label.LabelStyle small = new Label.LabelStyle();
    private static final Label.LabelStyle hpAndDamage = new Label.LabelStyle();
    private static final Label.LabelStyle mediumHpAndDamage = new Label.LabelStyle();

    public static void initialize() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.genMipMaps = true;
        parameter.size = 32;
        BitmapFont font32 = generator.generateFont(parameter);
        parameter.size = 24;
        BitmapFont font24 = generator.generateFont(parameter);
        parameter.size = 18;
        BitmapFont font18 = generator.generateFont(parameter);
        generator.dispose();

        large.font = font32;
        large.font.getData().markupEnabled = true;

        medium.font = font24;
        medium.font.getData().markupEnabled = true;

        small.font = font18;
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
