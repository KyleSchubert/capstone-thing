package com.roguelikedeckbuilder.mygame.stages.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.roguelikedeckbuilder.mygame.MyGame;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;
import com.roguelikedeckbuilder.mygame.tracking.statistics.StatisticsType;

public class ResultsMenuStage extends GenericStage {
    private final Label wonOrLost;
    private final Label timeSpent;
    private final Label fights;
    private final Label cardsDrawn;
    private final Label enemiesKilled;
    private final Label damageTaken;
    private final Label damageDealt;
    private final Label itemsGained;
    private final Label coinsGained;
    private final Label turnsPlayed;
    private final Label superCoinsEarned; // "persistent money/currency" = SUPER Coins

    public ResultsMenuStage() {
        super("results background");

        getStageBackgroundActor().setPosition(960, 14);

        wonOrLost = LabelMaker.newLabel("", LabelMaker.getLarge());
        timeSpent = LabelMaker.newLabel("", LabelMaker.getMedium());
        fights = LabelMaker.newLabel("", LabelMaker.getMedium());
        cardsDrawn = LabelMaker.newLabel("", LabelMaker.getMedium());
        enemiesKilled = LabelMaker.newLabel("", LabelMaker.getMedium());
        damageTaken = LabelMaker.newLabel("", LabelMaker.getMedium());
        damageDealt = LabelMaker.newLabel("", LabelMaker.getMedium());
        itemsGained = LabelMaker.newLabel("", LabelMaker.getMedium());
        coinsGained = LabelMaker.newLabel("", LabelMaker.getMedium());
        turnsPlayed = LabelMaker.newLabel("", LabelMaker.getMedium());
        superCoinsEarned = LabelMaker.newLabel("", LabelMaker.getLarge());

        wonOrLost.setPosition(60, 820);
        timeSpent.setPosition(0, 740);
        fights.setPosition(0, 680);
        cardsDrawn.setPosition(0, 620);
        enemiesKilled.setPosition(0, 560);
        damageTaken.setPosition(0, 500);
        damageDealt.setPosition(0, 440);
        itemsGained.setPosition(0, 380);
        coinsGained.setPosition(0, 320);
        turnsPlayed.setPosition(0, 260);
        superCoinsEarned.setPosition(-60, 180);

        wonOrLost.setWidth(400);
        timeSpent.setWidth(400);
        fights.setWidth(400);
        cardsDrawn.setWidth(400);
        enemiesKilled.setWidth(400);
        damageTaken.setWidth(400);
        damageDealt.setWidth(400);
        itemsGained.setWidth(400);
        coinsGained.setWidth(400);
        turnsPlayed.setWidth(400);
        superCoinsEarned.setWidth(400);
        superCoinsEarned.setAlignment(Align.center);

        Image superCoin = new Image(new Texture(Gdx.files.internal("ITEMS/persistent coin.png")));
        superCoin.setPosition(64, 146);

        Group groupHoldingAllLabels = new Group();
        groupHoldingAllLabels.setPosition(1040, 0);

        groupHoldingAllLabels.addActor(wonOrLost);
        groupHoldingAllLabels.addActor(timeSpent);
        groupHoldingAllLabels.addActor(fights);
        groupHoldingAllLabels.addActor(cardsDrawn);
        groupHoldingAllLabels.addActor(enemiesKilled);
        groupHoldingAllLabels.addActor(damageTaken);
        groupHoldingAllLabels.addActor(damageDealt);
        groupHoldingAllLabels.addActor(itemsGained);
        groupHoldingAllLabels.addActor(coinsGained);
        groupHoldingAllLabels.addActor(turnsPlayed);
        groupHoldingAllLabels.addActor(superCoinsEarned);

        groupHoldingAllLabels.addActor(superCoin);

        addActor(groupHoldingAllLabels);

        ImageButton mainMenuButton = ClickListenerManager.getMenuSwitchingButton(
                "main menu", MenuState.MAIN_MENU, MenuSoundType.CLOSE, 1040, 40);
        addActor(mainMenuButton);
    }

    public void setAllLabels(boolean victory) {
        if (victory) {
            wonOrLost.setText("Victory");
        } else {
            wonOrLost.setText("You Died");
        }

        timeSpent.setText(String.format("Time: %s", MyGame.timeText));

        fights.setText(String.format("Fights: %d", Statistics.countOccurrencesInCurrentRun(StatisticsType.COMBAT_STARTED)));

        cardsDrawn.setText(String.format("Cards Drawn: %d", Statistics.countOccurrencesInCurrentRun(StatisticsType.DREW_CARD)));

        enemiesKilled.setText(String.format("Enemies Killed: %d", Statistics.countOccurrencesInCurrentRun(StatisticsType.ENEMY_DIED)));

        damageTaken.setText(String.format("Damage Taken: %d", Statistics.sumValuesOfOccurrencesInCurrentRun(StatisticsType.PLAYER_TOOK_DAMAGE)));

        damageDealt.setText(String.format("Damage Dealt: %d", Statistics.sumValuesOfOccurrencesInCurrentRun(StatisticsType.ENEMY_TOOK_DAMAGE)));

        itemsGained.setText(String.format("Items Gained: %d", Statistics.countOccurrencesInCurrentRun(StatisticsType.GAINED_ITEM)));

        coinsGained.setText(String.format("Coins Gained: %d", Statistics.sumValuesOfOccurrencesInCurrentRun(StatisticsType.GAINED_COINS)));

        turnsPlayed.setText(String.format("Turns Played: %d", Statistics.countOccurrencesInCurrentRun(StatisticsType.TURN_ENDED)));

        int amountOfSuperCoinsEarned = 100;
        superCoinsEarned.setText(String.format("SUPER Coins Earned:\n%d", amountOfSuperCoinsEarned));
        Player.changePersistentMoney(amountOfSuperCoinsEarned);
    }
}
