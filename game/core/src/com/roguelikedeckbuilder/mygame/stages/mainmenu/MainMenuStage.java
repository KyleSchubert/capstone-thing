package com.roguelikedeckbuilder.mygame.stages.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterState;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import java.util.Random;

import static com.roguelikedeckbuilder.mygame.MyGame.batch;
import static com.roguelikedeckbuilder.mygame.MyGame.font;

public class MainMenuStage extends GenericStage {
    private final Random random = new Random();
    private float runningAnimationAddClock = 0;

    public MainMenuStage(ScreenViewport viewportForStage) {
        super(viewportForStage);

        Image persistentCurrencyCounterImage = new Image(new Texture(Gdx.files.internal("ITEMS/persistent coin.png")));
        persistentCurrencyCounterImage.setPosition(15.5f, 14);
        addActor(persistentCurrencyCounterImage);

        ImageButton playButton = ClickListenerManager.getMenuSwitchingButton(
                "play", MenuState.START_REWARDS, MenuSoundType.SILENT, 2, 18);
        getStage().addActor(playButton);

        ImageButton upgradesButton = ClickListenerManager.getMenuSwitchingButton(
                "upgrades", MenuState.UPGRADES, MenuSoundType.OPEN, 2, 13);
        getStage().addActor(upgradesButton);

        ImageButton settingsButton = ClickListenerManager.getMenuSwitchingButton(
                "settings", MenuState.SETTINGS, MenuSoundType.OPEN, 2, 8);
        settingsButton.addCaptureListener(ClickListenerManager.reloadSettingsMenu());
        getStage().addActor(settingsButton);

        ImageButton exitButton = ClickListenerManager.getImageButton("exit");
        exitButton.setPosition(2, 3);
        exitButton.addListener(ClickListenerManager.exitingGame());
        getStage().addActor(exitButton);

        // Characters on main menu
        getStage().addActor(new Character(CharacterTypeName.BIRD, 23, 6));
        getStage().addActor(new Character(CharacterTypeName.BLUE_MUSHROOM, 29, 6));
        getStage().addActor(new Character(CharacterTypeName.HELMET_PENGUIN, 35, 6));
        getStage().addActor(new Character(CharacterTypeName.ORANGE_MUSHROOM, 41, 6));
        getStage().addActor(new Character(CharacterTypeName.PIG, 47, 6));
        getStage().addActor(new Character(CharacterTypeName.PLANT, 53, 6));
        getStage().addActor(new Character(CharacterTypeName.STUMP, 59, 6));
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        font.draw(batch, "x " + Player.getPersistentMoney(), 17.3f, 15.3f); // text for currency counter

        // For the running animation looping
        runningAnimationAddClock += elapsedTime;
        if (runningAnimationAddClock > 0.2f) {
            runningAnimationAddClock -= 0.2f;
            animateRandomRunningCharacter();
        }
    }

    private void animateRandomRunningCharacter() {
        float startX = 90;
        float startY = 31;
        float endXOffset = -130;

        float randomX = random.nextFloat(15) - 7;
        float randomY = random.nextFloat(15) - 7;

        int randomTypeIndex = random.nextInt(CharacterTypeName.values().length);
        CharacterTypeName randomType = CharacterTypeName.values()[randomTypeIndex];

        addActor(new Character(randomType, startX + randomX, startY + randomY));
        Character character = (Character) getStage().getActors().get(getStage().getActors().size - 1);
        character.setTouchable(Touchable.disabled);
        character.setState(CharacterState.MOVING);

        SequenceAction sequenceAction = new SequenceAction(
                Actions.moveBy(endXOffset, 0, 26),
                Actions.removeActor()
        );
        character.addAction(sequenceAction);
    }
}
