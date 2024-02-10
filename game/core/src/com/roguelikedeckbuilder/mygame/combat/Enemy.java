package com.roguelikedeckbuilder.mygame.combat;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;

public class Enemy {
    private final Character character;
    private int hp;
    private int maxHp;
    private Ability nextAbility;

    public Enemy(Character.CharacterTypeName characterTypeName, float x, float y) {
        character = new Character(characterTypeName, x, y);
        character.setUserObject(UserObjectOptions.ENEMY);
    }

    public void putOnStage(Stage stage) {
        stage.addActor(character);
    }
}
