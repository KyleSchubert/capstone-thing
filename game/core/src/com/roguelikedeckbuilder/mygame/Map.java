package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Map {
    public enum MapNodeType {
        NORMAL_BATTLE, ELITE_BATTLE, BOSS_BATTLE, START, SHOP, REST, RANDOM_EVENT, TREASURE
    }

    private final Stage mapStage;
    private final Image mapBackground;
    private final Array<MapNode> mapNodes;

    public Map(ScreenViewport viewportForStage) {
        mapStage = new Stage(viewportForStage);
        mapBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/map background.png")));
        mapBackground.setSize(1442 * SCALE_FACTOR, 952 * SCALE_FACTOR);
        mapNodes = new Array<>();
    }

    public void batch(float elapsedTime) {
        for (int i = 0; i < mapStage.getActors().size; i++) {
            mapStage.getActors().get(i).setPosition(mapNodes.get(i).getPosX(), mapNodes.get(i).getPosY());
        }
        System.out.println(mapStage.getActors().size);
        mapStage.getViewport().apply();
        mapStage.act(elapsedTime);
        mapStage.draw();
    }

    public void generateMap() {
        for (int i = 0; i < 5000; i++) {
            MapNode mapNode = new MapNode();
            mapStage.addActor(mapNode.tempGetImage());
            mapNodes.add(mapNode);
        }
    }

    public void drawMap(SpriteBatch batch) {
        mapBackground.setPosition(0, -1);
        mapBackground.draw(batch, 1);
    }

    private class MapNode {
        private final Image nodeImage;
        private final float posX;
        private final float posY;
        private static final int POS_X_MIN = 8;
        private static final int POS_X_MAX = 68;
        private static final int POS_Y_MIN = 8;
        private static final int POS_Y_MAX = 38;

        public MapNode() {
            int randomNumber = random.nextInt(MapNodeType.values().length);
            MapNodeType nodeType = MapNodeType.values()[randomNumber];

            String filePath = "MAP NODES/";
            int x, y;

            switch (nodeType) {
                case NORMAL_BATTLE:
                    filePath += "normal battle.png";
                    x = 26;
                    y = 26;
                    break;
                case ELITE_BATTLE:
                    filePath += "elite battle.png";
                    x = 36;
                    y = 32;
                    break;
                case BOSS_BATTLE:
                    filePath += "boss battle.png";
                    x = 36;
                    y = 33;
                    break;
                case START:
                    filePath += "start.png";
                    x = 33;
                    y = 28;
                    break;
                case SHOP:
                    filePath += "shop.png";
                    x = 32;
                    y = 33;
                    break;
                case REST:
                    filePath += "rest.png";
                    x = 34;
                    y = 26;
                    break;
                case RANDOM_EVENT:
                    filePath += "random event.png";
                    x = 33;
                    y = 32;
                    break;
                case TREASURE:
                    filePath += "treasure.png";
                    x = 29;
                    y = 31;
                    break;
                default:
                    filePath += "default.png";
                    x = 29;
                    y = 31;
                    break;
            }
            nodeImage = new Image(new Texture(Gdx.files.internal(filePath)));
            nodeImage.setSize(x * SCALE_FACTOR, y * SCALE_FACTOR);

            posX = random.nextInt(POS_X_MAX - POS_X_MIN) + POS_X_MIN;
            posY = random.nextInt(POS_Y_MAX - POS_Y_MIN) + POS_Y_MIN;
        }

        public Image tempGetImage() {
            return nodeImage;
        }

        public float getPosX() {
            return posX;
        }

        public float getPosY() {
            return posY;
        }
    }
}
