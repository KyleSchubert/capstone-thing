package com.roguelikedeckbuilder.mygame;

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

import static com.roguelikedeckbuilder.mygame.MyGame.*;

public class MenuController {
    private Stage mainMenuStage;
    private Stage pauseMenuStage;
    private Stage resultsMenuStage;
    private Stage upgradesMenuStage;
    private Stage settingsMenuStage;
    private Map map;
    private Tooltip tooltip;
    private Image darkTransparentScreen;
    private Image pauseBackground;
    private Image resultsBackground;
    private Image settingsBackground;
    private Image upgradesBackground;

    public enum MenuState {
        MAIN_MENU, MAP, PAUSED, RESULTS, UPGRADES, SETTINGS_BACK, RESUME, SETTINGS, START_REWARDS, EVENT, STAGE_RESULTS, COMBAT
    }

    private MenuState currentMenuState;
    private MenuState previousImportantMenuState;
    protected boolean isGameplayPaused;
    private boolean isDrawMainMenu;
    private boolean isDrawDarkTransparentScreen;
    private boolean isDrawPauseMenu;
    private boolean isDrawResultsMenu;
    private boolean isDrawUpgradesMenu;
    private boolean isDrawSettingsMenu;
    private boolean isDrawMapMenu;
    private boolean isDrawTooltipMenu;

    public void create(OrthographicCamera camera) {
        // For the UI and menus
        ScreenViewport viewportForStage = new ScreenViewport(camera);
        viewportForStage.setUnitsPerPixel(SCALE_FACTOR);
        mainMenuStage = new Stage(viewportForStage);
        pauseMenuStage = new Stage(viewportForStage);
        resultsMenuStage = new Stage(viewportForStage);
        upgradesMenuStage = new Stage(viewportForStage);
        settingsMenuStage = new Stage(viewportForStage);
        tooltip = new Tooltip(viewportForStage, makeClickListenerThatCallsSetMenuState(MenuState.MAP));

        ClickListener hoverAndClickListener = makeHoverAndClickListener();
        map = new Map(viewportForStage, hoverAndClickListener);

        Gdx.input.setInputProcessor(mainMenuStage);
        currentMenuState = MenuState.MAIN_MENU;
        previousImportantMenuState = MenuState.MAIN_MENU;

        // Load the in-game currency counter
        Image persistentCurrencyCounterImage = new Image(new Texture(Gdx.files.internal("ITEMS/doubloon.png")));
        persistentCurrencyCounterImage.setSize(29 * SCALE_FACTOR, 30 * SCALE_FACTOR);
        mainMenuStage.addActor(persistentCurrencyCounterImage);

        // Menu buttons below
        // PLAY button
        ImageButton playButton = newImageButtonFrom("play", MenuState.START_REWARDS);
        mainMenuStage.addActor(playButton);

        // UPGRADES button
        ImageButton upgradesButton = newImageButtonFrom("upgrades", MenuState.UPGRADES);
        mainMenuStage.addActor(upgradesButton);

        // SETTINGS button
        ImageButton settingsButton = newImageButtonFrom("settings", MenuState.SETTINGS);
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


        // Dark transparent screen
        darkTransparentScreen = new Image(new Texture(Gdx.files.internal("MENU backgrounds/dark transparent screen.png")));
        darkTransparentScreen.setSize(40 * SCALE_FACTOR * 300, 40 * SCALE_FACTOR * 300);

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
        ImageButton resumeButton = newImageButtonFrom("resume", MenuState.RESUME);
        pauseMenuStage.addActor(resumeButton);

        // Settings button
        ImageButton pauseSettingsButton = newImageButtonFrom("settings", MenuState.SETTINGS);
        pauseMenuStage.addActor(pauseSettingsButton);

        // Give up button
        ImageButton giveUpButton = newImageButtonFrom("give up", MenuState.RESULTS);
        pauseMenuStage.addActor(giveUpButton);

        // Results menu buttons
        // Main menu button
        ImageButton mainMenuButton = newImageButtonFrom("main menu", MenuState.MAIN_MENU);
        resultsMenuStage.addActor(mainMenuButton);

        // Upgrades menu buttons
        // Back button
        ImageButton upgradesBackButton = newImageButtonFrom("back", MenuState.MAIN_MENU);
        upgradesMenuStage.addActor(upgradesBackButton);

        // Settings menu buttons
        // Back button
        // TODO: this will need additional code to discard the changed settings
        ImageButton settingsBackButton = newImageButtonFrom("back", MenuState.SETTINGS_BACK);
        settingsMenuStage.addActor(settingsBackButton);

        // confirm button
        // TODO: this will need additional code to save and apply the changed settings
        ImageButton settingsConfirmButton = newImageButtonFrom("confirm", MenuState.SETTINGS_BACK);
        settingsMenuStage.addActor(settingsConfirmButton);

        setMenuState(MenuState.MAIN_MENU);
    }

