package com.roguelikedeckbuilder.mygame.stages.map;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;

public class CombatNodeOptions {
    private final CombatNodeComposition zoneOne = new CombatNodeComposition();
    private final CombatNodeComposition zoneTwo = new CombatNodeComposition();
    private final CombatNodeComposition zoneThree = new CombatNodeComposition();

    public CombatNodeOptions() {
        // Stage 1
        zoneOne.add(CombatNodeSectionName.FIRST, CharacterTypeName.HAMMIE, CharacterTypeName.HAMMIE);

        zoneOne.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.BURGER, CharacterTypeName.STARER);
        zoneOne.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.BURGER, CharacterTypeName.HAMMIE);
        zoneOne.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.HAMMIE, CharacterTypeName.HAMMIE, CharacterTypeName.HAMMIE);
        zoneOne.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.STARER, CharacterTypeName.HAMMIE);

        zoneOne.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.HAM_SHAMWITCH, CharacterTypeName.HAM_AND_FIST, CharacterTypeName.HAM_AND_FIST);
        zoneOne.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.STARER, CharacterTypeName.BURGER, CharacterTypeName.HAMMIE);
        zoneOne.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.HOT_DOG, CharacterTypeName.HAMMIE, CharacterTypeName.HAMMIE);
        zoneOne.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.HOT_DOG, CharacterTypeName.BURGER, CharacterTypeName.BURGER);
        zoneOne.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.HAM_SHAMWITCH, CharacterTypeName.HAM_AND_FIST, CharacterTypeName.HOT_DOG);

        zoneOne.add(CombatNodeSectionName.ELITE, CharacterTypeName.SAD_DOLLAR);
        zoneOne.add(CombatNodeSectionName.ELITE, CharacterTypeName.EVIL_HH);
        zoneOne.add(CombatNodeSectionName.ELITE, CharacterTypeName.POINTER);
        zoneOne.add(CombatNodeSectionName.ELITE, CharacterTypeName.UNIMPRESSED_FISH);

        zoneOne.add(CombatNodeSectionName.BOSS, CharacterTypeName.ANTEATER);

        // Stage 2
        zoneTwo.add(CombatNodeSectionName.FIRST, CharacterTypeName.ALIEN);
        zoneTwo.add(CombatNodeSectionName.FIRST, CharacterTypeName.SWORD_FISH);

        zoneTwo.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.ALIEN, CharacterTypeName.STARER, CharacterTypeName.SOCK);
        zoneTwo.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.SOCK, CharacterTypeName.SWORD_FISH);
        zoneTwo.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.SWORD_FISH, CharacterTypeName.BURGER, CharacterTypeName.CHIPS);
        zoneTwo.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.SWORD_FISH, CharacterTypeName.HAM_SHAMWITCH, CharacterTypeName.HAM_SHAMWITCH, CharacterTypeName.CHIPS);

        zoneTwo.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.UNIMPRESSED_FISH, CharacterTypeName.ALIEN);
        zoneTwo.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.EVIL_HH, CharacterTypeName.SWORD_FISH);
        zoneTwo.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.SAD_DOLLAR, CharacterTypeName.CHIPS);
        zoneTwo.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.SOCK, CharacterTypeName.POINTER);
        zoneTwo.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.KNIGHT);
        zoneTwo.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.UNIMPRESSED_FISH, CharacterTypeName.SWORD_FISH);

        zoneTwo.add(CombatNodeSectionName.ELITE, CharacterTypeName.HELMET_PENGUIN);
        zoneTwo.add(CombatNodeSectionName.ELITE, CharacterTypeName.PEANUT_BEE, CharacterTypeName.PEANUT_BEE);
        zoneTwo.add(CombatNodeSectionName.ELITE, CharacterTypeName.KNIGHT, CharacterTypeName.KNIGHT);
        zoneTwo.add(CombatNodeSectionName.ELITE, CharacterTypeName.MONOLITH);

        zoneTwo.add(CombatNodeSectionName.BOSS, CharacterTypeName.KING_OF_THE_BURROW);

        // Stage 3
        zoneThree.add(CombatNodeSectionName.FIRST, CharacterTypeName.SAD_DOLLAR);
        zoneThree.add(CombatNodeSectionName.FIRST, CharacterTypeName.POINTER);

        zoneThree.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.HAM_AND_FIST, CharacterTypeName.HAM_AND_FIST, CharacterTypeName.HAM_AND_FIST, CharacterTypeName.EVIL_HH);
        zoneThree.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.UNIMPRESSED_FISH, CharacterTypeName.SOCK, CharacterTypeName.HAM_SHAMWITCH);
        zoneThree.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.BURGER, CharacterTypeName.HOT_DOG, CharacterTypeName.CHIPS, CharacterTypeName.PEANUT_BEE);
        zoneThree.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.ALIEN, CharacterTypeName.ALIEN, CharacterTypeName.SOCK);
        zoneThree.add(CombatNodeSectionName.FIRST_HALF, CharacterTypeName.SWORD_FISH, CharacterTypeName.STARER, CharacterTypeName.PEANUT_BEE);

        zoneThree.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.MONOLITH, CharacterTypeName.SWORD_FISH, CharacterTypeName.ALIEN);
        zoneThree.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.ANTEATER);
        zoneThree.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.PEANUT_BEE, CharacterTypeName.POINTER);
        zoneThree.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.HELMET_PENGUIN, CharacterTypeName.SAD_DOLLAR);
        zoneThree.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.KNIGHT, CharacterTypeName.HELMET_PENGUIN);
        zoneThree.add(CombatNodeSectionName.SECOND_HALF, CharacterTypeName.SAD_DOLLAR, CharacterTypeName.MONOLITH);

        zoneThree.add(CombatNodeSectionName.ELITE, CharacterTypeName.KING_OF_THE_BURROW);
        zoneThree.add(CombatNodeSectionName.ELITE, CharacterTypeName.HELMET_PENGUIN, CharacterTypeName.HELMET_PENGUIN, CharacterTypeName.HELMET_PENGUIN, CharacterTypeName.HELMET_PENGUIN);
        zoneThree.add(CombatNodeSectionName.ELITE, CharacterTypeName.KNIGHT, CharacterTypeName.MONOLITH);
        zoneThree.add(CombatNodeSectionName.ELITE, CharacterTypeName.EVIL_HH, CharacterTypeName.EVIL_HH);

        zoneThree.add(CombatNodeSectionName.BOSS, CharacterTypeName.HAMMIE);
    }

    public Array<CharacterTypeName> getAlmostRandomFromOptions(int zoneNumber, CombatNodeSectionName combatNodeSectionName) {
        CombatNodeComposition options;

        switch (zoneNumber) {
            case 1 -> options = zoneOne;
            case 2 -> options = zoneTwo;
            case 3 -> options = zoneThree;
            default -> throw new IllegalStateException("Unexpected value: " + zoneNumber);
        }

        return options.getAlmostRandom(combatNodeSectionName);
    }
}
