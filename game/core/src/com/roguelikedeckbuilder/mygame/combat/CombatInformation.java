package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.tracking.Statistics;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class CombatInformation {
    private int hp;
    private int maxHp;
    private int defense;
    private final HpBar hpBar;
    private boolean isPlayerInformation = false;
    private XYPair<Float> damageNumberCenter;
    private final BitmapFont font;

    public CombatInformation() {
        hpBar = new HpBar();

        font = new BitmapFont(Gdx.files.internal("hp_and_damage.fnt"));
        font.setUseIntegerPositions(false);
        font.getData().setScale(SCALE_FACTOR / 6);

        defense = 0;
    }

    public void setPlayerInformation(boolean playerInformation) {
        isPlayerInformation = playerInformation;
    }

    public void loadEnemyStats(Character.CharacterTypeName characterTypeName) {
        maxHp = EnemyData.getMaxHp(characterTypeName);
        hp = maxHp;
        updateHpBar();
    }

    public void loadPlayerStats() {
        maxHp = 70;
        hp = maxHp;
        updateHpBar();
    }

    public void changeHp(int change) {
        int hpBefore = hp;

        if (hp + change >= 1) {
            hp = Math.min(hp + change, maxHp);
        } else {
            hp = 0;
            if (isPlayerInformation) {
                Statistics.playerDied();
            } else {
                Statistics.enemyDied();
            }
        }

        if (change > 0) {
            if (isPlayerInformation) {
                Statistics.playerHealed(hp - hpBefore);
            } else {
                Statistics.enemyHealed(hp - hpBefore);
            }
        }
        updateHpBar();
    }

    public void changeMaxHp(int change) {
        if (maxHp + change >= 1) {
            maxHp += change;
            changeHp(change);

            if (isPlayerInformation) {
                Statistics.playerMaxHpChanged(change);
            } else {
                Statistics.enemyMaxHpChanged(change);
            }
        } else {
            maxHp = 1;
            hp = 1;
            updateHpBar();
        }
    }

    public boolean takeDamage(int amount) {
        if (hp == 0) {
            return true;
        }

        if (isPlayerInformation) {
            Statistics.playerTookDamage(amount);
        } else {
            Statistics.enemyTookDamage(amount);
        }

        createHpChangeNumbers(amount);
        int excessDamage = changeDefense(-amount);
        changeHp(excessDamage);
        return false;
    }

    public void grantDefense(int amount) {
        changeDefense(amount);
    }

    private int changeDefense(int change) {
        int excessDamage = 0;
        defense += change;

        if (change > 0) {
            if (isPlayerInformation) {
                Statistics.playerGainedDefense(change);
            } else {
                Statistics.enemyGainedDefense(change);
            }
        }

        if (defense < 0) {
            excessDamage = defense;
            defense = 0;
        }

        updateHpBar();
        return excessDamage;
    }

    private void updateHpBar() {
        hpBar.update(hp, maxHp, defense);
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

    public void clearDefense() {
        changeDefense(-defense);
    }
}
