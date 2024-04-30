package com.roguelikedeckbuilder.mygame.stages.treasure;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.items.ItemTier;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;
import com.roguelikedeckbuilder.mygame.stages.map.MapNodeType;
import com.roguelikedeckbuilder.mygame.treasure.Treasure;
import com.roguelikedeckbuilder.mygame.treasure.TreasureType;

import java.util.Random;

public class TreasureMenuStage extends GenericStage {
    private final ClickListener cardChoiceClickListener;
    private final ClickListener cardChoicePreparerClickListener;
    private final Random random;
    private final Array<TreasureType> options = new Array<>();
    private Group treasureGroup;

    public TreasureMenuStage() {
        super("treasure background");
        getStageBackgroundActor().setPosition(320, 32);

        ImageButton exitButton = ClickListenerManager.getMenuSwitchingButton(
                "exit", MenuState.MAP, MenuSoundType.CLOSE, 840, 60);
        addActor(exitButton);

        treasureGroup = new Group();
        treasureGroup.setUserObject("");

        this.cardChoiceClickListener = ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN);
        this.cardChoicePreparerClickListener = ClickListenerManager.preparingCardChoiceMenu();

        random = new Random();
        options.addAll(TreasureType.values());
    }

    private void addRandomRewardFromOptions(Treasure treasure) {
        TreasureType result = options.random();

        switch (result) {
            case CARDS -> addCardTreasure(treasure);
            case CURRENCY -> treasure.addTreasure(TreasureType.CURRENCY);
            case PERSISTENT_CURRENCY -> treasure.addTreasure(TreasureType.PERSISTENT_CURRENCY);
        }
    }

    private void addCardTreasure(Treasure treasure) {
        treasure.addTreasure(TreasureType.CARDS);
        treasure.getChild(treasure.getChildren().size - 1).addCaptureListener(cardChoicePreparerClickListener);
    }

    private void positionTreasures() {
        int yOffset = 50;
        for (Actor group : treasureGroup.getChildren()) {
            group.setPosition(334, 700 - yOffset);
            yOffset += 79;
        }
    }

    public void prepareTreasureFor(MapNodeType combatNodeType) {
        if (treasureGroup.getUserObject().equals(UserObjectOptions.TREASURE_GROUP)) {
            treasureGroup.remove();
        }
        Treasure treasure = new Treasure(cardChoiceClickListener);

        int amount = 0;
        switch (combatNodeType) {

            case BOSS_BATTLE -> {

                addCardTreasure(treasure);
                treasure.addTreasure(TreasureType.CURRENCY);
                Array<ItemTypeName> items = ItemData.getSomeRandomItemNamesByTier(ItemTier.COMMON, 2, false);
                treasure.addItem(items.pop());
                treasure.addItem(items.pop());
                amount = 2;
            }
            case ELITE_BATTLE -> {
                addCardTreasure(treasure);
                Array<ItemTypeName> items = ItemData.getSomeRandomItemNamesByTier(ItemTier.COMMON, 1, false);
                treasure.addItem(items.pop());
                treasure.addTreasure(TreasureType.CURRENCY);
                amount = 2;
            }
            case NORMAL_BATTLE -> {
                addCardTreasure(treasure);
                treasure.addTreasure(TreasureType.CURRENCY);
                amount = 1;
            }
            case TREASURE -> amount = random.nextInt(4) + 4;
        }

        for (int i = 0; i < amount; i++) {
            addRandomRewardFromOptions(treasure);
        }

        treasureGroup = treasure;
        addActor(treasureGroup);
        positionTreasures();
    }
}
