package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterState;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.combat.enemy.Enemy;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.CombatMenuStage;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

import java.util.Random;

public class CombatHandler {
    private static boolean isTargetingValid = false;
    private static CombatInformation mainTarget = Player.getCombatInformation();

    public static void enemyUsesAbility(AbilityTypeName abilityTypeName, CombatInformation self) {
        AbilityData.useAbility(self, abilityTypeName, false);
    }

    public static void playerUsesCard(Card card) {
        for (Enemy enemy : CombatMenuStage.getCurrentEnemies()) {
            enemy.setTargeted(false);
        }
        CombatMenuStage.getPlayerCharacter().setTargeted(false);

        if (isTargetingValid) {
            if (Player.tryToSpendEnergy(AbilityData.getEnergyCost(card.getUsedAbilityTypeName()))) {
                card.setUsed(true);
                AbilityData.useAbility(Player.getCombatInformation(), card.getUsedAbilityTypeName(), true);
                card.setToGoToShufflePile(true);
                card.setDiscardedByUse(true);
                Statistics.playedCard();
                System.out.println("Player used this card: " + AbilityData.getName(card.getUsedAbilityTypeName()));
            }
            isTargetingValid = false;
        }
    }

    public static void setEnemyTargets(Array<Enemy> allEnemies, Enemy mainTargettedEnemy) {
        setTargets(allEnemies, mainTargettedEnemy);
    }

    public static void setEnemyTargets(Array<Enemy> allEnemies) {
        Random random = new Random();
        int randomIndex = random.nextInt(allEnemies.size);

        setTargets(allEnemies, allEnemies.get(randomIndex));
    }

    private static void setTargets(Array<Enemy> allEnemies, Enemy mainTargettedEnemy) {
        Array<Enemy> filteredEnemies = new Array<>();
        Array<CombatInformation> filteredEnemiesCombatInformation = new Array<>();
        TargetType visualTargetType = Player.getPotentialAbilityTargetType();

        boolean validCardTargeting = true;

        // This determines which targets should have the "target glow" show up behind them and which things can be targeted
        if (visualTargetType == TargetType.SELF) {
            CombatMenuStage.getPlayerCharacter().setTargeted(true);
        }
        for (Enemy enemy : allEnemies) {
            if (enemy.getCharacter().getState() != CharacterState.DYING
                    && enemy.getCharacter().getState() != CharacterState.DEAD) {
                if (visualTargetType == TargetType.ALL) {
                    enemy.setTargeted(true);
                }
                filteredEnemies.add(enemy);
                filteredEnemiesCombatInformation.add(enemy.getCombatInformation());
            }
        }
        if (filteredEnemies.isEmpty()) {
            // All enemies are dead or dying
            validCardTargeting = false;
        }

        CombatInformation oneTarget = mainTargettedEnemy.getCombatInformation();

        if (visualTargetType == TargetType.ONE) {
            if (mainTargettedEnemy.getCharacter().getState() != CharacterState.DYING
                    && mainTargettedEnemy.getCharacter().getState() != CharacterState.DEAD) {
                mainTargettedEnemy.setTargeted(true);
            } else {
                // The enemy they're trying to hover over is dead or dying
                validCardTargeting = false;
            }
        }

        isTargetingValid = validCardTargeting;
        mainTarget = oneTarget;
    }

    public static CombatInformation getMainTarget() {
        return mainTarget;
    }

    public static void resetIsTargetingValid() {
        isTargetingValid = false;
    }
}
