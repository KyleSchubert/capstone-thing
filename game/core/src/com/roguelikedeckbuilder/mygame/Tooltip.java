package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.roguelikedeckbuilder.mygame.MyGame.*;

public class Tooltip {
    public enum Size {
        LARGE, MEDIUM, SMALL
    }

    public enum Location {
        LEFT, RIGHT, MIDDLE
    }

    private float tooltipLingerTime;
    private final BitmapFont tooltipFont;
    public final Stage tooltipStage;
    private String tooltipTitleText;
    private String tooltipBodyText;
    private Size size;
    private Location location;
    private boolean isUsingTooltipLingerTime = false;
    private static final Vector2 leftPosition = new Vector2(1, 8);
    private static final Vector2 rightPosition = new Vector2(71, 8);
    private static final Vector2 middlePosition = new Vector2(21.2f, 14);
    private static final Vector2 offScreen = new Vector2(9999, 9999);

    Tooltip(ScreenViewport viewportForStage, ClickListener clickListenerForItems) {
        tooltipStage = new Stage(viewportForStage);
        size = Size.SMALL;
        location = Location.LEFT;

        tooltipFont = new BitmapFont(Gdx.files.internal("font2.fnt"), false);
        tooltipFont.setUseIntegerPositions(false);
        tooltipFont.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);

        // Tooltip text
        tooltipTitleText = "";
        tooltipBodyText = "";

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

        // 3 Items
        for (int i = 0; i < 3; i++) {
            Image item = new Image(new Texture(Gdx.files.internal("ITEMS/default.png")));
            item.setSize(29 * SCALE_FACTOR * 2, 31 * SCALE_FACTOR * 2);
            item.setPosition(offScreen.x, offScreen.y);
            item.addListener(clickListenerForItems);
            tooltipStage.addActor(item);
        }
    }

    public void batch(float elapsedTime) {
        float x = 0, y = 0, titleX = 0, bodyX = 0;
        float usedTooltipWidth = tooltipStage.getActors().get(size.ordinal()).getWidth();
        float usedTooltipHeight = tooltipStage.getActors().get(size.ordinal()).getHeight();

        this.tooltipLingerTime -= elapsedTime;
        if (tooltipLingerTime < 0 && isUsingTooltipLingerTime) {
            return;
        }

        switch (location) {
            case LEFT -> {
                x = leftPosition.x;
                y = leftPosition.y;
                tooltipStage.getActors().get(2).setScaleX(1);
                titleX = 1 + x;
                bodyX = x + usedTooltipWidth - 2.3f;
            }
            case RIGHT -> {
                x = rightPosition.x;
                y = rightPosition.y;
                tooltipStage.getActors().get(2).setScaleX(-1);
                titleX = 1 + x - usedTooltipWidth;
                bodyX = x - 2.3f;
            }
            case MIDDLE -> {
                x = middlePosition.x;
                y = middlePosition.y;
                tooltipStage.getActors().get(2).setScaleX(1);
                titleX = 1 + x;
                bodyX = x + usedTooltipWidth - 2.3f;
            }
        }
        float titleY = y + usedTooltipHeight - 1;

        tooltipStage.getActors().get(size.ordinal()).setPosition(x, y); // Tooltip background
        tooltipStage.getViewport().apply();
        tooltipStage.act(elapsedTime);
        tooltipStage.draw();

        font.draw(batch, tooltipTitleText, titleX, titleY); // Text for tooltip title
        tooltipFont.draw(batch, tooltipBodyText, bodyX, 1.6f + y); // Text for tooltip body
    }

    public void useMapNodeData(Map.MapNodeType mapNodeType, int stageNumber, int index) {
        resetPositionsOffscreen();
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

    public float getTooltipLingerTime() {
        return tooltipLingerTime;
    }

    public void refreshTooltipLingerTime() {
        this.tooltipLingerTime = 0.15f;
    }

    public void setUsingTooltipLingerTime(boolean usingTooltipLingerTime) {
        isUsingTooltipLingerTime = usingTooltipLingerTime;
    }

    public boolean isUsingTooltipLingerTime() {
        return isUsingTooltipLingerTime;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void artifactReward() {
        resetPositionsOffscreen();
        setSize(Tooltip.Size.LARGE);
        setLocation(Tooltip.Location.MIDDLE);
        tooltipTitleText = "Choose an item to start with";

        float x = middlePosition.x;
        float y = middlePosition.y;

        tooltipStage.getActors().get(3).setPosition(x + 2, y + 17); // Item 1
        tooltipStage.getActors().get(4).setPosition(x + 2, y + 10); // Item 2
        tooltipStage.getActors().get(5).setPosition(x + 2, y + 3); // Item 3
    }

    public void dispose() {
        tooltipStage.dispose();
        tooltipFont.dispose();
    }

    private void resetPositionsOffscreen() {
        for (Actor actor : tooltipStage.getActors()) {
            actor.setPosition(offScreen.x, offScreen.y);
        }
    }
}
