package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;

public class CombatHandler {
    private static Array<CombatInformation> targets = new Array<>();
    private static Array<Enemy> enemiesThePlayerIsHoveringOver = new Array<>();
    private static boolean isTargetingPlayer = false;

    public static void enemyUsesAbility(Ability.AbilityTypeName abilityTypeName) {
        // do any additional PRE effects of the enemy --> an effect on them triggers?
        useAbility(abilityTypeName);
        // do any additional POST effects of the enemy --> they self-destruct?
    }

    private static void useAbility(Ability.AbilityTypeName abilityTypeName) {
        int repetitions = AbilityData.getRepetitions(abilityTypeName);
        int effectiveness = AbilityData.getEffectiveness(abilityTypeName);
        EffectType effectType = AbilityData.getEffectType(abilityTypeName);

        boolean stopEarly = false;

        for (CombatInformation combatInformation : targets) {
            for (int i = 0; i < repetitions; i++) {
                if (effectType == EffectType.ATTACK) {
                    stopEarly = combatInformation.takeDamage(effectiveness);
                } else if (effectType == EffectType.DEFEND) {
                    combatInformation.grantDefense(effectiveness);
                }

                if (stopEarly) {
                    break;
                }
            }
        }
    }

    public static void playerUsesCard(Card card) {
        System.out.println("Player used this card: " + card.getCardType());
        for (Enemy enemy : enemiesThePlayerIsHoveringOver) {
            enemy.setTargeted(false);
        }
        Player.getCharacter().setTargeted(false);

        if (targets.size > 0) {
            if (Player.tryToSpendEnergy(card.getAbilityEnergyCost())) {
                // do any additional PRE effects of card/player
                useAbility(card.getAbilityTypeName());
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
