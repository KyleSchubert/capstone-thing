package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class GenericStage {
    private final Stage stage;
    private final BitmapFont stageFont;

    public GenericStage(ScreenViewport viewportForStage, String stageBackgroundFilename) {
        stage = new Stage(viewportForStage);

        stageFont = new BitmapFont(Gdx.files.internal("font2.fnt"), false);
        stageFont.setUseIntegerPositions(false);
        stageFont.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);

        Image stageBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/" + stageBackgroundFilename + ".png")));
        stageBackground.setScale(SCALE_FACTOR);
        stageBackground.setPosition(0, 0);
        stage.addActor(stageBackground);
    }

    public void dispose() {
        stage.dispose();
        stageFont.dispose();
    }

    public void batch(float elapsedTime) {
        stage.getViewport().apply();
        stage.act(elapsedTime);
        stage.draw();
    }

    public void addActor(Actor actor) {
        actor.setScale(SCALE_FACTOR);
        getStage().addActor(actor);
    }

    public Stage getStage() {
        return stage;
    }

    public BitmapFont getStageFont() {
        return stageFont;
    }

    public Actor getStageBackgroundActor() {
        return stage.getActors().get(0);
    }
}
