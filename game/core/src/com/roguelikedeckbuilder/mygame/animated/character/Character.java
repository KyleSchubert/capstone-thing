package com.roguelikedeckbuilder.mygame.animated.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffect;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffectName;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;


public class Character extends Group {
    private final CharacterTypeName characterTypeName;
    private final Image targetGlow;
    private CharacterState state;
    private float frameTime = 0;
    private int frame = 0;
    private int stateFrameStartIndex = 0;
    private int stateFrameEndIndex = 0;
    private int isFacingLeft = 1;
    private boolean isTargeted = false;
    private boolean hasEvilAura = false;
    private VisualEffect evilAura;

    public Character(CharacterTypeName characterTypeName, float x, float y) {
        this.characterTypeName = characterTypeName;

        x -= CharacterData.getOrigin(characterTypeName).x();
        y -= (CharacterData.getDimensions(characterTypeName).y() - CharacterData.getOrigin(characterTypeName).y());

        this.setPosition(x, y);
        this.setSize(CharacterData.getDimensions(characterTypeName).x(), CharacterData.getDimensions(characterTypeName).y());

        this.setState(CharacterState.STANDING);
        setBounds(x, y,
                CharacterData.getDimensions(characterTypeName).x(),
                CharacterData.getDimensions(characterTypeName).y());

        targetGlow = new Image(new Texture(Gdx.files.internal("OTHER UI/target glow.png")));

        if (characterTypeName == CharacterTypeName.EVIL_HH) {
            hasEvilAura = true;
            evilAura = new VisualEffect(
                    VisualEffectName.EVIL_HH_AURA,
                    getCharacterCenter().x(),
                    getCharacterCenter().y() + 52,
                    this.getScaleX()
            );
            evilAura.setLooping();
        }

        this.setTouchable(Touchable.disabled);
    }

    private void prepareFrameStartAndEndIndex() {
        this.stateFrameStartIndex = CharacterData.getStartFrameIndex(characterTypeName, this.state);
        this.stateFrameEndIndex = CharacterData.getEndFrameIndex(characterTypeName, this.state);
    }

    @Override
    public void act(float elapsedTime) {
        this.frameTime += elapsedTime;

        if (hasEvilAura) {
            evilAura.act(elapsedTime);
        }

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
        if (isTargeted) {
            targetGlow.draw(batch, parentAlpha);
        }

        if (hasEvilAura) {
            evilAura.draw(batch, parentAlpha);
        }

        float additionalShiftX = 0;
        if (this.isFacingLeft == -1) {
            additionalShiftX = 2 * CharacterData.getOrigin(characterTypeName).x();
        }

        batch.draw(CharacterData.getAllAnimationFrames(characterTypeName)[this.frame],
                this.getX() + additionalShiftX,
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

    public CharacterState getState() {
        return state;
    }

    public void setState(CharacterState state) {
        if (state.equals(CharacterState.DEAD)) {
            this.state = state;
            return;
        }
        if (!state.equals(this.state)) {
            this.state = state;
            prepareFrameStartAndEndIndex();
            this.frame = this.stateFrameStartIndex;
        }
    }

    public void setTargeted(boolean targeted) {
        isTargeted = targeted;
        if (targeted) {
            refreshTargetPosition();
        }
    }

    private float getBottomUpYOrigin() {
        return (CharacterData.getDimensions(characterTypeName).y() - CharacterData.getOrigin(characterTypeName).y());
    }

    public XYPair<Float> getCharacterCenter() {
        float x = this.getX() + CharacterData.getOrigin(characterTypeName).x();
        float y = this.getY() + getBottomUpYOrigin();

        return new XYPair<>(x, y);
    }

    private void refreshTargetPosition() {
        XYPair<Float> coordinates = getCharacterCenter();
        targetGlow.setPosition(coordinates.x() - targetGlow.getWidth() / 2, coordinates.y());
    }
}
