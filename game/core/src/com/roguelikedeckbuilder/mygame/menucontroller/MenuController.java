package com.roguelikedeckbuilder.mygame.menucontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.helpers.ClickListenerManager;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;
import com.roguelikedeckbuilder.mygame.stages.cardchange.CardChangeStage;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.CombatMenuStage;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.UseLine;
import com.roguelikedeckbuilder.mygame.stages.mainmenu.MainMenuStage;
import com.roguelikedeckbuilder.mygame.stages.map.Map;
import com.roguelikedeckbuilder.mygame.stages.map.MapNodeType;
import com.roguelikedeckbuilder.mygame.stages.pause.PauseMenuStage;
import com.roguelikedeckbuilder.mygame.stages.rest.RestMenuStage;
import com.roguelikedeckbuilder.mygame.stages.results.ResultsMenuStage;
import com.roguelikedeckbuilder.mygame.stages.settings.SettingsMenuStage;
import com.roguelikedeckbuilder.mygame.stages.shop.ShopMenuStage;
import com.roguelikedeckbuilder.mygame.stages.tooltip.TooltipStage;
import com.roguelikedeckbuilder.mygame.stages.treasure.TreasureMenuStage;
import com.roguelikedeckbuilder.mygame.stages.upgrades.UpgradesMenuStage;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

import java.util.Random;

import static com.roguelikedeckbuilder.mygame.MyGame.*;
import static com.roguelikedeckbuilder.mygame.stages.map.MapNodeType.RANDOM_EVENT;

public class MenuController {
    public static MenuState previousNonimportantMenuState;
    protected static boolean isGameplayPaused;
    private static boolean isDrawTooltipMenu;
    private MainMenuStage mainMenuStage;
    private PauseMenuStage pauseMenuStage;
    private ResultsMenuStage resultsMenuStage;
    private UpgradesMenuStage upgradesMenuStage;
    private SettingsMenuStage settingsMenuStage;
    private CardChangeStage cardChangeMenuStage;
    private RestMenuStage restMenuStage;
    private TreasureMenuStage treasureMenuStage;
    private ShopMenuStage shopMenuStage;
    private CombatMenuStage combatMenuStage;
    private Map map;
    private TooltipStage tooltipStage;
    private Stage topBarStage;
    private Image darkTransparentScreen;
    private MenuState currentMenuState;
    private MenuState previousImportantMenuState;
    private Stage currentInputProcessor;
    private Stage previousInputProcessor;
    private boolean isDrawMainMenu;
    private boolean isDrawDarkTransparentScreen;
    private boolean isDrawPauseMenu;
    private boolean isDrawResultsMenu;
    private boolean isDrawUpgradesMenu;
    private boolean isDrawSettingsMenu;
    private boolean isDrawMapMenu;
    private boolean isDrawRestMenu;
    private boolean isDrawTreasureMenuStage;
    private boolean isDrawCardChangeMenuStage;
    private boolean isDrawShopMenuStage;
    private boolean isDrawCombatMenuStage;

    public static void setDrawTooltipMenu(boolean drawTooltipMenu) {
        isDrawTooltipMenu = drawTooltipMenu;
    }

