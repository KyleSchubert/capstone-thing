package com.roguelikedeckbuilder.mygame.stages.combatmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.roguelikedeckbuilder.mygame.Player;
import com.roguelikedeckbuilder.mygame.animated.character.Character;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterState;
import com.roguelikedeckbuilder.mygame.animated.character.CharacterTypeName;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffect;
import com.roguelikedeckbuilder.mygame.animated.visualeffect.VisualEffectName;
import com.roguelikedeckbuilder.mygame.cards.Card;
import com.roguelikedeckbuilder.mygame.combat.CombatHandler;
import com.roguelikedeckbuilder.mygame.combat.CombatInformation;
import com.roguelikedeckbuilder.mygame.combat.HpChangeNumberHandler;
import com.roguelikedeckbuilder.mygame.combat.TargetType;
import com.roguelikedeckbuilder.mygame.combat.enemy.Enemy;
import com.roguelikedeckbuilder.mygame.helpers.*;
import com.roguelikedeckbuilder.mygame.items.Item;
import com.roguelikedeckbuilder.mygame.menucontroller.MenuState;
import com.roguelikedeckbuilder.mygame.stages.GenericStage;
import com.roguelikedeckbuilder.mygame.tracking.statistics.Statistics;

import static com.roguelikedeckbuilder.mygame.MyGame.getMousePosition;

public class CombatMenuStage extends GenericStage {
    private static final Array<Enemy> currentEnemies = new Array<>();
    private static Character playerCharacter;
    private final Label drawPileAmountText;
    private final Label shufflePileAmountText;
    private final Array<Enemy> mustRemoveBecauseDead;
    private final Label energyLabel;
    private final Label maximumAmountOfCardsLabel;
    private int currentAttackingEnemyIndex = 0;
    private boolean isPlayerTurn = true;
    private boolean victory = false;

    public CombatMenuStage() {
        super("combat background");

        // Reposition the background
        getStageBackgroundActor().setPosition(-140, -76);

        // Add the player
        setPlayerCharacter();
        addActor(Player.getCombatInformation().getStatusEffectVisuals());

        // Add the card piles
        Image drawPile = new Image(new Texture(Gdx.files.internal("CARDS/draw pile.png")));
        drawPile.setPosition(80, 60);
        addActor(drawPile);
        drawPile.addCaptureListener(ClickListenerManager.lookingAtDrawPile());
        drawPile.addCaptureListener(ClickListenerManager.triggeringMenuState(MenuState.CARD_CHOICE, MenuSoundType.OPEN));
        drawPile.addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip("Draw Pile", "Click to view", -999));

        Image shufflePile = new Image(new Texture(Gdx.files.internal("CARDS/shuffle pile.png")));
        shufflePile.setPosition(1280, 60);
        shufflePile.addCaptureListener(ClickListenerManager.hoverAndPutTextInTooltip("Shuffle Pile", "Unable to view", -999));
        addActor(shufflePile);

        // Labels for amount of cards in each pile
        drawPileAmountText = LabelMaker.newLabel("", LabelMaker.getLarge());
        drawPileAmountText.setPosition(108, 40);
        addActor(drawPileAmountText);

        shufflePileAmountText = LabelMaker.newLabel("", LabelMaker.getLarge());
        shufflePileAmountText.setPosition(1308, 40);
        addActor(shufflePileAmountText);

        // End turn button
        ImageButton endTurnButton = ClickListenerManager.getImageButton("end turn");
        endTurnButton.setPosition(1190, 220);
        endTurnButton.addCaptureListener(
                ClickListenerManager.hoverAndPutTextInTooltip("End Turn", "Clicking this ends your turn and starts the Enemies' turns from left to right.", -999)
        );

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
        addActor(endTurnButton);

        mustRemoveBecauseDead = new Array<>();

        Label.LabelStyle labelStyleLarge = new Label.LabelStyle();
        labelStyleLarge.font = new BitmapFont(Gdx.files.internal("hp_and_damage.fnt"));
        labelStyleLarge.font.getData().setScale(0.5f);
        labelStyleLarge.font.setUseIntegerPositions(false);

        // Energy
        Image energyImage = new Image(new Texture(Gdx.files.internal("CARDS/energy.png")));
        energyImage.setPosition(60, 160);
        energyImage.addCaptureListener(
                ClickListenerManager.hoverAndPutTextInTooltip(
                        "Energy",
                        "Normally, your energy returns to 3 when your turn starts. Spend energy to use cards in your hand.",
                        -999
                )
        );
        addActor(energyImage);

        energyLabel = new Label(String.valueOf(Player.getEnergy()), labelStyleLarge);
        energyLabel.setWidth(6);
        energyLabel.setWrap(true);
        energyLabel.setPosition(100, 186);
        energyLabel.setTouchable(Touchable.disabled);
        addActor(energyLabel);

