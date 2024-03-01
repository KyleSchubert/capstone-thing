package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class UseLine {
    private static XYPair<Float> start;
    private static XYPair<Float> end;
    private static ShapeRenderer shapeRenderer;
    private static boolean isVisible;
    private static Color mainColor;

    public static void initialize() {
        start = new XYPair<>(0f, 0f);
        end = new XYPair<>(0f, 0f);
        shapeRenderer = new ShapeRenderer();
        isVisible = true;
        mainColor = Color.ORANGE;
    }

    public static void setPosition(XYPair<Float> startCoords, XYPair<Float> endCoords) {
        start = new XYPair<>(startCoords.x() / SCALE_FACTOR, startCoords.y() / SCALE_FACTOR);
        end = new XYPair<>(endCoords.x() / SCALE_FACTOR, endCoords.y() / SCALE_FACTOR);
    }

    public static void setVisibility(boolean visibility) {
        isVisible = visibility;
    }

    public static boolean isVisible() {
        return isVisible;
    }

    public static void setMainColor(Color color) {
        mainColor = color;
    }

    public static void draw() {
        // Must be called between batch.begin() and batch.end()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


        // Outline 1
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rectLine(start.x(), start.y(), end.x(), end.y(), 13);

        // Outline 2
        shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1);
        shapeRenderer.rectLine(start.x(), start.y(), end.x(), end.y(), 11);

        // Outline 3
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rectLine(start.x(), start.y(), end.x(), end.y(), 9);

        // Inner line
        shapeRenderer.setColor(mainColor);
        shapeRenderer.rectLine(start.x(), start.y(), end.x(), end.y(), 7);

        shapeRenderer.end();
    }

    public static void dispose() {
        shapeRenderer.dispose();
    }
}
