package com.roguelikedeckbuilder.mygame.stages.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

public class CharacterSelection extends Group {
    private static final XYPair<Float> CHOSEN_CHARACTER_POS = new XYPair<>(340f, 640f);
    private Character chosenCharacter = new Character(CharacterTypeName.HELMET_PENGUIN, CHOSEN_CHARACTER_POS.x(), CHOSEN_CHARACTER_POS.y());

    public CharacterSelection(Stage stageThisIsOn) {
        Pixmap characterSelectionPixmap = new Pixmap(Gdx.files.internal("characters/character select.png"));
        stageThisIsOn.addCaptureListener(ClickListenerManager.characterSelectPixelColor(characterSelectionPixmap, stageThisIsOn, this));

        Image characterSelection = new Image(new Texture(Gdx.files.internal("characters/character select.png")));
        addActor(characterSelection);

        Label label = LabelMaker.newLabel("Chosen Character:", LabelMaker.getLarge());
        label.setPosition(40, 720);
        addActor(label);

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

        addActor(chosenCharacter);
    }


    public void setCharacter(CharacterTypeName characterTypeName) {
        getChildren().removeValue(chosenCharacter, true);

        chosenCharacter = new Character(characterTypeName, CHOSEN_CHARACTER_POS.x(), CHOSEN_CHARACTER_POS.y());
        addActor(chosenCharacter);

        Player.setCharacterTypeName(characterTypeName);
    }
}
