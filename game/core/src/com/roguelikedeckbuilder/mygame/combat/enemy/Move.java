package com.roguelikedeckbuilder.mygame.combat.enemy;

import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;

public class Move {
    private final int weighting;
    private final AbilityTypeName abilityTypeName;
    private int useLimit = Integer.MAX_VALUE;
    private int repetitionLimit = Integer.MAX_VALUE;
    private int uses = 0;
    private int repetitions = 0;

    public Move(int weighting, AbilityTypeName abilityTypeName) {
        this.weighting = weighting;
        this.abilityTypeName = abilityTypeName;
    }

    private Move(int weighting, AbilityTypeName abilityTypeName, int useLimit, int repetitionLimit) {
        this.weighting = weighting;
        this.abilityTypeName = abilityTypeName;
        setUseLimit(useLimit);
        setRepetitionLimit(repetitionLimit);
    }

    public Move copy() {
        return new Move(this.weighting, this.abilityTypeName, this.useLimit, this.repetitionLimit);
    }

    public boolean canUse() {
        return uses < useLimit && repetitions < repetitionLimit;
    }

    public int getWeighting() {
        return weighting;
    }

    public AbilityTypeName getAbilityTypeName() {
        return abilityTypeName;
    }

    public void increaseUses() {
        this.uses++;
    }

    public void increaseRepetitions() {
        this.repetitions++;
    }

    public void resetRepetitions() {
        this.repetitions = 0;
    }

    public void setUseLimit(int useLimit) {
        this.useLimit = useLimit;
    }

    public void setRepetitionLimit(int repetitionLimit) {
        this.repetitionLimit = repetitionLimit;
    }
}
