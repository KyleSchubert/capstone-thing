package com.roguelikedeckbuilder.mygame.stages.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import static com.roguelikedeckbuilder.mygame.MyGame.batch;
import static com.roguelikedeckbuilder.mygame.MyGame.font;

public class MainMenuStage extends GenericStage {
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
        getStage().addActor(new Character(CharacterTypeName.BIRD, 12, 0));
        getStage().addActor(new Character(CharacterTypeName.BLUE_MUSHROOM, 17, 0));
        getStage().addActor(new Character(CharacterTypeName.HELMET_PENGUIN, 22, 0));
        getStage().addActor(new Character(CharacterTypeName.ORANGE_MUSHROOM, 27, 0));
        getStage().addActor(new Character(CharacterTypeName.PIG, 32, 0));
        getStage().addActor(new Character(CharacterTypeName.PLANT, 37, 0));
        getStage().addActor(new Character(CharacterTypeName.STUMP, 42, 0));

        Group creditsHolder = new Group();
        creditsHolder.setX(49);

        Label creditsTitle = LabelMaker.newLabel("Credits", LabelMaker.getMedium());
        creditsTitle.setAlignment(Align.topLeft);
        creditsHolder.addActor(creditsTitle);

        Label credits = LabelMaker.newLabel(
                "- \"Local Forecast - Elevator\" Kevin MacLeod (incompetech.com) Licensed under Creative Commons: By Attribution 4.0 License http://creativecommons.org/licenses/by/4.0/\n\n"
                        + "- \"Autumn Warrior Remastered\" by Darkkit10 (Newgrounds.com)\n\n"
                        + "- \"Ordinary\" by zybor, Eurns (Newgrounds.com)\n\n"
                        + "- \"Blood Reaper Kane (Boss Theme)\" by Dutonic (Newgrounds.com)",
                LabelMaker.getSmall());

        credits.setAlignment(Align.topLeft);
        credits.setWidth(450);
        credits.setHeight(840);
        credits.setY(12);
        creditsHolder.addActor(credits);
        addActor(creditsHolder);

        creditsTitle.setY(credits.getY() + credits.getHeight() + 12);
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        font.draw(batch, "x " + Player.getPersistentMoney(), 17.3f, 15.3f); // text for currency counter
    }
}
