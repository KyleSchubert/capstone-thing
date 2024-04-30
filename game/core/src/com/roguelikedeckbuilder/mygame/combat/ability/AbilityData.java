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
                case STRIKE -> {
                    name = "Strike";
                    energyCost = 1;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                }
                case STRIKE_UPGRADED -> {
                    name = "Strike+";
                    energyCost = 1;
                    effect = EffectName.HIGH_DAMAGE_ONCE;
                    postEffect = EffectName.DEFEND_SOME;
                }
                case AFFLICTION -> {
                    name = "Affliction";
                    energyCost = 1;
                    effect = EffectName.AFFLICTION_BURNING;
                    postEffect = EffectName.AFFLICTION_POISON;
                }
                case AFFLICTION_UPGRADED -> {
                    name = "Affliction+";
                    energyCost = 1;
                    effect = EffectName.AFFLICTION_BURNING_UP;
                    postEffect = EffectName.AFFLICTION_POISON_UP;
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
                case BLOCK_AND_STUFF -> {
                    name = "Block and Stuff";
                    energyCost = 2;
                    preEffect = EffectName.BLOCK_AND_STUFF_BURN;
                    effect = EffectName.BLOCK_AND_STUFF_DEFEND;
                    postEffect = EffectName.BLOCK_AND_STUFF_POISON;
                }
                case BLOCK_AND_STUFF_UPGRADED -> {
                    name = "Block and Stuff+";
                    energyCost = 2;
                    preEffect = EffectName.BLOCK_AND_STUFF_BURN_UP;
                    effect = EffectName.BLOCK_AND_STUFF_DEFEND;
                    postEffect = EffectName.BLOCK_AND_STUFF_POISON_UP;
                }
                case BOULDER_THROW -> {
                    name = "Boulder Throw";
                    energyCost = 2;
                    effect = EffectName.BOULDER_THROW;
                }
                case BOULDER_THROW_UPGRADED -> {
                    name = "Boulder Throw+";
                    energyCost = 2;
                    preEffect = EffectName.STRENGTH_ONE;
                    effect = EffectName.BOULDER_THROW;
                }
                case BURN_STEEL_WOOL -> {
                    name = "Burn Steel Wool";
                    energyCost = 2;
                    effect = EffectName.BURN_STEEL_WOOL;
                    postEffect = EffectName.DRAW_CARD_ONE;
                }
                case BURN_STEEL_WOOL_UPGRADED -> {
                    name = "Burn Steel Wool+";
                    energyCost = 2;
                    effect = EffectName.BURN_STEEL_WOOL;
                    postEffect = EffectName.BURN_STEEL_WOOL_DRAW;
                }
                case COIN_WALL -> {
                    name = "Coin Wall";
                    energyCost = 1;
                    effect = EffectName.COIN_WALL_GOLD;
                    postEffect = EffectName.COIN_WALL_DEFEND;
                }
                case COIN_WALL_UPGRADED -> {
                    name = "Coin Wall+";
                    energyCost = 1;
                    effect = EffectName.COIN_WALL_GOLD;
                    postEffect = EffectName.COIN_WALL_DEFEND_UP;
                }
                case CRACK_KNUCKLES -> {
                    name = "Crack Knuckles";
                    energyCost = 1;
                    effect = EffectName.CRACK_KNUCKLES;
                }
                case CRACK_KNUCKLES_UPGRADED -> {
                    name = "Crack Knuckles+";
                    energyCost = 1;
                    effect = EffectName.CRACK_KNUCKLES_UP;
                }
                case CULL -> {
                    name = "Cull";
                    energyCost = 3;
                    effect = EffectName.CULL_ATTACK;
                    postEffect = EffectName.CULL_HEAL;
                }
                case CULL_UPGRADED -> {
                    name = "Cull+";
                    energyCost = 2;
                    effect = EffectName.CULL_ATTACK;
                    postEffect = EffectName.CULL_HEAL;
                }
                case CURSE -> {
                    name = "Curse";
                    energyCost = 1;
                    effect = EffectName.CURSE_WEAKNESS;
                }
                case CURSE_UPGRADED -> {
                    name = "Curse+";
                    energyCost = 1;
                    effect = EffectName.CURSE_WEAKNESS_UP;
                }
                case DEBILITATE -> {
                    name = "Debilitate";
                    energyCost = 2;
                    effect = EffectName.DEBILITATE_WEAKNESS;
                    postEffect = EffectName.DEBILITATE_VULNERABILITY;
                }
                case DEBILITATE_UPGRADED -> {
                    name = "Debilitate+";
                    energyCost = 1;
                    effect = EffectName.DEBILITATE_WEAKNESS;
                    postEffect = EffectName.DEBILITATE_VULNERABILITY;
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
                case PRE_CURE -> {
                    name = "Pre-Cure";
                    energyCost = 1;
                    effect = EffectName.PRE_CURE_ONE;
                }
                case PRE_CURE_UPGRADED -> {
                    name = "Pre-Cure+";
                    energyCost = 0;
                    effect = EffectName.PRE_CURE_ONE;
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
                case ITEM_BURN_ONE -> effect = EffectName.ENEMY_ALIEN_BURNING;
                case ITEM_HEAL_5 -> effect = EffectName.ITEM_HEAL_5;
                case ITEM_DEFEND_30 -> effect = EffectName.ITEM_DEFEND_30;
                case ITEM_TRUE_DAMAGE_35 -> effect = EffectName.ITEM_TRUE_DAMAGE_35;
                case ITEM_GAIN_CONSTITUTION -> effect = EffectName.ITEM_GAIN_CONSTITUTION;
                case ITEM_GAIN_ENERGY_ONE -> effect = EffectName.GAIN_ENERGY_ONE;
                case ITEM_DEFEND_14 -> effect = EffectName.ITEM_DEFEND_14;
                case ITEM_REDUCE_MAX_HP_5_PERCENT -> effect = EffectName.ITEM_REDUCE_MAX_HP_5_PERCENT;
                case ITEM_REDUCE_MAX_HP_10_PERCENT -> effect = EffectName.ITEM_REDUCE_MAX_HP_10_PERCENT;
                case ITEM_REDUCE_MAX_HP_15_PERCENT -> effect = EffectName.ITEM_REDUCE_MAX_HP_15_PERCENT;
                case ITEM_3_BURNING -> effect = EffectName.ITEM_3_BURNING;
                case ITEM_1_POISON -> effect = EffectName.ITEM_1_POISON;
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
                case DISTRACTION -> {
                    name = "Distraction";
                    energyCost = 1;
                    effect = EffectName.DISTRACTION;
                    postEffect = EffectName.DISCARD_RANDOM_CARD_ONE;
                }
                case DISTRACTION_UPGRADED -> {
                    name = "Distraction+";
                    energyCost = 0;
                    effect = EffectName.DISTRACTION;
                    postEffect = EffectName.DISCARD_RANDOM_CARD_ONE;
                }
                case DUST -> {
                    name = "Dust";
                    energyCost = 2;
                    effect = EffectName.DUST;
                }
                case DUST_UPGRADED -> {
                    name = "Dust+";
                    energyCost = 2;
                    effect = EffectName.DUST_UP;
                }
                case FIRE_BREATH -> {
                    name = "Fire Breath";
                    energyCost = 3;
                    effect = EffectName.FIRE_BREATH;
                }
                case FIRE_BREATH_UPGRADED -> {
                    name = "Fire Breath+";
                    energyCost = 2;
                    effect = EffectName.FIRE_BREATH;
                }
                case FIRE_WHIRL -> {
                    name = "Fire Whirl";
                    energyCost = 3;
                    effect = EffectName.FIRE_WHIRL;
                }
                case FIRE_WHIRL_UPGRADED -> {
                    name = "Fire Whirl+";
                    energyCost = 2;
                    effect = EffectName.FIRE_WHIRL;
                }
                case FOIL_ARMOR -> {
                    name = "Foil Armor";
                    energyCost = 1;
                    effect = EffectName.FOIL_ARMOR;
                }
                case FOIL_ARMOR_UPGRADED -> {
                    name = "Foil Armor+";
                    energyCost = 1;
                    effect = EffectName.FOIL_ARMOR_UP;
                }
                case FRYING_PAN -> {
                    name = "Frying Pan";
                    energyCost = 1;
                    effect = EffectName.FRYING_PAN_DEFEND;
                    postEffect = EffectName.FRYING_PAN_BURNING;
                }
                case FRYING_PAN_UPGRADED -> {
                    name = "Frying Pan+";
                    energyCost = 1;
                    effect = EffectName.FRYING_PAN_DEFEND;
                    postEffect = EffectName.FRYING_PAN_BURNING_UP;
                }
                case HEX -> {
                    name = "Hex";
                    energyCost = 1;
                    effect = EffectName.HEX_VULNERABILITY;
                }
                case HEX_UPGRADED -> {
                    name = "Hex+";
                    energyCost = 1;
                    effect = EffectName.HEX_VULNERABILITY_UP;
                }
                case HOT_HOLY_WATER -> {
                    name = "Hot Holy Water";
                    energyCost = 1;
                    effect = EffectName.HOT_HOLY_WATER_BURNING;
                    postEffect = EffectName.HOT_HOLY_WATER_PRE_CURE;
                }
                case HOT_HOLY_WATER_UPGRADED -> {
                    name = "Hot Holy Water+";
                    energyCost = 1;
                    effect = EffectName.HOT_HOLY_WATER_BURNING;
                    postEffect = EffectName.HOT_HOLY_WATER_PRE_CURE_LESS;
                }
                case ITEM_TWO_VULNERABILITY_ALL -> effect = EffectName.ITEM_TWO_VULNERABILITY_ALL;
                case LIFE_LEECHING -> {
                    name = "Life Leeching";
                    energyCost = 3;
                    effect = EffectName.LIFE_LEECHING_DAMAGE;
                    postEffect = EffectName.LIFE_LEECHING_HEAL;
                }
                case LIFE_LEECHING_UPGRADED -> {
                    name = "Life Leeching+";
                    energyCost = 2;
                    effect = EffectName.LIFE_LEECHING_DAMAGE;
                    postEffect = EffectName.LIFE_LEECHING_HEAL;
                }
                case MAGIC_BARRIER -> {
                    name = "Magic Barrier";
                    energyCost = 2;
                    effect = EffectName.MAGIC_BARRIER_DEFEND;
                    postEffect = EffectName.MAGIC_BARRIER_PRE_CURE;
                }
                case MAGIC_BARRIER_UPGRADED -> {
                    name = "Magic Barrier+";
                    energyCost = 1;
                    effect = EffectName.MAGIC_BARRIER_DEFEND;
                    postEffect = EffectName.MAGIC_BARRIER_PRE_CURE;
                }
                case MULTI_STAB -> {
                    name = "Multi-Stab";
                    energyCost = 1;
                    effect = EffectName.MULTI_STAB;
                }
                case MULTI_STAB_UPGRADED -> {
                    name = "Multi-Stab+";
                    energyCost = 1;
                    effect = EffectName.MULTI_STAB_UP;
                }
                case NAILS_ON_CHALKBOARD -> {
                    name = "Nails on a Chalkboard";
                    energyCost = 1;
                    effect = EffectName.NAILS_ON_CHALKBOARD_TRUE_DAMAGE;
                }
                case NAILS_ON_CHALKBOARD_UPGRADED -> {
                    name = "Nails on a Chalkboard+";
                    energyCost = 1;
                    effect = EffectName.NAILS_ON_CHALKBOARD_TRUE_DAMAGE_UP;
                }
                case NOTHING_BUT_GOLD, NOTHING_BUT_GOOD -> {
                    name = "Nothing?";
                    energyCost = 1;
                    effect = EffectName.NOTHING;
                }
                case NOTHING_BUT_GOLD_UPGRADED -> {
                    name = "Nothing, but gold";
                    energyCost = 1;
                    effect = EffectName.NOTHING_BUT_GOLD_ONE_GOLD;
                }
                case NOTHING_BUT_GOOD_UPGRADED -> {
                    name = "Nothing, but good";
                    energyCost = 1;
                    effect = EffectName.NOTHING_BUT_GOOD_ATTACK;
                    postEffect = EffectName.NOTHING_BUT_GOOD_DEFEND;
                }
                case RAMPING -> {
                    name = "Ramping";
                    energyCost = 2;
                    effect = EffectName.STRENGTH_ONE;
                    postEffect = EffectName.RAMPING_WEAKNESS;
                }
                case RAMPING_UPGRADED -> {
                    name = "Ramping+";
                    energyCost = 2;
                    effect = EffectName.STRENGTH_ONE;
                }
                case REGENERATE -> {
                    name = "Regenerate";
                    energyCost = 1;
                    effect = EffectName.HEAL_SMALL;
                    postEffect = EffectName.REGENERATE;
                }
                case REGENERATE_UPGRADED -> {
                    name = "Regenerate+";
                    energyCost = 1;
                    effect = EffectName.REGENERATE_UP;
                    postEffect = EffectName.REGENERATE;
                }
                case SCATTERSHOT -> {
                    name = "Scattershot";
                    energyCost = 1;
                    effect = EffectName.SCATTERSHOT_ATTACK;
                    postEffect = EffectName.SCATTERSHOT_DEFEND;
                }
                case SCATTERSHOT_UPGRADED -> {
                    name = "Scattershot+";
                    energyCost = 1;
                    effect = EffectName.SCATTERSHOT_ATTACK_UP;
                    postEffect = EffectName.SCATTERSHOT_DEFEND_UP;
                }
                case SPIKED_SHIELD -> {
                    name = "Spiked Shield";
                    energyCost = 1;
                    effect = EffectName.SPIKED_SHIELD_DEFEND;
                    postEffect = EffectName.SPIKED_SHIELD_ATTACK;
                }
                case SPIKED_SHIELD_UPGRADED -> {
                    name = "Spiked Shield+";
                    energyCost = 1;
                    effect = EffectName.SPIKED_SHIELD_DEFEND_UP;
                    postEffect = EffectName.SPIKED_SHIELD_ATTACK_UP;
                }
                case SPREAD_GERMS -> {
                    name = "Spread Germs";
                    energyCost = 1;
                    effect = EffectName.SPREAD_GERMS;
                }
                case SPREAD_GERMS_UPGRADED -> {
                    name = "Spread Germs+";
                    energyCost = 1;
                    effect = EffectName.SPREAD_GERMS_UP;
                }
                case SWIFT_BLOCK -> {
                    name = "Swift Block";
                    energyCost = 1;
                    effect = EffectName.SWIFT_BLOCK;
                    postEffect = EffectName.DRAW_CARD_ONE;
                }
                case SWIFT_BLOCK_UPGRADED -> {
                    name = "Swift Block+";
                    energyCost = 0;
                    effect = EffectName.SWIFT_BLOCK;
                    postEffect = EffectName.DRAW_CARD_ONE;
                }
                case SYMPATHETIC_PAIN -> {
                    name = "Sympathetic Pain";
                    energyCost = 2;
                    effect = EffectName.SYMPATHETIC_PAIN;
                    postEffect = EffectName.LIFE_LEECHING_HEAL;
                }
                case SYMPATHETIC_PAIN_UPGRADED -> {
                    name = "Sympathetic Pain+";
                    energyCost = 1;
                    effect = EffectName.SYMPATHETIC_PAIN;
                    postEffect = EffectName.LIFE_LEECHING_HEAL;
                }
                case TETANUS -> {
                    name = "Tetanus";
                    energyCost = 1;
                    effect = EffectName.TETANUS_ATTACK;
                    postEffect = EffectName.TETANUS_POISON;
                }
                case TETANUS_UPGRADED -> {
                    name = "Tetanus+";
                    energyCost = 0;
                    effect = EffectName.TETANUS_ATTACK;
                    postEffect = EffectName.TETANUS_POISON;
                }
                case THORNS -> {
                    name = "Thorns";
                    energyCost = 3;
                    effect = EffectName.THORNS;
                }
                case THORNS_UPGRADED -> {
                    name = "Thorns+";
                    energyCost = 2;
                    effect = EffectName.THORNS;
                }
                case TWO_SHIELDS -> {
                    name = "Two Shields";
                    energyCost = 2;
                    effect = EffectName.TWO_SHIELDS;
                }
                case TWO_SHIELDS_UPGRADED -> {
                    name = "Two Shields+";
                    energyCost = 1;
                    effect = EffectName.TWO_SHIELDS;
                }
                case VERY_ILL -> {
                    name = "Very Ill";
                    energyCost = 1;
                    effect = EffectName.VERY_ILL;
                    postEffect = EffectName.RAMPING_WEAKNESS;
                }
                case VERY_ILL_UPGRADED -> {
                    name = "Very Ill+";
                    energyCost = 1;
                    effect = EffectName.VERY_ILL_UP;
                    postEffect = EffectName.RAMPING_WEAKNESS;
                }
                case WEAR_POISON -> {
                    name = "Wear Poison";
                    energyCost = 3;
                    effect = EffectName.WEAR_POISON;
                }
                case WEAR_POISON_UPGRADED -> {
                    name = "Wear Poison+";
                    energyCost = 2;
                    effect = EffectName.WEAR_POISON;
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
