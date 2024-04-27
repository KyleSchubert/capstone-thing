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

import static com.roguelikedeckbuilder.mygame.MyGame.batch;
import static com.roguelikedeckbuilder.mygame.MyGame.font;

public class MainMenuStage extends GenericStage {
    private static final XYPair<Float> CHOSEN_CHARACTER_POS = new XYPair<>(340f, 640f);
    private Character chosenCharacter = new Character(CharacterTypeName.HELMET_PENGUIN, CHOSEN_CHARACTER_POS.x(), CHOSEN_CHARACTER_POS.y());

    public MainMenuStage() {
        super();

        // Hidden character selection bounds-by-color image
        Pixmap characterSelectionPixmap = new Pixmap(Gdx.files.internal("characters/character select.png"));
        getStage().addCaptureListener(ClickListenerManager.characterSelectPixelColor(characterSelectionPixmap, this));

        Image characterSelection = new Image(new Texture(Gdx.files.internal("characters/character select.png")));
        addActor(characterSelection);

        // Everything else
        Group groupForLabel = new Group();
        groupForLabel.setPosition(40, 720);
        Label label = LabelMaker.newLabel("Chosen Character:", LabelMaker.getLarge());
        groupForLabel.addActor(label);
        addActor(groupForLabel);

        Image persistentCurrencyCounterImage = new Image(new Texture(Gdx.files.internal("ITEMS/persistent coin.png")));
        persistentCurrencyCounterImage.setPosition(310, 280);
        addActor(persistentCurrencyCounterImage);

        ImageButton playButton = ClickListenerManager.getMenuSwitchingButton(
                "play", MenuState.START_REWARDS, MenuSoundType.SILENT, 40, 360);
        addActor(playButton);

        ImageButton upgradesButton = ClickListenerManager.getMenuSwitchingButton(
                "upgrades", MenuState.UPGRADES, MenuSoundType.OPEN, 40, 260);
        addActor(upgradesButton);

        ImageButton settingsButton = ClickListenerManager.getMenuSwitchingButton(
                "settings", MenuState.SETTINGS, MenuSoundType.OPEN, 40, 160);
        settingsButton.addCaptureListener(ClickListenerManager.reloadSettingsMenu());
        addActor(settingsButton);

        ImageButton exitButton = ClickListenerManager.getImageButton("exit");
        exitButton.setPosition(40, 60);
        exitButton.addListener(ClickListenerManager.exitingGame());
        addActor(exitButton);

        // Characters on main menu
        addActor(new Character(CharacterTypeName.SWORD_FISH, 380, 0));
        addActor(new Character(CharacterTypeName.HELMET_PENGUIN, 240, 0));
        addActor(new Character(CharacterTypeName.UNIMPRESSED_FISH, 592, -6));
        addActor(new Character(CharacterTypeName.BURGER, 742, 0));
        addActor(new Character(CharacterTypeName.KNIGHT, 966, 0));
        addActor(new Character(CharacterTypeName.HAMMIE, 1140, 0));
        addActor(new Character(CharacterTypeName.ANTEATER, 1326, 0));
        addActor(new Character(CharacterTypeName.CHIPS, 1136, 100));
        addActor(new Character(CharacterTypeName.SAD_DOLLAR, 396, 132));
        addActor(new Character(CharacterTypeName.EVIL_HH, 1240, 240));
        addActor(new Character(CharacterTypeName.HOT_DOG, 1228, 88));
        addActor(new Character(CharacterTypeName.STARER, 1032, 180));
        addActor(new Character(CharacterTypeName.HAM_SHAMWITCH, 780, 160));
        addActor(new Character(CharacterTypeName.POINTER, 860, 260));
        addActor(new Character(CharacterTypeName.SOCK, 1148, 260));
        addActor(new Character(CharacterTypeName.MONOLITH, 1360, 140));
        addActor(new Character(CharacterTypeName.ALIEN, 680, 226));
        addActor(new Character(CharacterTypeName.HAM_AND_FIST, 866, 0));
        addActor(new Character(CharacterTypeName.PEANUT_BEE, 1020, 260));

        Group creditsHolder = new Group();
        creditsHolder.setTouchable(Touchable.disabled);
        creditsHolder.setX(980);

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

        addActor(chosenCharacter);
    }

    public void setCharacter(CharacterTypeName characterTypeName) {
        getStage().getActors().removeValue(chosenCharacter, true);

        chosenCharacter = new Character(characterTypeName, CHOSEN_CHARACTER_POS.x(), CHOSEN_CHARACTER_POS.y());
        addActor(chosenCharacter);

        Player.setCharacterTypeName(characterTypeName);
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        font.draw(batch, "x " + Player.getPersistentMoney(), 346, 306); // text for currency counter
    }
}
