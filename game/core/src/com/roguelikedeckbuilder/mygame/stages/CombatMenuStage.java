package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.combat.Enemy;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

public class CombatMenuStage extends GenericStage {
    private final Array<Enemy> currentEnemies = new Array<>();

    public CombatMenuStage(ScreenViewport viewportForStage, ImageButton exitButtonForTesting) {
        super(viewportForStage, "combat background");

        // Reposition the background
        getStageBackgroundActor().setPosition(-7, -5);

        // TODO: REMOVE THIS AFTER COMBAT IS IMPLEMENTED
        exitButtonForTesting.setPosition(33, 36);
        this.getStage().addActor(exitButtonForTesting);

        // Add the player
        this.getStage().addActor(Player.getCharacter());
    }

    public void batch(float elapsedTime) {
        super.batch(elapsedTime);
    }

    public void addEnemy(Character.CharacterTypeName characterTypeName) {
        XYPair<Float> position = generateEnemyPosition();
        Enemy enemy = new Enemy(characterTypeName, position.x(), position.y());

        currentEnemies.add(enemy);
        enemy.putOnStage(getStage());
    }

    private XYPair<Float> generateEnemyPosition() {
        return switch (currentEnemies.size) {
            case 0 -> EnemyPositions.ENEMY1.getPos();
            case 1 -> EnemyPositions.ENEMY2.getPos();
            case 2 -> EnemyPositions.ENEMY3.getPos();
            case 3 -> EnemyPositions.ENEMY4.getPos();
            default -> {
                System.out.println("[Huh?] There shouldn't be more than 4 enemies.");
                yield new XYPair<>(4f, 4f);
            }
        };
    }

    public void reset() {
        Array<Actor> mustRemove = new Array<>();

        for (Actor actor : this.getStage().getActors()) {
            UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
            if (actorType == UserObjectOptions.ENEMY) {
                mustRemove.add(actor);
            }
        }

        for (Actor actor : mustRemove) {
            boolean removalSuccess = this.getStage().getActors().removeValue(actor, true);
            if (!removalSuccess) {
                System.out.println("[Huh?] Tried but did not remove the actor " + actor + " from the CombatMenuStage.");
            }
        }

        currentEnemies.clear();
    }

    public enum EnemyPositions {
        ENEMY1(44, 22.8f),
        ENEMY2(49, 22.8f),
        ENEMY3(54, 22.8f),
        ENEMY4(59, 22.8f);

        private final XYPair<Float> pos;

        EnemyPositions(float x, float y) {
            pos = new XYPair<>(x, y);
        }

        public XYPair<Float> getPos() {
            return pos;
        }
    }
}
