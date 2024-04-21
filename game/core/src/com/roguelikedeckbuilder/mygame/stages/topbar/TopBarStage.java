package com.roguelikedeckbuilder.mygame.stages.topbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import static com.roguelikedeckbuilder.mygame.MyGame.*;

public class TopBarStage extends GenericStage {

    public TopBarStage(ScreenViewport viewportForStage) {
        super(viewportForStage, "top bar background");

        getStageBackgroundActor().setPosition(0, 42.7f);

        Image topBarCoin = new Image(new Texture(Gdx.files.internal("ITEMS/doubloon.png")));
        topBarCoin.setPosition(53.2f, 43.6f);
        addActor(topBarCoin);

        Image topBarDeckIcon = new Image(new Texture(Gdx.files.internal("OTHER UI/deck.png")));
        topBarDeckIcon.setPosition(46.2f, 42.9f);
        topBarDeckIcon.addCaptureListener(ClickListenerManager.viewingPlayerCards());
        topBarDeckIcon.addCaptureListener(ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN));
        addActor(topBarDeckIcon);
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        font.draw(batch, "Deck", 46, 45);
        font.draw(batch, timeText, 65, 45); // text for time elapsed in game
        font.draw(batch, "HP: " + Player.getCombatInformation().getHp() + " / " + Player.getCombatInformation().getMaxHp(), 2, 45);
        font.draw(batch, Integer.toString(Player.getMoney()), 55, 45);
    }
}
