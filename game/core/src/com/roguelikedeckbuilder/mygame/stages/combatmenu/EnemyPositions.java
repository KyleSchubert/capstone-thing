package com.roguelikedeckbuilder.mygame.stages.combatmenu;

import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

public enum EnemyPositions {
    DEBUG_POSITION(400, 400),
    ENEMY1(660, Player.getPositionOnStage().y()),
    ENEMY2(880, Player.getPositionOnStage().y()),
    ENEMY3(1100, Player.getPositionOnStage().y()),
    ENEMY4(1320, Player.getPositionOnStage().y());

    private final XYPair<Float> pos;

    EnemyPositions(float x, float y) {
        pos = new XYPair<>(x, y);
    }

    public XYPair<Float> getPos() {
        return pos;
    }
}
