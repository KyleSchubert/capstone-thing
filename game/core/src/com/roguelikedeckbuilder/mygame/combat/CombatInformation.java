package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.combat.enemy.EnemyData;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffect;
import com.roguelikedeckbuilder.mygame.combat.statuseffect.StatusEffectTypeName;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

import java.util.Optional;


public class CombatInformation {
    private final HpBar hpBar;
    private final Array<StatusEffect> statusEffects = new Array<>();
    private final Array<Item> temporaryItems = new Array<>();
    private final Group statusEffectVisuals = new Group();
    private float statusEffectVisualX = 0;
    private int hp;
    private int maxHp;
    private int defense;
    private boolean isPlayerInformation = false;
    private XYPair<Float> damageNumberCenter;
    private boolean needToPlayHitEffect = false;

    public CombatInformation() {
        hpBar = new HpBar();
        defense = 0;
    }

    public void setPlayerInformation(boolean playerInformation) {
        isPlayerInformation = playerInformation;
    }

    public void loadEnemyStats(CharacterTypeName characterTypeName) {
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

    public boolean takeDamage(double amount, boolean isIgnoringDefense) {
        if (hp == 0) {
            return true;
        }

        if (getStatusEffectValue(StatusEffectTypeName.VULNERABILITY) > 0) {
            amount *= 1.5;
        }

        int adjustedAmount = (int) Math.round(amount);

        int totalDamageTaken;
        if (isIgnoringDefense) {
            totalDamageTaken = Math.min(hp, adjustedAmount);
        } else {
            totalDamageTaken = Math.min(defense + hp, adjustedAmount);
        }

        if (isPlayerInformation) {
            Statistics.playerTookDamage(totalDamageTaken);
        } else {
            Statistics.enemyTookDamage(totalDamageTaken);
        }

        createHpChangeNumbers(totalDamageTaken);

        int excessDamage;
        if (isIgnoringDefense) {
            excessDamage = -adjustedAmount;
        } else {
            excessDamage = changeDefense(-adjustedAmount);
        }

        setNeedToPlayHitEffect(true);

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
        HpChangeNumberHandler.create(damageNumberCenter, amount);
    }

    public void setPositions(XYPair<Float> position) {
        damageNumberCenter = new XYPair<>(position.x(), position.y() + 120);
        hpBar.setPosition(new XYPair<>(position.x() - 82, position.y() - 30));
        statusEffectVisualX = position.x() - 48;
        repositionStatusEffectVisuals();
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

    public void draw(SpriteBatch batch) {
        hpBar.draw(batch);
    }

    public void clearDefense() {
        changeDefense(-defense);
    }

    public void resetStatusEffects() {
        this.statusEffectVisuals.clear();
        this.statusEffects.clear();
    }

    public int getStatusEffectValue(StatusEffectTypeName statusEffectTypeName) {
        Optional<StatusEffect> statusEffect = findStatusEffect(statusEffectTypeName);
        return statusEffect.map(StatusEffect::getAmount).orElse(0);
    }

    private Optional<StatusEffect> findStatusEffect(StatusEffectTypeName statusEffectTypeName) {
        for (StatusEffect statusEffect : statusEffects) {
            if (statusEffect.getStatusEffectTypeName() == statusEffectTypeName) {
                return Optional.of(statusEffect);
            }
        }
        return Optional.empty();
    }

    private void repositionStatusEffectVisuals() {
        // The player's Y position matches the enemies' Y positions
        float TOPMOST_POSITION = Player.getPositionOnStage().y() - 72;
        float VERTICAL_GAP = 34;
        float HORIZONTAL_GAP = 72;
        int AMOUNT_PER_COLUMN = 3;

        float i = 0;
        float additionalX = 0;
        for (StatusEffect statusEffect : statusEffects) {
            statusEffect.setPosition(statusEffectVisualX + additionalX, TOPMOST_POSITION - (i % AMOUNT_PER_COLUMN) * VERTICAL_GAP);
            if (statusEffect.isToBeAdded()) {
                statusEffectVisuals.addActor(statusEffect);
                statusEffect.setToBeAdded(false);
            }
            i++;

            if (i % AMOUNT_PER_COLUMN == 0) {
                additionalX += HORIZONTAL_GAP;
            }
        }
    }

    public void addStatusEffect(StatusEffect statusEffect) {
        // Pre-Cure prevents getting an entire instance of a debuff once
        if (getStatusEffectValue(StatusEffectTypeName.PRE_CURE) > 0 && statusEffect.isDebuff()) {
            Optional<StatusEffect> preCure = findStatusEffect(StatusEffectTypeName.PRE_CURE);
            preCure.ifPresent(effect -> effect.setAmount(effect.getAmount() - 1));
            return;
        }

        Optional<StatusEffect> alreadyExistingStatusEffect = findStatusEffect(statusEffect.getStatusEffectTypeName());
        if (alreadyExistingStatusEffect.isPresent()) {
            int currentAmount = alreadyExistingStatusEffect.get().getAmount();
            alreadyExistingStatusEffect.get().setAmount(currentAmount + statusEffect.getAmount());
        } else {
            statusEffects.add(statusEffect);
        }
        repositionStatusEffectVisuals();
    }

    public Group getStatusEffectVisuals() {
        return statusEffectVisuals;
    }

    public void obtainTemporaryItem(ItemTypeName temporaryItem, boolean singleUseItem) {
        Item item = new Item(temporaryItem);
        if (singleUseItem) {
            item.setActivationLimit(1);
        }
        temporaryItems.add(item);
    }

    public Array<Item> getTemporaryItems() {
        return temporaryItems;
    }

    public void removeUsedUpTemporaryItems() {
        for (int i = temporaryItems.size - 1; i >= 0; i--) {
            if (temporaryItems.get(i).isUsedUp()) {
                temporaryItems.removeIndex(i);
            }
        }
    }

    public void activateEndTurnStatusEffects() {
        // Poison
        int poisonStacks = getStatusEffectValue(StatusEffectTypeName.POISON);
        if (poisonStacks > 0) {
            takeDamage(poisonStacks, false);
        }
    }

    public void activateStartTurnStatusEffects() {
        // Burning
        int burningStacks = getStatusEffectValue(StatusEffectTypeName.BURNING);
        if (burningStacks > 0) {
            takeDamage(burningStacks, false);
        }
    }

    public void tickDownDebuffStatusEffects() {
        boolean removedSomething = false;

        for (int i = statusEffects.size - 1; i >= 0; i--) {
            StatusEffect statusEffect = statusEffects.get(i);
            if (statusEffect.isDebuff()) {
                statusEffect.setAmount(statusEffect.getAmount() - 1);
                if (statusEffect.getAmount() <= 0) {
                    statusEffects.removeIndex(i);
                    removedSomething = true;
                }
            }
        }

        if (removedSomething) {
            repositionStatusEffectVisuals();
        }
    }

    public boolean getNeedToPlayHitEffect() {
        return needToPlayHitEffect;
    }

    public void setNeedToPlayHitEffect(boolean needToPlayHitEffect) {
        this.needToPlayHitEffect = needToPlayHitEffect;
    }
}
