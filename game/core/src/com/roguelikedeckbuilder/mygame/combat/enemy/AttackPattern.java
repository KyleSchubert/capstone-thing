package com.roguelikedeckbuilder.mygame.combat.enemy;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;

import java.util.Random;

public class AttackPattern {
    private final Array<Move> moves = new Array<>();
    private final Random random;
    private int totalWeight;
    private AbilityTypeName initialAbility;
    private AbilityTypeName halfHealthAbility;
    private boolean needToUseInitialAbility = false;
    private boolean canUseHalfHealthAbility = false;
    private boolean haveUsedHalfHealthAbility = false;

    public AttackPattern() {
        totalWeight = 0;
        random = new Random();
    }

    public void addMove(Move move) {
        if (move == null) {
            return;
        }

        this.moves.add(move);
        totalWeight += move.getWeighting();
    }

    public void setInitialAbility(AbilityTypeName initialAbility) {
        if (initialAbility == null) {
            return;
        }

        this.initialAbility = initialAbility;
        needToUseInitialAbility = true;
    }

    public AbilityTypeName getNextMove() {
        if (needToUseInitialAbility) {
            needToUseInitialAbility = false;
            return initialAbility;
        } else if (canUseHalfHealthAbility) {
            canUseHalfHealthAbility = false;
            return halfHealthAbility;
        }

        // Ex: with 3 Moves with weightings of 2, randomNumber is in the range of 1-6 as an integer.
        int adjustedTotalWeight = totalWeight;

        for (int i = 0; i < moves.size; i++) {
            if (!moves.get(i).canUse()) {
                adjustedTotalWeight -= moves.get(i).getWeighting();
            }
        }

        int randomNumber = random.nextInt(adjustedTotalWeight + 1);

        for (int i = 0; i < moves.size; i++) {
            Move move = moves.get(i);

            int weightOfMove = move.getWeighting();

            if (move.canUse()) {
                randomNumber -= weightOfMove;

                if (randomNumber <= 0) {
                    handleRepetitionChanges(i);

                    move.increaseUses();
                    return move.getAbilityTypeName();
                }
            }
        }

        // It should never get here anyway, unless an Enemy was given only moves that could be exhausted permanently
        System.out.println("IT GOT TO THE PART IN AttackPattern WHERE IT SHOULDN'T! Using DEFEND, instead.");
        return AbilityTypeName.DEFEND;
    }

    private void handleRepetitionChanges(int indexOfMoveThatIsBeingUsed) {
        for (int i = 0; i < moves.size; i++) {
            if (i == indexOfMoveThatIsBeingUsed) {
                moves.get(i).increaseRepetitions();
            } else {
                moves.get(i).resetRepetitions();
            }
        }
    }

    public void setHalfHealthAbility(AbilityTypeName halfHealthAbility) {
        if (halfHealthAbility == null) {
            return;
        }

        this.halfHealthAbility = halfHealthAbility;
    }

    public void belowHalfHealth() {
        if (halfHealthAbility != null && !haveUsedHalfHealthAbility) {
            canUseHalfHealthAbility = true;
            haveUsedHalfHealthAbility = true;
        }
    }
}
