package com.roguelikedeckbuilder.mygame.combat.enemy;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;

public class EnemyData {
    private static Array<IndividualEnemyData> data;

    public static void initialize() {
        data = new Array<>();
        for (CharacterTypeName name : CharacterTypeName.values()) {
            data.add(new IndividualEnemyData(name));
        }
    }

    public static int getMaxHp(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getMaxHp();
    }

    private static Array<Move> getMoves(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getMoves();
    }

    private static AbilityTypeName getInitialAbility(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getInitialAbility();
    }

    private static AbilityTypeName getHalfHealthAbility(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getHalfHealthAbility();
    }

    public static AttackPattern getAttackPattern(CharacterTypeName typeName) {
        AttackPattern attackPattern = new AttackPattern();

        for (Move move : getMoves(typeName)) {
            attackPattern.addMove(move.copy());
        }

        attackPattern.setInitialAbility(getInitialAbility(typeName));
        attackPattern.setHalfHealthAbility(getHalfHealthAbility(typeName));

        return attackPattern;
    }

    private static class IndividualEnemyData {
        private final Array<Move> moves;
        private AbilityTypeName initialAbility;
        private AbilityTypeName halfHealthAbility;
        private int maxHp;

        public IndividualEnemyData(CharacterTypeName characterTypeName) {
            moves = new Array<>();
            Move move1, move2, move3, move4, move5, move6;

            switch (characterTypeName) {
                case ALIEN:
                    maxHp = 34;

                    move1 = new Move(1, AbilityTypeName.ENEMY_ALIEN_HIT_AND_BURN);

                    moves.add(move1);
                    break;
                case ANTEATER:
                    maxHp = 160;

                    move1 = new Move(999999, AbilityTypeName.ENEMY_ANTEATER_DEFEND);
                    move1.setUseLimit(2);

                    move2 = new Move(10, AbilityTypeName.ENEMY_ANTEATER_HIT_TWICE_AND_POISON_TWICE);
                    move2.setRepetitionLimit(1);

                    move3 = new Move(10, AbilityTypeName.ENEMY_ANTEATER_HIT_ONCE_AND_POISON_ONCE);

                    move4 = new Move(4, AbilityTypeName.ENEMY_ANTEATER_WEAKNESS_AND_DEFEND);

                    moves.add(move1, move2, move3, move4);

                    initialAbility = AbilityTypeName.ENEMY_ANTEATER_INITIAL;
                    halfHealthAbility = AbilityTypeName.ENEMY_ANTEATER_HALF_HEALTH;
                    break;
                case BURGER:
                    maxHp = 14;

                    move1 = new Move(1, AbilityTypeName.ENEMY_BURGER_SMALLER_HIT);

                    move2 = new Move(1, AbilityTypeName.ENEMY_BURGER_LARGER_HIT);

                    moves.add(move1, move2);

                    initialAbility = AbilityTypeName.ENEMY_BURGER_DEFEND;
                    break;
                case CHIPS:
                    maxHp = 72;

                    move1 = new Move(1, AbilityTypeName.ENEMY_CHIPS_STRENGTH_TEAM);

                    move2 = new Move(1, AbilityTypeName.ENEMY_CHIPS_HIT);
                    move2.setRepetitionLimit(1);

                    moves.add(move1, move2);

                    initialAbility = AbilityTypeName.ENEMY_CHIPS_INITIAL;
                    break;
                case EVIL_HH:
                    maxHp = 70;

                    move1 = new Move(4, AbilityTypeName.ENEMY_EVIL_HH_HEAL_AND_DEFEND);

                    move2 = new Move(6, AbilityTypeName.ENEMY_EVIL_HH_HIT_AND_DEFEND);
                    move2.setRepetitionLimit(2);

                    moves.add(move1, move2);

                    initialAbility = AbilityTypeName.ENEMY_EVIL_HH_INITIAL;
                    break;
                case HAM_AND_FIST:
                    maxHp = 20;

                    move1 = new Move(1, AbilityTypeName.ENEMY_HAM_AND_FIST_DOUBLE_HIT);

                    move2 = new Move(1, AbilityTypeName.ENEMY_HAM_AND_FIST_DEFEND);

                    moves.add(move1, move2);

                    initialAbility = AbilityTypeName.ENEMY_HAM_AND_FIST_WEAKNESS_SELF;
                    break;
                case HAM_SHAMWITCH:
                    maxHp = 10;

                    move1 = new Move(1, AbilityTypeName.ENEMY_HAM_SHAMWITCH_HEAL_TEAM);

                    move2 = new Move(1, AbilityTypeName.ENEMY_HAM_SHAMWITCH_DEFEND);

                    move3 = new Move(1, AbilityTypeName.ENEMY_HAM_SHAMWITCH_HIT);

                    moves.add(move1, move2, move3);

                    break;
                case HAMMIE:
                    maxHp = 8;

                    move1 = new Move(1, AbilityTypeName.ENEMY_HAMMIE_DEFEND);

                    move2 = new Move(1, AbilityTypeName.ENEMY_HAMMIE_SINGLE_HIT);

                    move3 = new Move(1, AbilityTypeName.ENEMY_HAMMIE_DOUBLE_HIT);

                    moves.add(move1, move2, move3);

                    break;
                case HELMET_PENGUIN:
                    maxHp = 175;

                    move1 = new Move(1, AbilityTypeName.ENEMY_HELMET_PENGUIN_CONSTITUTION_TEAM);
                    move1.setUseLimit(2);

                    move2 = new Move(1, AbilityTypeName.ENEMY_HELMET_PENGUIN_VULNERABILITY_AND_HIT);

                    move3 = new Move(1, AbilityTypeName.ENEMY_HELMET_PENGUIN_WEAKNESS_AND_HIT);

                    move4 = new Move(1, AbilityTypeName.ENEMY_HELMET_PENGUIN_DEFEND);

                    move5 = new Move(1, AbilityTypeName.ENEMY_HELMET_PENGUIN_HIT);

                    moves.addAll(move1, move2, move3, move4, move5);

                    initialAbility = AbilityTypeName.ENEMY_HELMET_PENGUIN_INITIAL;
                    break;
                case HOT_DOG:
                    maxHp = 13;

                    move1 = new Move(1, AbilityTypeName.ENEMY_HOT_DOG_STRENGTH_TEAM);

                    moves.add(move1);

                    initialAbility = AbilityTypeName.ENEMY_HOT_DOG_VULNERABILITY_TEAM;
                    break;
                case KING_OF_THE_BURROW:
                    maxHp = 560;

                    move1 = new Move(10, AbilityTypeName.ENEMY_KING_OF_THE_BURROW_HIT_FOUR_TIMES);

                    move2 = new Move(10, AbilityTypeName.ENEMY_KING_OF_THE_BURROW_HIT_EIGHT_TIMES);

                    move3 = new Move(6, AbilityTypeName.ENEMY_KING_OF_THE_BURROW_STRENGTH_AND_CONSTITUTION);

                    move4 = new Move(10, AbilityTypeName.ENEMY_KING_OF_THE_BURROW_DEFEND_AND_HEAL);

                    moves.add(move1, move2, move3, move4);

                    initialAbility = AbilityTypeName.ENEMY_KING_OF_THE_BURROW_INITIAL;
                    halfHealthAbility = AbilityTypeName.ENEMY_KING_OF_THE_BURROW_HALF_HEALTH;
                    break;
                case KNIGHT:
                    maxHp = 142;

                    move1 = new Move(1, AbilityTypeName.ENEMY_KNIGHT_DEFEND);

                    move2 = new Move(1, AbilityTypeName.ENEMY_KNIGHT_HIT_LARGE_AND_DEFEND);

                    move3 = new Move(1, AbilityTypeName.ENEMY_KNIGHT_HIT_AND_DEFEND);

                    moves.add(move1, move2, move3);

                    break;
                case MONOLITH:
                    maxHp = 260;

                    move1 = new Move(10, AbilityTypeName.ENEMY_MONOLITH_DEFEND);

                    move2 = new Move(10, AbilityTypeName.ENEMY_MONOLITH_CONSTITUTION);
                    move2.setUseLimit(2);

                    move3 = new Move(6, AbilityTypeName.ENEMY_MONOLITH_HIT);

                    move4 = new Move(1, AbilityTypeName.ENEMY_MONOLITH_TRUE_HIT);

                    moves.add(move1, move2, move3, move4);

                    break;
                case PEANUT_BEE:
                    maxHp = 120;

                    move1 = new Move(1, AbilityTypeName.ENEMY_PEANUT_BEE_HIT_TWICE_AND_POISON);

                    move2 = new Move(1, AbilityTypeName.ENEMY_PEANUT_BEE_HIT_ONCE_AND_POISON);

                    move3 = new Move(1, AbilityTypeName.ENEMY_PEANUT_BEE_WEAKNESS_AND_VULNERABILITY);

                    moves.add(move1, move2, move3);

                    initialAbility = AbilityTypeName.ENEMY_PEANUT_BEE_INITIAL;
                    break;
                case POINTER:
                    maxHp = 32;

                    move1 = new Move(1, AbilityTypeName.ENEMY_POINTER_HIT);

                    move2 = new Move(1, AbilityTypeName.ENEMY_POINTER_TRUE_HIT);

                    moves.add(move1, move2);

                    initialAbility = AbilityTypeName.ENEMY_POINTER_INITIAL;
                    break;
                case PUFF:
                    maxHp = 1850;

                    move1 = new Move(1, AbilityTypeName.ENEMY_PUFF_VULNERABILITY_AND_WEAKNESS);

                    move2 = new Move(1, AbilityTypeName.ENEMY_PUFF_POISON_AND_BURNING);
                    move2.setUseLimit(1);

                    move3 = new Move(3, AbilityTypeName.ENEMY_PUFF_BIG_HIT_AND_HEAL);

                    move4 = new Move(3, AbilityTypeName.ENEMY_PUFF_GIGANTIC_HIT_AND_HEAL);

                    move5 = new Move(3, AbilityTypeName.ENEMY_PUFF_DEFEND_AND_HEAL);

                    move6 = new Move(3, AbilityTypeName.ENEMY_PUFF_TRUE_DAMAGE);

                    moves.addAll(move1, move2, move3, move4, move5, move6);

                    initialAbility = AbilityTypeName.ENEMY_PUFF_INITIAL;
                    halfHealthAbility = AbilityTypeName.ENEMY_PUFF_HALF_HEALTH;
                    break;
                case SAD_DOLLAR:
                    maxHp = 49;

                    move1 = new Move(1, AbilityTypeName.ENEMY_SAD_DOLLAR_CONSTITUTION_SELF);
                    move1.setUseLimit(2);
                    move1.setRepetitionLimit(1);

                    move2 = new Move(1, AbilityTypeName.ENEMY_SAD_DOLLAR_DEFEND);

                    move3 = new Move(1, AbilityTypeName.ENEMY_SAD_DOLLAR_HIT);

                    move4 = new Move(3, AbilityTypeName.ENEMY_SAD_DOLLAR_POISON);
                    move4.setRepetitionLimit(1);

                    moves.add(move1, move2, move3, move4);

                    initialAbility = AbilityTypeName.ENEMY_SAD_DOLLAR_CONSTITUTION_TEAM;
                    break;
                case SOCK:
                    maxHp = 40;

                    move1 = new Move(3, AbilityTypeName.ENEMY_SOCK_POISON);
                    move1.setRepetitionLimit(1);

                    move2 = new Move(1, AbilityTypeName.ENEMY_SOCK_DEFEND_TEAM);

                    moves.add(move1, move2);

                    break;
                case STARER:
                    maxHp = 16;

                    move1 = new Move(3, AbilityTypeName.ENEMY_STARER_DEFEND_TEAM);

                    move2 = new Move(1, AbilityTypeName.ENEMY_STARER_DEFEND_SELF);

                    moves.add(move1, move2);

                    break;
                case SWORD_FISH:
                    maxHp = 41;

                    move1 = new Move(1, AbilityTypeName.ENEMY_SWORD_FISH_HIT_SMALL);

                    move2 = new Move(1, AbilityTypeName.ENEMY_SWORD_FISH_HIT_LARGE);

                    move3 = new Move(1, AbilityTypeName.ENEMY_SWORD_FISH_VULNERABILITY);
                    move3.setRepetitionLimit(1);

                    moves.add(move1, move2, move3);

                    break;
                case UNIMPRESSED_FISH:
                    maxHp = 55;

                    move1 = new Move(1, AbilityTypeName.ENEMY_UNIMPRESSED_FISH_STRENGTH_TEAM);
                    move1.setUseLimit(2);

                    move2 = new Move(999999, AbilityTypeName.ENEMY_UNIMPRESSED_FISH_HIT);

                    moves.add(move1, move2);

                    break;
                default:
                    System.out.println("Why was an enemy's stats almost generated from no matching type name? characterTypeName:  " + characterTypeName);
            }
        }

        public int getMaxHp() {
            return maxHp;
        }

        public Array<Move> getMoves() {
            return moves;
        }

        public AbilityTypeName getInitialAbility() {
            return initialAbility;
        }

        public AbilityTypeName getHalfHealthAbility() {
            return halfHealthAbility;
        }
    }
}
