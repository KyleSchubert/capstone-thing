package com.roguelikedeckbuilder.mygame.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffect;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffectName;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;
import com.roguelikedeckbuilder.mygame.tracking.trigger.Trigger;


public class Item extends Group {
    private final ItemTypeName itemTypeName;
    private final Trigger trigger;
    private final Label priceLabel;
    private int activations = 0;
    private int activationLimit = 999999999;
    private boolean usedUp = false;

    public Item(ItemTypeName itemTypeName) {
        this.itemTypeName = itemTypeName;

        Image itemImage = new Image(new Texture(Gdx.files.internal(getImagePath())));
        itemImage.setPosition(0, 0);
        itemImage.setZIndex(10);

        addActor(itemImage);
        setUserObject(UserObjectOptions.ITEM);

        setScale(2);

        Group groupHoldingPriceLabel = new Group();
        priceLabel = LabelMaker.newLabel("Price: " + ItemData.getValue(itemTypeName), LabelMaker.getMedium());
        priceLabel.setPosition(72, 16);
        priceLabel.setVisible(false);
        groupHoldingPriceLabel.setScale(1 / getScaleX());
        groupHoldingPriceLabel.addActor(priceLabel);
        groupHoldingPriceLabel.setTouchable(Touchable.disabled);
        addActor(groupHoldingPriceLabel);

        addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip(getName(), getDescription(), Statistics.getRunNumber()));

        trigger = new Trigger(ItemData.getTriggerName(itemTypeName));
    }

    public void checkTrigger(Statistics.StatisticsRow newRow, int indexOfThisNewRow) {
        boolean hasTriggered = trigger.check(newRow, indexOfThisNewRow);

        // Currently, items can do things that trigger other items.
        // A loop could happen - Ex: On dealing damage, give defense. + On gaining defense, deal damage.
        // But breaking a game (in a good way) with a strategy is fun
        if (hasTriggered) {
            AbilityTypeName abilityTypeName = ItemData.getAbilityTypeName(itemTypeName);
            VisualEffect visualEffect = new VisualEffect(VisualEffectName.ITEM_TRIGGERED_2,
                    getChild(0).getWidth() / 2,
                    getChild(0).getHeight() / 2,
                    1 / getScaleX());

            addActor(visualEffect);

            // With this, items scale with status effects. But maybe they shouldn't
            AbilityData.useAbility(Player.getCombatInformation(), abilityTypeName, true);

            activations++;
            if (activations == activationLimit) {
                usedUp = true;
            }

            Statistics.itemTriggered();
        }
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

    public AbilityTypeName getAbilityTypeName() {
        return ItemData.getAbilityTypeName(itemTypeName);
    }

    public ItemTier getItemTier() {
        return ItemData.getItemTier(itemTypeName);
    }

    public ItemTypeName getItemTypeName() {
        return itemTypeName;
    }

    public void setShowPriceLabel(boolean showPriceLabel) {
        priceLabel.setVisible(showPriceLabel);
    }

    public void setActivationLimit(int activationLimit) {
        this.activationLimit = activationLimit;
    }

    public boolean isUsedUp() {
        return usedUp;
    }
}
