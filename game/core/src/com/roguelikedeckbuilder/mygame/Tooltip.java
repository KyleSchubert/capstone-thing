package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.roguelikedeckbuilder.mygame.MyGame.*;

public class Tooltip {
    public enum Size {
        LARGE, MEDIUM, SMALL
    }

    public enum Location {
        LEFT, RIGHT
    }

    private final BitmapFont tooltipFont;
    private final Stage tooltipStage;
    private String tooltipTitleText;
    private String tooltipBodyText;
    private Size size;
    private Location location;
    private static final Vector2 leftPosition = new Vector2(1, 8);
    private static final Vector2 rightPosition = new Vector2(71, 8);
    private static final Vector2 offScreen = new Vector2(9999, 9999);

    Tooltip(ScreenViewport viewportForStage) {
        tooltipStage = new Stage(viewportForStage);
        size = Size.SMALL;

        tooltipFont = new BitmapFont(Gdx.files.internal("font2.fnt"), false);
        tooltipFont.setUseIntegerPositions(false);
        tooltipFont.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);

        // Tooltip text
        tooltipTitleText = "";
        // Tooltip backgrounds
        Image tooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/tooltip.png")));
        tooltipBackground.setSize(580 * SCALE_FACTOR, 490 * SCALE_FACTOR);
        tooltipBackground.setPosition(offScreen.x, offScreen.y);
        tooltipStage.addActor(tooltipBackground);

        Image mediumTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/medium tooltip.png")));
        mediumTooltipBackground.setSize(435 * SCALE_FACTOR, 368 * SCALE_FACTOR);
        mediumTooltipBackground.setPosition(offScreen.x, offScreen.y);
        tooltipStage.addActor(mediumTooltipBackground);

        Image smallTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/small tooltip.png")));
        smallTooltipBackground.setSize(290 * SCALE_FACTOR, 245 * SCALE_FACTOR);
        smallTooltipBackground.setPosition(offScreen.x, offScreen.y);
        tooltipStage.addActor(smallTooltipBackground);
    }

    public void batch(float elapsedTime) {
        tooltipStage.getActors().get(0).setPosition(offScreen.x, offScreen.y); // Tooltip background
        tooltipStage.getActors().get(1).setPosition(offScreen.x, offScreen.y); // Medium tooltip background
        tooltipStage.getActors().get(2).setPosition(offScreen.x, offScreen.y); // Small tooltip background

        float x = 0, y = 0, titleX = 0, bodyX = 0;
        switch (location) {
            case LEFT -> {
                x = leftPosition.x;
                y = leftPosition.y;
                tooltipStage.getActors().get(2).setScaleX(1);
                float usedTooltipWidth = tooltipStage.getActors().get(size.ordinal()).getWidth();
                titleX = 1 + x;
                bodyX = x + usedTooltipWidth - 2.3f;
            }
            case RIGHT -> {
                x = rightPosition.x;
                y = rightPosition.y;
                tooltipStage.getActors().get(2).setScaleX(-1);
                float usedTooltipWidth = tooltipStage.getActors().get(size.ordinal()).getWidth();
                titleX = 1 + x - usedTooltipWidth;
                bodyX = x - 2.3f;
            }
        }

        tooltipStage.getActors().get(size.ordinal()).setPosition(x, y); // Tooltip background
        tooltipStage.getViewport().apply();
        tooltipStage.act(elapsedTime);
        tooltipStage.draw();

        font.draw(batch, tooltipTitleText, titleX, 11.3f + y); // Text for tooltip title
        tooltipFont.draw(batch, tooltipBodyText, bodyX, 1.6f + y); // Text for tooltip body
    }

    public void useMapNodeData(Map.MapNodeType mapNodeType, int stageNumber, int index) {
        String typeText = "";
        switch (mapNodeType) {
            case NORMAL_BATTLE -> typeText = "Normal Monster";
            case ELITE_BATTLE -> typeText = "Elite Monster";
            case BOSS_BATTLE -> typeText = "Boss Monster";
            case START -> typeText = "Start";
            case SHOP -> typeText = "Shop";
            case REST -> typeText = "Resting Area";
            case RANDOM_EVENT -> typeText = "???";
            case TREASURE -> typeText = "Treasure";
        }
        this.tooltipTitleText = String.format("%s", typeText);
        this.tooltipBodyText = String.format("%d-%d", stageNumber, index);
    }


    public void setSize(Size size) {
        this.size = size;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void dispose() {
        tooltipStage.dispose();
        tooltipFont.dispose();
    }
}
