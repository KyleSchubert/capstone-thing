package com.roguelikedeckbuilder.mygame.stages.combatmenu;

import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

public enum EnemyPositions {
    DEBUG_POSITION(20, 20),
    ENEMY1(33, Player.getPositionOnStage().y()),
    ENEMY2(44, Player.getPositionOnStage().y()),
    ENEMY3(55, Player.getPositionOnStage().y()),
    ENEMY4(66, Player.getPositionOnStage().y());

    private final XYPair<Float> pos;

    EnemyPositions(float x, float y) {
        pos = new XYPair<>(x, y);
    }

    public XYPair<Float> getPos() {
        return pos;
    }
}
