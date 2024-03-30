package com.roguelikedeckbuilder.mygame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.MenuController;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.UseLine;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.characters.Character;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.combat.Enemy;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.helpers.DelayScheduler;
import com.roguelikedeckbuilder.mygame.helpers.SoundManager;
import com.roguelikedeckbuilder.mygame.helpers.UserObjectOptions;
import com.roguelikedeckbuilder.mygame.helpers.XYPair;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class CombatMenuStage extends GenericStage {
    private final Array<Enemy> currentEnemies = new Array<>();
    private int currentAttackingEnemyIndex = 0;
    private boolean isPlayerTurn = true;
    private final Array<Card> drawPileContents = new Array<>();
    private final Array<Card> shufflePileContents = new Array<>();
    private final Array<Card> handContents = new Array<>();
    private final Label.LabelStyle labelStyle = new Label.LabelStyle();
    private final Label.LabelStyle labelStyleLarge = new Label.LabelStyle();
    private final int drawPileAmountTextIndex;
    private final int shufflePileAmountTextIndex;
    private final Array<Enemy> mustRemoveBecauseDead;
    private final Label energyLabel;
    private boolean victory = false;

    public CombatMenuStage(ScreenViewport viewportForStage, ImageButton exitButtonForTesting, CardChangeStage cardChangeMenuStage, ClickListener cardChangeStageTrigger) {
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
        drawPile.addCaptureListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                cardChangeMenuStage.prepareViewDrawPile(drawPileContents);
                SoundManager.playMenuOpenSound();
            }
        });
        drawPile.addCaptureListener(cardChangeStageTrigger);

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
                if (isPlayerTurn) {
                    isPlayerTurn = false;
                    endTurn();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        this.getStage().addActor(endTurnButton);

        mustRemoveBecauseDead = new Array<>();

        labelStyleLarge.font = new BitmapFont(Gdx.files.internal("hp_and_damage.fnt"));
        labelStyleLarge.font.getData().setScale(SCALE_FACTOR / 2);
        labelStyleLarge.font.setUseIntegerPositions(false);

        // Energy
        Image energyImage = new Image(new Texture(Gdx.files.internal("CARDS/energy.png")));
        energyImage.setPosition(3, 8);
        energyImage.setScale(SCALE_FACTOR);
        this.getStage().addActor(energyImage);

        energyLabel = new Label(String.valueOf(Player.getEnergy()), labelStyleLarge);
        energyLabel.setWidth(6);
        energyLabel.setWrap(true);
        energyLabel.setPosition(5, 9.3f);
        this.getStage().addActor(energyLabel);
    }

    public void batch(float elapsedTime, SpriteBatch batch) {
        super.batch(elapsedTime);
        delayHandler();
        targetHoverListener();
        for (Enemy enemy : currentEnemies) {
            enemy.getCombatInformation().drawHpBar(batch);
            if (enemy.getCharacter().getState() == Character.CharacterState.DEAD) {
                mustRemoveBecauseDead.add(enemy);
            } else if (enemy.getCombatInformation().getHp() == 0 && enemy.getCharacter().getState() != Character.CharacterState.DYING) {
                enemy.getCharacter().setState(Character.CharacterState.DYING);
            }
        }

        for (Enemy enemy : mustRemoveBecauseDead) {
            removeEnemy(enemy);
        }
        mustRemoveBecauseDead.clear();

        Player.getCombatInformation().drawHpBar(batch);
        energyLabel.setText(String.valueOf(Player.getEnergy()));
    }

    private void delayHandler() {
        Array<DelayScheduler.Delay> delaysCopy = new Array<>();
        delaysCopy.addAll(getScheduledDelays());

        for (DelayScheduler.Delay delay : delaysCopy) {
            if (delay.getAdditionalInformation().equals("enemyTurnStart")) {
                if (delay.isDone()) {
                    doNextEnemyAttack();
                    deleteDelay(delay);
                }
            } else if (delay.getAdditionalInformation().equals("enemyTurnEnd")) {
                if (delay.isDone()) {
                    for (Enemy enemy : currentEnemies) {
                        enemy.endTurn();
                    }

                    drawCards(5);

                    isPlayerTurn = true;
                    Player.startTurn();

                    deleteDelay(delay);
                }
            }
        }
    }

    private void doNextEnemyAttack() {
        if (currentAttackingEnemyIndex >= currentEnemies.size) {
            scheduleNewDelay(0.8f, "enemyTurnEnd");
        } else {
            if (currentEnemies.get(currentAttackingEnemyIndex).getCharacter().getState() != Character.CharacterState.DYING) {
                currentEnemies.get(currentAttackingEnemyIndex).beginTurn();
            }
            currentAttackingEnemyIndex++;
            scheduleNewDelay(0.8f, "enemyTurnStart");
        }
    }

    private void removeEnemy(Enemy enemy) {
        enemy.removeFromStage(getStage());
        int indexOfDeadEnemy = currentEnemies.indexOf(enemy, true);
        currentEnemies.removeValue(enemy, true);

        if (indexOfDeadEnemy < currentAttackingEnemyIndex) {
            currentAttackingEnemyIndex--;
        }
        if (currentEnemies.isEmpty()) {
            victory = true;
            SoundManager.playFunnyTadaSound();
        }
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public void addEnemy(Character.CharacterTypeName characterTypeName) {
        Enemy enemy = new Enemy(characterTypeName, generateEnemyPosition());

        currentEnemies.add(enemy);
        enemy.putOnStage(getStage());
    }

    private EnemyPositions generateEnemyPosition() {
        return switch (currentEnemies.size) {
            case 0 -> EnemyPositions.ENEMY1;
            case 1 -> EnemyPositions.ENEMY2;
            case 2 -> EnemyPositions.ENEMY3;
            case 3 -> EnemyPositions.ENEMY4;
            default -> {
                System.out.println("[Huh?] There shouldn't be more than 4 enemies.");
                yield EnemyPositions.DEBUG_POSITION;
            }
        };
    }

    public void reset() {
        removeActorsByType(UserObjectOptions.ENEMY);
        removeActorsByType(UserObjectOptions.CARD);

        getScheduledDelays().clear();
        isPlayerTurn = true;

        victory = false;

        Player.combatStart();

        currentEnemies.clear();

        drawPileContents.clear();
        drawPileContents.addAll(Player.getOwnedCards());
        drawPileContents.shuffle();

        shufflePileContents.clear();

        handContents.clear();

        drawCards(5);
        updatePileText();

        UseLine.setMainColor(Color.PURPLE);
        UseLine.setVisibility(false);
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

        currentAttackingEnemyIndex = 0;

        for (Enemy enemy : currentEnemies) {
            enemy.getCombatInformation().clearDefense();
        }
        scheduleNewDelay(0.8f, "enemyTurnStart");
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

    private void targetHoverListener() {
        if (UseLine.isVisible()) {
            XYPair<Float> mousePosition = getMousePosition();

            for (Enemy enemy : currentEnemies) {
                enemy.setTargeted(false);
            }
            Player.getCharacter().setTargeted(false);

            Array<Enemy> enemies = new Array<>();
            if (Player.getPotentialAbilityTargetType() == TargetType.SELF) {
                CombatHandler.setIsTargetingPlayer(Player.isPointWithinRange(mousePosition));
            } else {
                for (Enemy enemy : currentEnemies) {
                    if (Enemy.isPointWithinRange(mousePosition, enemy.getPositionOnStage())) {
                        CombatHandler.setIsTargetingPlayer(false);
                        // Check for TargetType.ALL first, so players can cancel attacking all enemies, by not hovering any enemy.
                        if (Player.getPotentialAbilityTargetType() == TargetType.ALL) {
                            enemies = currentEnemies;
                            break;
                        }
                        enemies.add(enemy);
                        break;
                    }
                }
            }

            CombatHandler.setEnemiesThePlayerIsHoveringOver(enemies);
        }
    }

    public enum EnemyPositions {
        ENEMY1(38, 22.8f),
        ENEMY2(47, 22.8f),
        ENEMY3(56, 22.8f),
        ENEMY4(65, 22.8f),
        DEBUG_POSITION(20, 20);

        private final XYPair<Float> pos;

        EnemyPositions(float x, float y) {
            pos = new XYPair<>(x, y);
        }

        public XYPair<Float> getPos() {
            return pos;
        }
    }
}
