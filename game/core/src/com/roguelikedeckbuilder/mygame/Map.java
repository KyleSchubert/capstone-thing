package com.roguelikedeckbuilder.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.IntStream;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.roguelikedeckbuilder.mygame.MyGame.SCALE_FACTOR;

public class Map {
    ShapeRenderer shapeRenderer;

    public enum MapNodeType {
        NORMAL_BATTLE, ELITE_BATTLE, BOSS_BATTLE, START, SHOP, REST, RANDOM_EVENT, TREASURE
    }

    public HashMap<MapNodeType, Integer> mapNodeTypeWeights;
    public int weightSum;
    public final Stage mapStage;
    private final Image mapBackground;
    private final Array<Array<MapNode>> mapNodes;
    private static final int MAX_STAGES = 10; // Minimum of 2: 1 for start, 1 for boss.
    private static final int MAX_NODES_PER_STAGE = 6;
    private static final int MIN_NODES_PER_STAGE = 4;
    private static final int MAX_NEXT_CONNECTIONS_PER_NODE = 2;
    private static final int MIN_ELITE_BATTLE = 3;
    private static final int MIN_SHOP = 2;
    private static final int MIN_REST = 4;
    private static final int MIN_TREASURE = 2;
    private static final int MAX_ELITE_BATTLE = 8;
    private static final int MAX_SHOP = 6;
    private static final int MAX_REST = 8;
    private static final int MAX_TREASURE = 6;
    private static final int NO_REST_NODES_BEFORE_STAGE_NUMBER = 3;
    private static final int NO_SHOP_NODES_BEFORE_STAGE_NUMBER = 4;
    private int currentNodeStage = 0;
    private int currentNodeIndex = 0;
    private final ClickListener hoverAndClickListener;

    public Map(ScreenViewport viewportForStage, ClickListener hoverAndClickListener) {
        mapStage = new Stage(viewportForStage);
        mapBackground = new Image(new Texture(Gdx.files.internal("MENU backgrounds/map background.png")));
        mapBackground.setSize(1442 * SCALE_FACTOR, 952 * SCALE_FACTOR);

        mapNodes = new Array<>();
        shapeRenderer = new ShapeRenderer();

        this.hoverAndClickListener = hoverAndClickListener;

        mapNodeTypeWeights = new HashMap<>();
        mapNodeTypeWeights.put(MapNodeType.NORMAL_BATTLE, 60);
        mapNodeTypeWeights.put(MapNodeType.ELITE_BATTLE, 10);
        mapNodeTypeWeights.put(MapNodeType.SHOP, 10);
        mapNodeTypeWeights.put(MapNodeType.REST, 20);
        mapNodeTypeWeights.put(MapNodeType.RANDOM_EVENT, 15);
        mapNodeTypeWeights.put(MapNodeType.TREASURE, 10);

        weightSum = mapNodeTypeWeights.values().stream().reduce(0, Integer::sum);

        reset();
    }

