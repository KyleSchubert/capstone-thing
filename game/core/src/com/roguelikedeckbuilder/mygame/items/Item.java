package com.roguelikedeckbuilder.mygame.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.tracking.Statistics;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Item {
    private final ItemData.ItemTypeName itemTypeName;
    private final Group group;

    public Item(ItemData.ItemTypeName itemTypeName) {
        this.itemTypeName = itemTypeName;

        group = new Group();

        Image itemImage = new Image(new Texture(Gdx.files.internal(getImagePath())));
        itemImage.setPosition(0, 0);

        group.addActor(itemImage);
        group.setUserObject(UserObjectOptions.ITEM);

        group.setScale(SCALE_FACTOR * 2);

        group.addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip(getName(), getDescription(), Statistics.getRunNumber()));
    }

    public Group getGroup() {
        return group;
    }

    public String getName() {
        return ItemData.getName(itemTypeName);
    }

    public String getDescription() {
        return ItemData.getPartialDescription(itemTypeName);
    }

    public String getImagePath() {
        return ItemData.getImagePath(itemTypeName);
    }

    public AbilityData.AbilityTypeName getAbilityTypeName() {
        return ItemData.getAbilityTypeName(itemTypeName);
    }

    public ItemData.ItemTier getItemTier() {
        return ItemData.getItemTier(itemTypeName);
    }

    public ItemData.ItemTypeName getItemTypeName() {
        return itemTypeName;
    }
}
