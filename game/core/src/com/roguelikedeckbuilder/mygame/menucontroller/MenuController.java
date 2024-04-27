package com.roguelikedeckbuilder.mygame.menucontroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.helpers.AudioManager;
import com.roguelikedeckbuilder.mygame.stages.cardchange.CardChangeStage;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.CombatMenuStage;
import com.roguelikedeckbuilder.mygame.stages.combatmenu.UseLine;
import com.roguelikedeckbuilder.mygame.stages.mainmenu.MainMenuStage;
import com.roguelikedeckbuilder.mygame.stages.map.MapMenuStage;
import com.roguelikedeckbuilder.mygame.stages.map.MapNodeType;
import com.roguelikedeckbuilder.mygame.stages.pause.PauseMenuStage;
import com.roguelikedeckbuilder.mygame.stages.rest.RestMenuStage;
import com.roguelikedeckbuilder.mygame.stages.results.ResultsMenuStage;
import com.roguelikedeckbuilder.mygame.stages.settings.SettingsMenuStage;
import com.roguelikedeckbuilder.mygame.stages.shop.ShopMenuStage;
import com.roguelikedeckbuilder.mygame.stages.tooltip.TooltipStage;
import com.roguelikedeckbuilder.mygame.stages.topbar.TopBarStage;
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
    private MapMenuStage mapMenuStage;
    private TooltipStage tooltipStage;
    private TopBarStage topBarStage;
    private Image darkTransparentScreen;
    private MenuState currentMenuState;
    private MenuState previousImportantMenuState;
    private Stage currentInputProcessor;
    private Stage previousInputProcessor;
    private boolean isDrawDarkTransparentScreen;

    public static void setDrawTooltipMenu(boolean drawTooltipMenu) {
        isDrawTooltipMenu = drawTooltipMenu;
    }

    public static boolean getIsGameplayPaused() {
        return isGameplayPaused;
    }

    public void create() {
        // For the UI and menus
        mainMenuStage = new MainMenuStage();
        pauseMenuStage = new PauseMenuStage();
        resultsMenuStage = new ResultsMenuStage();
        upgradesMenuStage = new UpgradesMenuStage();
        settingsMenuStage = new SettingsMenuStage();
        cardChangeMenuStage = new CardChangeStage();
        restMenuStage = new RestMenuStage();
        treasureMenuStage = new TreasureMenuStage();
        shopMenuStage = new ShopMenuStage();
        combatMenuStage = new CombatMenuStage();
        tooltipStage = new TooltipStage();
        topBarStage = new TopBarStage();

        ClickListener hoverAndClickListener = makeHoverAndClickListener();
        mapMenuStage = new MapMenuStage(hoverAndClickListener);

        Gdx.input.setInputProcessor(mainMenuStage.getStage());
        currentInputProcessor = mainMenuStage.getStage();
        previousInputProcessor = mainMenuStage.getStage();
        currentMenuState = MenuState.MAIN_MENU;
        previousImportantMenuState = MenuState.MAIN_MENU;
        previousNonimportantMenuState = currentMenuState;

        // Dark transparent screen
        darkTransparentScreen = new Image(new Texture(Gdx.files.internal("MENU backgrounds/dark transparent screen.png")));
        darkTransparentScreen.setScale(SCALE_FACTOR);
        darkTransparentScreen.setSize(40 * 300, 40 * 300);
        darkTransparentScreen.setPosition(0, 0);


        setMenuState(MenuState.MAIN_MENU);
    }

    public void batch(float elapsedTime) {
        if (Player.isFlagGoBackToPreviousMenuState()) {
            Player.setFlagGoBackToPreviousMenuState(false);
            System.out.println("Going back to PREVIOUS: " + previousNonimportantMenuState);

            if (previousNonimportantMenuState == MenuState.SHOP) {
                shopMenuStage.useCorrectButtons();
                setDrawDarkTransparentScreen(false);
            }
            if (previousNonimportantMenuState == MenuState.COMBAT) {
                // The combat stage gets special treatment, since loading it normally restarts the fight at the moment
                Gdx.input.setInputProcessor(combatMenuStage.getStage());
                cardChangeMenuStage.setDraw(false);
                previousNonimportantMenuState = currentMenuState;
                currentMenuState = MenuState.COMBAT;
                previousInputProcessor = currentInputProcessor;
                currentInputProcessor = combatMenuStage.getStage();
                setDrawDarkTransparentScreen(false);
            } else {
                setMenuState(previousNonimportantMenuState);
            }
        }

        // For letting the player click things on the topBar no matter the current stage they are on
        if (currentMenuState == MenuState.MAP || currentMenuState == MenuState.SHOP || currentMenuState == MenuState.COMBAT) {
            if (getMousePosition().y() > 40) {
                if (currentInputProcessor != topBarStage.getStage()) {
                    Gdx.input.setInputProcessor(topBarStage.getStage());
                    previousInputProcessor = currentInputProcessor;
                    currentInputProcessor = topBarStage.getStage();
                    System.out.println("Changed input processor. OLD: " + previousInputProcessor + " CURRENT: " + currentInputProcessor);
                }
            } else {
                if (currentInputProcessor == topBarStage.getStage()) {
                    Gdx.input.setInputProcessor(previousInputProcessor);
                    currentInputProcessor = previousInputProcessor;
                    previousInputProcessor = topBarStage.getStage();
                    System.out.println("Changed input processor. OLD: " + previousInputProcessor + " CURRENT: " + currentInputProcessor);
                }
            }
        }

        if (combatMenuStage.isDraw()) {
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
        if (mapMenuStage.isDraw()) {
            mapMenuStage.batch(elapsedTime);
        }
        batch.end();
        batch.begin();

        if (mainMenuStage.isDraw()) {
            mainMenuStage.batch(elapsedTime);
        } else {
            topBarStage.batch(elapsedTime);
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

        if (restMenuStage.isDraw()) {
            restMenuStage.batch(elapsedTime);
        }
        if (treasureMenuStage.isDraw()) {
            treasureMenuStage.batch(elapsedTime);
        }
        if (shopMenuStage.isDraw()) {
            shopMenuStage.batch(elapsedTime);
        }

        batch.end();
        batch.begin();
        if (cardChangeMenuStage.isDraw()) {
            cardChangeMenuStage.batch(elapsedTime);
        }
        if (isDrawTooltipMenu) {
            tooltipStage.batch(elapsedTime);
        }
        if (pauseMenuStage.isDraw()) {
            pauseMenuStage.batch(elapsedTime);
        }
        if (upgradesMenuStage.isDraw()) {
            upgradesMenuStage.batch(elapsedTime);
        }
        if (settingsMenuStage.isDraw()) {
            settingsMenuStage.batch(elapsedTime);
        }
        if (resultsMenuStage.isDraw()) {
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
        mapMenuStage.dispose();
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
                MapMenuStage.MapNode.MapNodeData data = (MapMenuStage.MapNode.MapNodeData) event.getTarget().getUserObject();

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
                MapMenuStage.MapNode.MapNodeData data = (MapMenuStage.MapNode.MapNodeData) event.getTarget().getUserObject();

                // Check if the node is a valid choice
                if (mapMenuStage.isValidChoice(data.stageNumberOfSelf(), data.indexOfSelf())) {
                    AudioManager.playTravelSound();

                    // Make ??? (RANDOM_EVENT) nodes act like a random mapMenuStage node type
                    MapNodeType nodeType;
                    if (data.nodeType() == RANDOM_EVENT) {
                        nodeType = mapMenuStage.getRandomEventOptions().random();
                    } else {
                        nodeType = data.nodeType();
                    }

                    // Set the correct menu state
                    switch (nodeType) {
                        case NORMAL_BATTLE -> {
                            setMenuState(MenuState.COMBAT);

                            AudioManager.playNormalCombatMusic();
                        }
                        case ELITE_BATTLE -> {
                            setMenuState(MenuState.COMBAT);

                            AudioManager.playNormalCombatMusic();
                        }
                        case BOSS_BATTLE -> {
                            setMenuState(MenuState.COMBAT);

                            AudioManager.playBossMusic();
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
                    mapMenuStage.completeNode(data.stageNumberOfSelf(), data.indexOfSelf());
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
                mapMenuStage.reset();
                Gdx.input.setInputProcessor(mainMenuStage.getStage());
                currentInputProcessor = mainMenuStage.getStage();
                UseLine.setVisibility(false);
                Statistics.resetVariables();
                setGameplayPaused(true);
                mainMenuStage.setDraw(true);
                setDrawDarkTransparentScreen(false);
                resultsMenuStage.setDraw(false);
                upgradesMenuStage.setDraw(false);
                settingsMenuStage.setDraw(false);
                mapMenuStage.setDraw(false);
                restMenuStage.setDraw(false);
                treasureMenuStage.setDraw(false);
                shopMenuStage.setDraw(false);
                combatMenuStage.setDraw(false);

                AudioManager.playMenuMusic();
            }
            case MAP -> {
                mapMenuStage.setDraw(true);
                setDrawTooltipMenu(false);
                currentMenuState = MenuState.MAP;
                Gdx.input.setInputProcessor(mapMenuStage.getStage());
                currentInputProcessor = mapMenuStage.getStage();
                previousImportantMenuState = MenuState.MAP;
                pauseMenuStage.setDraw(false);
                isGameplayPaused = false;
                setDrawDarkTransparentScreen(false);
                restMenuStage.setDraw(false);
                treasureMenuStage.setDraw(false);
                shopMenuStage.setDraw(false);
                combatMenuStage.setDraw(false);
                cardChangeMenuStage.setDraw(false);
                UseLine.setVisibility(false);
                tooltipStage.setShowChooseOneItemDetails(false);

                AudioManager.playMapMusic();
            }
            case UPGRADES -> {
                currentMenuState = MenuState.UPGRADES;
                Gdx.input.setInputProcessor(upgradesMenuStage.getStage());
                currentInputProcessor = upgradesMenuStage.getStage();
                setDrawDarkTransparentScreen(true);
                upgradesMenuStage.setDraw(true);
            }
            case SETTINGS -> {
                Gdx.input.setInputProcessor(settingsMenuStage.getStage());
                currentInputProcessor = settingsMenuStage.getStage();
                setDrawDarkTransparentScreen(true);
                pauseMenuStage.setDraw(false);
                settingsMenuStage.setDraw(true);
            }
            case SETTINGS_BACK -> // exclusive to the buttons in the settings menu
                    setMenuState(currentMenuState);
            case PAUSED -> {
                currentMenuState = MenuState.PAUSED;
                Gdx.input.setInputProcessor(pauseMenuStage.getStage());
                currentInputProcessor = pauseMenuStage.getStage();
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                pauseMenuStage.setDraw(true);
                settingsMenuStage.setDraw(false);
            }
            case RESUME -> setMenuState(previousImportantMenuState);
            case RESULTS -> {
                Statistics.runEnded();

                boolean victory = false;
                resultsMenuStage.setAllLabels(victory);

                currentMenuState = MenuState.RESULTS;
                Gdx.input.setInputProcessor(resultsMenuStage.getStage());
                currentInputProcessor = resultsMenuStage.getStage();
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                pauseMenuStage.setDraw(false);
                resultsMenuStage.setDraw(true);
                setDrawTooltipMenu(false);
            }
            case START_REWARDS -> {
                Statistics.setRunNumber(Statistics.getRunNumber() + 1);
                Statistics.runStarted();
                Statistics.resetVariables();
                tooltipStage.itemReward();
                currentMenuState = MenuState.START_REWARDS;
                Gdx.input.setInputProcessor(tooltipStage.getStage());
                currentInputProcessor = tooltipStage.getStage();
                setDrawTooltipMenu(true);
                setDrawDarkTransparentScreen(true);
                setGameplayPaused(false);
                mainMenuStage.setDraw(false);
                pauseMenuStage.setDraw(false);
                mapMenuStage.setDraw(true);

                AudioManager.playMapMusic();
            }
            case REST_AREA -> {
                currentMenuState = MenuState.REST_AREA;
                Gdx.input.setInputProcessor(restMenuStage.getStage());
                currentInputProcessor = restMenuStage.getStage();
                setDrawTooltipMenu(false);
                restMenuStage.setDraw(true);
            }
            case TREASURE -> {
                currentMenuState = MenuState.TREASURE;
                UseLine.setVisibility(false);
                treasureMenuStage.setDraw(true);
                cardChangeMenuStage.setDraw(false);
                setDrawDarkTransparentScreen(true);
                Gdx.input.setInputProcessor(treasureMenuStage.getStage());
                currentInputProcessor = treasureMenuStage.getStage();
                setDrawTooltipMenu(false);
            }
            case CARD_CHOICE -> {
                currentMenuState = MenuState.CARD_CHOICE;
                cardChangeMenuStage.setDraw(true);
                setDrawTooltipMenu(false);
                setDrawDarkTransparentScreen(true);
                Gdx.input.setInputProcessor(cardChangeMenuStage.getStage());
                currentInputProcessor = cardChangeMenuStage.getStage();
            }
            case CARD_UPGRADE -> {
                currentMenuState = MenuState.CARD_UPGRADE;
                cardChangeMenuStage.setDraw(true);
                setDrawDarkTransparentScreen(true);
                Gdx.input.setInputProcessor(cardChangeMenuStage.getStage());
                currentInputProcessor = cardChangeMenuStage.getStage();
            }
            case SHOP -> {
                currentMenuState = MenuState.SHOP;
                Gdx.input.setInputProcessor(shopMenuStage.getStage());
                currentInputProcessor = shopMenuStage.getStage();
                setDrawTooltipMenu(false);
                setDrawDarkTransparentScreen(false);
                shopMenuStage.setDraw(true);
                cardChangeMenuStage.setDraw(false);
            }
            case COMBAT -> {
                currentMenuState = MenuState.COMBAT;
                Gdx.input.setInputProcessor(combatMenuStage.getStage());
                currentInputProcessor = combatMenuStage.getStage();
                setDrawTooltipMenu(false);
                isGameplayPaused = false;
                pauseMenuStage.setDraw(false);
                combatMenuStage.setDraw(true);
                mapMenuStage.setDraw(false);
                cardChangeMenuStage.setDraw(false);
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

    private void setDrawDarkTransparentScreen(boolean drawDarkTransparentScreen) {
        this.isDrawDarkTransparentScreen = drawDarkTransparentScreen;
    }

    private void setGameplayPaused(boolean gameplayPaused) {
        isGameplayPaused = gameplayPaused;
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

    public TopBarStage getTopBarStage() {
        return topBarStage;
    }
}
