package com.roguelikedeckbuilder.mygame.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.VisualEffect;
import com.roguelikedeckbuilder.mygame.animated.VisualEffectData;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.Enemy;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.stages.CombatMenuStage;
import com.roguelikedeckbuilder.mygame.tracking.Statistics;
import com.roguelikedeckbuilder.mygame.tracking.Trigger;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Item {
    private final ItemData.ItemTypeName itemTypeName;
    private final Group group;
    private final Trigger trigger;

    public Item(ItemData.ItemTypeName itemTypeName) {
        this.itemTypeName = itemTypeName;

        group = new Group();

        Image itemImage = new Image(new Texture(Gdx.files.internal(getImagePath())));
        itemImage.setPosition(0, 0);
        itemImage.setZIndex(10);

        group.addActor(itemImage);
        group.setUserObject(UserObjectOptions.ITEM);

        group.setScale(SCALE_FACTOR * 2);

        group.addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip(getName(), getDescription(), Statistics.getRunNumber()));

        trigger = new Trigger(ItemData.getTriggerName(itemTypeName));
    }

    public void checkTrigger(Statistics.StatisticsRow newRow, int indexOfThisNewRow) {
        boolean hasTriggered = trigger.check(newRow, indexOfThisNewRow);

        // Currently, items can do things that trigger other items.
        // A loop could happen - Ex: On dealing damage, give defense. + On gaining defense, deal damage.
        // But breaking a game (in a good way) with a strategy is fun
        if (hasTriggered) {
            AbilityData.AbilityTypeName abilityTypeName = ItemData.getAbilityTypeName(itemTypeName);
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

            VisualEffect visualEffect = new VisualEffect(VisualEffectData.VisualEffectName.ITEM_TRIGGERED,
                    -group.getChild(0).getWidth(),
                    -group.getChild(0).getHeight());
            visualEffect.setScale(SCALE_FACTOR / this.group.getScaleX());

            this.group.addActorBefore(group.getChild(0), visualEffect);
            AbilityData.useAbility(abilityTypeName, targets);

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
