package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.MenuController;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.combat.AbilityData;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.treasure.Treasure;

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


    public static ClickListener obtainingItem(ItemData.ItemName itemName) {
        return getClickListenerForTouchUp(() -> Player.obtainItem(itemName));
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

    public static ClickListener triggeringMenuState(MenuController.MenuState menuState, MenuController.MenuSoundType menuSoundType) {
        return getClickListenerForTouchUp(() -> {
            menuController.setMenuState(menuState);
            if (menuSoundType == MenuController.MenuSoundType.OPEN) {
                SoundManager.playMenuOpenSound();
            } else if (menuSoundType == MenuController.MenuSoundType.CLOSE) {
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

    public static ClickListener obtainingCard(CardData.CardTypeName cardTypeName, boolean isUpgraded) {
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

    public static ClickListener upgradingCard(int cardIndex, CardData.CardTypeName cardTypeName) {
        return getClickListenerForTouchUp(() -> {
            String cardName = AbilityData.getName(CardData.getUpgradedAbilityTypeName(cardTypeName));

            System.out.println("Player upgraded card into: " + cardName);
            Player.removeCard(cardIndex);
            Player.obtainCard(cardTypeName, true);
            Player.setFlagGoBackToPreviousMenuState(true);
            SoundManager.playGetCardSound();
        });
    }

    public static ClickListener removingCard(int cardIndex) {
        return getClickListenerForTouchUp(() -> {
            System.out.println("Player removed card at index: " + cardIndex);
            Player.removeCard(cardIndex);
            Player.setFlagGoBackToPreviousMenuState(true);
            SoundManager.playGetCardSound(); // but this doesn't make super sense
        });
    }

    public static ClickListener triggerTreasure(Treasure.TreasureType treasureType, int amount, Group groupToRemove) {
        return getClickListenerForTouchUp(() -> {
            Treasure.triggerTreasure(treasureType, amount);
            groupToRemove.remove();
        });
    }

    public static ClickListener buyingCard(Card card) {
        return getClickListenerForTouchUp(() -> {
            Player.buyCard(CardData.getValue(card.getCardTypeName()), card.getCardTypeName(), card.isUpgraded());
            SoundManager.playBuyInShopSound();
            menuController.getShopMenuStage().useCorrectButtons();
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
}
