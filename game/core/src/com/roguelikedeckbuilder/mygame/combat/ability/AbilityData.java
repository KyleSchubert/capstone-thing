package com.roguelikedeckbuilder.mygame.combat.ability;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectData;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectName;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.CombatMenuStage;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

public class AbilityData {
    private static Array<IndividualAbilityData> data;

    public static void initialize() {
        data = new Array<>();

        for (AbilityTypeName name : AbilityTypeName.values()) {
            data.add(new IndividualAbilityData(name));
        }
    }

    public static String getName(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getName();
    }

    public static int getEnergyCost(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getEnergyCost();
    }

    public static TargetType getTargetTypeForHoveringAndHighlighting(AbilityTypeName typeName) {
        TargetType targetType1 = EffectData.getTargetType(getPreEffect(typeName));
        TargetType targetType2 = EffectData.getTargetType(getEffect(typeName));
        TargetType targetType3 = EffectData.getTargetType(getPostEffect(typeName));

        // This is the order of priority
        if (targetType1 == TargetType.ALL || targetType2 == TargetType.ALL || targetType3 == TargetType.ALL) {
            return TargetType.ALL;
        } else if (targetType1 == TargetType.ONE || targetType2 == TargetType.ONE || targetType3 == TargetType.ONE) {
            return TargetType.ONE;
        } else if (targetType1 == TargetType.SELF || targetType2 == TargetType.SELF || targetType3 == TargetType.SELF) {
            return TargetType.SELF;
        }
        // It would never get here anyway
        System.out.println("IT GOT HERE ANYWAY <----------------");
        return TargetType.SELF;
    }