    public void batch(float elapsedTime, String timeText, int amountOfPersistentCurrency) {
        if (this.isDrawMapMenu) {
            map.drawMap(batch);
        }

        if (this.isDrawMainMenu) {
            mainMenuStage.getActors().get(0).setPosition(15.5f, 14); // persistentCurrencyCounterImage
            mainMenuStage.getActors().get(1).setPosition(2, 18); // playButton
            mainMenuStage.getActors().get(2).setPosition(2, 13); // upgradesButton
            mainMenuStage.getActors().get(3).setPosition(2, 8); // settingsButton
            mainMenuStage.getActors().get(4).setPosition(2, 3); // exitButton
            mainMenuStage.getViewport().apply();
            mainMenuStage.act(elapsedTime);
            mainMenuStage.draw();
            font.draw(batch, "x " + amountOfPersistentCurrency, 17.3f, 15.3f); // text for currency counter
        } else {
            font.draw(batch, timeText, 68, 45); // text for time elapsed in game
            // Draw the map stuff
            map.batch(elapsedTime);
        }

        batch.end();
        batch.begin();

        if (this.isDrawDarkTransparentScreen) {
            // draw the dark transparent screen
            darkTransparentScreen.setPosition(0, 0);
            darkTransparentScreen.draw(batch, 1);
        }

        batch.end();
        batch.begin();
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
    }

    public void resize(int width, int height) {
        mainMenuStage.getViewport().update(width, height, true);
    }

    private ClickListener makeHoverAndClickListener() {
        return new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
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
                setDrawTooltipMenu(false);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Get the MapNodeData object from the image actor that triggered the click event
                Map.MapNode.MapNodeData data = (Map.MapNode.MapNodeData) event.getTarget().getUserObject();

                // Check if the node is a valid choice
                if (map.isValidChoice(data.stageNumberOfSelf(), data.indexOfSelf())) {
                    // Mark it as completed
                    map.completeNode(data.stageNumberOfSelf(), data.indexOfSelf());
                }

            }
        };
    }

    private ClickListener makeClickListenerThatCallsSetMenuState(MenuState menuState) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenuState(menuState);
            }
        };
    }

    public void setMenuState(MenuState menuState) {
        switch (menuState) {
            case MAIN_MENU -> {
                // GAME STARTS IN THIS STATE
                currentMenuState = MenuState.MAIN_MENU;
                Gdx.input.setInputProcessor(mainMenuStage);
                MyGame.setTimeElapsedInGame(0f);
                setGameplayPaused(true);
                setDrawMainMenu(true);
                setDrawDarkTransparentScreen(false);
                setDrawResultsMenu(false);
                setDrawUpgradesMenu(false);
                setDrawSettingsMenu(false);
                setDrawMapMenu(false);
            }
            case MAP -> {
                setDrawTooltipMenu(false);
                currentMenuState = MenuState.MAP;
                Gdx.input.setInputProcessor(map.mapStage);
                previousImportantMenuState = MenuState.MAP;
                setDrawDarkTransparentScreen(false);
            }
            case UPGRADES -> {
                currentMenuState = MenuState.UPGRADES;
                Gdx.input.setInputProcessor(upgradesMenuStage);
                setDrawDarkTransparentScreen(true);
                setDrawUpgradesMenu(true);
            }
            case SETTINGS -> {
                Gdx.input.setInputProcessor(settingsMenuStage);
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(false);
                setDrawSettingsMenu(true);
            }
            case SETTINGS_BACK -> // exclusive to the buttons in the settings menu
                    setMenuState(currentMenuState);
            case PAUSED -> {
                currentMenuState = MenuState.PAUSED;
                Gdx.input.setInputProcessor(pauseMenuStage);
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(true);
                setDrawSettingsMenu(false);
            }
            case RESUME -> setMenuState(previousImportantMenuState);
            case RESULTS -> {
                currentMenuState = MenuState.RESULTS;
                Gdx.input.setInputProcessor(resultsMenuStage);
                setGameplayPaused(true);
                setDrawDarkTransparentScreen(true);
                setDrawPauseMenu(false);
                setDrawResultsMenu(true);
                setDrawTooltipMenu(false);
            }
            case START_REWARDS -> {
                tooltip.artifactReward();
                currentMenuState = MenuState.START_REWARDS;
                Gdx.input.setInputProcessor(tooltip.tooltipStage);
                setDrawTooltipMenu(true);
                setDrawDarkTransparentScreen(true);
                setGameplayPaused(false);
                setDrawMainMenu(false);
                setDrawPauseMenu(false);
                setDrawMapMenu(true);
            }
            case EVENT -> {
            }
            case STAGE_RESULTS -> {
            }
            case COMBAT -> {
            }

        }
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

    private ImageButton newImageButtonFrom(String buttonInternalFolderName, MenuState menuState) {
        Texture notClickedTexture = new Texture(Gdx.files.internal("MENU BUTTONS/" + buttonInternalFolderName + "/default.png"));
        Texture clickedTexture = new Texture(Gdx.files.internal("MENU BUTTONS/" + buttonInternalFolderName + "/hover.png"));
        ImageButton button = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(notClickedTexture)),
                new TextureRegionDrawable(new TextureRegion(clickedTexture))
        );
        button.setSize(notClickedTexture.getWidth() * SCALE_FACTOR, notClickedTexture.getHeight() * SCALE_FACTOR);
        button.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setMenuState(menuState);
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
}
