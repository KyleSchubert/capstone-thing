package com.roguelikedeckbuilder.mygame.combat;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.GenericHelpers;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.stages.CombatMenuStage;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Enemy {
    private final Character character;
    private final CombatInformation combatInformation;
    private AbilityData nextAbility;
    private final XYPair<Float> positionOnStage;

    public Enemy(Character.CharacterTypeName characterTypeName, CombatMenuStage.EnemyPositions position) {
        positionOnStage = position.getPos();
        character = new Character(characterTypeName, positionOnStage.x(), positionOnStage.y());
        character.setUserObject(UserObjectOptions.ENEMY);

        combatInformation = new CombatInformation();
        combatInformation.setPositions(character.getCharacterCenter());
        combatInformation.loadEnemyStats(characterTypeName);
        combatInformation.setHpBarVisibility(true);
    }

    public void putOnStage(Stage stage) {
        stage.addActor(character);
    }

    public void removeFromStage(Stage stage) {
        stage.getActors().removeValue(character, true);
    }

    public XYPair<Float> getPositionOnStage() {
        return positionOnStage;
    }

    public static boolean isPointWithinRange(XYPair<Float> point, XYPair<Float> positionOfEnemy) {
        return GenericHelpers.isPointWithinRange(point, positionOfEnemy);
    }

    public void setTargeted(boolean targeted) {
        character.setTargeted(targeted);
    }

    public CombatInformation getCombatInformation() {
        return combatInformation;
    }

    public Character getCharacter() {
        return character;
    }
}
