package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;

public class CombatHandler {
    private static Array<CombatInformation> targets = new Array<>();
    private static Array<Enemy> enemiesThePlayerIsHoveringOver = new Array<>();
    private static boolean isTargetingPlayer = false;

    public static void enemyUsesAbility(AbilityData.AbilityTypeName abilityTypeName) {
        // do any additional PRE effects of the enemy --> an effect on them triggers?
        AbilityData.useAbility(abilityTypeName, targets);
        // do any additional POST effects of the enemy --> they self-destruct?
    }

    public static void playerUsesCard(Card card) {
        System.out.println("Player used this card: " + AbilityData.getName(card.getUsedAbilityTypeName()));
        for (Enemy enemy : enemiesThePlayerIsHoveringOver) {
            enemy.setTargeted(false);
        }
        Player.getCharacter().setTargeted(false);

        if (targets.size > 0) {
            if (Player.tryToSpendEnergy(AbilityData.getEnergyCost(card.getUsedAbilityTypeName()))) {
                // do any additional PRE effects of card/player
                AbilityData.useAbility(card.getUsedAbilityTypeName(), targets);
                // do any additional POST effects of card/player
                // send card to shuffle pile
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
