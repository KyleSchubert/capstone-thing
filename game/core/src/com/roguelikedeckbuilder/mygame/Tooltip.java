package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.stages.Map;

import static com.roguelikedeckbuilder.mygame.MyGame.*;

public class Tooltip {
    private static final XYPair<Float> leftPosition = new XYPair<>(1f, 8f);
    private static final XYPair<Float> rightPosition = new XYPair<>(71f, 8f);
    private static final XYPair<Float> middlePosition = new XYPair<>(21.2f, 14f);
    private static final XYPair<Float> offScreen = new XYPair<>(9999f, 9999f);
    public final Stage tooltipStage;
    private final BitmapFont tooltipFont;
    private final ClickListener clickListenerExitingToMap;
    private String tooltipTitleText;
    private String tooltipBodyText;
    private Label itemChoiceLabel1 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private Label itemChoiceLabel2 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private Label itemChoiceLabel3 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private Size size;
    private Location location;
    private boolean isAbove;
    private boolean showChooseOneItemDetails = false;

    Tooltip(ScreenViewport viewportForStage, ClickListener clickListenerExitingToMap) {
        tooltipStage = new Stage(viewportForStage);
        size = Size.SMALL;
        location = Location.LEFT;
        isAbove = true;

        tooltipFont = new BitmapFont(Gdx.files.internal("font2.fnt"), false);
        tooltipFont.setUseIntegerPositions(false);
        tooltipFont.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);
        tooltipFont.getData().markupEnabled = true;

        // Tooltip text
        tooltipTitleText = "";
        tooltipBodyText = "";

        // Tooltip backgrounds
        Image tooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/tooltip.png")));
        tooltipBackground.setSize(580 * SCALE_FACTOR, 490 * SCALE_FACTOR);
        tooltipBackground.setPosition(offScreen.x(), offScreen.y());
        tooltipBackground.setTouchable(Touchable.disabled);
        tooltipStage.addActor(tooltipBackground);

        Image mediumTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/medium tooltip.png")));
        mediumTooltipBackground.setSize(435 * SCALE_FACTOR, 368 * SCALE_FACTOR);
        mediumTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        mediumTooltipBackground.setTouchable(Touchable.disabled);
        tooltipStage.addActor(mediumTooltipBackground);

        Image smallTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/small tooltip.png")));
        smallTooltipBackground.setSize(290 * SCALE_FACTOR, 245 * SCALE_FACTOR);
        smallTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        smallTooltipBackground.setTouchable(Touchable.disabled);
        tooltipStage.addActor(smallTooltipBackground);

        // 3 labels for the "Choose one of these 3 items" screen
        itemChoiceLabel1.setFontScale(SCALE_FACTOR);
        itemChoiceLabel2.setFontScale(SCALE_FACTOR);
        itemChoiceLabel3.setFontScale(SCALE_FACTOR);

        itemChoiceLabel1.setWidth(21);
        itemChoiceLabel2.setWidth(21);
        itemChoiceLabel3.setWidth(21);

        itemChoiceLabel1.setAlignment(Align.topLeft);
        itemChoiceLabel2.setAlignment(Align.topLeft);
        itemChoiceLabel3.setAlignment(Align.topLeft);

        tooltipStage.addActor(itemChoiceLabel1);
        tooltipStage.addActor(itemChoiceLabel2);
        tooltipStage.addActor(itemChoiceLabel3);

