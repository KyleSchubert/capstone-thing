package com.roguelikedeckbuilder.mygame.animated.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import java.util.ArrayList;
import java.util.Collections;


public class CharacterData {
    private static Array<IndividualCharacterData> data;

    public static void initialize() {
        data = new Array<>();
        for (CharacterTypeName name : CharacterTypeName.values()) {
            data.add(new IndividualCharacterData(name));
        }
    }

    public static XYPair<Integer> getOrigin(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getOrigin();
    }

    public static XYPair<Integer> getDimensions(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getDimensions();
    }

    public static TextureRegion[] getAllAnimationFrames(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getAllAnimationFrames();
    }

    public static ArrayList<Float> getAllAnimationFrameDelays(CharacterTypeName typeName) {
        return data.get(typeName.ordinal()).getAllAnimationFrameDelays();
    }

    public static int getStartFrameIndex(CharacterTypeName typeName, CharacterState state) {
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

    public static int getEndFrameIndex(CharacterTypeName typeName, CharacterState state) {
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

    public static CharacterTypeName colorToCharacterTypeName(Color color) {
        String colorString = color.toString();

        CharacterTypeName characterTypeName;
        switch (colorString) {
            case "404040ff" -> characterTypeName = CharacterTypeName.HELMET_PENGUIN;
            case "ff0000ff" -> characterTypeName = CharacterTypeName.SWORD_FISH;
            case "ff6a00ff" -> characterTypeName = CharacterTypeName.SAD_DOLLAR;
            case "000000ff" -> characterTypeName = CharacterTypeName.UNIMPRESSED_FISH;
            case "ffd800ff" -> characterTypeName = CharacterTypeName.ALIEN;
            case "4cff00ff" -> characterTypeName = CharacterTypeName.BURGER;
            case "b6ff00ff" -> characterTypeName = CharacterTypeName.HAM_SHAMWITCH;
            case "00ff90ff" -> characterTypeName = CharacterTypeName.HAM_AND_FIST;
            case "808080ff" -> characterTypeName = CharacterTypeName.POINTER;
            case "00ffffff" -> characterTypeName = CharacterTypeName.KNIGHT;
            case "ffffffff" -> characterTypeName = CharacterTypeName.STARER;
            case "0094ffff" -> characterTypeName = CharacterTypeName.HAMMIE;
            case "ff006eff" -> characterTypeName = CharacterTypeName.CHIPS;
            case "7f0000ff" -> characterTypeName = CharacterTypeName.SOCK;
            case "ff00dcff" -> characterTypeName = CharacterTypeName.EVIL_HH;
            case "8c00ffff" -> characterTypeName = CharacterTypeName.HOT_DOG;
            case "0026ffff" -> characterTypeName = CharacterTypeName.ANTEATER;
            case "b200ffff" -> characterTypeName = CharacterTypeName.MONOLITH;
            case "c0ff96ff" -> characterTypeName = CharacterTypeName.PEANUT_BEE;
            default -> {
                System.out.println("Got this color: " + colorString + " which doesn't correspond to anything");
                return CharacterTypeName.HELMET_PENGUIN;
            }
        }

        return characterTypeName;
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

        public IndividualCharacterData(CharacterTypeName characterTypeName) {
            ArrayList<Float> dyingAnimationFrameDelays = new ArrayList<>();
            ArrayList<Float> movingAnimationFrameDelays = new ArrayList<>();
            ArrayList<Float> standingAnimationFrameDelays = new ArrayList<>();

            switch (characterTypeName) {
                case ALIEN:
                    this.internalName = "alien";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.190f));
                    this.origin = new XYPair<>(130, 220);
                    this.dimensions = new XYPair<>(334, 270);
                    break;
                case ANTEATER:
                    this.internalName = "anteater";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(21, 0.120f));
                    this.origin = new XYPair<>(110, 241);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case BURGER:
                    this.internalName = "burger";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(8, 0.130f));
                    this.origin = new XYPair<>(65, 160);
                    this.dimensions = new XYPair<>(132, 162);
                    break;
                case CHIPS:
                    this.internalName = "chips";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(4, 0.190f));
                    this.origin = new XYPair<>(77, 172);
                    this.dimensions = new XYPair<>(154, 189);
                    break;
                case EVIL_HH:
                    this.internalName = "evil hh";
                    this.origin = new XYPair<>(55, 124);
                    this.dimensions = new XYPair<>(110, 135);
                    break;
                case HAM_AND_FIST:
                    this.internalName = "ham and fist";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(11, 0.160f));
                    this.origin = new XYPair<>(70, 114);
                    this.dimensions = new XYPair<>(128, 128);
                    break;
                case HAM_SHAMWITCH:
                    this.internalName = "ham shamwitch";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(10, 0.180f));
                    this.origin = new XYPair<>(70, 114);
                    this.dimensions = new XYPair<>(128, 128);
                    break;
                case HAMMIE:
                    this.internalName = "hammie";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(4, 0.140f));
                    this.origin = new XYPair<>(70, 114);
                    this.dimensions = new XYPair<>(128, 128);
                    break;
                case HELMET_PENGUIN:
                    this.internalName = "helmet penguin";
                    dyingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(10, 0.150f));
                    movingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(8, 0.100f));
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.180f));
                    this.origin = new XYPair<>(244, 194);
                    this.dimensions = new XYPair<>(488, 292);
                    break;
                case HOT_DOG:
                    this.internalName = "hot dog";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(14, 0.110f));
                    this.origin = new XYPair<>(55, 134);
                    this.dimensions = new XYPair<>(110, 135);
                    break;
                case KNIGHT:
                    this.internalName = "knight";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.230f));
                    this.origin = new XYPair<>(110, 261);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case KING_OF_THE_BURROW:
                    this.internalName = "king of the burrow";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(39, 0.140f));
                    this.origin = new XYPair<>(110, 269);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case MONOLITH:
                    this.internalName = "monolith";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(8, 0.010f));
                    this.origin = new XYPair<>(115, 240);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case PEANUT_BEE:
                    this.internalName = "peanut bee";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(2, 0.010f));
                    this.origin = new XYPair<>(110, 260);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case POINTER:
                    this.internalName = "pointer";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(6, 0.240f));
                    this.origin = new XYPair<>(115, 200);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case SAD_DOLLAR:
                    this.internalName = "sad dollar";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(5, 0.190f));
                    this.origin = new XYPair<>(115, 267);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case SOCK:
                    this.internalName = "sock";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(4, 0.170f));
                    this.origin = new XYPair<>(51, 133);
                    this.dimensions = new XYPair<>(110, 135);
                    break;
                case STARER:
                    this.internalName = "starer";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(8, 0.160f));
                    this.origin = new XYPair<>(51, 142);
                    this.dimensions = new XYPair<>(110, 135);
                    break;
                case SWORD_FISH:
                    this.internalName = "sword fish";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(8, 0.120f));
                    this.origin = new XYPair<>(110, 266);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                case UNIMPRESSED_FISH:
                    this.internalName = "unimpressed fish";
                    standingAnimationFrameDelays = new ArrayList<>(Collections.nCopies(5, 0.190f));
                    this.origin = new XYPair<>(110, 285);
                    this.dimensions = new XYPair<>(220, 270);
                    break;
                default:
                    System.out.println("Why was a character almost generated with no matching type name? characterTypeName:  " + characterTypeName);
                    return;
            }

            // If the standingAnimationFrameDelays is empty, then it must be a 1-frame character, so play the frame forever:
            if (standingAnimationFrameDelays.isEmpty()) {
                standingAnimationFrameDelays.add(Float.MAX_VALUE);
            }

            // If missing data, duplicate the standing information and spritesheets to these
            boolean noDying = false;
            boolean noMoving = false;
            if (dyingAnimationFrameDelays.isEmpty()) {
                dyingAnimationFrameDelays = standingAnimationFrameDelays;
                noDying = true;
            }
            if (movingAnimationFrameDelays.isEmpty()) {
                movingAnimationFrameDelays = standingAnimationFrameDelays;
                noMoving = true;
            }

            int totalFrameCount = dyingAnimationFrameDelays.size()
                    + movingAnimationFrameDelays.size()
                    + standingAnimationFrameDelays.size();

            // ---- Prepare All Animation Frames ----
            TextureRegion[] animationFrames = new TextureRegion[totalFrameCount];
            int nextIndex = 0;

            // DYING FRAMES
            nextIndex = addFrames(animationFrames, nextIndex, FileNameByType.DYING, dyingAnimationFrameDelays, noDying, noMoving);

            // MOVING FRAMES
            nextIndex = addFrames(animationFrames, nextIndex, FileNameByType.MOVING, movingAnimationFrameDelays, noDying, noMoving);

            // STANDING FRAMES
            addFrames(animationFrames, nextIndex, FileNameByType.STANDING, standingAnimationFrameDelays, noDying, noMoving);

            // DONE
            this.allAnimationFrames = animationFrames;
        }

        private int addFrames(TextureRegion[] animationFrames, int nextIndex, FileNameByType fileName, ArrayList<Float> delays, boolean noDying, boolean noMoving) {
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
                    if (noDying) {
                        internalPath += "/stand.png";
                    } else {
                        internalPath += "/die.png";
                    }
                    this.dyingAnimationStartFrameIndex = startIndex;
                    this.dyingAnimationEndFrameIndex = endIndex;
                }
                case MOVING -> {
                    if (noMoving) {
                        internalPath += "/stand.png";
                    } else {
                        internalPath += "/move.png";
                    }
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
            STANDING,
            DYING,
            MOVING
        }
    }
}
