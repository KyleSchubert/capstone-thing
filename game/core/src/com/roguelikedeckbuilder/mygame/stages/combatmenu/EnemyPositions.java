package com.roguelikedeckbuilder.mygame.stages.combatmenu;

import com.roguelikedeckbuilder.mygame.helpers.XYPair;

public enum EnemyPositions {
    DEBUG_POSITION(20, 20),
    ENEMY1(38, 22.8f),
    ENEMY2(47, 22.8f),
    ENEMY3(56, 22.8f),
    ENEMY4(65, 22.8f);

    private final XYPair<Float> pos;

    EnemyPositions(float x, float y) {
        pos = new XYPair<>(x, y);
    }

    public XYPair<Float> getPos() {
        return pos;
    }
}
