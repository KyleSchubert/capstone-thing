package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class RestMenuStage extends GenericStage {
    public RestMenuStage(ScreenViewport viewportForStage, ClickListener clickListener, ClickListener cardChangeStageTrigger, ClickListener cardUpgradePreparerClickListener) {
        super(viewportForStage, "gray background");
        float backgroundXPosition = 13.5f;
        super.getStageBackgroundActor().setPosition(backgroundXPosition, 4);

        Image restButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/rest area/Rest.png")));
        float restButtonXPosition = backgroundXPosition + (getStageBackgroundActor().getWidth() - restButton.getWidth()) / 2 * SCALE_FACTOR;
        restButton.setPosition(restButtonXPosition, 26);
        restButton.setScale(SCALE_FACTOR);
        restButton.addListener(clickListener);
        restButton.addListener(getClickListenerForHealing());
        getStage().addActor(restButton);

        Image upgradeButton = new Image(new Texture(Gdx.files.internal("MENU BUTTONS/rest area/Upgrade.png")));
        float upgradeButtonXPosition = backgroundXPosition + (getStageBackgroundActor().getWidth() - upgradeButton.getWidth()) / 2 * SCALE_FACTOR;
        upgradeButton.setPosition(upgradeButtonXPosition, 6);
        upgradeButton.setScale(SCALE_FACTOR);
        upgradeButton.addListener(clickListener);
        upgradeButton.addListener(cardChangeStageTrigger);
        upgradeButton.addListener(cardUpgradePreparerClickListener);
        getStage().addActor(upgradeButton);
    }

    private ClickListener getClickListenerForHealing() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                int healAmount = Math.round(Player.getCombatInformation().getMaxHp() * 0.35f);
                Player.getCombatInformation().changeHp(healAmount);
                SoundManager.playNotSureWhatThisCouldBeUsedForSound();
            }
        };
    }

    public void batch(float elapsedTime) {
        super.batch(elapsedTime);
    }
}
