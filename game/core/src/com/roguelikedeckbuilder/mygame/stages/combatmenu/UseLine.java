package com.roguelikedeckbuilder.mygame.stages.combatmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;


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
        isVisible = false;
        mainColor = Color.ORANGE;
    }

    public static void setPosition(XYPair<Float> startCoords, XYPair<Float> endCoords) {
        start = new XYPair<>(startCoords.x(), startCoords.y());
        end = new XYPair<>(endCoords.x(), endCoords.y());
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
