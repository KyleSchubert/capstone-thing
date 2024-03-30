package com.roguelikedeckbuilder.mygame.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
                case BELLFLOWER:
                    this.internalName = "bellflower";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(54, 117);
                    this.dimensions = new XYPair<>(108, 146);
                    break;
                case BIG_BOAR:
                    this.internalName = "big boar";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(269, 202);
                    this.dimensions = new XYPair<>(538, 309);
                    break;
                case BIG_SLIME:
                    this.internalName = "big slime";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.080f, 0.080f, 0.080f, 0.130f, 0.130f, 0.130f, 0.140f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.200f, 0.050f, 0.150f, 0.150f, 0.100f, 0.100f, 0.300f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.300f, 0.250f));
                    this.origin = new XYPair<>(277, 679);
                    this.dimensions = new XYPair<>(554, 1119);
                    break;
                case BIG_SNAIL:
                    this.internalName = "big snail";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.450f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    this.origin = new XYPair<>(109, 216);
                    this.dimensions = new XYPair<>(218, 330);
                    break;
                case BUFF_PIG:
                    this.internalName = "buff pig";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f));
                    this.origin = new XYPair<>(444, 428);
                    this.dimensions = new XYPair<>(888, 647);
                    break;
                case DRAGON:
                    this.internalName = "dragon";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.110f, 0.110f, 0.110f, 0.110f, 0.110f, 0.110f, 0.110f, 0.110f, 0.110f, 0.110f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.130f, 0.130f, 0.130f, 0.130f, 0.130f, 0.130f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.130f, 0.130f, 0.130f, 0.130f, 0.130f, 0.130f, 0.130f, 0.130f));
                    this.origin = new XYPair<>(431, 876);
                    this.dimensions = new XYPair<>(862, 1252);
                    break;
                case DYLE:
                    this.internalName = "dyle";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.210f, 0.210f, 0.210f, 0.210f, 0.210f, 0.210f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    this.origin = new XYPair<>(199, 294);
                    this.dimensions = new XYPair<>(398, 436);
                    break;
                case FANCY_BIRD:
                    this.internalName = "fancy bird";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f));
                    this.origin = new XYPair<>(278, 172);
                    this.dimensions = new XYPair<>(556, 260);
                    break;
                case FIRE_SPIRIT:
                    this.internalName = "fire spirit";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(380, 408);
                    this.dimensions = new XYPair<>(760, 667);
                    break;
                case GOLEM:
                    this.internalName = "golem";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.250f, 0.250f, 0.250f, 0.250f, 0.250f, 0.250f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.350f, 0.350f, 0.350f, 0.350f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.300f, 0.150f, 0.300f));
                    this.origin = new XYPair<>(216, 282);
                    this.dimensions = new XYPair<>(432, 408);
                    break;
                case KING_PENGUIN:
                    this.internalName = "king penguin";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(712, 586);
                    this.dimensions = new XYPair<>(1424, 861);
                    break;
                case LICH:
                    this.internalName = "lich";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.200f, 0.200f, 0.200f, 0.200f, 0.200f, 0.200f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.200f, 0.200f, 0.200f, 0.200f, 0.200f, 0.200f));
                    this.origin = new XYPair<>(321, 376);
                    this.dimensions = new XYPair<>(642, 607);
                    break;
                case LIVING_MONEY:
                    this.internalName = "living money";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.200f, 0.200f, 0.200f, 0.200f, 0.200f, 0.200f, 0.200f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.250f, 0.250f, 0.250f, 0.250f, 0.250f, 0.250f, 0.250f, 0.250f)); // last 4 were 999???
                    this.origin = new XYPair<>(114, 174);
                    this.dimensions = new XYPair<>(228, 301);
                    break;
                case MOUSE:
                    this.internalName = "mouse";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(94, 60);
                    this.dimensions = new XYPair<>(188, 67);
                    break;
                case ROBOT:
                    this.internalName = "robot";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(1439, 499);
                    this.dimensions = new XYPair<>(2878, 914);
                    break;
                case ROCK:
                    this.internalName = "rock";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f));
                    this.origin = new XYPair<>(130, 217);
                    this.dimensions = new XYPair<>(260, 313);
                    break;
                case SEAL:
                    this.internalName = "seal";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(172, 193);
                    this.dimensions = new XYPair<>(344, 311);
                    break;
                case SKELETON:
                    this.internalName = "skeleton";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f));
                    this.origin = new XYPair<>(200, 305);
                    this.dimensions = new XYPair<>(400, 505);
                    break;
                case SQUID:
                    this.internalName = "squid";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f));
                    this.origin = new XYPair<>(296, 170);
                    this.dimensions = new XYPair<>(592, 245);
                    break;
                case THUNDER_SPIRIT:
                    this.internalName = "thunder spirit";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(232, 532);
                    this.dimensions = new XYPair<>(464, 900);
                    break;
                case TOY_BEAR:
                    this.internalName = "toy bear";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.130f, 0.130f, 0.130f, 0.130f, 0.130f, 0.130f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f, 0.100f));
                    this.origin = new XYPair<>(62, 88);
                    this.dimensions = new XYPair<>(124, 127);
                    break;
                case TRAINING_DUMMY:
                    this.internalName = "training dummy";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.090f, 0.090f, 0.090f, 0.090f, 0.090f, 0.090f));
                    this.origin = new XYPair<>(65, 100);
                    this.dimensions = new XYPair<>(130, 113);
                    break;
                case TURNIP:
                    this.internalName = "turnip";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.150f, 0.150f, 0.150f, 0.150f));
                    this.origin = new XYPair<>(85, 139);
                    this.dimensions = new XYPair<>(170, 203);
                    break;
                case WALRUS:
                    this.internalName = "walrus";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.120f, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.120f, 0.120f, 0.120f, 0.120f));
                    this.origin = new XYPair<>(172, 210);
                    this.dimensions = new XYPair<>(344, 317);
                    break;
                case WARRIOR:
                    this.internalName = "warrior";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f, 0.180f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f));
                    standingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.180f, 0.180f, 0.180f, 0.180f));
                    this.origin = new XYPair<>(465, 486);
                    this.dimensions = new XYPair<>(930, 781);
                    break;
                case WRAITH:
                    this.internalName = "wraith";
                    dyingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.130f, 0.130f, 0.130f, 0.130f, 0.130f, 0.300f));
                    movingAnimationFrameDelays = new ArrayList<>(Arrays.asList(0.150f, 0.150f, 0.100f, 0.150f));
                    standingAnimationFrameDelays = new ArrayList<>(List.of(0.999f));
                    this.origin = new XYPair<>(81, 149);
                    this.dimensions = new XYPair<>(162, 160);
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

        private enum FileNameByType {
            STANDING, DYING, MOVING
        }
    }
}


