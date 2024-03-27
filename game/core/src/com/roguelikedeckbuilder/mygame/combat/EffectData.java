package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;

public class EffectData {
    private static Array<IndividualEffectData> data;

    public static void initialize() {
        data = new Array<>();

        for (EffectName name : EffectName.values()) {
            data.add(new IndividualEffectData(name));
        }
    }

    public static EffectType getEffectType(EffectName effectName) {
        return data.get(effectName.ordinal()).getEffectType();
    }

    public static int getEffectiveness(EffectName effectName) {
        return data.get(effectName.ordinal()).getEffectiveness();
    }

    public static int getRepetitions(EffectName effectName) {
        return data.get(effectName.ordinal()).getRepetitions();
    }

    public static void useEffect(EffectName effectName, Array<CombatInformation> targets) {
        EffectType effectType = getEffectType(effectName);

        if (effectType == EffectType.NOTHING) {
            return;
        }

        int repetitions = getRepetitions(effectName);
        int effectiveness = getEffectiveness(effectName);

        boolean stopEarly = false;

        for (CombatInformation combatInformation : targets) {
            for (int i = 0; i < repetitions; i++) {
                if (effectType == EffectData.EffectType.ATTACK) {
                    stopEarly = combatInformation.takeDamage(effectiveness);
                    SoundManager.playHitSound();
                } else if (effectType == EffectData.EffectType.DEFEND) {
                    combatInformation.grantDefense(effectiveness);
                    SoundManager.playDefendSound();
                }

                if (stopEarly) {
                    break;
                }
            }
        }
    }

    private static class IndividualEffectData {
        private EffectType effectType;
        private int effectiveness;
        private int repetitions;

        public IndividualEffectData(EffectName effectName) {
            switch (effectName) {
                case NOTHING -> {
                    effectType = EffectType.NOTHING;
                    effectiveness = 0;
                    repetitions = 0;
                }
                case DAMAGE_MANY_TIMES -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 1;
                    repetitions = 8;
                }
                case HIGH_DAMAGE_MANY_TIMES -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 3;
                    repetitions = 7;
                }
                case DAMAGE_A_BIT -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 9;
                    repetitions = 1;
                }
                case DEFEND_SOME -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 5;
                    repetitions = 1;
                }
                case DEFEND_TWICE_A_BIT -> {
                    effectType = EffectType.DEFEND;
                    effectiveness = 4;
                    repetitions = 2;
                }
            }
        }

        public EffectData.EffectType getEffectType() {
            return effectType;
        }

        public int getEffectiveness() {
            return effectiveness;
        }

        public int getRepetitions() {
            return repetitions;
        }
    }

    public enum EffectName {
        NOTHING,
        DAMAGE_MANY_TIMES,
        HIGH_DAMAGE_MANY_TIMES,
        DAMAGE_A_BIT,
        DEFEND_SOME,
        DEFEND_TWICE_A_BIT
    }

    public enum EffectType {
        ATTACK, DEFEND, NOTHING
    }
}
