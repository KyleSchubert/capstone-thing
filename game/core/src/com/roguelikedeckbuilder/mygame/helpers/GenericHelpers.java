package com.roguelikedeckbuilder.mygame.helpers;

public class GenericHelpers {
    public static boolean isPointWithinRange(XYPair<Float> point, XYPair<Float> positionOnStage) {
        float width = 11;
        float height = 20;
        float heightBottomOffset = 6;

        float left = positionOnStage.x() - width / 2;
        float right = positionOnStage.x() + width / 2;

        float bottom = positionOnStage.y() - height / 2 + heightBottomOffset;
        float top = positionOnStage.y() + height / 2 + heightBottomOffset;

        return (point.x() < right
                && point.x() > left
                && point.y() < top
                && point.y() > bottom);
    }
}
