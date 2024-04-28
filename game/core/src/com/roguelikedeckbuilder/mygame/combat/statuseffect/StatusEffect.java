package com.roguelikedeckbuilder.mygame.combat.statuseffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;


public class StatusEffect extends Group {
    private final StatusEffectTypeName statusEffectTypeName;
    private final Label amountOfStacks;
    private int amount;
    private boolean toBeAdded = true;
    private boolean isDebuff;

    public StatusEffect(StatusEffectTypeName statusEffectTypeName, int amount) {
        this.statusEffectTypeName = statusEffectTypeName;
        this.amount = amount;

        Image icon = new Image(new Texture(Gdx.files.internal(StatusEffectData.getImagePath(statusEffectTypeName))));
        this.addActor(icon);

        amountOfStacks = LabelMaker.newLabel(Integer.toString(amount), LabelMaker.getSmall());
        amountOfStacks.setPosition(38, 3);
        amountOfStacks.setTouchable(Touchable.disabled);
        this.addActor(amountOfStacks);

        this.addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip(
                StatusEffectData.getName(statusEffectTypeName),
                StatusEffectData.getDescription(statusEffectTypeName),
                Statistics.getRunNumber()
        ));

        isDebuff = StatusEffectData.isDebuff(statusEffectTypeName);

        setUserObject(UserObjectOptions.STATUS_EFFECT);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        if (this.amount <= 0) {
            this.remove();
            return;
        }
        amountOfStacks.setText(Integer.toString(this.amount));
    }

    public boolean isDebuff() {
        return isDebuff;
    }

    public StatusEffectTypeName getStatusEffectTypeName() {
        return statusEffectTypeName;
    }

    public boolean isToBeAdded() {
        return toBeAdded;
    }

    public void setToBeAdded(boolean toBeAdded) {
        this.toBeAdded = toBeAdded;
    }
}
