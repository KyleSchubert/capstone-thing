package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.characters.Character;

public class EnemyData {
    private static Array<IndividualEnemyData> data;

    public static void initialize() {
        data = new Array<>();
        for (Character.CharacterTypeName name : Character.CharacterTypeName.values()) {
            data.add(new IndividualEnemyData(name));
        }
    }

    public static int getMaxHp(Character.CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getMaxHp();
    }

    private static class IndividualEnemyData {
        private int maxHp;

        public IndividualEnemyData(Character.CharacterTypeName characterTypeName) {
            switch (characterTypeName) {
                case BIRD:
                    maxHp = 10;
                    break;
                case PLANT:
                    maxHp = 14;
                    break;
                case STUMP:
                    maxHp = 30;
                    break;
                case PIG:
                    maxHp = 22;
                    break;
                case ORANGE_MUSHROOM:
                    maxHp = 34;
                    break;
                case BLUE_MUSHROOM:
                    maxHp = 50;
                    break;
                case HELMET_PENGUIN:
                    maxHp = 80;
                    break;
                default:
                    System.out.println("Why was a character almost generated with no matching type name? characterTypeName:  " + characterTypeName);
            }
        }

        public int getMaxHp() {
            return maxHp;
        }
    }
}
