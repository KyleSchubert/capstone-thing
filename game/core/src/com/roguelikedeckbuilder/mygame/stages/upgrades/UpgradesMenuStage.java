package com.roguelikedeckbuilder.mygame.stages.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.LabelMaker;
import com.roguelikedeckbuilder.mygame.helpers.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;

import static com.roguelikedeckbuilder.mygame.MyGame.batch;
import static com.roguelikedeckbuilder.mygame.MyGame.font;

public class UpgradesMenuStage extends GenericStage {
    private static final Array<Label> allOwnAndPriceLabels = new Array<>();

    public UpgradesMenuStage() {
        super("upgrades background");
        getStageBackgroundActor().setPosition(460, 74);

        ImageButton confirmButton = ClickListenerManager.getMenuSwitchingButton(
                "confirm", MenuState.MAIN_MENU, MenuSoundType.CLOSE, 1080, 100);
        confirmButton.addCaptureListener(ClickListenerManager.saveUpgrades());
        addActor(confirmButton);

        ImageButton resetButton = ClickListenerManager.getImageButton("reset");
        resetButton.addCaptureListener(ClickListenerManager.resetUpgrades());
        resetButton.setPosition(1100, 580);
        addActor(resetButton);

        Label title = LabelMaker.newLabel("Upgrades", LabelMaker.getLarge());
        title.setPosition(1120, 810);
        addActor(title);

        Image persistentCurrencyCounterImage = new Image(new Texture(Gdx.files.internal("ITEMS/persistent coin.png")));
        persistentCurrencyCounterImage.setPosition(1096, 764);
        addActor(persistentCurrencyCounterImage);

        // Upgrade buttons
        int X_POSITION = 480;
        int Y_GAP = 65;
        int Y_INITIAL = 810;

        int i = 0;
        for (String upgradeName : Player.allUpgradeNames) {
            makeUpgrade(upgradeName, X_POSITION, Y_INITIAL - i * Y_GAP);
            i++;
        }
    }

    public static int getUpgradeLimit(String upgradeName) {
        int limit = 999999;

        switch (upgradeName) {
            case "upgrade-bypassImmunity" -> limit = 1;
            case "upgrade-draw" -> limit = 3;
        }

        return limit;
    }

    public static int getBasePrice(String upgradeName) {
        int price = 999999;

        switch (upgradeName) {
            case "upgrade-bypassImmunity" -> price = 2000;
            case "upgrade-coins" -> price = 4;
            case "upgrade-con" -> price = 16;
            case "upgrade-draw" -> price = 300;
            case "upgrade-energy" -> price = 75;
            case "upgrade-item" -> price = 20;
            case "upgrade-maxHP" -> price = 12;
            case "upgrade-str" -> price = 18;
            case "upgrade-x2Damage" -> price = 3000;
            case "upgrade-x2SUPER" -> price = 22;
            case "upgrade-preCure" -> price = 30;
        }

        return price;
    }

    public static String getOwnAmountAndPriceString(String upgradeName) {
        int amount = Player.getUpgrades().get(upgradeName);

        if (Player.getUpgrades().get(upgradeName) >= UpgradesMenuStage.getUpgradeLimit(upgradeName)) {
            return String.format("Own %d%nSOLD OUT", amount);
        }

        return String.format("Own: %d%nPrice: %d", amount, getTotalPrice(upgradeName));
    }

    public static int getTotalPrice(String upgradeName) {
        int amount = Player.getUpgrades().get(upgradeName);
        double multiplier = Math.pow(1.8, amount);

        // Since the largest base price that can increase infinitely is 3000, limit the multiplier to the integer limit / 4096.
        // That just means the price will stagnate eventually, but that's fine.
        int intMultiplier = (int) Math.min(multiplier, Integer.MAX_VALUE >>> 12);

        return intMultiplier * getBasePrice(upgradeName);
    }

    public static void resetLabels() {
        int i = 0;
        for (Label label : allOwnAndPriceLabels) {
            label.setText(getOwnAmountAndPriceString(Player.allUpgradeNames.get(i)));
            i++;
        }
    }

    private void makeUpgrade(String upgradeName, float x, float y) {
        ImageButton upgradeButton = ClickListenerManager.getImageButton(upgradeName);
        Label amountAndPrice = LabelMaker.newLabel(
                getOwnAmountAndPriceString(upgradeName), LabelMaker.getSmall()
        );
        amountAndPrice.setWidth(700);
        amountAndPrice.setTouchable(Touchable.disabled);

        upgradeButton.addCaptureListener(ClickListenerManager.increasingCostInUpgrades(amountAndPrice, upgradeName));

        upgradeButton.setPosition(x, y);
        amountAndPrice.setPosition(x + 300, y + 10);

        allOwnAndPriceLabels.add(amountAndPrice);
        addActor(amountAndPrice);
        addActor(upgradeButton);
    }

    @Override
    public void batch(float elapsedTime) {
        super.batch(elapsedTime);

        font.draw(batch, "x " + (Player.getPersistentMoney() - Player.getSpentPersistentMoney()), 1130, 790); // text for currency counter
    }
}
