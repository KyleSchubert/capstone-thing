package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.stages.*;

import java.util.Random;

import static com.roguelikedeckbuilder.mygame.MyGame.*;
import static com.roguelikedeckbuilder.mygame.stages.Map.MapNodeType.RANDOM_EVENT;

public class MenuController {
    protected boolean isGameplayPaused;
    private Stage mainMenuStage;
    private Stage pauseMenuStage;
    private Stage resultsMenuStage;
    private Stage upgradesMenuStage;
    private Stage settingsMenuStage;
    private CardChangeStage cardChangeMenuStage;
    private RestMenuStage restMenuStage;
    private TreasureMenuStage treasureMenuStage;
    private ShopMenuStage shopMenuStage;
    private CombatMenuStage combatMenuStage;
    private Map map;
    private Tooltip tooltip;
    private Stage topBarStage;
    private Image darkTransparentScreen;
    private Image pauseBackground;
    private Image resultsBackground;
    private Image settingsBackground;
    private Image upgradesBackground;
    private MenuState currentMenuState;
    private MenuState previousImportantMenuState;
    public static MenuState previousNonimportantMenuState;
    private Stage currentInputProcessor;
    private Stage previousInputProcessor;
    private boolean isDrawMainMenu;
    private boolean isDrawDarkTransparentScreen;
    private boolean isDrawPauseMenu;
    private boolean isDrawResultsMenu;
    private boolean isDrawUpgradesMenu;
    private boolean isDrawSettingsMenu;
    private boolean isDrawMapMenu;
    private boolean isDrawTooltipMenu;
    private boolean isDrawRestMenu;
    private boolean isDrawTreasureMenuStage;
    private boolean isDrawCardChangeMenuStage;
    private boolean isDrawShopMenuStage;
    private boolean isDrawCombatMenuStage;
    private float runningAnimationAddClock = 0;
    private float runningAnimationRemovalClock = 0;
    private final Random random = new Random();

