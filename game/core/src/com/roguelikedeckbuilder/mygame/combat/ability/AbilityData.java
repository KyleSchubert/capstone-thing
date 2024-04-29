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

            name = "";
            energyCost = 0;

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
                case ITEM_SWORD_ABILITY -> effect = EffectName.DAMAGE_A_BIT;
                case ITEM_SWORD_2_ABILITY -> effect = EffectName.DAMAGE_A_BIT_TO_ONE;
                case HEAL_SELF_ENEMY -> {
                    name = "";
                    energyCost = 4;
                    effect = EffectName.HEAL_MODERATE_ENEMY;
                }
                case ITEM_DRAW_CARD -> effect = EffectName.DRAW_CARD_ONE;
                case ITEM_GAIN_STRENGTH -> effect = EffectName.STRENGTH_ONE;
                case ITEM_SHIELD_ABILITY -> {
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
                case PINS_AND_NEEDLES -> {
                    name = "Pins And Needles";
                    energyCost = 2;
                    effect = EffectName.ATTACK_ON_DRAW;
                }
                case PINS_AND_NEEDLES_UPGRADED -> {
                    name = "Pins And Needles+";
                    energyCost = 1;
                    effect = EffectName.ATTACK_ON_DRAW;
                }
                case PUMP_IRON -> {
                    name = "Pump Iron";
                    energyCost = 2;
                    effect = EffectName.STRENGTH_AT_START_OF_TURN;
                }
                case PUMP_IRON_UPGRADED -> {
                    name = "Pump Iron+";
                    energyCost = 1;
                    effect = EffectName.STRENGTH_AT_START_OF_TURN;
                }
                case SHIELDS_UP -> {
                    name = "Shields Up";
                    energyCost = 2;
                    effect = EffectName.DEFEND_AT_END_OF_TURN;
                }
                case SHIELDS_UP_UPGRADED -> {
                    name = "Shields Up+";
                    energyCost = 1;
                    effect = EffectName.DEFEND_AT_END_OF_TURN;
                }
                case SMALL_DAMAGE_EVERY_TURN -> {
                    name = "Routine Poking";
                    energyCost = 2;
                    effect = EffectName.ATTACK_AT_END_OF_TURN;
                }
                case ITEM_SMALL_DAMAGE -> effect = EffectName.DAMAGE_ALL_VERY_SMALL;
                case SMALL_DAMAGE_EVERY_TURN_UPGRADED -> {
                    name = "Routine Poking+";
                    energyCost = 1;
                    effect = EffectName.ATTACK_AT_END_OF_TURN;
                }
                case ITEM_SMALL_TRUE_DAMAGE_ONE -> effect = EffectName.TRUE_DAMAGE_ONE_SMALL;
                case ITEM_SMALL_DEFENSE -> effect = EffectName.DEFEND_SOME;
                case ITEM_SMALL_HEAL -> effect = EffectName.HEAL_SMALL;
                case SPEED_DIAL -> {
                    name = "Speed Dial";
                    energyCost = 2;
                    effect = EffectName.DRAW_CARD_AT_START_OF_TURN;
                }
                case SPEED_DIAL_UPGRADED -> {
                    name = "Speed Dial+";
                    energyCost = 1;
                    effect = EffectName.DRAW_CARD_AT_START_OF_TURN;
                }
                case ENEMY_ALIEN_HIT_AND_BURN -> {
                    effect = EffectName.ENEMY_ALIEN_HIT;
                    postEffect = EffectName.ENEMY_ALIEN_BURNING;
                }
                case ENEMY_ANTEATER_DEFEND -> effect = EffectName.ENEMY_ANTEATER_DEFEND;
                case ENEMY_ANTEATER_HALF_HEALTH -> {
                    effect = EffectName.ENEMY_ANTEATER_HALF_HEALTH_HIT;
                    postEffect = EffectName.ENEMY_ANTEATER_POISON_THREE;
                }
                case ENEMY_ANTEATER_HIT_ONCE_AND_POISON_ONCE -> {
                    effect = EffectName.ENEMY_ANTEATER_HIT_ONCE;
                    postEffect = EffectName.ENEMY_ANTEATER_POISON_ONE;
                }
                case ENEMY_ANTEATER_HIT_TWICE_AND_POISON_TWICE, ENEMY_ANTEATER_INITIAL -> {
                    effect = EffectName.ENEMY_ANTEATER_HIT_TWICE;
                    postEffect = EffectName.ENEMY_ANTEATER_INITIAL_POISON_TWO;
                }
                case ENEMY_ANTEATER_WEAKNESS_AND_DEFEND -> {
                    effect = EffectName.ENEMY_ANTEATER_WEAKNESS;
                    postEffect = EffectName.ENEMY_ANTEATER_DEFEND;
                }
                case ENEMY_BURGER_DEFEND -> effect = EffectName.ENEMY_BURGER_DEFEND;
                case ENEMY_BURGER_LARGER_HIT -> effect = EffectName.ENEMY_BURGER_LARGER_HIT;
                case ENEMY_BURGER_SMALLER_HIT -> effect = EffectName.ENEMY_BURGER_SMALLER_HIT;
                case ENEMY_CHIPS_HIT -> effect = EffectName.ENEMY_CHIPS_HIT;
                case ENEMY_CHIPS_INITIAL -> effect = EffectName.ENEMY_CHIPS_WEAKNESS;
                case ENEMY_CHIPS_STRENGTH_TEAM -> effect = EffectName.ENEMY_CHIPS_STRENGTH_TEAM;
                case ENEMY_EVIL_HH_HEAL_AND_DEFEND -> {
                    effect = EffectName.ENEMY_EVIL_HH_HEAL;
                    postEffect = EffectName.ENEMY_EVIL_HH_DEFEND;
                }
                case ENEMY_EVIL_HH_HIT_AND_DEFEND -> {
                    effect = EffectName.ENEMY_EVIL_HH_HIT;
                    postEffect = EffectName.ENEMY_EVIL_HH_DEFEND;
                }
                case ENEMY_EVIL_HH_INITIAL -> {
                    preEffect = EffectName.ENEMY_EVIL_HH_INITIAL_STRENGTH;
                    effect = EffectName.ENEMY_EVIL_HH_INITIAL_POISON;
                    postEffect = EffectName.ENEMY_EVIL_HH_INITIAL_CONSTITUTION;
                }
                case ENEMY_HAMMIE_DEFEND -> effect = EffectName.ENEMY_HAMMIE_DEFEND;
                case ENEMY_HAMMIE_DOUBLE_HIT -> effect = EffectName.ENEMY_HAMMIE_DOUBLE_HIT;
                case ENEMY_HAMMIE_SINGLE_HIT -> effect = EffectName.ENEMY_HAMMIE_SINGLE_HIT;
                case ENEMY_HAM_AND_FIST_DEFEND -> effect = EffectName.ENEMY_HAM_AND_FIST_DEFEND;
                case ENEMY_HAM_AND_FIST_DOUBLE_HIT -> effect = EffectName.ENEMY_HAM_AND_FIST_DOUBLE_HIT;
                case ENEMY_HAM_AND_FIST_WEAKNESS_SELF -> effect = EffectName.ENEMY_HAM_AND_FIST_WEAKNESS_SELF;
                case ENEMY_HAM_SHAMWITCH_DEFEND -> effect = EffectName.ENEMY_HAM_SHAMWITCH_DEFEND;
                case ENEMY_HAM_SHAMWITCH_HEAL_TEAM -> effect = EffectName.ENEMY_HAM_SHAMWITCH_HEAL_TEAM;
                case ENEMY_HAM_SHAMWITCH_HIT -> effect = EffectName.ENEMY_HAM_SHAMWITCH_HIT;
                case ENEMY_HELMET_PENGUIN_CONSTITUTION_TEAM ->
                        effect = EffectName.ENEMY_HELMET_PENGUIN_CONSTITUTION_TEAM;
                case ENEMY_HELMET_PENGUIN_DEFEND -> effect = EffectName.ENEMY_HELMET_PENGUIN_DEFEND;
                case ENEMY_HELMET_PENGUIN_HIT -> effect = EffectName.ENEMY_HELMET_PENGUIN_HIT;
                case ENEMY_HELMET_PENGUIN_INITIAL -> effect = EffectName.ENEMY_HELMET_PENGUIN_STRENGTH;
                case ENEMY_HELMET_PENGUIN_VULNERABILITY_AND_HIT -> {
                    effect = EffectName.ENEMY_HELMET_PENGUIN_SMALLER_HIT;
                    postEffect = EffectName.ENEMY_HELMET_PENGUIN_VULNERABILITY;
                }
                case ENEMY_HELMET_PENGUIN_WEAKNESS_AND_HIT -> {
                    effect = EffectName.ENEMY_HELMET_PENGUIN_SMALLER_HIT;
                    postEffect = EffectName.ENEMY_HELMET_PENGUIN_WEAKNESS;
                }
                case ENEMY_HOT_DOG_STRENGTH_TEAM -> effect = EffectName.ENEMY_HOT_DOG_STRENGTH_TEAM;
                case ENEMY_HOT_DOG_VULNERABILITY_TEAM -> effect = EffectName.ENEMY_HOT_DOG_VULNERABILITY_TEAM;
                case ENEMY_KING_OF_THE_BURROW_DEFEND_AND_HEAL -> {
                    effect = EffectName.ENEMY_KING_OF_THE_BURROW_DEFEND;
                    postEffect = EffectName.ENEMY_KING_OF_THE_BURROW_HEAL;
                }
                case ENEMY_KING_OF_THE_BURROW_HALF_HEALTH -> {
                    effect = EffectName.ENEMY_KING_OF_THE_BURROW_MORE_STRENGTH;
                    postEffect = EffectName.ENEMY_KING_OF_THE_BURROW_MORE_CONSTITUTION;
                }
                case ENEMY_KING_OF_THE_BURROW_HIT_EIGHT_TIMES ->
                        effect = EffectName.ENEMY_KING_OF_THE_BURROW_HIT_EIGHT_TIMES;
                case ENEMY_KING_OF_THE_BURROW_HIT_FOUR_TIMES ->
                        effect = EffectName.ENEMY_KING_OF_THE_BURROW_HIT_FOUR_TIMES;
                case ENEMY_KING_OF_THE_BURROW_INITIAL -> effect = EffectName.ENEMY_KING_OF_THE_BURROW_VULNERABILITY;
                case ENEMY_KING_OF_THE_BURROW_STRENGTH_AND_CONSTITUTION -> {
                    effect = EffectName.ENEMY_KING_OF_THE_BURROW_STRENGTH;
                    postEffect = EffectName.ENEMY_KING_OF_THE_BURROW_CONSTITUTION;
                }
                case ENEMY_KNIGHT_DEFEND -> effect = EffectName.ENEMY_KNIGHT_DEFEND;
                case ENEMY_KNIGHT_HIT_AND_DEFEND -> {
                    effect = EffectName.ENEMY_KNIGHT_HIT;
                    postEffect = EffectName.ENEMY_KNIGHT_DEFEND;
                }
                case ENEMY_KNIGHT_HIT_LARGE_AND_DEFEND -> {
                    effect = EffectName.ENEMY_KNIGHT_HIT_LARGE;
                    postEffect = EffectName.ENEMY_KNIGHT_DEFEND;
                }
                case ENEMY_MONOLITH_CONSTITUTION -> effect = EffectName.ENEMY_MONOLITH_CONSTITUTION;
                case ENEMY_MONOLITH_DEFEND -> effect = EffectName.ENEMY_MONOLITH_DEFEND;
                case ENEMY_MONOLITH_HIT -> effect = EffectName.ENEMY_MONOLITH_HIT;
                case ENEMY_MONOLITH_TRUE_HIT -> effect = EffectName.ENEMY_MONOLITH_TRUE_HIT;
                case ENEMY_PEANUT_BEE_HIT_ONCE_AND_POISON -> {
                    effect = EffectName.ENEMY_PEANUT_BEE_HIT_ONCE;
                    postEffect = EffectName.ENEMY_PEANUT_BEE_POISON_TWICE;
                }
                case ENEMY_PEANUT_BEE_HIT_TWICE_AND_POISON -> {
                    effect = EffectName.ENEMY_PEANUT_BEE_HIT_TWICE;
                    postEffect = EffectName.ENEMY_PEANUT_BEE_POISON_TWICE;
                }
                case ENEMY_PEANUT_BEE_INITIAL -> {
                    effect = EffectName.ENEMY_PEANUT_BEE_HIT_ONCE;
                    postEffect = EffectName.ENEMY_PEANUT_BEE_POISON_ONCE;
                }
                case ENEMY_PEANUT_BEE_WEAKNESS_AND_VULNERABILITY -> {
                    effect = EffectName.ENEMY_PEANUT_BEE_WEAKNESS;
                    postEffect = EffectName.ENEMY_PEANUT_BEE_VULNERABILITY;
                }
                case ENEMY_POINTER_HIT -> effect = EffectName.ENEMY_POINTER_HIT;
                case ENEMY_POINTER_INITIAL -> effect = EffectName.ENEMY_POINTER_INITIAL;
                case ENEMY_POINTER_TRUE_HIT -> effect = EffectName.ENEMY_POINTER_TRUE_HIT;
                case ENEMY_PUFF_BIG_HIT_AND_HEAL -> {
                    effect = EffectName.ENEMY_PUFF_BIG_HIT;
                    postEffect = EffectName.ENEMY_PUFF_BIG_HEAL;
                }
                case ENEMY_PUFF_DEFEND_AND_HEAL -> {
                    effect = EffectName.ENEMY_PUFF_DEFEND;
                    postEffect = EffectName.ENEMY_PUFF_HEAL_BIGGER_THAN_GIGANTIC;
                }
                case ENEMY_PUFF_GIGANTIC_HIT_AND_HEAL -> {
                    effect = EffectName.ENEMY_PUFF_GIGANTIC_HIT;
                    postEffect = EffectName.ENEMY_PUFF_GIGANTIC_HEAL;
                }
                case ENEMY_PUFF_HALF_HEALTH -> {
                    preEffect = EffectName.ENEMY_PUFF_HALF_HEALTH_CONSTITUTION;
                    effect = EffectName.ENEMY_PUFF_HALF_HEALTH_HIT;
                    postEffect = EffectName.ENEMY_PUFF_HALF_HEALTH_STRENGTH;
                }
                case ENEMY_PUFF_INITIAL -> effect = EffectName.ENEMY_PUFF_INITIAL;
                case ENEMY_PUFF_POISON_AND_BURNING -> {
                    effect = EffectName.ENEMY_PUFF_POISON;
                    postEffect = EffectName.ENEMY_PUFF_BURNING;
                }
                case ENEMY_PUFF_TRUE_DAMAGE -> effect = EffectName.ENEMY_PUFF_TRUE_DAMAGE;
                case ENEMY_PUFF_VULNERABILITY_AND_WEAKNESS -> {
                    effect = EffectName.ENEMY_PUFF_VULNERABILITY;
                    postEffect = EffectName.ENEMY_PUFF_WEAKNESS;
                }
                case ENEMY_SAD_DOLLAR_CONSTITUTION_SELF -> effect = EffectName.ENEMY_SAD_DOLLAR_CONSTITUTION_SELF;
                case ENEMY_SAD_DOLLAR_CONSTITUTION_TEAM -> effect = EffectName.ENEMY_SAD_DOLLAR_CONSTITUTION_TEAM;
                case ENEMY_SAD_DOLLAR_DEFEND -> effect = EffectName.ENEMY_SAD_DOLLAR_DEFEND;
                case ENEMY_SAD_DOLLAR_HIT -> effect = EffectName.ENEMY_SAD_DOLLAR_HIT;
                case ENEMY_SAD_DOLLAR_POISON -> effect = EffectName.ENEMY_SAD_DOLLAR_POISON;
                case ENEMY_SOCK_DEFEND_TEAM -> effect = EffectName.ENEMY_SOCK_DEFEND_TEAM;
                case ENEMY_SOCK_POISON -> effect = EffectName.ENEMY_SOCK_POISON;
                case ENEMY_STARER_DEFEND_SELF -> effect = EffectName.ENEMY_STARER_DEFEND_SELF;
                case ENEMY_STARER_DEFEND_TEAM -> effect = EffectName.ENEMY_STARER_DEFEND_TEAM;
                case ENEMY_SWORD_FISH_HIT_LARGE -> effect = EffectName.ENEMY_SWORD_FISH_HIT_LARGE;
                case ENEMY_SWORD_FISH_HIT_SMALL -> effect = EffectName.ENEMY_SWORD_FISH_HIT_SMALL;
                case ENEMY_SWORD_FISH_VULNERABILITY -> effect = EffectName.ENEMY_SWORD_FISH_VULNERABILITY;
                case ENEMY_UNIMPRESSED_FISH_HIT -> effect = EffectName.ENEMY_UNIMPRESSED_FISH_HIT;
                case ENEMY_UNIMPRESSED_FISH_STRENGTH_TEAM -> effect = EffectName.ENEMY_UNIMPRESSED_FISH_STRENGTH_TEAM;
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
