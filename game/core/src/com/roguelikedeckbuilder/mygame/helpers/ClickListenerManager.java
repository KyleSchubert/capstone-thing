package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.cards.CardTypeName;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuController;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuSoundType;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.tooltip.Size;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;
import com.roguelikedeckbuilder.mygame.treasure.Treasure;
import com.roguelikedeckbuilder.mygame.treasure.TreasureType;

public class ClickListenerManager {
    // These could be in MenuController, but I wanted the organization bonus of it being in another file so yeah

    private static MenuController menuController;

    public static void initialize(MenuController _menuController) {
        menuController = _menuController;
    }

    private static ClickListener getClickListenerForTouchUp(Runnable action) {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                action.run();
            }
        };
    }


    public static ClickListener obtainingItem(ItemTypeName itemTypeName) {
        return getClickListenerForTouchUp(() -> Player.obtainItem(itemTypeName));
    }

    public static ClickListener viewingPlayerCards() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareViewPlayerCards();
            SoundManager.playMenuOpenSound();
        });
    }

    public static ClickListener healing() {
        return getClickListenerForTouchUp(() -> {
            int healAmount = Math.round(Player.getCombatInformation().getMaxHp() * 0.35f);
            Player.getCombatInformation().changeHp(healAmount);
            SoundManager.playHealSound();
        });
    }

    public static ClickListener triggeringMenuState(MenuState menuState, MenuSoundType menuSoundType) {
        return getClickListenerForTouchUp(() -> {
            menuController.setMenuState(menuState);
            if (menuSoundType == MenuSoundType.OPEN) {
                SoundManager.playMenuOpenSound();
            } else if (menuSoundType == MenuSoundType.CLOSE) {
                SoundManager.playMenuCloseSound();
            }
        });
    }

    public static ClickListener preparingCardRemoveMenu() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareRemovePlayerCards();
            SoundManager.playMenuOpenSound();
        });
    }

    public static ClickListener preparingCardUpgradeMenu() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareUpgradePlayerCards();
            SoundManager.playMenuOpenSound();
        });
    }

    public static ClickListener obtainingCard(CardTypeName cardTypeName, boolean isUpgraded) {
        return getClickListenerForTouchUp(() -> {
            String cardName;
            if (isUpgraded) {
                cardName = AbilityData.getName(CardData.getUpgradedAbilityTypeName(cardTypeName));
            } else {
                cardName = AbilityData.getName(CardData.getAbilityTypeName(cardTypeName));
            }
            System.out.println("Player chose card: " + cardName);
            Player.obtainCard(cardTypeName, isUpgraded);
            SoundManager.playGetCardSound();
        });
    }

    public static ClickListener preparingCardChoiceMenu() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareThreeCardChoice();
            SoundManager.playMenuOpenSound();
        });
    }

    public static ClickListener upgradingCard(int cardIndex, CardTypeName cardTypeName) {
        return getClickListenerForTouchUp(() -> {
            String cardName = AbilityData.getName(CardData.getUpgradedAbilityTypeName(cardTypeName));

            System.out.println("Player upgraded card into: " + cardName);
            Player.removeCard(cardIndex, false);
            Player.obtainCard(cardTypeName, true);
            Player.setFlagGoBackToPreviousMenuState(true);
            SoundManager.playGetCardSound();
        });
    }

    public static ClickListener removingCard(int cardIndex) {
        return getClickListenerForTouchUp(() -> {
            System.out.println("Player removed card at index: " + cardIndex);
            Player.removeCard(cardIndex, true);
            Player.setFlagGoBackToPreviousMenuState(true);
            SoundManager.playGetCardSound(); // but this doesn't make super sense
        });
    }

    public static ClickListener triggerTreasure(TreasureType treasureType, int amount, Group groupToRemove) {
        return getClickListenerForTouchUp(() -> {
            Treasure.triggerTreasure(treasureType, amount);
            groupToRemove.remove();
        });
    }

    public static ClickListener buyingCard(Card card) {
        return getClickListenerForTouchUp(() -> {
            if (card.getCardTypeName() == CardTypeName.OUT_OF_STOCK) {
                return;
            }
            boolean success = Player.buyCard(CardData.getValue(card.getCardTypeName()), card.getCardTypeName(), card.isUpgraded());
            if (success) {
                SoundManager.playBuyInShopSound();
                menuController.getShopMenuStage().useCorrectButtons();
                menuController.getShopMenuStage().setCardSold(card);
            }
        });
    }

    public static ClickListener lookingAtDrawPile(Array<Card> drawPileContents) {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareViewDrawPile(drawPileContents);
            SoundManager.playMenuOpenSound();
        });
    }

    public static ClickListener increasingCostInShop(Integer[] cost, Label label) {
        return getClickListenerForTouchUp(() -> {
            if (Player.getMoney() >= cost[0]) {
                Player.changeMoney(-cost[0]);
                cost[0] += 50;
                label.setText("Price: " + cost[0]);
                SoundManager.playBuyInShopSound();
            }
        });
    }

    public static ClickListener hoverAndPutTextInTooltip(String title, String body, int runNumber) {
        return new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                menuController.getTooltipStage().setSize(Size.SMALL);
                menuController.getTooltipStage().setLocation();
                menuController.getTooltipStage().setTitleText(title);
                menuController.getTooltipStage().setBodyText(body);
                menuController.getTooltipStage().resetPositionsOffscreen();
                MenuController.setDrawTooltipMenu(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                int currentRun = Statistics.getRunNumber();
                // Prevent going back to the main menu, storing this event, and triggering it in the next run
                if (runNumber == currentRun) {
                    menuController.getTooltipStage().resetPositionsOffscreen();
                    MenuController.setDrawTooltipMenu(false);
                }
            }
        };
    }
}
