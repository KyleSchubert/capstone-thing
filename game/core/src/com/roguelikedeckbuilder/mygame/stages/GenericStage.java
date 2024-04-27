package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.MyGame;
import com.roguelikedeckbuilder.mygame.helpers.DelayScheduler;


public class GenericStage {
    private final Stage stage;
    private final Array<DelayScheduler.Delay> scheduledDelays = new Array<>();
    private boolean isDraw = false;

    public GenericStage() {
        stage = new Stage(MyGame.viewport);
    }

    public GenericStage(String stageBackgroundFilename) {
        stage = new Stage(MyGame.viewport);

        Image stageBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/" + stageBackgroundFilename + ".png")));
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

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }
}
