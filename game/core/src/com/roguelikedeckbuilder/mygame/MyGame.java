package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.characters.CharacterData;

public class MyGame extends ApplicationAdapter {
    public static final float SCALE_FACTOR = 0.05f;
    static SpriteBatch batch;
    ScreenViewport viewport;
    OrthographicCamera camera;
    MenuController menuController;
    static BitmapFont font;
    private static final float windowWidth = 1440;
    private static final float windowHeight = 920;
    private static final float viewWidth = windowWidth * SCALE_FACTOR;
    private static final float viewHeight = windowHeight * SCALE_FACTOR;
    public static float timeElapsedInGame = 0.0f;
    private String timeText = "0:00";

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(viewWidth, viewHeight);

        viewport = new ScreenViewport(camera);
        viewport.setUnitsPerPixel(SCALE_FACTOR);

        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setUseIntegerPositions(false);
        font.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);
        
        CharacterData.initialize();
        Player.initialize();

        menuController = new MenuController();
        menuController.create(camera);
    }


    @Override
    public void render() {
        float elapsedTime = stepWorld();
        ScreenUtils.clear(0.24f, 0.35f, 0.42f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        viewport.apply();

        batch.begin();

        menuController.batch(elapsedTime, timeText);
        Player.animateCharacter(batch, elapsedTime);

        batch.end();

        menuController.postBatch(elapsedTime);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        menuController.resize(width, height);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        batch.dispose();
        menuController.dispose();
        font.dispose();
    }

    public static final float STEP_TIME = 1f / 60f;
    float accumulator = 0;

    private float stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            if (Player.getHp() == 0) {
                menuController.setMenuState(MenuController.MenuState.RESULTS);
            }
            if (!menuController.isGameplayPaused) {
                timeElapsedInGame += STEP_TIME;
                int minutes = (int) timeElapsedInGame / 60;
                int seconds = (int) timeElapsedInGame - minutes * 60;
                if (seconds < 10) {
                    timeText = minutes + ":0" + seconds;
                } else {
                    timeText = minutes + ":" + seconds;
                }
            }
            // Check for ESCAPE key -- Toggle pause menu
            if (menuController.getCurrentMenuState() == MenuController.MenuState.MAP || menuController.getCurrentMenuState() == MenuController.MenuState.COMBAT) {
                if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                    menuController.setMenuState(MenuController.MenuState.PAUSED);
                }
            }

            return STEP_TIME;
        } else {
            return 0;
        }
    }

    public static void setTimeElapsedInGame(float newTime) {
        timeElapsedInGame = newTime;
    }
}