    public static EffectName getPreEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getPreEffect();
    }

    public static EffectName getEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getEffect();
    }

    public static EffectName getPostEffect(AbilityTypeName typeName) {
        return data.get(typeName.ordinal()).getPostEffect();
    }

    public static String getDescription(AbilityTypeName typeName) {
        String preEffectText = "";
        String effectText = "";
        String postEffectText = "";

        EffectName preEffect = getPreEffect(typeName);
        if (preEffect != EffectName.NOTHING) {
            preEffectText = EffectData.prepareOneEffectDescription(preEffect);
        }

        EffectName effect = getEffect(typeName);
        if (effect != EffectName.NOTHING) {
            effectText = EffectData.prepareOneEffectDescription(effect);
        }

        EffectName postEffect = getPostEffect(typeName);
        if (postEffect != EffectName.NOTHING) {
            postEffectText = EffectData.prepareOneEffectDescription(postEffect);
        }

        return String.join("", preEffectText, effectText, postEffectText);
    }

    public static void useAbility(CombatInformation theAttacker, AbilityTypeName abilityTypeName, boolean isPlayerTheAttacker) {
        Array<CombatInformation> allEnemies = CombatMenuStage.getCombatInformationForLivingEnemies();
        CombatInformation mainTarget; // Can be the CombatInformation of the Player or one Enemy
        if (isPlayerTheAttacker) {
            mainTarget = CombatHandler.getMainTarget();
        } else {
            mainTarget = Player.getCombatInformation();
        }

        Array<CombatInformation> preEffectTargets = getTargets(AbilityData.getPreEffect(abilityTypeName), theAttacker, allEnemies, mainTarget, isPlayerTheAttacker);
        EffectData.useEffect(theAttacker, AbilityData.getPreEffect(abilityTypeName), preEffectTargets);

        Array<CombatInformation> effectTargets = getTargets(AbilityData.getEffect(abilityTypeName), theAttacker, allEnemies, mainTarget, isPlayerTheAttacker);
        EffectData.useEffect(theAttacker, AbilityData.getEffect(abilityTypeName), effectTargets);

        Array<CombatInformation> postEffectTargets = getTargets(AbilityData.getPostEffect(abilityTypeName), theAttacker, allEnemies, mainTarget, isPlayerTheAttacker);
        EffectData.useEffect(theAttacker, AbilityData.getPostEffect(abilityTypeName), postEffectTargets);
    }

    private static Array<CombatInformation> getTargets(EffectName effectName, CombatInformation theAttacker, Array<CombatInformation> allEnemies, CombatInformation mainTarget, boolean isPlayerTheAttacker) {
        // Now to look at the targeting of the individual effects and use what is needed
        Array<CombatInformation> targets = new Array<>();

        TargetType targetType = EffectData.getTargetType(effectName);
        if (targetType == TargetType.ALL) {
            targets = allEnemies;
            if (isPlayerTheAttacker) {
                Statistics.enemyWasTargeted();
            } else {
                Statistics.playerWasTargeted();
            }
        } else if (targetType == TargetType.ONE) {
            targets.add(mainTarget);
            if (isPlayerTheAttacker) {
                Statistics.enemyWasTargeted();
            } else {
                Statistics.playerWasTargeted();
            }
        } else if (targetType == TargetType.SELF) {
            targets.add(theAttacker);
        }

        return targets;
    }

    private static class IndividualAbilityData {
        private String name;
        private int energyCost;
        private EffectName preEffect;
        private EffectName effect;
        private EffectName postEffect;

        public IndividualAbilityData(AbilityTypeName abilityTypeName) {
            preEffect = EffectName.NOTHING;
            postEffect = EffectName.NOTHING;

            switch (abilityTypeName) {
                case DISCARD_DRAW -> {
                    name = "Risky Draw";
                    energyCost = 1;
                    preEffect = EffectName.DISCARD_RANDOM_CARD_ONE;
                    effect = EffectName.DRAW_CARD_ONE;
                    postEffect = EffectName.DRAW_CARD_ONE;
                }
                case DRAW -> {
                    name = "You Draw 2 Cards.";
                    energyCost = 1;
                    preEffect = EffectName.DRAW_CARD_ONE;
                    effect = EffectName.DRAW_CARD_ONE;
                }
                case ENERGY_SLICES -> {
                    name = "Energy Slices";
                    energyCost = 2;
                    effect = EffectName.DAMAGE_MANY_TIMES;
                }
                case ENERGY_SLICES_UPGRADED -> {
                    name = "Energy Slices+";
                    energyCost = 1;
                    effect = EffectName.DAMAGE_MANY_TIMES;
                }
                case FLAME -> {
                    name = "Flame";
                    energyCost = 1;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                }
                case FLAME_UPGRADED -> {
                    name = "Double Flame";
                    energyCost = 1;
                    effect = EffectName.DAMAGE_MANY_TIMES;
                    postEffect = EffectName.DAMAGE_A_BIT;
                }
                case FIRE_STRIKE -> {
                    name = "Fire Strike";
                    energyCost = 1;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                }
                case FIRE_STRIKE_UPGRADED -> {
                    name = "Fire Strike+";
                    energyCost = 1;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                    postEffect = EffectName.DEFEND_SOME;
                }
                case AMPLIFY -> {
                    name = "Amplify";
                    energyCost = 3;
                    preEffect = EffectName.STRENGTH_ONE;
                    effect = EffectName.CONSTITUTION_ONE;
                }
                case AMPLIFY_UPGRADED -> {
                    name = "Amplify+";
                    energyCost = 2;
                    preEffect = EffectName.STRENGTH_ONE;
                    effect = EffectName.CONSTITUTION_ONE;
                }
                case DEFEND -> {
                    name = "Defend";
                    energyCost = 1;
                    effect = EffectName.DEFEND_SOME;
                }
                case DEFEND_UPGRADED -> {
                    name = "Defend+";
                    energyCost = 1;
                    effect = EffectName.DEFEND_TWICE_A_BIT;
                }
                case ITEM_SWORD_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    effect = EffectName.DAMAGE_A_BIT;
                }
                case ITEM_SWORD_2_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    effect = EffectName.DAMAGE_A_BIT_TO_ONE;
                }
                case ITEM_SHIELD_ABILITY -> {
                    name = "";
                    energyCost = 0;
                    effect = EffectName.DEFEND_SOME;
                    postEffect = EffectName.HEAL_SMALL;
                }
                case NOTHING -> {
                    name = "Sold";
                    energyCost = 0;
                    effect = EffectName.NOTHING;
                }
                case PERCENTAGE_PUNCH -> {
                    name = "% Punch";
                    energyCost = 1;
                    effect = EffectName.TRUE_DAMAGE_PERCENT_SMALL;
                }
                case PERCENTAGE_PUNCH_UPGRADED -> {
                    name = "% Punch+";
                    energyCost = 1;
                    effect = EffectName.TRUE_DAMAGE_PERCENT_A_LITTLE_MORE;
                }
                case SMALL_DAMAGE_EVERY_TURN -> {
                    name = "Routine Poking";
                    energyCost = 2;
                    effect = EffectName.ATTACK_AT_END_OF_TURN;
                }
                case ITEM_SMALL_DAMAGE -> {
                    name = "";
                    energyCost = 0;
                    effect = EffectName.DAMAGE_ALL_VERY_SMALL;
                }
                case SMALL_DAMAGE_EVERY_TURN_UPGRADED -> {
                    name = "Routine Poking+";
                    energyCost = 1;
                    effect = EffectName.ATTACK_AT_END_OF_TURN;
                }
                default ->
                        System.out.println("Why was an ability almost generated with no matching type name? abilityTypeName:  " + abilityTypeName);
            }
        }

        public String getName() {
            return name;
        }

        public int getEnergyCost() {
            return energyCost;
        }

        public EffectName getPreEffect() {
            return preEffect;
        }

        public EffectName getEffect() {
            return effect;
        }

        public EffectName getPostEffect() {
            return postEffect;
        }
    }
}