        this.clickListenerExitingToMap = clickListenerExitingToMap;
    }

    public void batch(float elapsedTime) {
        if (MenuController.getIsGameplayPaused()) {
            return;
        }
        XYPair<Float> pos;
        float titleX = 0, bodyX = 0, titleY, bodyY;
        float usedTooltipWidth = tooltipStage.getActors().get(size.ordinal()).getWidth();
        float usedTooltipHeight = tooltipStage.getActors().get(size.ordinal()).getHeight();

        switch (location) {
            case RIGHT -> {
                pos = getMousePosition();
                tooltipStage.getActors().get(2).setScaleX(1);
                titleX = 1 + pos.x();
                bodyX = pos.x() + usedTooltipWidth - 2.3f;
            }
            case LEFT -> {
                pos = getMousePosition();
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
        if (isAbove) {
            tooltipStage.getActors().get(2).setScaleY(1);
            titleY = pos.y() + usedTooltipHeight - 1;
            bodyY = 1.6f + pos.y();
        } else {
            tooltipStage.getActors().get(2).setScaleY(-1);
            titleY = pos.y() - 1;
            bodyY = 1.6f + pos.y() - usedTooltipHeight;
        }


        tooltipStage.getActors().get(size.ordinal()).setPosition(pos.x(), pos.y()); // Tooltip background
        tooltipStage.getViewport().apply();
        tooltipStage.act(elapsedTime);
        tooltipStage.draw();

        font.draw(batch, tooltipTitleText, titleX, titleY); // Text for tooltip title
        if (!showChooseOneItemDetails) {
            tooltipFont.draw(batch, tooltipBodyText, bodyX, bodyY); // Text for tooltip body
        }
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

    public void setSize(Size size) {
        this.size = size;
    }

    public void setLocation(Location location) {
        this.location = location;
        isAbove = getMousePosition().y() < 25;
    }

    public void itemReward() {
        setShowChooseOneItemDetails(true);
        resetItemRewards();
        generateItemRewards();

        resetPositionsOffscreen();
        setSize(Tooltip.Size.LARGE);
        setLocation(Tooltip.Location.MIDDLE);
        tooltipTitleText = "Choose an item to start with";

        XYPair<Float> pos = middlePosition;

        itemChoiceLabel1.setPosition(pos.x() + 6, pos.y() + 21);
        itemChoiceLabel2.setPosition(pos.x() + 6, pos.y() + 14);
        itemChoiceLabel3.setPosition(pos.x() + 6, pos.y() + 7);

        tooltipStage.getActors().get(6).setPosition(pos.x() + 2, pos.y() + 17); // Item 1
        tooltipStage.getActors().get(7).setPosition(pos.x() + 2, pos.y() + 10); // Item 2
        tooltipStage.getActors().get(8).setPosition(pos.x() + 2, pos.y() + 3); // Item 3
    }

    private void resetItemRewards() {
        Array<Actor> mustRemove = new Array<>();

        for (Actor actor : tooltipStage.getActors()) {
            UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
            if (actorType == UserObjectOptions.ITEM) {
                mustRemove.add(actor);
            }
        }

        for (Actor actor : mustRemove) {
            tooltipStage.getActors().removeValue(actor, true);
        }
    }

    private void generateItemRewards() {
        Array<ItemData.ItemName> itemNames = ItemData.getSomeRandomItemNamesByTier(ItemData.ItemTier.COMMON, 3, false);

        for (int i = 0; i < 3; i++) {
            ItemData.ItemName itemName = itemNames.get(i);
            Image item = new Image(new Texture(Gdx.files.internal(ItemData.getImagePath(itemName))));

            if (i == 0) {
                itemChoiceLabel1.setText(ItemData.getFullDescription(itemName));
            } else if (i == 1) {
                itemChoiceLabel2.setText(ItemData.getFullDescription(itemName));
            } else {
                itemChoiceLabel3.setText(ItemData.getFullDescription(itemName));
            }

            item.setScale(SCALE_FACTOR * 2);
            item.setPosition(offScreen.x(), offScreen.y());
            item.addListener(clickListenerExitingToMap);
            item.addListener(ClickListenerManager.obtainingItem(itemName));
            item.setUserObject(UserObjectOptions.ITEM);
            tooltipStage.addActor(item);
        }
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

    public void setShowChooseOneItemDetails(boolean showChooseOneItemDetails) {
        this.showChooseOneItemDetails = showChooseOneItemDetails;
        if (!showChooseOneItemDetails) {
            itemChoiceLabel1.setPosition(offScreen.x(), offScreen.y());
            itemChoiceLabel2.setPosition(offScreen.x(), offScreen.y());
            itemChoiceLabel3.setPosition(offScreen.x(), offScreen.y());
        }
    }

    public enum Size {
        LARGE, MEDIUM, SMALL
    }

    public enum Location {
        LEFT, RIGHT, MIDDLE
    }
}
