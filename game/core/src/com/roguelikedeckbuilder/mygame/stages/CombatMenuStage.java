package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.MenuController;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.UseLine;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.combat.Enemy;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class CombatMenuStage extends GenericStage {
    private final Array<Enemy> currentEnemies = new Array<>();
    private final Array<Card> drawPileContents = new Array<>();
    private final Array<Card> shufflePileContents = new Array<>();
    private final Array<Card> handContents = new Array<>();
    private final Label.LabelStyle labelStyle = new Label.LabelStyle();
    private int drawPileAmountTextIndex = 0;
    private int shufflePileAmountTextIndex = 0;

    public CombatMenuStage(ScreenViewport viewportForStage, ImageButton exitButtonForTesting) {
        super(viewportForStage, "combat background");

        // Reposition the background
        getStageBackgroundActor().setPosition(-7, -5);

        // TODO: REMOVE THIS AFTER COMBAT IS IMPLEMENTED
        exitButtonForTesting.setPosition(33, 36);
        this.getStage().addActor(exitButtonForTesting);

        // Add the player
        this.getStage().addActor(Player.getCharacter());

        // Add the card piles
        Image drawPile = new Image(new Texture(Gdx.files.internal("CARDS/draw pile.png")));
        drawPile.setPosition(4, 3);
        drawPile.setScale(SCALE_FACTOR);
        this.getStage().addActor(drawPile);

        Image shufflePile = new Image(new Texture(Gdx.files.internal("CARDS/shuffle pile.png")));
        shufflePile.setPosition(64, 3);
        shufflePile.setScale(SCALE_FACTOR);
        this.getStage().addActor(shufflePile);

        // Labels for amount of cards in each pile
        labelStyle.font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        labelStyle.font.setUseIntegerPositions(false);
        labelStyle.font.getData().setScale(SCALE_FACTOR, SCALE_FACTOR);

        Label drawPileAmountText = new Label(Integer.toString(drawPileContents.size), labelStyle);
        drawPileAmountText.setPosition(5, 1);
        this.drawPileAmountTextIndex = this.getStage().getActors().size;
        this.getStage().addActor(drawPileAmountText);

        Label shufflePileAmountText = new Label(Integer.toString(shufflePileContents.size), labelStyle);
        shufflePileAmountText.setPosition(65, 1);
        this.shufflePileAmountTextIndex = this.getStage().getActors().size;
        this.getStage().addActor(shufflePileAmountText);

        // End turn button
        ImageButton endTurnButton = MenuController.getImageButton("end turn");
        endTurnButton.setPosition(58, 9);
        endTurnButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                endTurn();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        this.getStage().addActor(endTurnButton);
    }

    public void batch(float elapsedTime) {
        super.batch(elapsedTime);
        UseLine.setMainColor(Color.PURPLE);
        UseLine.setPosition(new XYPair<>(19f, 12f), getMousePosition());
    }

    public void addEnemy(Character.CharacterTypeName characterTypeName) {
        XYPair<Float> position = generateEnemyPosition();
        Enemy enemy = new Enemy(characterTypeName, position.x(), position.y());

        currentEnemies.add(enemy);
        enemy.putOnStage(getStage());
    }

    private XYPair<Float> generateEnemyPosition() {
        return switch (currentEnemies.size) {
            case 0 -> EnemyPositions.ENEMY1.getPos();
            case 1 -> EnemyPositions.ENEMY2.getPos();
            case 2 -> EnemyPositions.ENEMY3.getPos();
            case 3 -> EnemyPositions.ENEMY4.getPos();
            default -> {
                System.out.println("[Huh?] There shouldn't be more than 4 enemies.");
                yield new XYPair<>(4f, 4f);
            }
        };
    }

    public void reset() {
        removeActorsByType(UserObjectOptions.ENEMY);
        removeActorsByType(UserObjectOptions.CARD);

        currentEnemies.clear();

        drawPileContents.clear();
        drawPileContents.addAll(Player.getOwnedCards());
        drawPileContents.shuffle();

        shufflePileContents.clear();

        handContents.clear();

        drawCards(5);
        updatePileText();
    }

    private void removeActorsByType(UserObjectOptions userObjectOption) {
        Array<Actor> mustRemove = new Array<>();

        for (Actor actor : this.getStage().getActors()) {
            UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
            if (actorType == userObjectOption) {
                mustRemove.add(actor);
            }
        }

        for (Actor actor : mustRemove) {
            actor.remove();
        }
    }

    private void updatePileText() {
        Label drawPileAmountText = (Label) this.getStage().getActors().get(drawPileAmountTextIndex);
        drawPileAmountText.setText(drawPileContents.size);

        Label shufflePileAmountText = (Label) this.getStage().getActors().get(shufflePileAmountTextIndex);
        shufflePileAmountText.setText(shufflePileContents.size);
    }

    private void endTurn() {
        // TODO: when enemies can fight, make this end the player's turn and prevent this button from being spammed
        System.out.println("Ended turn.");

        removeActorsByType(UserObjectOptions.CARD);

        shufflePileContents.addAll(handContents);
        handContents.clear();

        drawCards(5);
    }

    private void drawCards(int amount) {
        amount = Math.min(amount, drawPileContents.size + shufflePileContents.size);

        for (int i = 0; i < amount; i++) {
            if (drawPileContents.size == 0) {
                // Shuffle the shuffle pile into the draw pile
                System.out.println("Shuffled in.");
                System.out.println("AMOUNT: " + amount + " sizes: draw: " + drawPileContents.size + " shuffle: " + shufflePileContents.size);

                drawPileContents.addAll(shufflePileContents);
                drawPileContents.shuffle();
                shufflePileContents.clear();
            }
            drawPileContents.get(0).getGroup().setPosition(12 + getAmountOfCardsInHand() * 7, 0);
            this.getStage().addActor(drawPileContents.get(0).getGroup());
            handContents.add(drawPileContents.get(0));
            drawPileContents.removeIndex(0);
        }
        updatePileText();
    }

    private int getAmountOfCardsInHand() {
        int total = 0;

        for (Actor actor : this.getStage().getActors()) {
            UserObjectOptions actorType = (UserObjectOptions) actor.getUserObject();
            if (actorType == UserObjectOptions.CARD) {
                total++;
            }
        }

        return total;
    }

    public enum EnemyPositions {
        ENEMY1(44, 22.8f),
        ENEMY2(49, 22.8f),
        ENEMY3(54, 22.8f),
        ENEMY4(59, 22.8f);

        private final XYPair<Float> pos;

        EnemyPositions(float x, float y) {
            pos = new XYPair<>(x, y);
        }

        public XYPair<Float> getPos() {
            return pos;
        }
    }
}
