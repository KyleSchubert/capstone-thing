package com.roguelikedeckbuilder.mygame.stages.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.roguelikedeckbuilder.mygame.helpers.*;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.items.ItemTier;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuController;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;
import com.roguelikedeckbuilder.mygame.stages.map.MapNodeType;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class TooltipStage extends GenericStage {
    private static final XYPair<Float> middlePosition = new XYPair<>(21.2f, 14f);
    private static final XYPair<Float> offScreen = new XYPair<>(9999f, 9999f);
    private final Label itemChoiceLabel1 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Label itemChoiceLabel2 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Label itemChoiceLabel3 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Label title = LabelMaker.newLabel("", LabelMaker.getLarge());
    private final Label body = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Group nonBackgroundThings = new Group();
    private Size size;
    private Location location;
    private boolean isAbove;
    private boolean showChooseOneItemDetails = false;

    public TooltipStage() {
        super("tooltip");
        getStageBackgroundActor().setTouchable(Touchable.disabled);
        getStageBackgroundActor().setPosition(offScreen.x(), offScreen.y());
        getStageBackgroundActor().setScale(SCALE_FACTOR);

        size = Size.SMALL;
        location = Location.LEFT;
        isAbove = true;

        // Other sizes of tooltipStage backgrounds
        Image mediumTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/medium tooltip.png")));
        mediumTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        mediumTooltipBackground.setTouchable(Touchable.disabled);
        getStage().addActor(mediumTooltipBackground);
        mediumTooltipBackground.setScale(SCALE_FACTOR);

        Image smallTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/small tooltip.png")));
        smallTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        smallTooltipBackground.setTouchable(Touchable.disabled);
        getStage().addActor(smallTooltipBackground);
        smallTooltipBackground.setScale(SCALE_FACTOR);

        // 3 labels for the "Choose one of these 3 items" screen
        itemChoiceLabel1.setWidth(420);
        itemChoiceLabel2.setWidth(420);
        itemChoiceLabel3.setWidth(420);

        itemChoiceLabel1.setAlignment(Align.topLeft);
        itemChoiceLabel2.setAlignment(Align.topLeft);
        itemChoiceLabel3.setAlignment(Align.topLeft);

        nonBackgroundThings.addActor(itemChoiceLabel1);
        nonBackgroundThings.addActor(itemChoiceLabel2);
        nonBackgroundThings.addActor(itemChoiceLabel3);

        // TooltipStage text
        title.setAlignment(Align.topLeft);
        body.setAlignment(Align.topLeft);
        nonBackgroundThings.addActor(title);
        nonBackgroundThings.addActor(body);

        nonBackgroundThings.setScale(SCALE_FACTOR);
        getStage().addActor(nonBackgroundThings);
    }

    @Override
    public void batch(float elapsedTime) {
        if (MenuController.getIsGameplayPaused()) {
            return;
        }
        XYPair<Float> pos;
        float titleX = 0, bodyX = 0, titleY, bodyY;
        float usedTooltipWidth = getStage().getActors().get(size.ordinal()).getWidth();
        float usedTooltipHeight = getStage().getActors().get(size.ordinal()).getHeight();

        switch (location) {
            case RIGHT -> {
                pos = getMousePosition();
                getStage().getActors().get(2).setScaleX(SCALE_FACTOR);
                titleX = pos.x() / SCALE_FACTOR + 20;
                bodyX = pos.x() / SCALE_FACTOR + 20;
            }
            case LEFT -> {
                pos = getMousePosition();
                getStage().getActors().get(2).setScaleX(-SCALE_FACTOR);
                titleX = pos.x() / SCALE_FACTOR - usedTooltipWidth + 20;
                bodyX = pos.x() / SCALE_FACTOR - usedTooltipWidth + 20;
            }
            case MIDDLE -> {
                pos = middlePosition;
                getStage().getActors().get(2).setScaleX(SCALE_FACTOR);
                titleX = pos.x() / SCALE_FACTOR + 20;
                bodyX = pos.x() / SCALE_FACTOR + usedTooltipWidth - 20; // this is probably wrong
            }
            default -> pos = new XYPair<>(0f, 0f);
        }
        if (isAbove) {
            getStage().getActors().get(2).setScaleY(SCALE_FACTOR);
            titleY = pos.y() / SCALE_FACTOR + usedTooltipHeight - 20;
            bodyY = pos.y() / SCALE_FACTOR + usedTooltipHeight - 64;
        } else {
            getStage().getActors().get(2).setScaleY(-SCALE_FACTOR);
            titleY = pos.y() / SCALE_FACTOR - 20;
            bodyY = pos.y() / SCALE_FACTOR - 64;
        }

        getStage().getActors().get(size.ordinal()).setPosition(pos.x(), pos.y()); // TooltipStage background
        super.batch(elapsedTime);

        title.setPosition(titleX, titleY); // Text for tooltip title
        if (!showChooseOneItemDetails) {
            body.setPosition(bodyX, bodyY); // Text for tooltip body
        }
    }

    public void useMapNodeData(MapNodeType mapNodeType, int stageNumber, int index) {
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
        title.setText(String.format("%s", typeText));
        body.setText(String.format("%d-%d", stageNumber, index));
    }

    public void setSize(Size size) {
        this.size = size;

        if (size == Size.SMALL) {
            title.setWidth(250);
            body.setWidth(250);
        } else if (size == Size.MEDIUM) {
            title.setWidth(395);
            body.setWidth(395);
        } else if (size == Size.LARGE) {
            title.setWidth(540);
            body.setWidth(540);
        }
    }

    public void setLocation(Location location) {
        this.location = location;
        isAbove = getMousePosition().y() < 25;
    }

    public void setLocation() {
        float x = getMousePosition().x();
        if (x < 40) {
            location = Location.RIGHT;
        } else {
            location = Location.LEFT;
        }
        isAbove = getMousePosition().y() < 25;
    }

    public void itemReward() {
        setShowChooseOneItemDetails(true);
        resetItemRewards();
        Array<Image> itemImages = generateItemRewards();

        resetPositionsOffscreen();
        setSize(Size.LARGE);
        setLocation(Location.MIDDLE);
        title.setText("Choose an item to start with");

        XYPair<Float> pos = middlePosition;
        float realX = pos.x() / SCALE_FACTOR;
        float realY = pos.y() / SCALE_FACTOR;

        itemChoiceLabel1.setPosition(realX + 120, realY + 420);
        itemChoiceLabel2.setPosition(realX + 120, realY + 280);
        itemChoiceLabel3.setPosition(realX + 120, realY + 140);

        for (int i = 0; i < itemImages.size; i++) {
            itemImages.get(i).setPosition(realX + 40, realY + 340 - i * 140);
            nonBackgroundThings.addActor(itemImages.get(i));
        }
    }

    private void resetItemRewards() {
        SnapshotArray<Actor> array = nonBackgroundThings.getChildren();

        Object[] items = array.begin();
        for (int i = 0, n = array.size; i < n; i++) {
            Actor actor = (Actor) items[i];
            UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
            if (actorType == UserObjectOptions.ITEM) {
                nonBackgroundThings.removeActor(actor);
            }
        }
        array.end();
    }

    private Array<Image> generateItemRewards() {
        Array<ItemTypeName> itemNames = ItemData.getSomeRandomItemNamesByTier(ItemTier.COMMON, 3, false);
        Array<Image> itemImages = new Array<>();

        for (int i = 0; i < 3; i++) {
            ItemTypeName itemTypeName = itemNames.get(i);
            Image item = new Image(new Texture(Gdx.files.internal(ItemData.getImagePath(itemTypeName))));

            if (i == 0) {
                itemChoiceLabel1.setText(ItemData.getFullDescription(itemTypeName));
            } else if (i == 1) {
                itemChoiceLabel2.setText(ItemData.getFullDescription(itemTypeName));
            } else {
                itemChoiceLabel3.setText(ItemData.getFullDescription(itemTypeName));
            }

            item.setScale(2);
            item.setPosition(offScreen.x(), offScreen.y());
            item.addListener(ClickListenerManager.triggeringMenuState(MenuState.MAP, MenuSoundType.CLOSE));
            item.addListener(ClickListenerManager.obtainingItem(itemTypeName));
            item.setUserObject(UserObjectOptions.ITEM);

            itemImages.add(item);
        }
        return itemImages;
    }

    public void resetPositionsOffscreen() {
        getStage().getActors().get(0).setPosition(offScreen.x(), offScreen.y());
        getStage().getActors().get(1).setPosition(offScreen.x(), offScreen.y());
        getStage().getActors().get(2).setPosition(offScreen.x(), offScreen.y());

        for (Actor actor : nonBackgroundThings.getChildren()) {
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

    public void setTitleText(String text) {
        title.setText(text);
    }

    public void setBodyText(String text) {
        body.setText(text);
    }

}