        this.getStage().addListener(getCardHoverListener());

        maximumAmountOfCardsLabel = LabelMaker.newLabel(
                String.format("Cards in hand: %d / %d", Player.getHandContents().size, Player.MAXIMUM_CARDS_IN_HAND),
                LabelMaker.getMedium()
        );
        maximumAmountOfCardsLabel.setPosition(1236, 160);
        maximumAmountOfCardsLabel.setWidth(160);
        maximumAmountOfCardsLabel.setAlignment(Align.center);
        maximumAmountOfCardsLabel.setTouchable(Touchable.disabled);
        addActor(maximumAmountOfCardsLabel);
    }

    public static Array<Enemy> getCurrentEnemies() {
        return currentEnemies;
    }

    public static Array<CombatInformation> getCombatInformationForLivingEnemies() {
        Array<CombatInformation> filteredEnemies = new Array<>();

        for (Enemy enemy : currentEnemies) {
            if (enemy.getCharacter().getState() != CharacterState.DYING && enemy.getCharacter().getState() != CharacterState.DEAD) {
                filteredEnemies.add(enemy.getCombatInformation());
            }
        }

        return filteredEnemies;
    }

    public static Character getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter() {
        playerCharacter = Player.getCharacter();
        removeActorsByType(UserObjectOptions.PLAYER);
        addActor(playerCharacter);
    }

    public void batch(float elapsedTime, SpriteBatch batch) {
        while (HpChangeNumberHandler.size() > 0) {
            addActor(HpChangeNumberHandler.pop());
        }

        super.batch(elapsedTime);
        delayHandler();
        targetHoverListener();
        for (Enemy enemy : currentEnemies) {
            enemy.getCombatInformation().draw(batch);

            if (enemy.getCharacter().getState() == CharacterState.DEAD) {
                mustRemoveBecauseDead.add(enemy);
            } else if (enemy.getCombatInformation().getHp() == 0 && enemy.getCharacter().getState() != CharacterState.DYING) {
                VisualEffect deathEffect = new VisualEffect(VisualEffectName.DIE,
                        enemy.getPositionOnStage().x(),
                        enemy.getPositionOnStage().y() + 40,
                        1);

                addActor(deathEffect);

                // To use a dying animation, this should be .DYING rather than .DEAD
                enemy.getCharacter().setState(CharacterState.DEAD);
            } else if (enemy.getCombatInformation().getNeedToPlayHitEffect()) {
                addActor(
                        new VisualEffect(
                                VisualEffectName.HIT,
                                enemy.getPositionOnStage().x(),
                                enemy.getPositionOnStage().y() + 40,
                                enemy.getCharacter().getScaleX()
                        )
                );
                enemy.getCombatInformation().setNeedToPlayHitEffect(false);
            }
        }

        positionTemporaryItems();

        for (Enemy enemy : mustRemoveBecauseDead) {
            enemy.getCombatInformation().resetStatusEffects();
            enemy.removeIntentHolder();
            removeEnemy(enemy);
        }
        mustRemoveBecauseDead.clear();

        Player.getCombatInformation().draw(batch);
        energyLabel.setText(String.valueOf(Player.getEnergy()));

        if (Player.getCombatInformation().getNeedToPlayHitEffect()) {
            addActor(
                    new VisualEffect(
                            VisualEffectName.HIT,
                            Player.getPositionOnStage().x(),
                            Player.getPositionOnStage().y() + 40,
                            playerCharacter.getScaleX()
                    )
            );
            Player.getCombatInformation().setNeedToPlayHitEffect(false);
        }

        if (Player.potentiallyDiscardCards() || Player.isCombatMenuStageMustAddCard()) {
            int i = 0;
            int amountOfCards = Player.getHandContents().size;

            for (Card card : Player.getHandContents()) {
                // Reposition the cards
                float LEFTMOST_POSITION = 0;
                float RIGHTMOST_POSITION = 1140;
                float gapSize, positionX, positionY;

                // If there are more than 5 cards, some will be on a second, higher row behind the first row
                if (i >= 5) {
                    gapSize = (RIGHTMOST_POSITION - LEFTMOST_POSITION) / (amountOfCards - 5 + 1);
                    positionX = LEFTMOST_POSITION + gapSize * (i - 5 + 1);
                    positionY = 100;
                } else {
                    if (amountOfCards > 5) {
                        gapSize = (RIGHTMOST_POSITION - LEFTMOST_POSITION) / 6;
                    } else {
                        gapSize = (RIGHTMOST_POSITION - LEFTMOST_POSITION) / (amountOfCards + 1);
                    }
                    positionX = LEFTMOST_POSITION + gapSize * (i + 1);
                    positionY = 0;
                }

                card.clearActions();
                if (card.isToBeAddedToCombatMenuStage()) {
                    // Add it to the stage and snap its position to where it should be
                    addActor(card);
                    card.setToBeAddedToCombatMenuStage(false);
                    card.setPosition(positionX, positionY);
                } else {
                    // It's already on the stage, so slide it over
                    SequenceAction sequenceAction = new SequenceAction(
                            Actions.moveTo(positionX, positionY, 0.1f)
                    );
                    card.addAction(sequenceAction);
                }
                // Reset z index so it looks OK
                card.setZIndex(1);
                i++;
            }
            Player.setCombatMenuStageMustAddCard(false);
        }

        if (Player.combatMenuStageMustUpdatePileText) {
            updatePileText();
            updateMaximumAmountOfCardsLabel();
            Player.setCombatMenuStageMustUpdatePileText(false);
        }
    }

    private void positionTemporaryItems() {
        removeActorsByType(UserObjectOptions.ITEM);
        float GAP_X = 70;
        float GAP_Y = 70;
        float TOP_MOST = 720;
        float LEFT_MOST = 8;
        float MAX_AMOUNT_PER_COLUMN = 6;

        int amountOfTemporaryItems = 0;
        for (Item item : Player.getCombatInformation().getTemporaryItems()) {
            addActor(item);

            int rowNumber = (int) (amountOfTemporaryItems % MAX_AMOUNT_PER_COLUMN);
            int columnNumber = (int) Math.floor(amountOfTemporaryItems / MAX_AMOUNT_PER_COLUMN);

            item.setY(TOP_MOST - GAP_Y * rowNumber);
            item.setX(LEFT_MOST + GAP_X * columnNumber);
            amountOfTemporaryItems++;
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
                        for (int i = 0; i < currentEnemies.size; i++) {
                            currentEnemies.get(i).endTurn();
                        }
                        if (!currentEnemies.isEmpty()) {
                            Statistics.setTurnNumber(Statistics.getTurnNumber() + 1);
                            isPlayerTurn = true;
                            Player.startTurn();
                        }
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
            AudioManager.playFunnyTadaSound();
        }
    }

    public void addEnemy(CharacterTypeName characterTypeName) {
        Enemy enemy = new Enemy(characterTypeName, generateEnemyPosition());

        currentEnemies.add(enemy);
        enemy.putOnStage(getStage());
        enemy.getCombatInformation().getStatusEffectVisuals().setUserObject(UserObjectOptions.ENEMY);
        addActor(enemy.getCombatInformation().getStatusEffectVisuals());
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
        removeActorsByType(UserObjectOptions.ITEM);
        removeActorsByType(UserObjectOptions.INTENT);
        removeActorsByType(UserObjectOptions.STATUS_EFFECT);

        getScheduledDelays().clear();
        setPlayerCharacter();

        isPlayerTurn = true;

        victory = false;

        Statistics.setTurnNumber(1);
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

        updateMaximumAmountOfCardsLabel();

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
            playerCharacter.setTargeted(false);

            CombatHandler.resetIsTargetingValid();

            if (Player.getPotentialAbilityTargetType() == TargetType.SELF) {
                if (isPointWithinRange(mousePosition, Player.getPositionOnStage()) && currentEnemies.notEmpty()) {
                    CombatHandler.setEnemyTargets(currentEnemies);
                }
            } else {
                for (Enemy enemy : currentEnemies) {
                    if (isPointWithinRange(mousePosition, enemy.getPositionOnStage())) {
                        CombatHandler.setEnemyTargets(currentEnemies, enemy);
                        break;
                    }
                }
            }
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

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                if (pointer != 0) {
                    for (Card card : Player.getHandContents()) {
                        card.setZIndex(1);
                    }
                }
            }
        };
    }

    private boolean isPointWithinRange(XYPair<Float> point, XYPair<Float> positionOnStage) {
        float width = 220;
        float height = 400;
        float heightBottomOffset = 120;

        float left = positionOnStage.x() - width / 2;
        float right = positionOnStage.x() + width / 2;

        float bottom = positionOnStage.y() - height / 2 + heightBottomOffset;
        float top = positionOnStage.y() + height / 2 + heightBottomOffset;

        return (point.x() < right
                && point.x() > left
                && point.y() < top
                && point.y() > bottom);
    }

    private void updateMaximumAmountOfCardsLabel() {
        maximumAmountOfCardsLabel.setText(
                String.format("Cards in hand: %d / %d", Player.getHandContents().size, Player.MAXIMUM_CARDS_IN_HAND)
        );
    }
}
