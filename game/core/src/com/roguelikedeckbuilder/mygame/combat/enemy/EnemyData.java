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

    public static AttackPattern getAttackPattern(CharacterTypeName typeName) {
        AttackPattern attackPattern = new AttackPattern();

        for (Move move : getMoves(typeName)) {
            attackPattern.addMove(move.copy());
        }

        attackPattern.setInitialAbility(getInitialAbility(typeName));

        return attackPattern;
    }

    private static class IndividualEnemyData {
        private final Array<Move> moves;
        private final AbilityTypeName initialAbility;
        private int maxHp;

        public IndividualEnemyData(CharacterTypeName characterTypeName) {
            moves = new Array<>();

            Move move1 = new Move(99999, AbilityTypeName.FIRE_STRIKE);
            move1.setRepetitionLimit(1);
            moves.add(move1);

            Move move2 = new Move(1, AbilityTypeName.HEAL_SELF_ENEMY);
            move2.setUseLimit(1);
            moves.add(move2);

            Move move3 = new Move(1, AbilityTypeName.DEFEND);
            moves.add(move3);

            initialAbility = AbilityTypeName.AMPLIFY;

            switch (characterTypeName) {
                case ALIEN:
                    maxHp = 20;
                    break;
                case ANTEATER:
                    maxHp = 10;
                    break;
                case BURGER:
                    maxHp = 20;
                    break;
                case CHIPS:
                    maxHp = 20;
                    break;
                case EVIL_HH:
                    maxHp = 20;
                    break;
                case HAM_AND_FIST:
                    maxHp = 20;
                    break;
                case HAM_SHAMWITCH:
                    maxHp = 20;
                    break;
                case HAMMIE:
                    maxHp = 20;
                    break;
                case HELMET_PENGUIN:
                    maxHp = 80;
                    break;
                case HOT_DOG:
                    maxHp = 20;
                    break;
                case KNIGHT:
                    maxHp = 20;
                    break;
                case KING_OF_THE_BURROW:
                    maxHp = 20;
                    break;
                case MONOLITH:
                    maxHp = 20;
                    break;
                case PEANUT_BEE:
                    maxHp = 20;
                    break;
                case POINTER:
                    maxHp = 20;
                    break;
                case PUFF:
                    maxHp = 2000;
                    break;
                case SAD_DOLLAR:
                    maxHp = 20;
                    break;
                case SOCK:
                    maxHp = 20;
                    break;
                case STARER:
                    maxHp = 20;
                    break;
                case SWORD_FISH:
                    maxHp = 10;
                    break;
                case UNIMPRESSED_FISH:
                    maxHp = 50;
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
    }
}
