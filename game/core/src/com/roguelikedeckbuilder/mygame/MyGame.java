package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.animated.VisualEffectData;
import com.roguelikedeckbuilder.mygame.cards.CardData;
import com.roguelikedeckbuilder.mygame.animated.CharacterData;
import com.roguelikedeckbuilder.mygame.combat.*;
import com.roguelikedeckbuilder.mygame.helpers.*;
import com.roguelikedeckbuilder.mygame.items.ItemData;
import com.roguelikedeckbuilder.mygame.tracking.Statistics;
import com.roguelikedeckbuilder.mygame.tracking.TriggerData;

public class MyGame extends ApplicationAdapter {
    public static final float SCALE_FACTOR = 0.05f;
    public static final float STEP_TIME = 1f / 60f;
    private static final float windowWidth = 1440;
    private static final float windowHeight = 920;
    private static final float viewWidth = windowWidth * SCALE_FACTOR;
    private static final float viewHeight = windowHeight * SCALE_FACTOR;
    static SpriteBatch batch;
    static BitmapFont font;
    static ScreenViewport viewport;
    static OrthographicCamera camera;
    MenuController menuController;
    float accumulator = 0;
    private String timeText = "0:00";
    private boolean isSomeDebugOn = false;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(viewWidth, viewHeight);

        viewport = new ScreenViewport(camera);
        viewport.setUnitsPerPixel(SCALE_FACTOR);

        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setUseIntegerPositions(false);
        font.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);

        VisualEffectData.initialize();
        CharacterData.initialize();
        EnemyData.initialize();
        EffectData.initialize();
        AbilityData.initialize();
        CardData.initialize();
        LabelMaker.initialize();
        UseLine.initialize();
        TriggerData.initialize();
        ItemData.initialize();
        BuffOrDebuffData.initialize();
        SoundManager.initialize();
        Player.initialize();
        Statistics.resetVariables();

        menuController = new MenuController();
        menuController.create(camera);

        Player.referenceMenuController(menuController);
        ClickListenerManager.initialize(menuController);
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
        UseLine.dispose();
    }

    private float stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            if (Player.getCombatInformation().getHp() == 0 && menuController.getCurrentMenuState() != MenuController.MenuState.RESULTS) {
                menuController.setMenuState(MenuController.MenuState.RESULTS);
            }
            if (!MenuController.getIsGameplayPaused() || menuController.getCurrentMenuState() == MenuController.MenuState.MAIN_MENU) {
                float currentTime = Statistics.getSecondsIntoRun();
                Statistics.setSecondsIntoRun(currentTime + STEP_TIME);
                int hours = (int) currentTime / 3600;
                int minutes = (int) (currentTime % 3600) / 60;
                int seconds = (int) currentTime % 60;
                if (currentTime > 3600) {
                    timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                } else {
                    timeText = String.format("%02d:%02d", minutes, seconds);
                }

                DelayScheduler.changeAllDelays(-STEP_TIME);
            }

            // Check for ESCAPE key -- Toggle pause menu
            if (menuController.getCurrentMenuState() == MenuController.MenuState.MAP || menuController.getCurrentMenuState() == MenuController.MenuState.COMBAT) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    SoundManager.playMenuOpenSound();
                    menuController.setMenuState(MenuController.MenuState.PAUSED);
                }
            } else if (menuController.getCurrentMenuState() == MenuController.MenuState.PAUSED) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    SoundManager.playMenuCloseSound();
                    menuController.setMenuState(MenuController.MenuState.RESUME);
                }
            } else if (menuController.getCurrentMenuState() == MenuController.MenuState.MAIN_MENU) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                    isSomeDebugOn = !isSomeDebugOn;
                    if (isSomeDebugOn) {
                        System.out.println("DEBUG: ON");
                        System.out.println("- Q : Exit Combat");
                        System.out.println("- 1 : Shop");
                        System.out.println("- 2 : Treasure");
                        System.out.println("- 3 : Combat");
                        System.out.println("- 4 : Rest");
                        System.out.println("- 5 : Results");
                        System.out.println("- 6 : Give random item, if on the Map, Rest Area, Treasure, or Shop");
                        System.out.println("- 7 : +1000 persistent coins");
                        System.out.println("- 8 : +1000 coins");
                        System.out.println("- 9 : Full heal");
                        System.out.println("- L : Print all Statistics");
                        SoundManager.playHealSound();
                    } else {
                        System.out.println("DEBUG: OFF");
                        SoundManager.playDefendSound();
                    }
                }
            }
            if (isSomeDebugOn) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    menuController.setMenuState(MenuController.MenuState.RESULTS);
                    menuController.setMenuState(MenuController.MenuState.MAIN_MENU);
                    menuController.setMenuState(MenuController.MenuState.START_REWARDS);
                    menuController.setMenuState(MenuController.MenuState.MAP);
                    menuController.getShopMenuStage().generateShop();
                    menuController.setMenuState(MenuController.MenuState.SHOP);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    menuController.setMenuState(MenuController.MenuState.RESULTS);
                    menuController.setMenuState(MenuController.MenuState.MAIN_MENU);
                    menuController.setMenuState(MenuController.MenuState.START_REWARDS);
                    menuController.setMenuState(MenuController.MenuState.MAP);
                    menuController.getTreasureMenuStage().aLotOfTreasure();
                    menuController.setMenuState(MenuController.MenuState.TREASURE);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                    menuController.setMenuState(MenuController.MenuState.RESULTS);
                    menuController.setMenuState(MenuController.MenuState.MAIN_MENU);
                    menuController.setMenuState(MenuController.MenuState.START_REWARDS);
                    menuController.setMenuState(MenuController.MenuState.MAP);
                    menuController.setMenuState(MenuController.MenuState.REST_AREA);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                    menuController.setMenuState(MenuController.MenuState.RESULTS);
                    menuController.setMenuState(MenuController.MenuState.MAIN_MENU);
                    menuController.setMenuState(MenuController.MenuState.START_REWARDS);
                    menuController.setMenuState(MenuController.MenuState.MAP);
                    menuController.setMenuState(MenuController.MenuState.COMBAT);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                    menuController.setMenuState(MenuController.MenuState.RESULTS);
                    menuController.setMenuState(MenuController.MenuState.MAIN_MENU);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
                    if (menuController.getCurrentMenuState() == MenuController.MenuState.MAP ||
                            menuController.getCurrentMenuState() == MenuController.MenuState.REST_AREA ||
                            menuController.getCurrentMenuState() == MenuController.MenuState.TREASURE ||
                            menuController.getCurrentMenuState() == MenuController.MenuState.SHOP) {
                        Player.obtainItem(ItemData.getSomeRandomItemNamesByTier(ItemData.ItemTier.ANY, 1, false).get(0));
                    }
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
                    Player.changePersistentMoney(1000);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
                    Player.changeMoney(1000);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
                    Player.getCombatInformation().changeHp(Player.getCombatInformation().getMaxHp());
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    if (menuController.getCurrentMenuState() == MenuController.MenuState.COMBAT) {
                        Statistics.combatEnded();
                        menuController.setMenuState(MenuController.MenuState.MAP);
                        SoundManager.playMenuCloseSound();
                    }
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    SoundManager.playFunnyTadaSound();
                    Statistics.printAll();
                }
            }

            return STEP_TIME;
        } else {
            return 0;
        }
    }

    public static XYPair<Float> getMousePosition() {
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        Vector3 coordinates = new Vector3(x, y, 0);
        camera.unproject(coordinates);

        return new XYPair<>(coordinates.x, coordinates.y);
    }
}
