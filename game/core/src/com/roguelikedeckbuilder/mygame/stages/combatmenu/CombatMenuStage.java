package com.roguelikedeckbuilder.mygame.stages.combatmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterState;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffect;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffectName;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.combat.HpChangeNumberHandler;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.enemy.Enemy;
import com.roguelikedeckbuilder.mygame.helpers.*;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuController;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;
import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class CombatMenuStage extends GenericStage {
    private static final Array<Enemy> currentEnemies = new Array<>();
    private final Label drawPileAmountText;
    private final Label shufflePileAmountText;
    private final Array<Enemy> mustRemoveBecauseDead;
    private final Label energyLabel;
    private int currentAttackingEnemyIndex = 0;
    private boolean isPlayerTurn = true;
    private boolean victory = false;

    public CombatMenuStage(ScreenViewport viewportForStage, ClickListener cardChangeStageTrigger) {
        super(viewportForStage, "combat background");

        // Reposition the background
        getStageBackgroundActor().setPosition(-7, -5);

        // Add the player
        this.getStage().addActor(Player.getCharacter());

        // Add the card piles
        Image drawPile = new Image(new Texture(Gdx.files.internal("CARDS/draw pile.png")));
        drawPile.setPosition(4, 3);
        drawPile.setScale(SCALE_FACTOR);
        this.getStage().addActor(drawPile);
        drawPile.addCaptureListener(ClickListenerManager.lookingAtDrawPile());
        drawPile.addCaptureListener(cardChangeStageTrigger);

        Image shufflePile = new Image(new Texture(Gdx.files.internal("CARDS/shuffle pile.png")));
        shufflePile.setPosition(64, 3);
        shufflePile.setScale(SCALE_FACTOR);
        this.getStage().addActor(shufflePile);

        // Labels for amount of cards in each pile
        Group groupForLabels = new Group();
        groupForLabels.setScale(SCALE_FACTOR);

        drawPileAmountText = LabelMaker.newLabel("", LabelMaker.getLarge());
        drawPileAmountText.setPosition(108, 20);
        groupForLabels.addActor(drawPileAmountText);

        shufflePileAmountText = LabelMaker.newLabel("", LabelMaker.getLarge());
        shufflePileAmountText.setPosition(1308, 20);
        groupForLabels.addActor(shufflePileAmountText);

        this.getStage().addActor(groupForLabels);

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

        Label.LabelStyle labelStyleLarge = new Label.LabelStyle();
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

        this.getStage().addListener(getCardHoverListener());
    }

    public static Array<Enemy> getCurrentEnemies() {
        return currentEnemies;
    }

    public void batch(float elapsedTime, SpriteBatch batch) {
        while (HpChangeNumberHandler.size() > 0) {
            this.getStage().addActor(HpChangeNumberHandler.pop().getGroup());
        }

        super.batch(elapsedTime);
        delayHandler();
        targetHoverListener();
        for (Enemy enemy : currentEnemies) {
            enemy.getCombatInformation().drawHpBar(batch);
            if (enemy.getCharacter().getState() == CharacterState.DEAD) {
                mustRemoveBecauseDead.add(enemy);
            } else if (enemy.getCombatInformation().getHp() == 0 && enemy.getCharacter().getState() != CharacterState.DYING) {
                VisualEffect deathEffect = new VisualEffect(VisualEffectName.DIE,
                        enemy.getPositionOnStage().x(),
                        enemy.getPositionOnStage().y() + 2,
                        SCALE_FACTOR);

                getStage().addActor(deathEffect);

                // To use a dying animation, this should be .DYING rather than .DEAD
                enemy.getCharacter().setState(CharacterState.DEAD);
            }
        }

        for (Enemy enemy : mustRemoveBecauseDead) {
            removeEnemy(enemy);
        }
        mustRemoveBecauseDead.clear();

        Player.getCombatInformation().drawHpBar(batch);
        energyLabel.setText(String.valueOf(Player.getEnergy()));

        if (Player.potentiallyDiscardCards() || Player.isCombatMenuStageMustAddCard()) {
            int i = 0;
            int amountOfCards = Player.getHandContents().size;

            for (Card card : Player.getHandContents()) {
                // Reposition the cards
                float LEFTMOST_POSITION = 6;
                float RIGHTMOST_POSITION = 48;
                float gapSize = (RIGHTMOST_POSITION - LEFTMOST_POSITION) / (amountOfCards + 1);
                float positionX = LEFTMOST_POSITION + gapSize * (i + 1);

                if (card.isToBeAddedToCombatMenuStage()) {
                    // Add it to the stage and snap its position to where it should be
                    this.getStage().addActor(card.getGroup());
                    card.setToBeAddedToCombatMenuStage(false);
                    card.getGroup().setPosition(positionX, 0);
                } else {
                    // It's already on the stage, so slide it over
                    SequenceAction sequenceAction = new SequenceAction(
                            Actions.moveTo(positionX, 0, 0.1f)
                    );
                    card.getGroup().addAction(sequenceAction);
                }
                // Reset z index so it looks OK
                card.getGroup().setZIndex(99);
                i++;
            }
            Player.setCombatMenuStageMustAddCard(false);
        }

        if (Player.combatMenuStageMustUpdatePileText) {
            updatePileText();
            Player.setCombatMenuStageMustUpdatePileText(false);
        }
    }

    private void delayHandler() {
        Array<DelayScheduler.Delay> delaysCopy = new Array<>();
        delaysCopy.addAll(getScheduledDelays());

        for (DelayScheduler.Delay delay : delaysCopy) {
            if (delay.isDone()) {
                switch (delay.getAdditionalInformation()) {
                    case "enemyTurnStart" -> {
                        doNextEnemyAttack();
                        deleteDelay(delay);
                    }
                    case "enemyTurnEnd" -> {
                        for (Enemy enemy : currentEnemies) {
                            enemy.endTurn();
                        }
                        Statistics.setTurnNumber(Statistics.getTurnNumber() + 1);
                        Statistics.turnStarted();
                        isPlayerTurn = true;
                        Player.startTurn();
                        deleteDelay(delay);
                    }
                    case "victoryPause" -> {
                        setVictory(true);
                        deleteDelay(delay);
                    }
                }
            }
        }
    }

    private void doNextEnemyAttack() {
        if (currentAttackingEnemyIndex >= currentEnemies.size) {
            scheduleNewDelay(0.8f, "enemyTurnEnd");
        } else {
            if (currentEnemies.get(currentAttackingEnemyIndex).getCharacter().getState() != CharacterState.DYING) {
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
            scheduleNewDelay(2f, "victoryPause");
        }
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
        if (victory) {
            SoundManager.playFunnyTadaSound();
        }
    }

    public void addEnemy(CharacterTypeName characterTypeName) {
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

        Statistics.setTurnNumber(1);
        Statistics.turnStarted();
        Player.combatStart();

        currentEnemies.clear();

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
        drawPileAmountText.setText(Player.getDrawPileContents().size);
        shufflePileAmountText.setText(Player.getShufflePileContents().size);
    }

    private void endTurn() {
        System.out.println("Ended turn.");
        Statistics.turnEnded();

        removeActorsByType(UserObjectOptions.CARD);

        Player.endTurn();

        updatePileText();

        currentAttackingEnemyIndex = 0;

        for (Enemy enemy : currentEnemies) {
            enemy.getCombatInformation().clearDefense();
        }
        scheduleNewDelay(0.8f, "enemyTurnStart");
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

    private ClickListener getCardHoverListener() {
        return new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                Group target = event.getTarget().getParent();
                UserObjectOptions actorType = (UserObjectOptions) target.getUserObject();
                if (actorType == UserObjectOptions.CARD) {
                    int index = getStage().getActors().indexOf(target, true);
                    if (index != -1) {
                        target.setZIndex(99);
                    }
                }
            }
        };
    }
}