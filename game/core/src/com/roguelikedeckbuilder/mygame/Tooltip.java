package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.*;

public class Tooltip {
    private static final XYPair<Float> leftPosition = new XYPair<>(1f, 8f);
    private static final XYPair<Float> rightPosition = new XYPair<>(71f, 8f);
    private static final XYPair<Float> middlePosition = new XYPair<>(21.2f, 14f);
    private static final XYPair<Float> offScreen = new XYPair<>(9999f, 9999f);
    public final Stage tooltipStage;
    private final BitmapFont tooltipFont;
    private float tooltipLingerTime;
    private String tooltipTitleText;
    private String tooltipBodyText;
    private Size size;
    private Location location;
    private boolean isUsingTooltipLingerTime = false;

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
        tooltipBackground.setPosition(offScreen.x(), offScreen.y());
        tooltipStage.addActor(tooltipBackground);

        Image mediumTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/medium tooltip.png")));
        mediumTooltipBackground.setSize(435 * SCALE_FACTOR, 368 * SCALE_FACTOR);
        mediumTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        tooltipStage.addActor(mediumTooltipBackground);

        Image smallTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/small tooltip.png")));
        smallTooltipBackground.setSize(290 * SCALE_FACTOR, 245 * SCALE_FACTOR);
        smallTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        tooltipStage.addActor(smallTooltipBackground);

        // 3 Items
        for (int i = 0; i < 3; i++) {
            Image item = new Image(new Texture(Gdx.files.internal("ITEMS/default.png")));
            item.setSize(29 * SCALE_FACTOR * 2, 31 * SCALE_FACTOR * 2);
            item.setPosition(offScreen.x(), offScreen.y());
            item.addListener(clickListenerForItems);
            tooltipStage.addActor(item);
        }
    }

    public void batch(float elapsedTime) {
        XYPair<Float> pos;
        float titleX = 0, bodyX = 0;
        float usedTooltipWidth = tooltipStage.getActors().get(size.ordinal()).getWidth();
        float usedTooltipHeight = tooltipStage.getActors().get(size.ordinal()).getHeight();

        this.tooltipLingerTime -= elapsedTime;
        if (tooltipLingerTime < 0 && isUsingTooltipLingerTime) {
            return;
        }

        switch (location) {
            case LEFT -> {
                pos = leftPosition;
                tooltipStage.getActors().get(2).setScaleX(1);
                titleX = 1 + pos.x();
                bodyX = pos.x() + usedTooltipWidth - 2.3f;
            }
            case RIGHT -> {
                pos = rightPosition;
                tooltipStage.getActors().get(2).setScaleX(-1);
                titleX = 1 + pos.x() - usedTooltipWidth;
                bodyX = pos.x() - 2.3f;
            }
            case MIDDLE -> {
                pos = middlePosition;
                tooltipStage.getActors().get(2).setScaleX(1);
                titleX = 1 + pos.x();
                bodyX = pos.x() + usedTooltipWidth - 2.3f;
            }
            default -> pos = new XYPair<>(0f, 0f);
        }
        float titleY = pos.y() + usedTooltipHeight - 1;

        tooltipStage.getActors().get(size.ordinal()).setPosition(pos.x(), pos.y()); // Tooltip background
        tooltipStage.getViewport().apply();
        tooltipStage.act(elapsedTime);
        tooltipStage.draw();

        font.draw(batch, tooltipTitleText, titleX, titleY); // Text for tooltip title
        tooltipFont.draw(batch, tooltipBodyText, bodyX, 1.6f + pos.y()); // Text for tooltip body
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

    public boolean isUsingTooltipLingerTime() {
        return isUsingTooltipLingerTime;
    }

    public void setUsingTooltipLingerTime(boolean usingTooltipLingerTime) {
        isUsingTooltipLingerTime = usingTooltipLingerTime;
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

        XYPair<Float> pos = middlePosition;

        tooltipStage.getActors().get(3).setPosition(pos.x() + 2, pos.y() + 17); // Item 1
        tooltipStage.getActors().get(4).setPosition(pos.x() + 2, pos.y() + 10); // Item 2
        tooltipStage.getActors().get(5).setPosition(pos.x() + 2, pos.y() + 3); // Item 3
    }

    public void dispose() {
        tooltipStage.dispose();
        tooltipFont.dispose();
    }

    private void resetPositionsOffscreen() {
        for (Actor actor : tooltipStage.getActors()) {
            actor.setPosition(offScreen.x(), offScreen.y());
        }
    }

    public enum Size {
        LARGE, MEDIUM, SMALL
    }

    public enum Location {
        LEFT, RIGHT, MIDDLE
    }
}
