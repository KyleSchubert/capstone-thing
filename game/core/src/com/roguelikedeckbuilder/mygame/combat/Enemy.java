package com.roguelikedeckbuilder.mygame.combat;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.stages.CombatMenuStage;

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
        combatInformation.setHpBarPosition(character.getHpBarPosition());
        combatInformation.loadEnemyStats(characterTypeName);
        combatInformation.setHpBarVisibility(true);
    }

    public void putOnStage(Stage stage) {
        stage.addActor(character);
    }

    public boolean isPointWithinRange(XYPair<Float> point) {
        float width = 9;
        float height = 20;
        float heightBottomOffset = 6;

        float left = positionOnStage.x() - width / 2;
        float right = positionOnStage.x() + width / 2;

        float bottom = positionOnStage.y() - height / 2 + heightBottomOffset;
        float top = positionOnStage.y() + height / 2 + heightBottomOffset;

        return (point.x() < right
                && point.x() > left
                && point.y() < top
                && point.y() > bottom);
    }

    public void setTargeted(boolean targeted) {
        character.setTargeted(targeted);
    }

    public CombatInformation getCombatInformation() {
        return combatInformation;
    }

    public void dispose() {
        combatInformation.dispose();
    }
}
