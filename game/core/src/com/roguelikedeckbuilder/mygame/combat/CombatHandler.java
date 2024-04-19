package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterState;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.combat.enemy.Enemy;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

public class CombatHandler {
    private static Array<CombatInformation> targets = new Array<>();
    private static Array<Enemy> enemiesThePlayerIsHoveringOver = new Array<>();
    private static boolean isTargetingPlayer = false;

    public static void enemyUsesAbility(AbilityTypeName abilityTypeName, CombatInformation self, TargetType targetType) {
        // Convert the targetType to make sense since the enemy is using it
        if (targetType == TargetType.SELF) {
            targets.clear();
            targets.add(self);
        } else if (targetType == TargetType.ALL || targetType == TargetType.ONE) {
            targets.clear();
            targets.add(Player.getCombatInformation());
            Statistics.playerWasTargeted();
        }

        AbilityData.useAbility(abilityTypeName, targets);
    }

    public static void playerUsesCard(Card card) {
        System.out.println("Player used this card: " + AbilityData.getName(card.getUsedAbilityTypeName()));
        for (Enemy enemy : enemiesThePlayerIsHoveringOver) {
            enemy.setTargeted(false);
        }
        Player.getCharacter().setTargeted(false);

        if (targets.size > 0) {
            if (Player.tryToSpendEnergy(AbilityData.getEnergyCost(card.getUsedAbilityTypeName()))) {
                card.setUsed(true);
                AbilityData.useAbility(card.getUsedAbilityTypeName(), targets);
                card.setToGoToShufflePile(true);
                Statistics.playedCard();
            }
        }
        isTargetingPlayer = false;
    }

    public static void setEnemiesThePlayerIsHoveringOver(Array<Enemy> enemiesThePlayerIsHoveringOver) {
        CombatHandler.enemiesThePlayerIsHoveringOver = enemiesThePlayerIsHoveringOver;
        Array<CombatInformation> targetArray = new Array<>();

        if (isTargetingPlayer) {
            targetArray.add(Player.getCombatInformation());
            Player.getCharacter().setTargeted(true);
        } else {
            for (Enemy enemy : enemiesThePlayerIsHoveringOver) {
                if (enemy.getCharacter().getState() != CharacterState.DYING
                        && enemy.getCharacter().getState() != CharacterState.DEAD) {
                    targetArray.add(enemy.getCombatInformation());
                    enemy.setTargeted(true);
                }
            }
        }

        setTargets(targetArray);
    }

    private static void setTargets(Array<CombatInformation> theTargets) {
        targets = theTargets;
    }

    public static void setIsTargetingPlayer(boolean isTargetingPlayer) {
        CombatHandler.isTargetingPlayer = isTargetingPlayer;
    }
}
