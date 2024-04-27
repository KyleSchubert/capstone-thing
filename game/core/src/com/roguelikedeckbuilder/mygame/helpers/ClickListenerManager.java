package com.roguelikedeckbuilder.mygame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;
import com.roguelikedeckbuilder.mygame.MyGame;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterData;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffect;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffectName;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.cards.CardTypeName;
import com.roguelikedeckbuilder.mygame.combat.ability.AbilityData;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.items.ItemTypeName;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuController;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.mainmenu.CharacterSelection;
import com.roguelikedeckbuilder.mygame.stages.settings.SettingsMenuStage;
import com.roguelikedeckbuilder.mygame.stages.settings.Slider;
import com.roguelikedeckbuilder.mygame.stages.tooltip.Size;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;
import com.roguelikedeckbuilder.mygame.treasure.Treasure;
import com.roguelikedeckbuilder.mygame.treasure.TreasureType;

import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

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

    public static DragListener getDragListener() {
        return new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                UserObjectOptions userObjectOptions = (UserObjectOptions) event.getTarget().getUserObject();
                if (userObjectOptions == UserObjectOptions.SLIDER_KNOB) {
                    Actor knob = event.getTarget();
                    float sliderOffset = knob.getParent().getX() + knob.getWidth() / 2;
                    float correctedMousePosition = getMousePosition().x() - sliderOffset;

                    Slider slider = (Slider) knob.getParent();
                    slider.updateKnobPosition(correctedMousePosition);
                }
            }
        };
    }


    public static ClickListener obtainingItem(ItemTypeName itemTypeName) {
        return getClickListenerForTouchUp(() -> Player.obtainItem(itemTypeName));
    }

    public static ClickListener viewingPlayerCards() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareViewPlayerCards();
            AudioManager.playMenuOpenSound();
        });
    }

    public static ClickListener healing() {
        return getClickListenerForTouchUp(() -> {
            int healAmount = Math.round(Player.getCombatInformation().getMaxHp() * 0.35f);
            Player.getCombatInformation().changeHp(healAmount);
            AudioManager.playHealSound();
        });
    }

    public static ClickListener triggeringMenuState(MenuState menuState, MenuSoundType menuSoundType) {
        return getClickListenerForTouchUp(() -> {
            menuController.setMenuState(menuState);
            if (menuSoundType == MenuSoundType.OPEN) {
                AudioManager.playMenuOpenSound();
            } else if (menuSoundType == MenuSoundType.CLOSE) {
                AudioManager.playMenuCloseSound();
            }
        });
    }

    public static ClickListener preparingCardRemoveMenu() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareRemovePlayerCards();
            AudioManager.playMenuOpenSound();
        });
    }

    public static ClickListener preparingCardUpgradeMenu() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareUpgradePlayerCards();
            AudioManager.playMenuOpenSound();
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
            AudioManager.playGetCardSound();
        });
    }

    public static ClickListener preparingCardChoiceMenu() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareThreeCardChoice();
            AudioManager.playMenuOpenSound();
        });
    }

    public static ClickListener upgradingCard(int cardIndex, CardTypeName cardTypeName) {
        return getClickListenerForTouchUp(() -> {
            String cardName = AbilityData.getName(CardData.getUpgradedAbilityTypeName(cardTypeName));

            System.out.println("Player upgraded card into: " + cardName);
            Player.removeCard(cardIndex, false);
            Player.obtainCard(cardTypeName, true);
            Player.setFlagGoBackToPreviousMenuState(true);
            AudioManager.playGetCardSound();
        });
    }

    public static ClickListener removingCard(int cardIndex) {
        return getClickListenerForTouchUp(() -> {
            System.out.println("Player removed card at index: " + cardIndex);
            Player.removeCard(cardIndex, true);
            Player.setFlagGoBackToPreviousMenuState(true);
            AudioManager.playGetCardSound(); // but this doesn't make super sense
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
                AudioManager.playBuyInShopSound();
                menuController.getShopMenuStage().useCorrectButtons();
                menuController.getShopMenuStage().setCardSold(card);
            }
        });
    }

    public static ClickListener lookingAtDrawPile() {
        return getClickListenerForTouchUp(() -> {
            menuController.getCardChangeMenuStage().prepareViewDrawPile(Player.getDrawPileContents());
            AudioManager.playMenuOpenSound();
        });
    }

    public static ClickListener increasingCostInShop(Integer[] cost, Label label) {
        return getClickListenerForTouchUp(() -> {
            if (Player.getMoney() >= cost[0]) {
                Player.changeMoney(-cost[0]);
                cost[0] += 50;
                label.setText("Price: " + cost[0]);
                AudioManager.playBuyInShopSound();
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
                // -999 is a value I made up that is impossible to reach unless directly used
                if (runNumber == currentRun || runNumber == -999) {
                    menuController.getTooltipStage().resetPositionsOffscreen();
                    MenuController.setDrawTooltipMenu(false);
                }
            }
        };
    }

    public static ClickListener exitingGame() {
        return getClickListenerForTouchUp(() -> {
            Gdx.app.exit();
            System.exit(-1);
        });
    }

    public static ImageButton getImageButton(String buttonInternalFolderName) {
        Texture notClickedTexture = new Texture(Gdx.files.internal("MENU BUTTONS/" + buttonInternalFolderName + "/default.png"));
        Texture clickedTexture = new Texture(Gdx.files.internal("MENU BUTTONS/" + buttonInternalFolderName + "/hover.png"));
        ImageButton button = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(notClickedTexture)),
                new TextureRegionDrawable(new TextureRegion(clickedTexture))
        );
        button.setSize(notClickedTexture.getWidth(), notClickedTexture.getHeight());
        return button;
    }

    public static ImageButton getMenuSwitchingButton(String buttonInternalFolderName, MenuState menuState, MenuSoundType menuSoundType, float x, float y) {
        ImageButton newButton = ClickListenerManager.getImageButton(buttonInternalFolderName);
        newButton.addCaptureListener(ClickListenerManager.triggeringMenuState(menuState, menuSoundType));
        newButton.setPosition(x, y);
        return newButton;
    }

    public static ClickListener savingSettings() {
        return getClickListenerForTouchUp(SaveLoad::saveVolumeSettings);
    }

    public static ClickListener notSavingSettings() {
        return getClickListenerForTouchUp(SaveLoad::loadVolumeSettings);
    }

    public static ClickListener reloadSettingsMenu() {
        return getClickListenerForTouchUp(SettingsMenuStage::repositionSliders);
    }

    public static EventListener buyingItem(Item item) {
        return getClickListenerForTouchUp(() -> {
            boolean success = Player.buyItem(ItemData.getValue(item.getItemTypeName()), item.getItemTypeName());
            if (success) {
                AudioManager.playBuyInShopSound();
                menuController.getShopMenuStage().useCorrectButtons();
                menuController.getShopMenuStage().setItemSold(item);
            }
        });
    }

    public static ClickListener characterSelectPixelColor(Pixmap pixmap, Stage stageThisIsOn, CharacterSelection characterSelection) {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                float multiplier = 1 / event.getTarget().getScaleX();

                XYPair<Integer> pos = new XYPair<>(Math.round(x * multiplier), Math.round(pixmap.getHeight() - y * multiplier));

                Color color = new Color(pixmap.getPixel(pos.x(), pos.y()));

                if (color.toString().equals("00000000")) {
                    return;
                }

                VisualEffect visualEffect = new VisualEffect(
                        VisualEffectName.CHARACTER_SELECTED,
                        x,
                        y,
                        event.getTarget().getScaleX()
                );
                visualEffect.setTouchable(Touchable.disabled);
                stageThisIsOn.addActor(visualEffect);

                CharacterTypeName characterTypeName = CharacterData.colorToCharacterTypeName(color);
                characterSelection.setCharacter(characterTypeName);

                System.out.println(pos.x() + " " + pos.y() + "  -  " + color + "  Character:  " + characterTypeName);
            }
        };
    }

    public static ClickListener resettingResolution() {
        return getClickListenerForTouchUp(() -> {
            Gdx.graphics.setWindowedMode(MyGame.windowWidth, MyGame.windowHeight);
        });
    }
}
