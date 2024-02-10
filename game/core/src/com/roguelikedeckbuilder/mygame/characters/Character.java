package com.roguelikedeckbuilder.mygame.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Character {
    public enum CharacterState {
        DYING, MOVING, STANDING, DEAD
    }

    public enum CharacterTypeName {
        BIRD, PLANT, STUMP, PIG, ORANGE_MUSHROOM, BLUE_MUSHROOM, HELMET_PENGUIN
    }

    private final CharacterTypeName characterTypeName;
    private CharacterState state;
    private final XYPair<Float> pos;
    private float frameTime = 0;
    private int frame = 0;
    private int stateFrameStartIndex = 0;
    private int stateFrameEndIndex = 0;
    private int isFacingLeft = 1;

    public Character(CharacterTypeName characterTypeName, float x, float y) {
        this.characterTypeName = characterTypeName;

        x -= CharacterData.getOrigin(characterTypeName).x() * SCALE_FACTOR;
        y -= (CharacterData.getDimensions(characterTypeName).y() - CharacterData.getOrigin(characterTypeName).y()) * SCALE_FACTOR;

        pos = new XYPair<>(x, y);

        this.setState(CharacterState.STANDING);
    }

    private void setState(CharacterState state) {
        if (!state.equals(this.state)) {
            this.state = state;
            prepareFrameStartAndEndIndex();
            this.frame = this.stateFrameStartIndex;
        }
    }

    private void prepareFrameStartAndEndIndex() {
        this.stateFrameStartIndex = CharacterData.getStartFrameIndex(characterTypeName, this.state);
        this.stateFrameEndIndex = CharacterData.getEndFrameIndex(characterTypeName, this.state);
    }

    public void animate(SpriteBatch batch, float elapsedTime) {
        this.frameTime += elapsedTime;
        if (this.frameTime > CharacterData.getAllAnimationFrameDelays(characterTypeName).get(this.frame)) {
            this.frameTime -= CharacterData.getAllAnimationFrameDelays(characterTypeName).get(this.frame);
            if (this.frame == this.stateFrameEndIndex) {
                if (this.state == CharacterState.DYING) {
                    setState(CharacterState.DEAD);
                    return;
                }
                this.frame = this.stateFrameStartIndex;
            } else {
                this.frame++;
            }
        }

        batch.draw(CharacterData.getAllAnimationFrames(characterTypeName)[this.frame],
                this.pos.x(),
                this.pos.y(),
                CharacterData.getOrigin(characterTypeName).x() * SCALE_FACTOR,
                CharacterData.getOrigin(characterTypeName).y() * SCALE_FACTOR,
                CharacterData.getDimensions(characterTypeName).x() * SCALE_FACTOR,
                CharacterData.getDimensions(characterTypeName).y() * SCALE_FACTOR,
                isFacingLeft, 1, 0);
    }

    public void faceRight() {
        this.isFacingLeft = -1;
    }

    public void faceLeft() {
        this.isFacingLeft = 1;
    }

    public int getIsFacingLeft() {
        return this.isFacingLeft;
    }
}
