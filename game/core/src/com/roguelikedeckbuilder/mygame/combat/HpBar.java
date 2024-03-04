package com.roguelikedeckbuilder.mygame.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class HpBar {
    private final ShapeRenderer shapeRenderer;
    private boolean visible;
    private final Color mainColor;
    private final Color tickDownColor;
    private XYPair<Float> position;
    private XYPair<Float> mainStart;
    private XYPair<Float> mainEnd;
    private XYPair<Float> tickDownEnd;
    private final float hpTickDownX;
    private float hpRatio;
    private final float totalWidth;
    private final BitmapFont font;
    private String hpBarText;
    private final GlyphLayout layout;

    public HpBar() {
        shapeRenderer = new ShapeRenderer();
        mainColor = Color.RED;
        tickDownColor = Color.ORANGE;
        visible = false;
        totalWidth = 160;
        hpTickDownX = totalWidth;
        hpRatio = 1;

        font = new BitmapFont(Gdx.files.internal("hp_and_damage.fnt"));
        font.setUseIntegerPositions(false);
        font.getData().setScale(SCALE_FACTOR / 4);

        layout = new GlyphLayout(font, "");
    }

    public void update(int hp, int maxHp) {
        hpRatio = (float) hp / maxHp;
        hpBarText = String.format("%d / %d", hp, maxHp);
        layout.setText(font, hpBarText);
        updateBar();
    }

    public void setPosition(XYPair<Float> position) {
        this.position = new XYPair<>(position.x() / SCALE_FACTOR, position.y() / SCALE_FACTOR);
        updateBar();
    }

    private void updateBar() {
        mainStart = new XYPair<>(position.x() + 3, position.y());
        mainEnd = new XYPair<>(mainStart.x() + totalWidth * hpRatio, mainStart.y());
        tickDownEnd = new XYPair<>(mainStart.x() + hpTickDownX, mainStart.y());
    }

    public void draw(SpriteBatch batch) {
        if (visible) {
            // Must be called between batch.begin() and batch.end()
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            XYPair<Float> end = new XYPair<>(position.x() + totalWidth, position.y());


            // Outline 1
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.rectLine(position.x(), position.y(), end.x() + 6, end.y(), 13);

            // Outline 2
            shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1);
            shapeRenderer.rectLine(position.x() + 1, position.y(), end.x() + 5, end.y(), 11);

            // Outline 3
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.rectLine(position.x() + 2, position.y(), end.x() + 4, end.y(), 9);

            // tick-down
            shapeRenderer.setColor(tickDownColor);
            shapeRenderer.rectLine(mainStart.x(), mainStart.y(), tickDownEnd.x(), tickDownEnd.y(), 7);

            // main start
            shapeRenderer.setColor(mainColor);
            shapeRenderer.rectLine(mainStart.x(), mainStart.y(), mainEnd.x(), mainEnd.y(), 7);

            shapeRenderer.end();

            batch.end();
            batch.begin();
            // hp bar number text
            font.draw(batch, hpBarText, (this.position.x() + totalWidth / 2) * SCALE_FACTOR - layout.width / 2, this.position.y() * SCALE_FACTOR + 1.3f);
            batch.end();
            batch.begin();
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
