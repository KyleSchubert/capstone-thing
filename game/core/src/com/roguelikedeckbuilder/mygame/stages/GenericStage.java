package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.helpers.DelayScheduler;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class GenericStage {
    private final Stage stage;
    private final Array<DelayScheduler.Delay> scheduledDelays = new Array<>();

    public GenericStage(ScreenViewport viewportForStage) {
        stage = new Stage(viewportForStage);
    }

    public GenericStage(ScreenViewport viewportForStage, String stageBackgroundFilename) {
        stage = new Stage(viewportForStage);

        Image stageBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/" + stageBackgroundFilename + ".png")));
        stageBackground.setScale(SCALE_FACTOR);
        stageBackground.setPosition(0, 0);
        stage.addActor(stageBackground);
    }

    public void dispose() {
        stage.dispose();
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

    public Actor getStageBackgroundActor() {
        return stage.getActors().get(0);
    }

    public Array<DelayScheduler.Delay> getScheduledDelays() {
        return scheduledDelays;
    }

    public void scheduleNewDelay(float remainingTime, String additionalInformation) {
        scheduledDelays.add(DelayScheduler.scheduleNewDelay(remainingTime, additionalInformation));
    }

    public void deleteDelay(DelayScheduler.Delay delay) {
        scheduledDelays.removeValue(delay, true);
        DelayScheduler.deleteDelay(delay);
    }
}
