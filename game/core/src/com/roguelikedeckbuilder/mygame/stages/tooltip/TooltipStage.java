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

import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class TooltipStage extends GenericStage {
    private static final XYPair<Float> middlePosition = new XYPair<>(424f, 280f);
    private static final XYPair<Float> offScreen = new XYPair<>(9999f, 9999f);
    private final Label itemChoiceLabel1 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Label itemChoiceLabel2 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Label itemChoiceLabel3 = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Label title = LabelMaker.newLabel("", LabelMaker.getLarge());
    private final Label body = LabelMaker.newLabel("", LabelMaker.getSmall());
    private final Group nonBackgroundThings = new Group();
    private final Image mediumTooltipBackground;
    private final Image smallTooltipBackground;
    private Size size;
    private Location location;
    private boolean isAbove;
    private boolean showChooseOneItemDetails = false;

    public TooltipStage() {
        super("tooltip");
        getStageBackgroundActor().setTouchable(Touchable.disabled);
        getStageBackgroundActor().setPosition(offScreen.x(), offScreen.y());

        size = Size.SMALL;
        location = Location.LEFT;
        isAbove = true;

        // Other sizes of tooltipStage backgrounds
        mediumTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/medium tooltip.png")));
        mediumTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        mediumTooltipBackground.setTouchable(Touchable.disabled);
        addActor(mediumTooltipBackground);

        smallTooltipBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/small tooltip.png")));
        smallTooltipBackground.setPosition(offScreen.x(), offScreen.y());
        smallTooltipBackground.setTouchable(Touchable.disabled);
        addActor(smallTooltipBackground);

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

        addActor(nonBackgroundThings);
    }

    @Override
    public void batch(float elapsedTime) {
        if (MenuController.getIsGameplayPaused()) {
            return;
        }
        XYPair<Float> pos;
        float titleX = 0, bodyX = 0, titleY, bodyY;
        float usedTooltipWidth = getTooltip(size).getWidth();
        float usedTooltipHeight = getTooltip(size).getHeight();

        int scaleX = 1;
        switch (location) {
            case RIGHT -> {
                pos = getMousePosition();
                titleX = pos.x() + 20;
                bodyX = pos.x() + 20;
            }
            case LEFT -> {
                pos = getMousePosition();
                scaleX = -1;
                titleX = pos.x() - usedTooltipWidth + 20;
                bodyX = pos.x() - usedTooltipWidth + 20;
            }
            case MIDDLE -> {
                pos = middlePosition;
                titleX = pos.x() + 20;
                bodyX = pos.x() + usedTooltipWidth - 20; // this is probably wrong
            }
            default -> pos = new XYPair<>(0f, 0f);
        }

        int scaleY = 1;
        if (isAbove) {
            titleY = pos.y() + usedTooltipHeight - 20;
            bodyY = pos.y() + usedTooltipHeight - 64;
        } else {
            scaleY = -1;
            titleY = pos.y() - 20;
            bodyY = pos.y() - 64;
        }

        if (size == Size.SMALL) {
            smallTooltipBackground.setScaleX(scaleX);
            smallTooltipBackground.setScaleY(scaleY);
        }

        getTooltip(size).setPosition(pos.x(), pos.y()); // TooltipStage background
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
        isAbove = getMousePosition().y() < 500;
    }

    public void setLocation() {
        float x = getMousePosition().x();
        if (x < 800) {
            location = Location.RIGHT;
        } else {
            location = Location.LEFT;
        }
        // "isAbove" as in, "Should the tooltip be appearing above the mouse?"
        isAbove = getMousePosition().y() < 500;
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

        int VERTICAL_GAP = 140;
        int TOP_POSITION = 420;
        int X_OFFSET = 120;

        itemChoiceLabel1.setPosition(pos.x() + X_OFFSET, pos.y() + TOP_POSITION);
        itemChoiceLabel2.setPosition(pos.x() + X_OFFSET, pos.y() + TOP_POSITION - VERTICAL_GAP);
        itemChoiceLabel3.setPosition(pos.x() + X_OFFSET, pos.y() + TOP_POSITION - VERTICAL_GAP * 2);

        for (int i = 0; i < itemImages.size; i++) {
            itemImages.get(i).setPosition(pos.x() + 40, pos.y() + TOP_POSITION - i * VERTICAL_GAP - 80);
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
        getTooltip(Size.LARGE).setPosition(offScreen.x(), offScreen.y());
        getTooltip(Size.MEDIUM).setPosition(offScreen.x(), offScreen.y());
        getTooltip(Size.SMALL).setPosition(offScreen.x(), offScreen.y());

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

    private Image getTooltip(Size sizeToGet) {
        switch (sizeToGet) {
            case LARGE -> {
                return (Image) getStageBackgroundActor();
            }
            case MEDIUM -> {
                return mediumTooltipBackground;
            }
            case SMALL -> {
                return smallTooltipBackground;
            }
            default -> throw new IllegalStateException("Unexpected value in   getTooltip(): " + sizeToGet);
        }
    }

    public void setTitleText(String text) {
        title.setText(text);
    }

    public void setBodyText(String text) {
        body.setText(text);
    }

}
