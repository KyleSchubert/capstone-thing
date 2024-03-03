package com.roguelikedeckbuilder.mygame.combat;

public class CombatInformation {
    private int hp;
    private int maxHp;

    public void loadEnemyStats(EnemyData enemyData) {
        // use enemyData to load the stats in
    }

    public void loadPlayerStats() {
        maxHp = 70;
        hp = maxHp;
    }

    public void changeHp(int change) {
        if (hp + change >= 1) {
            hp = Math.min(hp + change, maxHp);
        } else {
            hp = 0;
        }
    }

    public void changeMaxHp(int change) {
        if (maxHp + change >= 1) {
            maxHp += change;
            changeHp(change);
        } else {
            maxHp = 1;
            hp = 1;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }
}
