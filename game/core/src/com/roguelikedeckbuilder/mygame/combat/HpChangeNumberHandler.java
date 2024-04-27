package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import java.util.Random;


public class HpChangeNumberHandler {
    private static final Array<HpChangeNumber> numbersThatNeedToBeDrawn = new Array<>();
    private static final Random random = new Random();

    public static void create(XYPair<Float> centerPoint, int hpChangeAmount) {
        numbersThatNeedToBeDrawn.add(new HpChangeNumber(centerPoint, hpChangeAmount));
    }

    public static int size() {
        return numbersThatNeedToBeDrawn.size;
    }

    public static HpChangeNumber pop() {
        return numbersThatNeedToBeDrawn.pop();
    }

    public static class HpChangeNumber {
        Group group = new Group();

        public HpChangeNumber(XYPair<Float> centerPoint, int hpChangeAmount) {

            float xFluctuation = random.nextFloat(120) - 60;
            float yFluctuation = random.nextFloat(120) - 60;

            group.setPosition(centerPoint.x() + xFluctuation, centerPoint.y() + yFluctuation);

            group.addActor(LabelMaker.newLabel(Integer.toString(hpChangeAmount), LabelMaker.getHpAndDamage()));

            SequenceAction sequenceAction = new SequenceAction(
                    Actions.moveBy(0, 80, 0.8f),
                    Actions.fadeOut(0.5f),
                    Actions.removeActor()
            );

            group.addAction(sequenceAction);
        }

        public Group getGroup() {
            return group;
        }
    }
}
