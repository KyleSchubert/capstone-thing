package com.roguelikedeckbuilder.mygame.combat;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.GenericHelpers;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.stages.CombatMenuStage;

public class Enemy {
    private final Character character;
    private final CombatInformation combatInformation;
    private Array<AbilityData.AbilityTypeName> abilityOptions;
    private AbilityData.AbilityTypeName nextAbility;
    private final XYPair<Float> positionOnStage;

    public Enemy(Character.CharacterTypeName characterTypeName, CombatMenuStage.EnemyPositions position) {
        positionOnStage = position.getPos();
        character = new Character(characterTypeName, positionOnStage.x(), positionOnStage.y());
        character.setTouchable(Touchable.disabled);
        character.setUserObject(UserObjectOptions.ENEMY);

        combatInformation = new CombatInformation();
        combatInformation.setPositions(character.getCharacterCenter());
        combatInformation.loadEnemyStats(characterTypeName);
        combatInformation.setHpBarVisibility(true);

        abilityOptions = EnemyData.getAbilityOptions(characterTypeName);

        nextAbility = abilityOptions.random();
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

    public void beginTurn() {
        System.out.println("An enemy is using: " + nextAbility.name());
        CombatHandler.enemyUsesAbility(nextAbility, combatInformation);
    }

    public void endTurn() {
        nextAbility = abilityOptions.random();
    }
}
