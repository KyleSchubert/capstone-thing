package com.roguelikedeckbuilder.mygame.characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import java.util.Iterator;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Character extends Actor {
    public enum CharacterState {
        DYING, MOVING, STANDING, DEAD
    }

    public enum CharacterTypeName {
        BIRD, PLANT, STUMP, PIG, ORANGE_MUSHROOM, BLUE_MUSHROOM, HELMET_PENGUIN
    }

    private final CharacterTypeName characterTypeName;
    private CharacterState state;
    private float frameTime = 0;
    private int frame = 0;
    private int stateFrameStartIndex = 0;
    private int stateFrameEndIndex = 0;
    private int isFacingLeft = 1;

    public Character(CharacterTypeName characterTypeName, float x, float y) {
        this.characterTypeName = characterTypeName;

        x -= CharacterData.getOrigin(characterTypeName).x() * SCALE_FACTOR;
        y -= (CharacterData.getDimensions(characterTypeName).y() - CharacterData.getOrigin(characterTypeName).y()) * SCALE_FACTOR;

        // It seems origin is not what I want
        // this.setOrigin(CharacterData.getOrigin(characterTypeName).x(), CharacterData.getOrigin(characterTypeName).y());

        this.setPosition(x, y);
        this.setSize(CharacterData.getDimensions(characterTypeName).x(), CharacterData.getDimensions(characterTypeName).y());
        this.setScale(SCALE_FACTOR);

        this.setState(CharacterState.STANDING);
        setBounds(x, y,
                CharacterData.getDimensions(characterTypeName).x(),
                CharacterData.getDimensions(characterTypeName).y());
    }

    public void setState(CharacterState state) {
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

    @Override
    public void act(float elapsedTime) {
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

        for (Action action : this.getActions()) {
            action.act(elapsedTime);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(CharacterData.getAllAnimationFrames(characterTypeName)[this.frame],
                this.getX(),
                this.getY(),
                this.getOriginX(),
                this.getOriginY(),
                this.getWidth(),
                this.getHeight(),
                isFacingLeft * this.getScaleX(), this.getScaleY(), this.getRotation()
        );
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
