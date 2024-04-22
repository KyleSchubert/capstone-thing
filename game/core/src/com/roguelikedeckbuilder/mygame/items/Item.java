package com.roguelikedeckbuilder.mygame.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffect;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffectName;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.combat.enemy.Enemy;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.CombatMenuStage;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;
import com.roguelikedeckbuilder.mygame.tracking.trigger.Trigger;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Item {
    private final ItemTypeName itemTypeName;
    private final Group group;
    private final Trigger trigger;
    private final Label priceLabel;

    public Item(ItemTypeName itemTypeName) {
        this.itemTypeName = itemTypeName;

        group = new Group();

        Image itemImage = new Image(new Texture(Gdx.files.internal(getImagePath())));
        itemImage.setPosition(0, 0);
        itemImage.setZIndex(10);

        group.addActor(itemImage);
        group.setUserObject(UserObjectOptions.ITEM);

        group.setScale(SCALE_FACTOR * 2);

        Group groupHoldingPriceLabel = new Group();
        priceLabel = LabelMaker.newLabel("Price: " + ItemData.getValue(itemTypeName), LabelMaker.getMedium());
        priceLabel.setPosition(72, 16);
        priceLabel.setVisible(false);
        groupHoldingPriceLabel.setScale(1 / (group.getScaleX() / SCALE_FACTOR));
        groupHoldingPriceLabel.addActor(priceLabel);
        groupHoldingPriceLabel.setTouchable(Touchable.disabled);
        group.addActor(groupHoldingPriceLabel);

        group.addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip(getName(), getDescription(), Statistics.getRunNumber()));

        trigger = new Trigger(ItemData.getTriggerName(itemTypeName));
    }

    public void checkTrigger(Statistics.StatisticsRow newRow, int indexOfThisNewRow) {
        boolean hasTriggered = trigger.check(newRow, indexOfThisNewRow);

        // Currently, items can do things that trigger other items.
        // A loop could happen - Ex: On dealing damage, give defense. + On gaining defense, deal damage.
        // But breaking a game (in a good way) with a strategy is fun
        if (hasTriggered) {
            AbilityTypeName abilityTypeName = ItemData.getAbilityTypeName(itemTypeName);
            TargetType targetType = AbilityData.getTargetType(abilityTypeName);
            Array<CombatInformation> targets = new Array<>();

            if (targetType == TargetType.SELF) {
                targets.add(Player.getCombatInformation());
            } else {
                Array<Enemy> enemies = CombatMenuStage.getCurrentEnemies();
                Array<Enemy> potentialTargets = new Array<>();
                for (Enemy enemy : enemies) {
                    if (enemy.getCombatInformation().getHp() > 0) {
                        potentialTargets.add(enemy);
                    }
                }

                if (targetType == TargetType.ONE) {
                    potentialTargets.shuffle();
                    targets.add(potentialTargets.first().getCombatInformation());
                } else if (targetType == TargetType.ALL) {
                    for (Enemy enemy : potentialTargets) {
                        targets.add(enemy.getCombatInformation());
                    }
                }
            }

            VisualEffect visualEffect = new VisualEffect(VisualEffectName.ITEM_TRIGGERED_2,
                    group.getChild(0).getWidth() / 2,
                    group.getChild(0).getHeight() / 2,
                    SCALE_FACTOR / this.group.getScaleX());

            this.group.addActor(visualEffect);

            // With this, items scale with status effects. But maybe they shouldn't
            AbilityData.useAbility(Player.getCombatInformation(), abilityTypeName, targets);

            Statistics.itemTriggered();
        }
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
}
