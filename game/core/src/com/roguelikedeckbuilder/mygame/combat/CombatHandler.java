package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;

public class CombatHandler {
    private static Array<CombatInformation> targets = new Array<>();
    private static Array<Enemy> enemiesThePlayerIsHoveringOver = new Array<>();
    private static boolean isTargetingPlayer = false;

    public static void enemyUsesAbility(AbilityData.AbilityTypeName abilityTypeName, CombatInformation self) {
        // Convert the targetType to make sense since the enemy is using it
        TargetType targetType = AbilityData.getTargetType(abilityTypeName);
        if (targetType == TargetType.SELF) {
            targets.clear();
            targets.add(self);
        } else if (targetType == TargetType.ALL || targetType == TargetType.ONE) {
            targets.clear();
            targets.add(Player.getCombatInformation());
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
                AbilityData.useAbility(card.getUsedAbilityTypeName(), targets);
                card.setToGoToShufflePile(true);
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
                if (enemy.getCharacter().getState() != Character.CharacterState.DYING
                        && enemy.getCharacter().getState() != Character.CharacterState.DEAD) {
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
