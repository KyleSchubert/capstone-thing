package com.roguelikedeckbuilder.mygame.combat.effect;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
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
                switch (effectType) {
                    case ATTACK -> {
                        stopEarly = combatInformation.takeDamage(effectiveness);
                        SoundManager.playHitSound();
                    }
                    case CONSTITUTION -> {
                    }
                    case DEFEND -> {
                        combatInformation.grantDefense(effectiveness);
                        SoundManager.playDefendSound();
                    }
                    case DISCARD_RANDOM_CARD -> {
                    }
                    case DRAW_CARD -> {
                    }
                    case GAIN_ENERGY -> Player.setEnergy(Player.getEnergy() + 1);
                    case GOLD_CHANGE -> Player.changeMoney(effectiveness);
                    case HEAL -> {
                        combatInformation.changeHp(effectiveness);
                        SoundManager.playHealSound();
                    }
                    case MAX_HP_CHANGE -> combatInformation.changeMaxHp(effectiveness);
                    case STRENGTH -> {
                    }
                    case TRUE_DAMAGE_FLAT -> combatInformation.changeHp(-effectiveness);
                    case TRUE_DAMAGE_PERCENT -> {
                        float percent = (float) effectiveness / 100;
                        int change = Math.round(combatInformation.getMaxHp() * percent);
                        combatInformation.changeHp(-change);
                    }
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
                case HIGH_DAMAGE_ONCE -> {
                    effectType = EffectType.ATTACK;
                    effectiveness = 20;
                    repetitions = 1;
                }
                case CONSTITUTION_ONE -> {
                    effectType = EffectType.CONSTITUTION;
                    effectiveness = 1;
                    repetitions = 1;
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
                case DISCARD_RANDOM_CARD_ONE -> {
                    effectType = EffectType.DISCARD_RANDOM_CARD;
                    effectiveness = 1;
                    repetitions = 0;
                }
                case DRAW_CARD_ONE -> {
                    effectType = EffectType.DRAW_CARD;
                    effectiveness = 1;
                    repetitions = 0;
                }
                case GAIN_ENERGY_ONE -> {
                    effectType = EffectType.GAIN_ENERGY;
                    effectiveness = 1;
                    repetitions = 1;
                }
                case GOLD_BONUS_BIG -> {
                    effectType = EffectType.GOLD_CHANGE;
                    effectiveness = 30;
                    repetitions = 1;
                }
                case HEAL_SMALL -> {
                    effectType = EffectType.HEAL;
                    effectiveness = 1;
                    repetitions = 1;
                }
                case MAX_HP_CHANGE_DOWN_ONE -> {
                    effectType = EffectType.MAX_HP_CHANGE;
                    effectiveness = -1;
                    repetitions = 1;
                }
                case MAX_HP_CHANGE_UP_ONE -> {
                    effectType = EffectType.MAX_HP_CHANGE;
                    effectiveness = 1;
                    repetitions = 1;
                }
                case STRENGTH_ONE -> {
                    effectType = EffectType.STRENGTH;
                    effectiveness = 1;
                    repetitions = 1;
                }
                case TRUE_DAMAGE_FLAT_MODERATE -> {
                    effectType = EffectType.TRUE_DAMAGE_FLAT;
                    effectiveness = 12;
                    repetitions = 1;
                }
                case TRUE_DAMAGE_PERCENT_SMALL -> {
                    effectType = EffectType.TRUE_DAMAGE_PERCENT;
                    effectiveness = 10;
                    repetitions = 1;
                }
            }
        }

        public EffectType getEffectType() {
            return effectType;
        }

        public int getEffectiveness() {
            return effectiveness;
        }

        public int getRepetitions() {
            return repetitions;
        }
    }
}
