package com.roguelikedeckbuilder.mygame.combat.enemy;


import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.EnemyPositions;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

public class Enemy {
    private final Character character;
    private final CombatInformation combatInformation;
    private final AttackPattern attackPattern;
    private final XYPair<Float> positionOnStage;
    private final Group intentHolder = new Group();
    private AbilityTypeName nextAbility;

    public Enemy(CharacterTypeName characterTypeName, EnemyPositions position) {
        positionOnStage = position.getPos();
        character = new Character(characterTypeName, positionOnStage.x(), positionOnStage.y());
        character.setUserObject(UserObjectOptions.ENEMY);

        combatInformation = new CombatInformation();
        combatInformation.setPositions(character.getCharacterCenter());
        combatInformation.loadEnemyStats(characterTypeName);
        combatInformation.setHpBarVisibility(true);

        attackPattern = EnemyData.getAttackPattern(characterTypeName);
        intentHolder.setUserObject(UserObjectOptions.INTENT);

        prepareNextAbility();
    }

    public void putOnStage(Stage stage) {
        character.addActor(combatInformation.getStatusEffectVisuals());
        stage.addActor(character);
        stage.addActor(intentHolder);
    }

    public void removeFromStage(Stage stage) {
        stage.getActors().removeValue(character, true);
    }

    public XYPair<Float> getPositionOnStage() {
        return positionOnStage;
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
        Statistics.enemyUsedAbility();

        TargetType targetType = AbilityData.getTargetTypeForHoveringAndHighlighting(nextAbility);
        if (targetType == TargetType.SELF) {
            Statistics.enemyWasTargeted();
        }
        CombatHandler.enemyUsesAbility(nextAbility, combatInformation);

        combatInformation.activateStartTurnStatusEffects();
    }

    private void prepareNextAbility() {
        nextAbility = attackPattern.getNextMove();

        XYPair<Float> position = character.getCharacterCenter();

        intentHolder.addActor(new Intent(nextAbility, position.x() - 18, position.y() - 28));
    }

    public void removeIntentHolder() {
        intentHolder.remove();
    }

    public void endTurn() {
        prepareNextAbility();

        combatInformation.activateEndTurnStatusEffects();
        combatInformation.tickDownDebuffStatusEffects();
    }
}
