package com.roguelikedeckbuilder.mygame.stages.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import static com.roguelikedeckbuilder.mygame.MyGame.*;

public class MainMenuStage extends GenericStage {
    private static final XYPair<Float> CHOSEN_CHARACTER_POS = new XYPair<>(17f, 32f);
    private Character chosenCharacter = new Character(CharacterTypeName.HELMET_PENGUIN, CHOSEN_CHARACTER_POS.x(), CHOSEN_CHARACTER_POS.y());

    public MainMenuStage() {
        super();

        // Hidden character selection bounds-by-color image
        Pixmap characterSelectionPixmap = new Pixmap(Gdx.files.internal("characters/character select.png"));
        getStage().addCaptureListener(ClickListenerManager.characterSelectPixelColor(characterSelectionPixmap, this));

        // For debugging:
        Image characterSelection = new Image(new Texture(Gdx.files.internal("characters/character select.png")));
        characterSelection.setScale(SCALE_FACTOR);
        getStage().addActor(characterSelection);

        // Everything else
        Group groupForLabel = new Group();
        groupForLabel.setPosition(2, 36);
        Label label = LabelMaker.newLabel("Chosen Character:", LabelMaker.getLarge());
        groupForLabel.addActor(label);
        addActor(groupForLabel);

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
        getStage().addActor(new Character(CharacterTypeName.SWORD_FISH, 19, 0));
        getStage().addActor(new Character(CharacterTypeName.HELMET_PENGUIN, 12, 0));
        getStage().addActor(new Character(CharacterTypeName.UNIMPRESSED_FISH, 29.6f, -0.3f));
        getStage().addActor(new Character(CharacterTypeName.BURGER, 37.1f, 0));
        getStage().addActor(new Character(CharacterTypeName.KNIGHT, 48.3f, 0));
        getStage().addActor(new Character(CharacterTypeName.HAMMIE, 57, 0));
        getStage().addActor(new Character(CharacterTypeName.ANTEATER, 66.3f, 0));
        getStage().addActor(new Character(CharacterTypeName.CHIPS, 56.8f, 5));
        getStage().addActor(new Character(CharacterTypeName.SAD_DOLLAR, 19.8f, 6.6f));
        getStage().addActor(new Character(CharacterTypeName.EVIL_HH, 62, 12));
        getStage().addActor(new Character(CharacterTypeName.HOT_DOG, 61.4f, 4.4f));
        getStage().addActor(new Character(CharacterTypeName.STARER, 51.6f, 9));
        getStage().addActor(new Character(CharacterTypeName.HAM_SHAMWITCH, 39, 8));
        getStage().addActor(new Character(CharacterTypeName.POINTER, 43, 13));
        getStage().addActor(new Character(CharacterTypeName.SOCK, 57.4f, 13));
        getStage().addActor(new Character(CharacterTypeName.MONOLITH, 68, 7));
        getStage().addActor(new Character(CharacterTypeName.ALIEN, 34, 11.3f));
        getStage().addActor(new Character(CharacterTypeName.HAM_AND_FIST, 43.3f, 0));

        Group creditsHolder = new Group();
        creditsHolder.setTouchable(Touchable.disabled);
        creditsHolder.setX(49);

        Label creditsTitle = LabelMaker.newLabel("Credits", LabelMaker.getMedium());
        creditsTitle.setAlignment(Align.topLeft);
        creditsHolder.addActor(creditsTitle);

        Label credits = LabelMaker.newLabel(
                """
                        - "Local Forecast - Elevator" Kevin MacLeod (incompetech.com) Licensed under Creative Commons: By Attribution 4.0 License http://creativecommons.org/licenses/by/4.0/

                        - "Autumn Warrior Remastered" by Darkkit10 (Newgrounds.com)

                        - "Ordinary" by zybor, Eurns (Newgrounds.com)

                        - "Blood Reaper Kane (Boss Theme)" by Dutonic (Newgrounds.com)""",
                LabelMaker.getSmall());

        credits.setAlignment(Align.topLeft);
        credits.setWidth(450);
        credits.setHeight(840);
        credits.setY(12);
        creditsHolder.addActor(credits);
        addActor(creditsHolder);

        creditsTitle.setY(credits.getY() + credits.getHeight() + 12);

        getStage().addActor(chosenCharacter);
    }

    public void setCharacter(CharacterTypeName characterTypeName) {
        getStage().getActors().removeValue(chosenCharacter, true);

        chosenCharacter = new Character(characterTypeName, CHOSEN_CHARACTER_POS.x(), CHOSEN_CHARACTER_POS.y());
        getStage().addActor(chosenCharacter);

        Player.setCharacterTypeName(characterTypeName);
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        font.draw(batch, "x " + Player.getPersistentMoney(), 17.3f, 15.3f); // text for currency counter
    }
}
