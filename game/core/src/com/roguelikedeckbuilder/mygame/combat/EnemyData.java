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

    public static Array<AbilityData.AbilityTypeName> getAbilityOptions(Character.CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getAbilityOptions();
    }


    private static class IndividualEnemyData {
        private int maxHp;
        private Array<AbilityData.AbilityTypeName> abilityOptions;

        public IndividualEnemyData(Character.CharacterTypeName characterTypeName) {
            switch (characterTypeName) {
                case BIRD:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case PLANT:
                    maxHp = 14;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.DEFEND, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case STUMP:
                    maxHp = 30;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.DEFEND_UPGRADED, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case PIG:
                    maxHp = 22;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.DEFEND, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case ORANGE_MUSHROOM:
                    maxHp = 34;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.FIRE_STRIKE_UPGRADED, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case BLUE_MUSHROOM:
                    maxHp = 50;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case HELMET_PENGUIN:
                    maxHp = 80;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.DEFEND_UPGRADED, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case BELLFLOWER:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case BIG_BOAR:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case BIG_SLIME:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case BIG_SNAIL:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case BUFF_PIG:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case DRAGON:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case DYLE:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case FANCY_BIRD:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case FIRE_SPIRIT:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case GOLEM:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case KING_PENGUIN:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case LICH:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case LIVING_MONEY:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case MOUSE:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case ROBOT:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case ROCK:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case SEAL:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case SKELETON:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case SQUID:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case THUNDER_SPIRIT:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case TOY_BEAR:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case TRAINING_DUMMY:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case TURNIP:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case WALRUS:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case WARRIOR:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                case WRAITH:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityData.AbilityTypeName.ENERGY_SLICES, AbilityData.AbilityTypeName.FIRE_STRIKE);
                    break;
                default:
                    System.out.println("Why was an enemy's stats almost generated from no matching type name? characterTypeName:  " + characterTypeName);
            }
        }

        public int getMaxHp() {
            return maxHp;
        }

        public Array<AbilityData.AbilityTypeName> getAbilityOptions() {
            return abilityOptions;
        }

        private Array<AbilityData.AbilityTypeName> prepareOptions(AbilityData.AbilityTypeName... options) {
            Array<AbilityData.AbilityTypeName> result = new Array<>();
            result.addAll(options);
            return result;
        }
    }
}
