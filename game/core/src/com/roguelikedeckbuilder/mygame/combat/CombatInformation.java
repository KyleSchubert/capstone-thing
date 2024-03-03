package com.roguelikedeckbuilder.mygame.combat;

import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

public class CombatInformation {
    private int hp;
    private int maxHp;
    private final HpBar hpBar;

    public CombatInformation() {
        hpBar = new HpBar();
    }

    public void loadEnemyStats(Character.CharacterTypeName characterTypeName) {
        maxHp = EnemyData.getMaxHp(characterTypeName);
        hp = maxHp;
        hpBar.update(hp, maxHp);
    }

    public void loadPlayerStats() {
        maxHp = 70;
        hp = maxHp;
        hpBar.update(hp, maxHp);
    }

    public void changeHp(int change) {
        if (hp + change >= 1) {
            hp = Math.min(hp + change, maxHp);
        } else {
            hp = 0;
        }
        hpBar.update(hp, maxHp);
    }

    public void changeMaxHp(int change) {
        if (maxHp + change >= 1) {
            maxHp += change;
            changeHp(change);
        } else {
            maxHp = 1;
            hp = 1;
            hpBar.update(hp, maxHp);
        }
    }

    public void setHpBarPosition(XYPair<Float> position) {
        hpBar.setPosition(position);
    }

    public void setHpBarVisibility(boolean visibility) {
        hpBar.setVisible(visibility);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public void dispose() {
        hpBar.dispose();
    }

    public void drawHpBar() {
        hpBar.draw();
    }
}