    public void create(OrthographicCamera camera) {
        // For the UI and menus
        ScreenViewport viewportForStage = new ScreenViewport(camera);
        viewportForStage.setUnitsPerPixel(SCALE_FACTOR);
        mainMenuStage = new Stage(viewportForStage);
        pauseMenuStage = new Stage(viewportForStage);
        resultsMenuStage = new Stage(viewportForStage);
        upgradesMenuStage = new Stage(viewportForStage);
        settingsMenuStage = new Stage(viewportForStage);

        cardChangeMenuStage = new CardChangeStage(viewportForStage, makeClickListenerTriggeringMenuState(MenuState.TREASURE, MenuSoundType.OPEN));
        restMenuStage = new RestMenuStage(
                viewportForStage,
                makeClickListenerTriggeringMenuState(MenuState.MAP, MenuSoundType.CLOSE),
                makeClickListenerTriggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN),
                getClickListenerForPreparingCardUpgradeMenu()
        );
        treasureMenuStage = new TreasureMenuStage(
                viewportForStage,
                newImageButtonFrom("exit", MenuState.MAP, MenuSoundType.CLOSE),
                makeClickListenerTriggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN),
                getCardChoicePreparerClickListener()
        );
        shopMenuStage = new ShopMenuStage(
                viewportForStage,
                newImageButtonFrom("exit", MenuState.MAP, MenuSoundType.CLOSE),
                makeClickListenerTriggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN),
                getClickListenerForPreparingCardUpgradeMenu(),
                getClickListenerForPreparingCardRemoveMenu()
        );
        combatMenuStage = new CombatMenuStage(
                viewportForStage,
                newImageButtonFrom("exit", MenuState.MAP, MenuSoundType.CLOSE),
                cardChangeMenuStage,
                makeClickListenerTriggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN)
        );

        tooltip = new Tooltip(viewportForStage, makeClickListenerTriggeringMenuState(MenuState.MAP, MenuSoundType.CLOSE));

        ClickListener hoverAndClickListener = makeHoverAndClickListener();
        map = new Map(viewportForStage, hoverAndClickListener);

        Gdx.input.setInputProcessor(mainMenuStage);
        currentInputProcessor = mainMenuStage;
        previousInputProcessor = mainMenuStage;
        currentMenuState = MenuState.MAIN_MENU;
        previousImportantMenuState = MenuState.MAIN_MENU;
        previousNonimportantMenuState = currentMenuState;

        // Load the in-game currency counter
        Image persistentCurrencyCounterImage = new Image(new Texture(Gdx.files.internal("ITEMS/persistent coin.png")));
        persistentCurrencyCounterImage.setScale(SCALE_FACTOR);
        mainMenuStage.addActor(persistentCurrencyCounterImage);

        // Menu buttons below
        // PLAY button
        ImageButton playButton = newImageButtonFrom("play", MenuState.START_REWARDS, MenuSoundType.SILENT);
        mainMenuStage.addActor(playButton);

        // UPGRADES button
        ImageButton upgradesButton = newImageButtonFrom("upgrades", MenuState.UPGRADES, MenuSoundType.OPEN);
        mainMenuStage.addActor(upgradesButton);

        // SETTINGS button
        ImageButton settingsButton = newImageButtonFrom("settings", MenuState.SETTINGS, MenuSoundType.OPEN);
        mainMenuStage.addActor(settingsButton);

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
        mainMenuStage.addActor(exitButton);


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
        topBarDeckIcon.addCaptureListener(getClickListenerForViewingPlayerCards());
        topBarDeckIcon.addCaptureListener(makeClickListenerTriggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN));

        topBarStage = new Stage(viewportForStage);
        topBarStage.addActor(topBarBackground);
        topBarStage.addActor(topBarCoin);
        topBarStage.addActor(topBarDeckIcon);

        // Dark transparent screen
        darkTransparentScreen = new Image(new Texture(Gdx.files.internal("MENU backgrounds/dark transparent screen.png")));
        darkTransparentScreen.setSize(40 * SCALE_FACTOR * 300, 40 * SCALE_FACTOR * 300);
        darkTransparentScreen.setPosition(0, 0);

        // Pause background
        pauseBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/pause background.png")));
        pauseBackground.setSize(296 * SCALE_FACTOR, 287 * SCALE_FACTOR);

        // Results background
        resultsBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/results background.png")));
        resultsBackground.setSize(429 * SCALE_FACTOR, 844 * SCALE_FACTOR);

        // Settings background
        settingsBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/settings background.png")));
        settingsBackground.setSize(771 * SCALE_FACTOR, 814 * SCALE_FACTOR);

        // Upgrades background
        upgradesBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/upgrades background.png")));
        upgradesBackground.setSize(873 * SCALE_FACTOR, 814 * SCALE_FACTOR);


        // Pause menu buttons
        // Resume button
        ImageButton resumeButton = newImageButtonFrom("resume", MenuState.RESUME, MenuSoundType.CLOSE);
        pauseMenuStage.addActor(resumeButton);

        // Settings button
        ImageButton pauseSettingsButton = newImageButtonFrom("settings", MenuState.SETTINGS, MenuSoundType.OPEN);
        pauseMenuStage.addActor(pauseSettingsButton);

        // Give up button
        ImageButton giveUpButton = newImageButtonFrom("give up", MenuState.RESULTS, MenuSoundType.SILENT);
        pauseMenuStage.addActor(giveUpButton);

        // Results menu buttons
        // Main menu button
        ImageButton mainMenuButton = newImageButtonFrom("main menu", MenuState.MAIN_MENU, MenuSoundType.CLOSE);
        resultsMenuStage.addActor(mainMenuButton);

        // Upgrades menu buttons
        // Back button
        ImageButton upgradesBackButton = newImageButtonFrom("back", MenuState.MAIN_MENU, MenuSoundType.CLOSE);
        upgradesMenuStage.addActor(upgradesBackButton);

        // Settings menu buttons
        // Back button
        // TODO: this will need additional code to discard the changed settings
        ImageButton settingsBackButton = newImageButtonFrom("back", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE);
        settingsMenuStage.addActor(settingsBackButton);

        // confirm button
        // TODO: this will need additional code to save and apply the changed settings
        ImageButton settingsConfirmButton = newImageButtonFrom("confirm", MenuState.SETTINGS_BACK, MenuSoundType.CLOSE);
        settingsMenuStage.addActor(settingsConfirmButton);

        // Characters on main menu

        mainMenuStage.addActor(new Character(Character.CharacterTypeName.BIRD, 23, 6));
        mainMenuStage.addActor(new Character(Character.CharacterTypeName.BLUE_MUSHROOM, 29, 6));
        mainMenuStage.addActor(new Character(Character.CharacterTypeName.HELMET_PENGUIN, 35, 6));
        mainMenuStage.addActor(new Character(Character.CharacterTypeName.ORANGE_MUSHROOM, 41, 6));
        mainMenuStage.addActor(new Character(Character.CharacterTypeName.PIG, 47, 6));
        mainMenuStage.addActor(new Character(Character.CharacterTypeName.PLANT, 53, 6));
        mainMenuStage.addActor(new Character(Character.CharacterTypeName.STUMP, 59, 6));

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
            if (getMousePosition().y() > 43) {
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
                combatMenuStage.setVictory(false);

                treasureMenuStage.addGenericWinTreasureSet();

                setMenuState(MenuState.MAP);
                setMenuState(MenuState.TREASURE);
            }
        }
        batch.end();
        batch.begin();

        if (this.isDrawMainMenu) {
            mainMenuStage.getActors().get(0).setPosition(15.5f, 14); // persistentCurrencyCounterImage
            mainMenuStage.getActors().get(1).setPosition(2, 18); // playButton
            mainMenuStage.getActors().get(2).setPosition(2, 13); // upgradesButton
            mainMenuStage.getActors().get(3).setPosition(2, 8); // settingsButton
            mainMenuStage.getActors().get(4).setPosition(2, 3); // exitButton
            mainMenuStage.getViewport().apply();
            mainMenuStage.act(elapsedTime);
            mainMenuStage.draw();
            font.draw(batch, "x " + Player.getPersistentMoney(), 17.3f, 15.3f); // text for currency counter

            // For the running animation looping
            runningAnimationAddClock += elapsedTime;
            if (runningAnimationAddClock > 0.2f) {
                runningAnimationAddClock -= 0.2f;
                animateRandomRunningCharacter();
            }

            runningAnimationRemovalClock += elapsedTime;
            if (runningAnimationRemovalClock > 3) {
                runningAnimationRemovalClock -= 3;

                Array<Actor> mustRemove = new Array<>();

                for (Actor actor : mainMenuStage.getActors()) {
                    UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
                    if (actorType == UserObjectOptions.RUNNING_ANIMATION_CHARACTER) {
                        if (actor.getX() < -15) {
                            mustRemove.add(actor);
                        }
                    }
                }

                for (Actor actor : mustRemove) {
                    actor.remove();
                }
            }
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
        if (this.isDrawTooltipMenu) {
            tooltip.batch(elapsedTime);
        }
        if (this.isDrawPauseMenu) { // JUST for the pause menu background texture
            pauseBackground.setPosition(29.5f, 20);
            pauseBackground.draw(batch, 1);
        }
        if (this.isDrawUpgradesMenu) { // JUST for the upgrades menu background texture
            upgradesBackground.setPosition(23, 3.7f);
            upgradesBackground.draw(batch, 1);
        }
        if (this.isDrawResultsMenu) { // JUST for the results menu background texture
            resultsBackground.setPosition(48, 0.7f);
            resultsBackground.draw(batch, 1);
        }
        if (this.isDrawSettingsMenu) { // JUST for the settings menu background texture
            settingsBackground.setPosition(23.7f, 3.7f);
            settingsBackground.draw(batch, 1);
        }
    }

    public void postBatch(float elapsedTime) {
        if (this.isDrawPauseMenu) {
            // draw the pause menu
            pauseMenuStage.getActors().get(0).setPosition(31, 29.2f); // resume button
            pauseMenuStage.getActors().get(1).setPosition(31, 25.1f); // settings button
            pauseMenuStage.getActors().get(2).setPosition(31.5f, 20.5f); // give up button
            pauseMenuStage.getViewport().apply();
            pauseMenuStage.act(elapsedTime);
            pauseMenuStage.draw();
        }
        if (this.isDrawUpgradesMenu) {
            // draw the upgrades menu
            upgradesMenuStage.getActors().get(0).setPosition(25, 5); // back button
            upgradesMenuStage.getViewport().apply();
            upgradesMenuStage.act(elapsedTime);
            upgradesMenuStage.draw();
        }
        if (this.isDrawResultsMenu) {
            // draw the results menu
            resultsMenuStage.getActors().get(0).setPosition(52, 2); // main menu button
            resultsMenuStage.getViewport().apply();
            resultsMenuStage.act(elapsedTime);
            resultsMenuStage.draw();
        }
        if (this.isDrawSettingsMenu) {
            // draw the settings menu
            settingsMenuStage.getActors().get(0).setPosition(25, 5); // back button
            settingsMenuStage.getActors().get(1).setPosition(49, 5); // confirm button
            settingsMenuStage.getViewport().apply();
            settingsMenuStage.act(elapsedTime);
            settingsMenuStage.draw();
        }
    }

    public void dispose() {
        mainMenuStage.dispose();
        pauseMenuStage.dispose();
        resultsMenuStage.dispose();
        upgradesMenuStage.dispose();
        settingsMenuStage.dispose();
        tooltip.dispose();
        map.dispose();
        cardChangeMenuStage.dispose();
        restMenuStage.dispose();
        combatMenuStage.dispose();
        shopMenuStage.dispose();
        treasureMenuStage.dispose();
        topBarStage.dispose();
    }

    public void resize(int width, int height) {
        mainMenuStage.getViewport().update(width, height, true);
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
                tooltip.useMapNodeData(data.nodeType(), data.stageNumberOfSelf(), data.indexOfSelf());
                tooltip.setSize(data.tooltipSize());
                tooltip.setLocation(data.tooltipLocation());

                // Draw the tooltip
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
                    Map.MapNodeType nodeType;
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

    private ClickListener makeClickListenerTriggeringMenuState(MenuState menuState, MenuSoundType menuSoundType) {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setMenuState(menuState);
                if (menuSoundType == MenuSoundType.OPEN) {
                    SoundManager.playMenuOpenSound();
                } else if (menuSoundType == MenuSoundType.CLOSE) {
                    SoundManager.playMenuCloseSound();
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
                Gdx.input.setInputProcessor(mainMenuStage);
                currentInputProcessor = mainMenuStage;
                UseLine.setVisibility(false);
                setTimeElapsedInGame(0f);
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
                tooltip.setShowChooseOneItemDetails(false);
            }
            case UPGRADES -> {
                currentMenuState = MenuState.UPGRADES;
                Gdx.input.setInputProcessor(upgradesMenuStage);
                currentInputProcessor = upgradesMenuStage;
                setDrawDarkTransparentScreen(true);
                setDrawUpgradesMenu(true);
            }
            case SETTINGS -> {
                Gdx.input.setInputProcessor(settingsMenuStage);
                currentInputProcessor = settingsMenuStage;
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(false);
                setDrawSettingsMenu(true);
            }
            case SETTINGS_BACK -> // exclusive to the buttons in the settings menu
                    setMenuState(currentMenuState);
            case PAUSED -> {
                currentMenuState = MenuState.PAUSED;
                Gdx.input.setInputProcessor(pauseMenuStage);
                currentInputProcessor = pauseMenuStage;
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(true);
                setDrawSettingsMenu(false);
            }
            case RESUME -> setMenuState(previousImportantMenuState);
            case RESULTS -> {
                currentMenuState = MenuState.RESULTS;
                Gdx.input.setInputProcessor(resultsMenuStage);
                currentInputProcessor = resultsMenuStage;
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(false);
                setDrawResultsMenu(true);
                setDrawTooltipMenu(false);
            }
            case START_REWARDS -> {
                tooltip.itemReward();
                currentMenuState = MenuState.START_REWARDS;
                Gdx.input.setInputProcessor(tooltip.tooltipStage);
                currentInputProcessor = tooltip.tooltipStage;
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
                        int randomNumber = random.nextInt(Character.CharacterTypeName.values().length);
                        combatMenuStage.addEnemy(Character.CharacterTypeName.values()[randomNumber]);

                    }
                }

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
        this.isGameplayPaused = gameplayPaused;
    }

    public void setDrawMapMenu(boolean drawMapMenu) {
        this.isDrawMapMenu = drawMapMenu;
    }

    public void setDrawTooltipMenu(boolean drawTooltipMenu) {
        this.isDrawTooltipMenu = drawTooltipMenu;
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

    public MenuState getCurrentMenuState() {
        return currentMenuState;
    }

    private void animateRandomRunningCharacter() {
        float startX = 80;
        float startY = 31;
        float endXOffset = -640;

        float randomX = random.nextFloat(15) - 7;
        float randomY = random.nextFloat(15) - 7;

        int randomTypeIndex = random.nextInt(Character.CharacterTypeName.values().length);
        Character.CharacterTypeName randomType = Character.CharacterTypeName.values()[randomTypeIndex];

        mainMenuStage.addActor(new Character(randomType, startX + randomX, startY + randomY));
        Character character = (Character) mainMenuStage.getActors().get(mainMenuStage.getActors().size - 1);
        character.setTouchable(Touchable.disabled);
        character.setUserObject(UserObjectOptions.RUNNING_ANIMATION_CHARACTER);
        character.setState(Character.CharacterState.MOVING);

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(character.getX() + endXOffset, character.getY());
        moveAction.setDuration(90f);
        character.addAction(moveAction);
    }

    private ClickListener getClickListenerForViewingPlayerCards() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                cardChangeMenuStage.prepareViewPlayerCards();
                SoundManager.playMenuOpenSound();
            }
        };
    }

    private ClickListener getClickListenerForPreparingCardRemoveMenu() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                cardChangeMenuStage.prepareRemovePlayerCards();
                SoundManager.playMenuOpenSound();
            }
        };
    }

    private ClickListener getClickListenerForPreparingCardUpgradeMenu() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                cardChangeMenuStage.prepareUpgradePlayerCards();
                SoundManager.playMenuOpenSound();
            }
        };
    }

    private ClickListener getCardChoicePreparerClickListener() {
        return new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                cardChangeMenuStage.prepareThreeCardChoice();
                SoundManager.playMenuOpenSound();
            }
        };
    }

    public TreasureMenuStage getTreasureMenuStage() {
        return treasureMenuStage;
    }

    public ShopMenuStage getShopMenuStage() {
        return shopMenuStage;
    }

    public enum MenuState {
        MAIN_MENU, MAP, PAUSED, RESULTS, UPGRADES, SETTINGS_BACK, RESUME, SETTINGS, START_REWARDS, REST_AREA, TREASURE, SHOP, CARD_UPGRADE, COMBAT, CARD_CHOICE
    }

    public enum MenuSoundType {
        OPEN, CLOSE, SILENT
    }
}