    public static ImageButton getImageButton(String buttonInternalFolderName) {
        Texture notClickedTexture = new Texture(Gdx.files.internal("MENU BUTTONS/" + buttonInternalFolderName + "/default.png"));
        Texture clickedTexture = new Texture(Gdx.files.internal("MENU BUTTONS/" + buttonInternalFolderName + "/hover.png"));
        ImageButton button = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(notClickedTexture)),
                new TextureRegionDrawable(new TextureRegion(clickedTexture))
        );
        button.setSize(notClickedTexture.getWidth() * SCALE_FACTOR, notClickedTexture.getHeight() * SCALE_FACTOR);
        return button;
    }

    public static boolean getIsGameplayPaused() {
        return isGameplayPaused;
    }

    public void create(OrthographicCamera camera) {
        // For the UI and menus
        ScreenViewport viewportForStage = new ScreenViewport(camera);
        viewportForStage.setUnitsPerPixel(SCALE_FACTOR);

        // EXIT button
        ImageButton exitButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("MENU BUTTONS/exit/default.png")))),
                new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("MENU BUTTONS/exit/hover.png"))))
        );
        exitButton.setSize(152 * SCALE_FACTOR, 72 * SCALE_FACTOR);
        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                System.exit(-1);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        mainMenuStage = new MainMenuStage(
                viewportForStage,
                newImageButtonFrom("play", MenuState.START_REWARDS, MenuSoundType.SILENT),
                newImageButtonFrom("upgrades", MenuState.UPGRADES, MenuSoundType.OPEN),
                newImageButtonFrom("settings", MenuState.SETTINGS, MenuSoundType.OPEN),
                exitButton
        );

        pauseMenuStage = new PauseMenuStage(
                viewportForStage,
                newImageButtonFrom("resume", MenuState.RESUME, MenuSoundType.CLOSE),
                newImageButtonFrom("settings", MenuState.SETTINGS, MenuSoundType.OPEN),
                newImageButtonFrom("give up", MenuState.RESULTS, MenuSoundType.SILENT)
        );

        resultsMenuStage = new ResultsMenuStage(
                viewportForStage,
                newImageButtonFrom("main menu", MenuState.MAIN_MENU, MenuSoundType.CLOSE)
        );

        upgradesMenuStage = new UpgradesMenuStage(
                viewportForStage,
                newImageButtonFrom("back", MenuState.MAIN_MENU, MenuSoundType.CLOSE)
        );

        settingsMenuStage = new SettingsMenuStage(
                viewportForStage,
                newImageButtonFrom("back", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE),
                newImageButtonFrom("confirm", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE)
        );

        cardChangeMenuStage = new CardChangeStage(
                viewportForStage,
                ClickListenerManager.triggeringMenuState(MenuState.TREASURE, MenuSoundType.OPEN)
        );

        restMenuStage = new RestMenuStage(
                viewportForStage,
                ClickListenerManager.triggeringMenuState(MenuState.MAP, MenuSoundType.CLOSE),
                ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN),
                ClickListenerManager.preparingCardUpgradeMenu()
        );

        treasureMenuStage = new TreasureMenuStage(
                viewportForStage,
                newImageButtonFrom("exit", MenuState.MAP, MenuSoundType.CLOSE),
                ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN),
                ClickListenerManager.preparingCardChoiceMenu()
        );

        shopMenuStage = new ShopMenuStage(
                viewportForStage,
                newImageButtonFrom("exit", MenuState.MAP, MenuSoundType.CLOSE),
                ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN),
                ClickListenerManager.preparingCardUpgradeMenu(),
                ClickListenerManager.preparingCardRemoveMenu()
        );

        combatMenuStage = new CombatMenuStage(
                viewportForStage,
                ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN)
        );

        tooltipStage = new TooltipStage(
                viewportForStage,
                ClickListenerManager.triggeringMenuState(MenuState.MAP, MenuSoundType.CLOSE)
        );

        ClickListener hoverAndClickListener = makeHoverAndClickListener();
        map = new Map(viewportForStage, hoverAndClickListener);

        Gdx.input.setInputProcessor(mainMenuStage.getStage());
        currentInputProcessor = mainMenuStage.getStage();
        previousInputProcessor = mainMenuStage.getStage();
        currentMenuState = MenuState.MAIN_MENU;
        previousImportantMenuState = MenuState.MAIN_MENU;
        previousNonimportantMenuState = currentMenuState;

        // Top Bar Images
        Image topBarBackground = new Image(new Texture(Gdx.files.internal("OTHER UI/top bar background.png")));
        topBarBackground.setScale(SCALE_FACTOR);
        topBarBackground.setPosition(0, 42.7f);

        Image topBarCoin = new Image(new Texture(Gdx.files.internal("ITEMS/doubloon.png")));
        topBarCoin.setScale(SCALE_FACTOR);
        topBarCoin.setPosition(53.2f, 43.6f);

        Image topBarDeckIcon = new Image(new Texture(Gdx.files.internal("OTHER UI/deck.png")));
        topBarDeckIcon.setScale(SCALE_FACTOR);
        topBarDeckIcon.setPosition(46.2f, 42.9f);
        topBarDeckIcon.addCaptureListener(ClickListenerManager.viewingPlayerCards());
        topBarDeckIcon.addCaptureListener(ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN));

        topBarStage = new Stage(viewportForStage);
        topBarStage.addActor(topBarBackground);
        topBarStage.addActor(topBarCoin);
        topBarStage.addActor(topBarDeckIcon);

        // Dark transparent screen
        darkTransparentScreen = new Image(new Texture(Gdx.files.internal("MENU backgrounds/dark transparent screen.png")));
        darkTransparentScreen.setSize(40 * SCALE_FACTOR * 300, 40 * SCALE_FACTOR * 300);
        darkTransparentScreen.setPosition(0, 0);


        setMenuState(MenuState.MAIN_MENU);
    }

    public void batch(float elapsedTime, String timeText) {
        if (Player.isFlagGoBackToPreviousMenuState()) {
            Player.setFlagGoBackToPreviousMenuState(false);
            System.out.println("Going back to PREVIOUS: " + previousNonimportantMenuState);

            if (previousNonimportantMenuState == MenuState.SHOP) {
                shopMenuStage.useCorrectButtons();
            }
            if (previousNonimportantMenuState == MenuState.COMBAT) {
                // The combat stage gets special treatment, since loading it normally restarts the fight at the moment
                Gdx.input.setInputProcessor(combatMenuStage.getStage());
                setDrawCardChangeMenu(false);
                previousNonimportantMenuState = currentMenuState;
                currentMenuState = MenuState.COMBAT;
                previousInputProcessor = currentInputProcessor;
                currentInputProcessor = combatMenuStage.getStage();
            } else {
                setMenuState(previousNonimportantMenuState);
            }
        }

        // For letting the player click things on the topBar no matter the current stage they are on
        if (currentMenuState == MenuState.MAP || currentMenuState == MenuState.SHOP || currentMenuState == MenuState.COMBAT) {
            if (getMousePosition().y() > 40) {
                if (currentInputProcessor != topBarStage) {
                    Gdx.input.setInputProcessor(topBarStage);
                    previousInputProcessor = currentInputProcessor;
                    currentInputProcessor = topBarStage;
                    System.out.println("Changed input processor. OLD: " + previousInputProcessor + " CURRENT: " + currentInputProcessor);
                }
            } else {
                if (currentInputProcessor == topBarStage) {
                    Gdx.input.setInputProcessor(previousInputProcessor);
                    currentInputProcessor = previousInputProcessor;
                    previousInputProcessor = topBarStage;
                    System.out.println("Changed input processor. OLD: " + previousInputProcessor + " CURRENT: " + currentInputProcessor);
                }
            }
        }

        if (this.isDrawMapMenu) {
            map.drawMap(batch);
        }
        if (this.isDrawCombatMenuStage) {
            combatMenuStage.batch(elapsedTime, batch);
            if (combatMenuStage.isVictory()) {
                Statistics.combatEnded();
                combatMenuStage.setVictory(false);

                treasureMenuStage.addGenericWinTreasureSet();

                setMenuState(MenuState.MAP);
                setMenuState(MenuState.TREASURE);
            }
        }
        batch.end();
        batch.begin();

        if (this.isDrawMainMenu) {
            mainMenuStage.batch(elapsedTime);
        } else {
            topBarStage.draw();
            topBarStage.act();
            font.draw(batch, "Deck", 46, 45);
            font.draw(batch, timeText, 65, 45); // text for time elapsed in game
            font.draw(batch, "HP: " + Player.getCombatInformation().getHp() + " / " + Player.getCombatInformation().getMaxHp(), 2, 45);
            font.draw(batch, Integer.toString(Player.getMoney()), 55, 45);
        }
        batch.end();
        batch.begin();
        if (this.isDrawMapMenu) {
            map.batch(elapsedTime);
        }
        batch.end();
        batch.begin();
        if (UseLine.isVisible()) {
            UseLine.draw();
        }
        batch.end();
        batch.begin();
        if (this.isDrawDarkTransparentScreen) {
            // draw the dark transparent screen
            darkTransparentScreen.draw(batch, 1);
        }
        batch.end();
        batch.begin();

        if (this.isDrawRestMenu) {
            restMenuStage.batch(elapsedTime);
        }
        if (this.isDrawTreasureMenuStage) {
            treasureMenuStage.batch(elapsedTime);
        }
        if (this.isDrawShopMenuStage) {
            shopMenuStage.batch(elapsedTime);
        }

        batch.end();
        batch.begin();
        if (this.isDrawCardChangeMenuStage) {
            cardChangeMenuStage.batch(elapsedTime);
        }
        if (isDrawTooltipMenu) {
            tooltipStage.batch(elapsedTime);
        }
        if (this.isDrawPauseMenu) {
            pauseMenuStage.batch(elapsedTime);
        }
        if (this.isDrawUpgradesMenu) {
            upgradesMenuStage.batch(elapsedTime);
        }
        if (this.isDrawSettingsMenu) {
            settingsMenuStage.batch(elapsedTime);
        }
        if (this.isDrawResultsMenu) {
            resultsMenuStage.batch(elapsedTime);
        }
    }

    public void dispose() {
        mainMenuStage.dispose();
        pauseMenuStage.dispose();
        resultsMenuStage.dispose();
        upgradesMenuStage.dispose();
        settingsMenuStage.dispose();
        tooltipStage.dispose();
        map.dispose();
        cardChangeMenuStage.dispose();
        restMenuStage.dispose();
        combatMenuStage.dispose();
        shopMenuStage.dispose();
        treasureMenuStage.dispose();
        topBarStage.dispose();
    }

    private ClickListener makeHoverAndClickListener() {
        return new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                if (currentMenuState == MenuState.START_REWARDS) {
                    return;
                }
                // Get the MapNodeData object from the image actor that triggered the mouse over event
                Map.MapNode.MapNodeData data = (Map.MapNode.MapNodeData) event.getTarget().getUserObject();

                // Use the data
                tooltipStage.useMapNodeData(data.nodeType(), data.stageNumberOfSelf(), data.indexOfSelf());
                Statistics.setStageNumber(data.stageNumberOfSelf());
                Statistics.setNodeNumber(data.indexOfSelf());
                tooltipStage.setSize(data.tooltipSize());
                tooltipStage.setLocation(data.tooltipLocation());

                // Draw the tooltipStage
                setDrawTooltipMenu(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                if (currentMenuState != MenuState.START_REWARDS) {
                    setDrawTooltipMenu(false);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Get the MapNodeData object from the image actor that triggered the click event
                Map.MapNode.MapNodeData data = (Map.MapNode.MapNodeData) event.getTarget().getUserObject();

                // Check if the node is a valid choice
                if (map.isValidChoice(data.stageNumberOfSelf(), data.indexOfSelf())) {
                    SoundManager.playTravelSound();

                    // Make ??? (RANDOM_EVENT) nodes act like a random map node type
                    MapNodeType nodeType;
                    if (data.nodeType() == RANDOM_EVENT) {
                        nodeType = map.getRandomEventOptions().random();
                    } else {
                        nodeType = data.nodeType();
                    }

                    // Set the correct menu state
                    switch (nodeType) {
                        case NORMAL_BATTLE -> {
                            setMenuState(MenuState.COMBAT);
                        }
                        case ELITE_BATTLE -> {
                            setMenuState(MenuState.COMBAT);
                        }
                        case BOSS_BATTLE -> {
                            setMenuState(MenuState.COMBAT);
                        }
                        case SHOP -> {
                            shopMenuStage.generateShop();
                            setMenuState(MenuState.SHOP);
                        }
                        case REST -> setMenuState(MenuState.REST_AREA);
                        case TREASURE -> {
                            treasureMenuStage.aLotOfTreasure();
                            setMenuState(MenuState.TREASURE);
                        }
                    }

                    // Mark it as completed
                    map.completeNode(data.stageNumberOfSelf(), data.indexOfSelf());
                }

            }
        };
    }

    public void setMenuState(MenuState menuState) {
        previousNonimportantMenuState = currentMenuState;
        previousInputProcessor = currentInputProcessor;

        switch (menuState) {
            case MAIN_MENU -> {
                // GAME STARTS IN THIS STATE
                Player.reset();
                currentMenuState = MenuState.MAIN_MENU;
                map.reset();
                Gdx.input.setInputProcessor(mainMenuStage.getStage());
                currentInputProcessor = mainMenuStage.getStage();
                UseLine.setVisibility(false);
                Statistics.resetVariables();
                setGameplayPaused(true);
                setDrawMainMenu(true);
                setDrawDarkTransparentScreen(false);
                setDrawResultsMenu(false);
                setDrawUpgradesMenu(false);
                setDrawSettingsMenu(false);
                setDrawMapMenu(false);
                setDrawRestMenu(false);
                setDrawTreasureMenu(false);
                setDrawShopMenu(false);
                setDrawCombatMenu(false);
            }
            case MAP -> {
                setDrawMapMenu(true);
                setDrawTooltipMenu(false);
                currentMenuState = MenuState.MAP;
                Gdx.input.setInputProcessor(map.mapStage);
                currentInputProcessor = map.mapStage;
                previousImportantMenuState = MenuState.MAP;
                setDrawPauseMenu(false);
                isGameplayPaused = false;
                setDrawDarkTransparentScreen(false);
                setDrawRestMenu(false);
                setDrawTreasureMenu(false);
                setDrawShopMenu(false);
                setDrawCombatMenu(false);
                setDrawCardChangeMenu(false);
                UseLine.setVisibility(false);
                tooltipStage.setShowChooseOneItemDetails(false);
            }
            case UPGRADES -> {
                currentMenuState = MenuState.UPGRADES;
                Gdx.input.setInputProcessor(upgradesMenuStage.getStage());
                currentInputProcessor = upgradesMenuStage.getStage();
                setDrawDarkTransparentScreen(true);
                setDrawUpgradesMenu(true);
            }
            case SETTINGS -> {
                Gdx.input.setInputProcessor(settingsMenuStage.getStage());
                currentInputProcessor = settingsMenuStage.getStage();
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(false);
                setDrawSettingsMenu(true);
            }
            case SETTINGS_BACK -> // exclusive to the buttons in the settings menu
                    setMenuState(currentMenuState);
            case PAUSED -> {
                currentMenuState = MenuState.PAUSED;
                Gdx.input.setInputProcessor(pauseMenuStage.getStage());
                currentInputProcessor = pauseMenuStage.getStage();
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(true);
                setDrawSettingsMenu(false);
            }
            case RESUME -> setMenuState(previousImportantMenuState);
            case RESULTS -> {
                Statistics.runEnded();
                currentMenuState = MenuState.RESULTS;
                Gdx.input.setInputProcessor(resultsMenuStage.getStage());
                currentInputProcessor = resultsMenuStage.getStage();
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(false);
                setDrawResultsMenu(true);
                setDrawTooltipMenu(false);
            }
            case START_REWARDS -> {
                Statistics.setRunNumber(Statistics.getRunNumber() + 1);
                Statistics.runStarted();
                tooltipStage.itemReward();
                currentMenuState = MenuState.START_REWARDS;
                Gdx.input.setInputProcessor(tooltipStage.getStage());
                currentInputProcessor = tooltipStage.getStage();
                setDrawTooltipMenu(true);
                setDrawDarkTransparentScreen(true);
                setGameplayPaused(false);
                setDrawMainMenu(false);
                setDrawPauseMenu(false);
                setDrawMapMenu(true);
            }
            case REST_AREA -> {
                currentMenuState = MenuState.REST_AREA;
                Gdx.input.setInputProcessor(restMenuStage.getStage());
                currentInputProcessor = restMenuStage.getStage();
                setDrawTooltipMenu(false);
                setDrawRestMenu(true);
            }
            case TREASURE -> {
                currentMenuState = MenuState.TREASURE;
                UseLine.setVisibility(false);
                setDrawTreasureMenu(true);
                setDrawCardChangeMenu(false);
                setDrawDarkTransparentScreen(true);
                Gdx.input.setInputProcessor(treasureMenuStage.getStage());
                currentInputProcessor = treasureMenuStage.getStage();
                setDrawTooltipMenu(false);
            }
            case CARD_CHOICE -> {
                currentMenuState = MenuState.CARD_CHOICE;
                setDrawCardChangeMenu(true);
                Gdx.input.setInputProcessor(cardChangeMenuStage.getStage());
                currentInputProcessor = cardChangeMenuStage.getStage();
            }
            case CARD_UPGRADE -> {
                currentMenuState = MenuState.CARD_UPGRADE;
                setDrawCardChangeMenu(true);
                Gdx.input.setInputProcessor(cardChangeMenuStage.getStage());
                currentInputProcessor = cardChangeMenuStage.getStage();
            }
            case SHOP -> {
                currentMenuState = MenuState.SHOP;
                Gdx.input.setInputProcessor(shopMenuStage.getStage());
                currentInputProcessor = shopMenuStage.getStage();
                setDrawTooltipMenu(false);
                setDrawShopMenu(true);
                setDrawCardChangeMenu(false);
            }
            case COMBAT -> {
                currentMenuState = MenuState.COMBAT;
                Gdx.input.setInputProcessor(combatMenuStage.getStage());
                currentInputProcessor = combatMenuStage.getStage();
                setDrawTooltipMenu(false);
                isGameplayPaused = false;
                setDrawPauseMenu(false);
                setDrawCombatMenu(true);
                setDrawMapMenu(false);
                setDrawCardChangeMenu(false);
                setGameplayPaused(false);
                setDrawDarkTransparentScreen(false);

                // Reset and then add 4 random enemies for testing
                if (previousImportantMenuState != MenuState.COMBAT) {
                    Random random = new Random();
                    combatMenuStage.reset();
                    for (int i = 0; i < 4; i++) {
                        int randomNumber = random.nextInt(CharacterTypeName.values().length);
                        combatMenuStage.addEnemy(CharacterTypeName.values()[randomNumber]);
                    }
                }
                Statistics.combatStarted();
                previousImportantMenuState = MenuState.COMBAT;
            }

            default -> throw new IllegalStateException("Unexpected value: " + menuState);
        }
        System.out.println("PREVIOUS: " + previousNonimportantMenuState + "  |  Current: " + currentMenuState);
    }

    private void setDrawMainMenu(boolean drawMainMenu) {
        this.isDrawMainMenu = drawMainMenu;
    }

    private void setDrawDarkTransparentScreen(boolean drawDarkTransparentScreen) {
        this.isDrawDarkTransparentScreen = drawDarkTransparentScreen;
    }

    private void setDrawPauseMenu(boolean drawPauseMenu) {
        this.isDrawPauseMenu = drawPauseMenu;
    }

    private void setDrawResultsMenu(boolean drawResultsMenu) {
        this.isDrawResultsMenu = drawResultsMenu;
    }

    private void setDrawUpgradesMenu(boolean drawUpgradesMenu) {
        this.isDrawUpgradesMenu = drawUpgradesMenu;
    }

    private void setDrawSettingsMenu(boolean drawSettingsMenu) {
        this.isDrawSettingsMenu = drawSettingsMenu;
    }

    private void setGameplayPaused(boolean gameplayPaused) {
        isGameplayPaused = gameplayPaused;
    }

    public void setDrawMapMenu(boolean drawMapMenu) {
        this.isDrawMapMenu = drawMapMenu;
    }

    public void setDrawRestMenu(boolean drawRestMenu) {
        this.isDrawRestMenu = drawRestMenu;
    }

    public void setDrawTreasureMenu(boolean drawTreasureMenu) {
        this.isDrawTreasureMenuStage = drawTreasureMenu;
    }

    public void setDrawCardChangeMenu(boolean drawCardChangeMenu) {
        this.isDrawCardChangeMenuStage = drawCardChangeMenu;
    }

    public void setDrawShopMenu(boolean drawShopMenu) {
        this.isDrawShopMenuStage = drawShopMenu;
    }

    public void setDrawCombatMenu(boolean drawCombatMenu) {
        this.isDrawCombatMenuStage = drawCombatMenu;
    }

    private ImageButton newImageButtonFrom(String buttonInternalFolderName, MenuState menuState, MenuSoundType menuSoundType) {
        ImageButton button = getImageButton(buttonInternalFolderName);
        button.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setMenuState(menuState);
                if (menuSoundType == MenuSoundType.OPEN) {
                    SoundManager.playMenuOpenSound();
                } else if (menuSoundType == MenuSoundType.CLOSE) {
                    SoundManager.playMenuCloseSound();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        return button;
    }

    public MenuState getCurrentMenuState() {
        return currentMenuState;
    }

    public TreasureMenuStage getTreasureMenuStage() {
        return treasureMenuStage;
    }

    public ShopMenuStage getShopMenuStage() {
        return shopMenuStage;
    }

    public CardChangeStage getCardChangeMenuStage() {
        return cardChangeMenuStage;
    }

    public TooltipStage getTooltipStage() {
        return tooltipStage;
    }

    public Stage getTopBarStage() {
        return topBarStage;
    }
}
