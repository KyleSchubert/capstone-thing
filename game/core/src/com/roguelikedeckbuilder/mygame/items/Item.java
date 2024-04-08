package com.roguelikedeckbuilder.mygame.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Item {
    private final ItemData.ItemName itemName;
    private final Group group;

    public Item(ItemData.ItemName itemName) {
        this.itemName = itemName;

        group = new Group();

        Image itemImage = new Image(new Texture(Gdx.files.internal(getImagePath())));
        itemImage.setPosition(0, 0);

        group.addActor(itemImage);
        group.setUserObject(UserObjectOptions.ITEM);

        group.setScale(SCALE_FACTOR * 2);
    }

    public Group getGroup() {
        return group;
    }

    public String getName() {
        return ItemData.getName(itemName);
    }

    public String getImagePath() {
        return ItemData.getImagePath(itemName);
    }

    public AbilityData.AbilityTypeName getAbilityTypeName() {
        return ItemData.getAbilityTypeName(itemName);
    }

    public ItemData.ItemTier getItemTier() {
        return ItemData.getItemTier(itemName);
    }

    public ItemData.ItemName getItemName() {
        return itemName;
    }
}