    public void batch(float elapsedTime) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int stageNumber = 0; stageNumber < MAX_STAGES; stageNumber++) {
            for (int i = 0; i < mapNodes.get(stageNumber).size; i++) {
                MapNode currentNode = getMapNode(stageNumber, i);

                for (int connection : getMapNode(stageNumber, i).nextConnections) {
                    MapNode nextNode = getMapNode(stageNumber + 1, connection);

                    if (nextNode.isCompleted() && currentNode.isCompleted()) {
                        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
                    } else if (stageNumber == currentNodeStage && i == currentNodeIndex) {
                        // If the node is the current node, show the player's next option paths in a different color
                        shapeRenderer.setColor(0, 1, 1, 1);
                    } else {
                        shapeRenderer.setColor(0.4f, 0.3f, 0.3f, 1);
                    }

                    shapeRenderer.rectLine(
                            (currentNode.getPosX() + 1.5f) / SCALE_FACTOR,
                            (currentNode.getPosY() + 1.5f) / SCALE_FACTOR,
                            (nextNode.getPosX() + 1.5f) / SCALE_FACTOR,
                            (nextNode.getPosY() + 1.5f) / SCALE_FACTOR,
                            4
                    );
                }
            }
        }
        shapeRenderer.end();

        mapStage.getViewport().apply();
        mapStage.act(elapsedTime);
        mapStage.draw();
    }

    public void generateMap() {
        int randomNumberOfNodes;

        // Create all the nodes
        for (int stageNumber = 0; stageNumber < MAX_STAGES; stageNumber++) {
            if (stageNumber == 0 || stageNumber == MAX_STAGES - 1) {
                randomNumberOfNodes = 1;
            } else {
                randomNumberOfNodes = random.nextInt(MAX_NODES_PER_STAGE - (MIN_NODES_PER_STAGE - 1)) + MIN_NODES_PER_STAGE;
            }

            Array<MapNode> mapNodesForStage = new Array<>();
            for (int i = 0; i < randomNumberOfNodes; i++) {
                MapNode mapNode = new MapNode(stageNumber, i, randomNumberOfNodes);
                mapNodesForStage.add(mapNode);
            }
            mapNodes.add(mapNodesForStage);
        }

        // Assign the next connections to each node
        for (int stageNumber = 0; stageNumber < MAX_STAGES; stageNumber++) {
            for (int i = 0; i < mapNodes.get(stageNumber).size; i++) {
                // Connect the starting node to all the next nodes, and connect the second-to-last nodes to the last node
                if (stageNumber == 0 || stageNumber == MAX_STAGES - 2) {
                    for (int nodeIndex = 0; nodeIndex < mapNodes.get(stageNumber + 1).size; nodeIndex++) {
                        getMapNode(stageNumber, i).addNextConnection(nodeIndex);
                        getMapNode(stageNumber + 1, nodeIndex).addPreviousConnection(i);
                    }
                }
                // Do not connect the final node to any next nodes
                else if (stageNumber != MAX_STAGES - 1) {
                    int targetStageNumber = stageNumber + 1;
                    addClosestConnectionsToNode(stageNumber, i, targetStageNumber);
                }
            }
        }

        // Run through nodes backwards to confirm the connections ignoring the start and end
        for (int stageNumber = MAX_STAGES - 2; stageNumber > 0; stageNumber--) {
            for (int i = 0; i < mapNodes.get(stageNumber).size; i++) {

                if (getMapNode(stageNumber, i).previousConnections.size == 0) {
                    int targetStageNumber = stageNumber - 1;
                    addClosestConnectionsToNode(stageNumber, i, targetStageNumber);
                }
            }
        }

        // These can bypass the rules of how early a node can spawn

        // Ensure the minimum amount of elite battles is met and the maximum is respected
        Array<MapNode> eliteNodes = findNodesByType(MapNodeType.ELITE_BATTLE);
        int eliteNodeCount = eliteNodes.size;
        while (eliteNodeCount < MIN_ELITE_BATTLE) {
            System.out.println("ELITE NODES: " + eliteNodeCount + " | ADDING MORE ELITE BATTLES");
            replaceOneRandomNodeByType(MapNodeType.NORMAL_BATTLE, MapNodeType.ELITE_BATTLE);
            eliteNodeCount++;
        }
        while (eliteNodeCount > MAX_ELITE_BATTLE) {
            System.out.println("ELITE NODES: " + eliteNodeCount + " | REMOVING ELITE BATTLES");
            replaceOneRandomNodeByType(MapNodeType.ELITE_BATTLE, MapNodeType.NORMAL_BATTLE);
            eliteNodeCount--;
        }

        // Ensure the minimum amount of shops is met and the maximum is respected
        Array<MapNode> shopNodes = findNodesByType(MapNodeType.SHOP);
        int shopNodeCount = shopNodes.size;
        while (shopNodeCount < MIN_SHOP) {
            System.out.println("SHOP NODES: " + shopNodeCount + " | ADDING MORE SHOPS");
            replaceOneRandomNodeByType(MapNodeType.NORMAL_BATTLE, MapNodeType.SHOP);
            shopNodeCount++;
        }
        while (shopNodeCount > MAX_SHOP) {
            System.out.println("SHOP NODES: " + shopNodeCount + " | REMOVING SHOPS");
            replaceOneRandomNodeByType(MapNodeType.SHOP, MapNodeType.NORMAL_BATTLE);
            shopNodeCount--;
        }

        // Ensure the minimum amount of rest areas is met and the maximum is respected
        Array<MapNode> restNodes = findNodesByType(MapNodeType.REST);
        int restNodeCount = restNodes.size;
        while (restNodeCount < MIN_REST) {
            System.out.println("REST NODES: " + restNodeCount + " | ADDING MORE REST AREAS");
            replaceOneRandomNodeByType(MapNodeType.NORMAL_BATTLE, MapNodeType.REST);
            restNodeCount++;
        }
        while (restNodeCount > MAX_REST) {
            System.out.println("REST NODES: " + shopNodeCount + " | REMOVING REST AREAS");
            replaceOneRandomNodeByType(MapNodeType.REST, MapNodeType.NORMAL_BATTLE);
            restNodeCount--;
        }

        // Ensure the minimum amount of treasure areas is met and the maximum is respected
        Array<MapNode> treasureNodes = findNodesByType(MapNodeType.TREASURE);
        int treasureNodeCount = treasureNodes.size;
        while (treasureNodeCount < MIN_TREASURE) {
            System.out.println("TREASURE NODES: " + treasureNodeCount + " | ADDING MORE TREASURE");
            replaceOneRandomNodeByType(MapNodeType.NORMAL_BATTLE, MapNodeType.TREASURE);
            treasureNodeCount++;
        }
        while (treasureNodeCount > MAX_TREASURE) {
            System.out.println("TREASURE NODES: " + treasureNodeCount + " | REMOVING TREASURE");
            replaceOneRandomNodeByType(MapNodeType.TREASURE, MapNodeType.NORMAL_BATTLE);
            treasureNodeCount--;
        }

        // Complete the start node
        completeNode(0, 0);
    }

    public void drawImagesAndAddActors() {
        // Create and use the images of all nodes
        for (int stageNumber = 0; stageNumber < MAX_STAGES; stageNumber++) {
            for (int i = 0; i < mapNodes.get(stageNumber).size; i++) {
                getMapNode(stageNumber, i).prepareNodeImage();
                getMapNode(stageNumber, i).getImage().addListener(hoverAndClickListener);
                getMapNode(stageNumber, i).getImage().setUserObject(getMapNode(stageNumber, i).getMapNodeData());
                mapStage.addActor(getMapNode(stageNumber, i).getImage());
            }
        }
    }

    private MapNode getMapNode(int stageNumber, int i) {
        return mapNodes.get(stageNumber).get(i);
    }

    private void replaceOneRandomNodeByType(MapNodeType undesiredType, MapNodeType desiredType) {
        Array<MapNode> nodes = findNodesByType(undesiredType);
        // If there are no spare nodes of the undesiredType, replace ANYTHING else and it should work out
        for (MapNodeType key : mapNodeTypeWeights.keySet()) {
            if (nodes.size != 0) {
                break;
            }
            nodes = findNodesByType(key);
        }
        int randomIndex = random.nextInt(nodes.size);
        int targetStage = nodes.get(randomIndex).stageNumberOfSelf;
        int targetIndex = nodes.get(randomIndex).indexOfSelf;
        System.out.println("Changed node at stage " + targetStage + " index " + targetIndex);
        getMapNode(targetStage, targetIndex).nodeType = desiredType;
    }

    private void addClosestConnectionsToNode(int stageNumber, int i, int targetStageNumber) {
        Array<MapNode> options = mapNodes.get(targetStageNumber);
        int numberOfOptions = options.size;
        int nConnections = Math.min(random.nextInt(MAX_NEXT_CONNECTIONS_PER_NODE) + 1, numberOfOptions);

        // Fewer nodes in next set of stages = only 1 connection
        if (numberOfOptions < mapNodes.get(stageNumber).size) {
            nConnections = 1;
        }

        // Randomly add more connections
        if (random.nextInt(10) > 8) {
            nConnections = Math.min(nConnections + 1, MAX_NEXT_CONNECTIONS_PER_NODE);
        }


        // Calculate ordering of closest potential next nodes in the following stage
        float currentStageNumberOfNodes = mapNodes.get(stageNumber).size;
        // Ex: 3 nodes --> 0/2 = 0%, 1/2 = 50%, 2/2 = 100%
        float yPositionPercent = (float) i / (currentStageNumberOfNodes - 1);
        yPositionPercent *= 1000;
        int yPositionSignificantDigits = (int) yPositionPercent;

        // Create the array of distances between all next nodes
        Integer[] yPositionPercentsOfOptions = new Integer[numberOfOptions];
        for (int k = 0; k < options.size; k++) {
            float percentage = (float) k / (numberOfOptions - 1);
            percentage *= 1000;
            int significantDigits = (int) percentage;

            // Randomly change the integers, so that equal numbers won't always take the first one (the lower node)
            significantDigits += random.nextInt(9) - 4;

            yPositionPercentsOfOptions[k] = Math.abs(yPositionSignificantDigits - significantDigits);
        }

        // Get an order of indexes of the closest nodes
        int[] sortedIndices = IntStream.range(0, yPositionPercentsOfOptions.length)
                .boxed().sorted(Comparator.comparing(k -> yPositionPercentsOfOptions[k]))
                .mapToInt(ele -> ele).toArray();

        // Use that order of indexes to add connections to the closest nodes
        for (int connection = 0; connection < nConnections; connection++) {
            if (targetStageNumber < stageNumber) {
                getMapNode(targetStageNumber, sortedIndices[connection]).addNextConnection(i);
                getMapNode(stageNumber, i).addPreviousConnection(sortedIndices[connection]);
            } else {
                getMapNode(stageNumber, i).addNextConnection(sortedIndices[connection]);
                getMapNode(targetStageNumber, sortedIndices[connection]).addPreviousConnection(i);
            }
        }
    }

    public void drawMap(SpriteBatch batch) {
        mapBackground.setPosition(0, -1);
        mapBackground.draw(batch, 1);
    }

    private Array<MapNode> findNodesByType(MapNodeType type) {
        Array<MapNode> results = new Array<>();
        for (Array<MapNode> stage : mapNodes) {
            for (MapNode mapNode : stage) {
                if (mapNode.nodeType == type) {
                    results.add(mapNode);
                }
            }
        }
        return results;
    }

    public void completeNode(int stage, int index) {
        getMapNode(stage, index).complete();
        currentNodeStage = stage;
        currentNodeIndex = index;

        // If the node that was just completed was the last node
        if (currentNodeStage == MAX_STAGES - 1) {
            reset();
        }
    }

    public boolean isValidChoice(int stage, int index) {
        for (int nodeIndex : getMapNode(stage, index).previousConnections) {
            if (stage - 1 == currentNodeStage && nodeIndex == currentNodeIndex) {
                return true;
            }
        }
        return false;
    }

    private void reset() {
        mapStage.clear();
        mapNodes.clear();
        generateMap();
        drawImagesAndAddActors();
    }

    public void dispose() {
        mapStage.dispose();
    }

    public class MapNode {
        public record MapNodeData(
                int stageNumberOfSelf,
                int indexOfSelf,
                Map.MapNodeType nodeType,
                Tooltip.Size tooltipSize,
                Tooltip.Location tooltipLocation
        ) {
        }

        private Image nodeImage;
        private final float posX;
        private final float posY;
        private static final float POS_X_MIN = 4;
        private static final float POS_X_MAX = 68;
        private static final float POS_Y_MIN = 5;
        private static final float POS_Y_MAX = 38;
        private final Array<Integer> nextConnections;
        private final Array<Integer> previousConnections;
        private final int stageNumberOfSelf;
        private final int indexOfSelf;
        private MapNodeType nodeType;
        private boolean isCompleted = false;

        public MapNode(int stageNumber, int thisNodesFutureIndex, int numberOfNodesInThisStage) {
            nextConnections = new Array<>();
            previousConnections = new Array<>();
            nodeType = MapNodeType.NORMAL_BATTLE;

            if (stageNumber == 0) {
                posX = POS_X_MIN;
                posY = (POS_Y_MAX + POS_Y_MIN) / 2;
                nodeType = MapNodeType.START;
            } else if (stageNumber == MAX_STAGES - 1) {
                posX = POS_X_MAX;
                posY = (POS_Y_MAX + POS_Y_MIN) / 2;
                nodeType = MapNodeType.BOSS_BATTLE;
            } else {
                posX = ((POS_X_MAX - POS_X_MIN) / (MAX_STAGES - 1)) * stageNumber + POS_X_MIN;
                posY = ((POS_Y_MAX - POS_Y_MIN) / (numberOfNodesInThisStage - 1)) * thisNodesFutureIndex + POS_Y_MIN;

                int usedNodeTypeWeight = weightSum;
                // No rest nodes earlier than this stage number
                if (stageNumber < NO_REST_NODES_BEFORE_STAGE_NUMBER) {
                    usedNodeTypeWeight -= mapNodeTypeWeights.get(MapNodeType.REST);
                }

                // No shop nodes earlier than this stage number
                if (stageNumber < NO_SHOP_NODES_BEFORE_STAGE_NUMBER) {
                    usedNodeTypeWeight -= mapNodeTypeWeights.get(MapNodeType.SHOP);
                }

                int randomNumber = random.nextInt(usedNodeTypeWeight);

                for (MapNodeType key : mapNodeTypeWeights.keySet()) {
                    if (stageNumber >= NO_SHOP_NODES_BEFORE_STAGE_NUMBER || key != MapNodeType.SHOP) {
                        if (stageNumber >= NO_REST_NODES_BEFORE_STAGE_NUMBER || key != MapNodeType.REST) {
                            randomNumber -= mapNodeTypeWeights.get(key);
                            if (randomNumber < 0) {
                                nodeType = key;
                                break;
                            }
                        }
                    }
                }
            }

            indexOfSelf = thisNodesFutureIndex;
            stageNumberOfSelf = stageNumber;
        }

        public void prepareNodeImage() {
            String filePath = "MAP NODES/";
            int x, y;

            switch (nodeType) {
                case NORMAL_BATTLE:
                    filePath += "normal battle.png";
                    x = 26;
                    y = 26;
                    break;
                case ELITE_BATTLE:
                    filePath += "elite battle.png";
                    x = 36;
                    y = 32;
                    break;
                case BOSS_BATTLE:
                    filePath += "boss battle.png";
                    x = 36;
                    y = 33;
                    break;
                case START:
                    filePath += "start.png";
                    x = 33;
                    y = 28;
                    break;
                case SHOP:
                    filePath += "shop.png";
                    x = 32;
                    y = 33;
                    break;
                case REST:
                    filePath += "rest.png";
                    x = 34;
                    y = 26;
                    break;
                case RANDOM_EVENT:
                    filePath += "random event.png";
                    x = 33;
                    y = 32;
                    break;
                case TREASURE:
                    filePath += "treasure.png";
                    x = 29;
                    y = 31;
                    break;
                default:
                    filePath += "default.png";
                    x = 29;
                    y = 31;
                    break;
            }
            nodeImage = new Image(new Texture(Gdx.files.internal(filePath)));
            nodeImage.setSize(x * SCALE_FACTOR * 2, y * SCALE_FACTOR * 2);
            nodeImage.setPosition(posX, posY);
        }

        public Image getImage() {
            return nodeImage;
        }

        public float getPosX() {
            return posX;
        }

        public float getPosY() {
            return posY;
        }

        public void addNextConnection(int nodeIndex) {
            this.nextConnections.add(nodeIndex);
        }

        public void addPreviousConnection(int nodeIndex) {
            this.previousConnections.add(nodeIndex);
        }

        public void complete() {
            this.isCompleted = true;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public MapNodeData getMapNodeData() {
            Tooltip.Location desiredLocation;
            if (this.stageNumberOfSelf <= MAX_STAGES * 0.5) {
                desiredLocation = Tooltip.Location.RIGHT;
            } else {
                desiredLocation = Tooltip.Location.LEFT;
            }

            return new MapNodeData(
                    this.stageNumberOfSelf,
                    this.indexOfSelf,
                    this.nodeType,
                    Tooltip.Size.SMALL,
                    desiredLocation
            );
        }
    }
}
