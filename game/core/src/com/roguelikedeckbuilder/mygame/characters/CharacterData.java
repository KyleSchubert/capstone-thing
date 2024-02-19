package com.roguelikedeckbuilder.mygame.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class CharacterData {
    private static Array<IndividualCharacterData> data;

    public static void initialize() {
        data = new Array<>();
        for (Character.CharacterTypeName name : Character.CharacterTypeName.values()) {
            data.add(new IndividualCharacterData(name));
        }
    }

    public static XYPair<Integer> getOrigin(Character.CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getOrigin();
    }

    public static XYPair<Integer> getDimensions(Character.CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getDimensions();
    }

    public static TextureRegion[] getAllAnimationFrames(Character.CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getAllAnimationFrames();
    }

    public static ArrayList<Float> getAllAnimationFrameDelays(Character.CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getAllAnimationFrameDelays();
    }

    public static int getStartFrameIndex(Character.CharacterTypeName typeName, Character.CharacterState state) {
        switch (state) {
            case DYING -> {
                return data.get(typeName.ordinal()).getDyingAnimationStartFrameIndex();
            }
            case MOVING -> {
                return data.get(typeName.ordinal()).getMovingAnimationStartFrameIndex();
            }
            case STANDING -> {
                return data.get(typeName.ordinal()).getStandingAnimationStartFrameIndex();
            }
            default -> {
                // Return the dying one by default
                System.out.println("[Huh?] Why was getStartFrameIndex() called with " + typeName + " and " + state + "?");
                return data.get(typeName.ordinal()).getDyingAnimationStartFrameIndex();
            }
        }
    }

    public static int getEndFrameIndex(Character.CharacterTypeName typeName, Character.CharacterState state) {
        switch (state) {
            case DYING -> {
                return data.get(typeName.ordinal()).getDyingAnimationEndFrameIndex();
            }
            case MOVING -> {
                return data.get(typeName.ordinal()).getMovingAnimationEndFrameIndex();
            }
            case STANDING -> {
                return data.get(typeName.ordinal()).getStandingAnimationEndFrameIndex();
            }
            default -> {
                // Return the dying one by default
                System.out.println("[Huh?] Why was getEndFrameIndex() called with " + typeName + " and " + state + "?");
                return data.get(typeName.ordinal()).getDyingAnimationEndFrameIndex();
            }
        }
    }

    public String getInternalName(Character.CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getInternalName();
    }


    private static class IndividualCharacterData {
        private final ArrayList<Float> allAnimationFrameDelays = new ArrayList<>();
        private TextureRegion[] allAnimationFrames;
        private XYPair<Integer> origin;
        private XYPair<Integer> dimensions;
        private String internalName = "";
        private int dyingAnimationStartFrameIndex;
        private int dyingAnimationEndFrameIndex;
        private int movingAnimationStartFrameIndex;
        private int movingAnimationEndFrameIndex;
        private int standingAnimationStartFrameIndex;
        private int standingEndFrameIndex;

        public IndividualCharacterData(Character.CharacterTypeName characterTypeName) {
            ArrayList<Float> dyingAnimationFrameDelays;
            ArrayList<Float> movingAnimationFrameDelays;
            ArrayList<Float> standingAnimationFrameDelays;

            switch (characterTypeName) {
                case BIRD:
                    this.internalName = "bird";
                    dyingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(13, 0.120f));
                    movingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(4, 0.210f));
                    this.origin = new XYPair<>(94, 62);
                    this.dimensions = new XYPair<>(188, 86);
                    break;
                case PLANT:
                    this.internalName = "plant";
                    dyingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(7, 0.120f));
                    movingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.090f));
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.120f));
                    this.origin = new XYPair<>(126, 103);
                    this.dimensions = new XYPair<>(252, 134);
                    break;
                case STUMP:
                    this.internalName = "stump";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(4, 0.180f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.100f, 0.200f, 0.100f, 0.100f));
                    this.origin = new XYPair<>(63, 130);
                    this.dimensions = new XYPair<>(126, 167);
                    break;
                case PIG:
                    this.internalName = "pig";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.100f, 0.100f, 0.300f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.150f, 0.180f));
                    this.origin = new XYPair<>(48, 74);
                    this.dimensions = new XYPair<>(96, 108);
                    break;
                case ORANGE_MUSHROOM:
                    this.internalName = "orange mushroom";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.120f, 0.180f));
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(2, 0.180f));
                    this.origin = new XYPair<>(42, 88);
                    this.dimensions = new XYPair<>(81, 121);
                    break;
                case BLUE_MUSHROOM:
                    this.internalName = "blue mushroom";
                    dyingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(5, 0.180f));
                    movingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(4, 0.180f));
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(2, 0.180f));
                    this.origin = new XYPair<>(121, 126);
                    this.dimensions = new XYPair<>(242, 194);
                    break;
                case HELMET_PENGUIN:
                    this.internalName = "helmet penguin";
                    dyingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(10, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(8, 0.100f));
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.180f));
                    this.origin = new XYPair<>(244, 194);
                    this.dimensions = new XYPair<>(488, 292);
                    break;
                default:
                    System.out.println("Why was a character almost generated with no matching type name? characterTypeName:  " + characterTypeName);
                    return;
            }

            int totalFrameCount = dyingAnimationFrameDelays.size()
                    + movingAnimationFrameDelays.size()
                    + standingAnimationFrameDelays.size();

            // ---- Prepare All Animation Frames ----
            TextureRegion[] animationFrames = new TextureRegion[totalFrameCount];
            int nextIndex = 0;

            // DYING FRAMES
            nextIndex = addFrames(animationFrames, nextIndex, FileNameByType.DYING, dyingAnimationFrameDelays);

            // MOVING FRAMES
            nextIndex = addFrames(animationFrames, nextIndex, FileNameByType.MOVING, movingAnimationFrameDelays);

            // STANDING FRAMES
            addFrames(animationFrames, nextIndex, FileNameByType.STANDING, standingAnimationFrameDelays);

            // DONE
            this.allAnimationFrames = animationFrames;
        }

        private int addFrames(TextureRegion[] animationFrames, int nextIndex, FileNameByType fileName, ArrayList<Float> delays) {
            String internalPath = "characters/" + internalName;

            int startIndex = nextIndex;
            int endIndex = nextIndex + delays.size() - 1;

            switch (fileName) {
                case STANDING -> {
                    internalPath += "/stand.png";
                    this.standingAnimationStartFrameIndex = startIndex;
                    this.standingEndFrameIndex = endIndex;
                }
                case DYING -> {
                    internalPath += "/die.png";
                    this.dyingAnimationStartFrameIndex = startIndex;
                    this.dyingAnimationEndFrameIndex = endIndex;
                }
                case MOVING -> {
                    internalPath += "/move.png";
                    this.movingAnimationStartFrameIndex = startIndex;
                    this.movingAnimationEndFrameIndex = endIndex;
                }
                default -> throw new IllegalStateException("Unexpected value: " + fileName);
            }

            TextureRegion[][] frames = TextureRegion.split(new Texture(internalPath), this.dimensions.x(), this.dimensions.y());

            for (int i = 0; i < delays.size(); i++) {
                animationFrames[nextIndex] = frames[0][i];
                allAnimationFrameDelays.add(delays.get(i));
                nextIndex++;
            }
            return nextIndex;
        }

        public TextureRegion[] getAllAnimationFrames() {
            return allAnimationFrames;
        }

        public ArrayList<Float> getAllAnimationFrameDelays() {
            return allAnimationFrameDelays;
        }

        public XYPair<Integer> getOrigin() {
            return origin;
        }

        public XYPair<Integer> getDimensions() {
            return dimensions;
        }

        public int getDyingAnimationStartFrameIndex() {
            return dyingAnimationStartFrameIndex;
        }

        public int getDyingAnimationEndFrameIndex() {
            return dyingAnimationEndFrameIndex;
        }

        public int getMovingAnimationStartFrameIndex() {
            return movingAnimationStartFrameIndex;
        }

        public int getMovingAnimationEndFrameIndex() {
            return movingAnimationEndFrameIndex;
        }

        public int getStandingAnimationStartFrameIndex() {
            return standingAnimationStartFrameIndex;
        }

        public int getStandingAnimationEndFrameIndex() {
            return standingEndFrameIndex;
        }

        public String getInternalName() {
            return internalName;
        }

        private enum FileNameByType {
            STANDING, DYING, MOVING
        }
    }
}


