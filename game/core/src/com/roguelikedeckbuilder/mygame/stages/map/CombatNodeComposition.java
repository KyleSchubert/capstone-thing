package com.roguelikedeckbuilder.mygame.stages.map;

import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;

public class CombatNodeComposition {
    private final Array<Array<CharacterTypeName>> first;
    private final Array<Array<CharacterTypeName>> firstHalf;
    private final Array<Array<CharacterTypeName>> secondHalf;
    private final Array<Array<CharacterTypeName>> elite;
    private final Array<Array<CharacterTypeName>> boss;
    private final Array<Array<CharacterTypeName>> workingSet;
    private CombatNodeSectionName workingSetCurrentSectionName;

    public CombatNodeComposition() {
        first = new Array<>();
        firstHalf = new Array<>();
        secondHalf = new Array<>();
        elite = new Array<>();
        boss = new Array<>();

        workingSet = new Array<>();
    }

    public void add(CombatNodeSectionName section, CharacterTypeName enemy1) {
        Array<CharacterTypeName> composition = new Array<>();

        composition.add(enemy1);

        putDataWhereItGoes(section, composition);
    }

    public void add(CombatNodeSectionName section, CharacterTypeName enemy1, CharacterTypeName enemy2) {
        Array<CharacterTypeName> composition = new Array<>();

        composition.add(enemy1);
        composition.add(enemy2);

        putDataWhereItGoes(section, composition);
    }

    public void add(CombatNodeSectionName section, CharacterTypeName enemy1, CharacterTypeName enemy2, CharacterTypeName enemy3) {
        Array<CharacterTypeName> composition = new Array<>();

        composition.add(enemy1);
        composition.add(enemy2);
        composition.add(enemy3);

        putDataWhereItGoes(section, composition);
    }

    public void add(CombatNodeSectionName section, CharacterTypeName enemy1, CharacterTypeName enemy2, CharacterTypeName enemy3, CharacterTypeName enemy4) {
        Array<CharacterTypeName> composition = new Array<>();

        composition.add(enemy1);
        composition.add(enemy2);
        composition.add(enemy3);
        composition.add(enemy4);

        putDataWhereItGoes(section, composition);
    }

    private void putDataWhereItGoes(CombatNodeSectionName section, Array<CharacterTypeName> composition) {
        switch (section) {
            case BOSS -> boss.add(composition);
            case ELITE -> elite.add(composition);
            case FIRST -> first.add(composition);
            case FIRST_HALF -> firstHalf.add(composition);
            case SECOND_HALF -> secondHalf.add(composition);
        }
    }

    public Array<CharacterTypeName> getAlmostRandom(CombatNodeSectionName section) {
        Array<Array<CharacterTypeName>> options;

        switch (section) {
            case BOSS -> options = boss;
            case ELITE -> options = elite;
            case FIRST -> options = first;
            case FIRST_HALF -> options = firstHalf;
            case SECOND_HALF -> options = secondHalf;
            default -> throw new IllegalStateException("Unexpected value: " + section);
        }

        if (workingSetCurrentSectionName != section) {
            workingSet.clear();
            workingSetCurrentSectionName = section;
        }

        if (workingSet.isEmpty()) {
            workingSet.addAll(options);
            workingSet.shuffle();
        }

        return workingSet.pop();
    }
}
