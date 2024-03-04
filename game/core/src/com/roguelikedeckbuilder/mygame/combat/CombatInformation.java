package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class CombatInformation {
    private int hp;
    private int maxHp;
    private final HpBar hpBar;
    private XYPair<Float> damageNumberCenter;
    private final BitmapFont font;

    public CombatInformation() {
        hpBar = new HpBar();

        font = new BitmapFont(Gdx.files.internal("hp_and_damage.fnt"));
        font.setUseIntegerPositions(false);
        font.getData().setScale(SCALE_FACTOR / 6);
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

    public boolean takeDamage(int amount) {
        if (hp == 0) {
            return true;
        }
        createHpChangeNumbers(-amount);
        changeHp(-amount);
        return false;
    }

    private void createHpChangeNumbers(int amount) {
        // The numbers should be move-able too, though
    }

    public void setPositions(XYPair<Float> position) {
        damageNumberCenter = position;
        hpBar.setPosition(new XYPair<>(position.x() - 4.1f, position.y() - 1.5f));
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

    public void drawHpBar(SpriteBatch batch) {
        hpBar.draw(batch);
    }
}
