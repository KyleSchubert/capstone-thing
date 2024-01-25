package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyGame extends ApplicationAdapter {
    public static final float SCALE_FACTOR = 0.05f;
    SpriteBatch batch;
    ScreenViewport viewport;
    OrthographicCamera camera;
    ScreenViewport viewportForStage;
    Stage mainMenuStage;
    Stage pauseMenuStage;
    Stage resultsMenuStage;
    Stage levelUpMenuStage;
    Stage upgradesMenuStage;
    Stage settingsMenuStage;
    BitmapFont font;
    ImageButton playButton;
    ImageButton upgradesButton;
    ImageButton settingsButton;
    ImageButton exitButton;
    ImageButton resumeButton;
    ImageButton giveUpButton;
    ImageButton pauseSettingsButton;
    ImageButton mainMenuButton;
    ImageButton levelUpConfirmButton1;
    ImageButton levelUpConfirmButton2;
    ImageButton levelUpConfirmButton3;
    ImageButton upgradesBackButton;
    ImageButton resetButton;
    ImageButton settingsBackButton;
    ImageButton settingsConfirmButton;

    public enum MenuState {
        MAIN_MENU, PLAYING, PAUSED, RESULTS, LEVEL_UP, UPGRADES, SETTINGS_BACK, SETTINGS
    }

    private boolean isDrawMainMenu;
    private boolean isGameplayPaused;
    private static final float windowWidth = 1440;
    private static final float windowHeight = 920;
    private static final float viewWidth = windowWidth * SCALE_FACTOR;
    private static final float viewHeight = windowHeight * SCALE_FACTOR;
    public float timeElapsedInGame = 0.0f;
    private String timeText = "0:00";

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(viewWidth, viewHeight);

        viewport = new ScreenViewport(camera);
        viewport.setUnitsPerPixel(SCALE_FACTOR);

        // For the UI and menus
        viewportForStage = new ScreenViewport(camera);
        viewportForStage.setUnitsPerPixel(SCALE_FACTOR);
        mainMenuStage = new Stage(viewportForStage);
        pauseMenuStage = new Stage(viewportForStage);
        resultsMenuStage = new Stage(viewportForStage);
        levelUpMenuStage = new Stage(viewportForStage);
        upgradesMenuStage = new Stage(viewportForStage);
        settingsMenuStage = new Stage(viewportForStage);

        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setUseIntegerPositions(false);
        font.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);

        Gdx.input.setInputProcessor(mainMenuStage);

        // Menu buttons below
        // PLAY button
        playButton = newImageButtonFrom("play", MenuState.PLAYING);
        mainMenuStage.addActor(playButton);

        // UPGRADES button
        upgradesButton = newImageButtonFrom("upgrades", MenuState.UPGRADES);
        mainMenuStage.addActor(upgradesButton);

        // SETTINGS button
        settingsButton = newImageButtonFrom("settings", MenuState.SETTINGS);
        mainMenuStage.addActor(settingsButton);

        // EXIT button
        exitButton = new ImageButton(
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

        // Pause menu buttons
        // Resume button
        resumeButton = newImageButtonFrom("resume", MenuState.PLAYING);
        pauseMenuStage.addActor(resumeButton);

        // Settings button
        pauseSettingsButton = newImageButtonFrom("settings", MenuState.SETTINGS);
        pauseMenuStage.addActor(pauseSettingsButton);

        // Give up button
        giveUpButton = newImageButtonFrom("give up", MenuState.RESULTS);
        pauseMenuStage.addActor(giveUpButton);

        // Results menu buttons
        // Main menu button
        mainMenuButton = newImageButtonFrom("main menu", MenuState.MAIN_MENU);
        resultsMenuStage.addActor(mainMenuButton);

        setMenuState(MenuState.MAIN_MENU);
    }


    @Override
    public void render() {
        float elapsedTime = stepWorld();
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        viewport.apply();


        batch.begin();

        if (this.isDrawMainMenu) {
            mainMenuStage.getActors().get(0).setPosition(2, 18); // playButton
            mainMenuStage.getActors().get(1).setPosition(2, 13); // upgradesButton
            mainMenuStage.getActors().get(2).setPosition(2, 8); // settingsButton
            mainMenuStage.getActors().get(3).setPosition(2, 3); // exitButton
            mainMenuStage.getViewport().apply();
            mainMenuStage.act(elapsedTime);
            mainMenuStage.draw();
        }
        font.draw(batch, timeText, 68, 45); // text for time elapsed in game

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public static final float STEP_TIME = 1f / 60f;
    float accumulator = 0;

    private float stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            if (!this.isGameplayPaused) {
                timeElapsedInGame += STEP_TIME;
                int minutes = (int) timeElapsedInGame / 60;
                int seconds = (int) timeElapsedInGame - minutes * 60;
                if (seconds < 10) {
                    timeText = minutes + ":0" + seconds;
                } else {
                    timeText = minutes + ":" + seconds;
                }
            }
            return STEP_TIME;
        } else {
            return 0;
        }
    }

    public void setMenuState(MenuState menuState) {
        switch (menuState) {
            case MAIN_MENU:
                this.setDrawMainMenu(true);
                this.setGameplayPaused(false);
                break;
        }
    }

    public void setDrawMainMenu(boolean drawMainMenu) {
        this.isDrawMainMenu = drawMainMenu;
    }

    public void setGameplayPaused(boolean gameplayPaused) {
        this.isGameplayPaused = gameplayPaused;
    }

    private ImageButton newImageButtonFrom(String buttonInternalFolderName, final MenuState menuState) {
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
}
