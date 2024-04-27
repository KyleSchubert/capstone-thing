package com.roguelikedeckbuilder.mygame.combat.enemy;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityTypeName;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectData;
import com.roguelikedeckbuilder.mygame.combat.effect.EffectName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;


// Enemies' attacks only do one thing for now.
public class Intent {
    private final Group group = new Group();

    public Intent(AbilityTypeName abilityTypeName) {
        EffectName effectName = AbilityData.getEffect(abilityTypeName);

        String description = EffectData.prepareOneEffectDescription(effectName);
        group.addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip("", description, Statistics.getRunNumber()));

        Image intentIcon = EffectData.getIntentIcon(effectName);
        group.addActor(intentIcon);
    }


    public Group getGroup() {
        return group;
    }
}
