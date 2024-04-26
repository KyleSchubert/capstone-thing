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

    public static Array<AbilityTypeName> getAbilityOptions(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getAbilityOptions();
    }

    public static EnemyClassification getEnemyClassification(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getEnemyClassification();
    }

    private static class IndividualEnemyData {
        private int maxHp;
        private Array<AbilityTypeName> abilityOptions;
        private EnemyClassification enemyClassification;

        public IndividualEnemyData(CharacterTypeName characterTypeName) {
            switch (characterTypeName) {
                case ALIEN:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.FIRE_STRIKE, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case ANTEATER:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.FIRE_STRIKE);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case BURGER:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.FIRE_STRIKE, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case CHIPS:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case EVIL_HH:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.FIRE_STRIKE, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case HAM_AND_FIST:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case HAM_SHAMWITCH:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.FIRE_STRIKE, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case HAMMIE:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case HELMET_PENGUIN:
                    maxHp = 80;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.FIRE_STRIKE);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case HOT_DOG:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.FIRE_STRIKE, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case KNIGHT:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.FIRE_STRIKE);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case MONOLITH:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case POINTER:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.FIRE_STRIKE);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case SAD_DOLLAR:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.FIRE_STRIKE, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case SOCK:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.DEFEND_UPGRADED, AbilityTypeName.FIRE_STRIKE);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case STARER:
                    maxHp = 20;
                    abilityOptions = prepareOptions(AbilityTypeName.ENERGY_SLICES, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case SWORD_FISH:
                    maxHp = 10;
                    abilityOptions = prepareOptions(AbilityTypeName.ENERGY_SLICES, AbilityTypeName.FIRE_STRIKE);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                case UNIMPRESSED_FISH:
                    maxHp = 50;
                    abilityOptions = prepareOptions(AbilityTypeName.ENERGY_SLICES, AbilityTypeName.HEAL_SELF_ENEMY);
                    enemyClassification = EnemyClassification.NORMAL;
                    break;
                default:
                    System.out.println("Why was an enemy's stats almost generated from no matching type name? characterTypeName:  " + characterTypeName);
            }
        }

        public int getMaxHp() {
            return maxHp;
        }

        public Array<AbilityTypeName> getAbilityOptions() {
            return abilityOptions;
        }

        public EnemyClassification getEnemyClassification() {
            return enemyClassification;
        }

        private Array<AbilityTypeName> prepareOptions(AbilityTypeName... options) {
            Array<AbilityTypeName> result = new Array<>();
            result.addAll(options);
            return result;
        }
    }
}
