package pepse;

import danogl.GameManager; // ודא שיש לך את ההפניה הנכונה ל-GameManager
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.util.*;


public class PepseGameManager extends GameManager {
    /**
     * The length of the day-night cycle in seconds.
     */
    public static final float CYCLE_LENGTH = 30;

    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private static GameObject night;
    private static GameObject sun;
    private static GameObject sunHalo;
    private static Avatar avatar;
    public static final int SEED = 0;
    private static final int TREE_CREATION_BUFFER = 200; // Create trees 500 pixels ahead
    private Flora flora;


    ///todo- in order to make infinite world we need this
    private static final int BLOCK_SIZE = 30;
    private static final int INITIAL_TERRAIN_WIDTH = 1500;
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private Terrain terrain;
    private Map<Integer, List<Block>> createdBlocks;
    private List<Tree> treeList;

    //////////////////////////////////////////////////////


    private void initializeSky() {
        Sky sky = new Sky();
        this.gameObjects().addGameObject(sky.create(windowController.getWindowDimensions()),
                Layer.BACKGROUND);

    }


    private void initializeTerrain() {
        terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        List<Block> blockList = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Block block : blockList) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
            createdBlocks.computeIfAbsent((int) block.getTopLeftCorner().x(),
                    k -> new ArrayList<>()).add(block);
        }
        minX = 0;
        maxX = (int) windowController.getWindowDimensions().x();


    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTerrain();

    }


    private void updateTerrain() {
        int[] newBounds = calculateNewBounds();
        updateTerrainBlocks(newBounds[0], newBounds[1]);
        createNewTrees(newBounds[1]);
        removeOldTrees(newBounds[0]); // Ensure this line is present
        updateBounds(newBounds[0], newBounds[1]);
    }

    private int[] calculateNewBounds() {
        float avatarX = avatar.getTopLeftCorner().x();
        int avatarBlockX = (int) Math.floor(avatarX / BLOCK_SIZE) * BLOCK_SIZE;
        int newMinX = avatarBlockX - INITIAL_TERRAIN_WIDTH / 2;
        int newMaxX = avatarBlockX + INITIAL_TERRAIN_WIDTH / 2;
        return new int[]{newMinX, newMaxX};
    }

    private void updateTerrainBlocks(int newMinX, int newMaxX) {
        createNewTerrainBlocks(newMinX, newMaxX);
        removeOldTerrainBlocks(newMinX, newMaxX);
    }

    private void createNewTerrainBlocks(int newMinX, int newMaxX) {
        if (newMinX < minX) {
            createBlocksInRange(newMinX, minX);
        }
        if (newMaxX > maxX) {
            createBlocksInRange(maxX, newMaxX);
        }
    }

    private void createBlocksInRange(int start, int end) {
        for (int x = start; x < end; x += BLOCK_SIZE) {
            if (!createdBlocks.containsKey(x)) {
                List<Block> blocks = terrain.createInRange(x, x + BLOCK_SIZE);
                createdBlocks.put(x, blocks);
                for (Block block : blocks) {
                    gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
                }
            }
        }
    }

    private void removeOldTerrainBlocks(int newMinX, int newMaxX) {
        removeBlocksOutsideRange(minX, newMinX);
        removeBlocksOutsideRange(newMaxX, maxX);
    }

    private void removeBlocksOutsideRange(int start, int end) {
        for (int x = start; x < end; x += BLOCK_SIZE) {
            if (createdBlocks.containsKey(x)) {
                List<Block> blocks = createdBlocks.get(x);
                for (Block block : blocks) {
                    gameObjects().removeGameObject(block, Layer.STATIC_OBJECTS);
                }
                createdBlocks.remove(x);
            }
        }
    }


    private void createNewTrees(int newX) {
        if (newX < minX) {
            createTreesInRange(newX, minX);
        }

        if (newX > maxX) {
            createTreesInRange(maxX, newX + TREE_CREATION_BUFFER);
        }

    }

    private void removeOldTrees(int newMinX) {
        Iterator<Tree> iterator = treeList.iterator();
        while (iterator.hasNext()) {
            Tree tree = iterator.next();
            if (tree.getTopLeftCorner().x() < newMinX - TREE_CREATION_BUFFER) {
                tree.removeTree(gameObjects());
                avatar.removeJumpCallback(tree);
                iterator.remove();
            }
        }

        if (!treeList.isEmpty()) {
            Tree lastTree = treeList.get(treeList.size() - 1);
            flora.setLastTreeX((int) lastTree.getTopLeftCorner().x());
        }
    }

    private void updateBounds(int newMinX, int newMaxX) {
        minX = newMinX;
        maxX = newMaxX;
    }


    private void createTreesInRange(int minX, int maxX) {
        List<Tree> newTrees = flora.createInRange(minX, maxX);
        for (Tree tree : newTrees) {
            tree.addTree(gameObjects());
            avatar.addJumpCallback(tree);
            treeList.add(tree);
        }
    }


    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener
            inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        createdBlocks = new HashMap<>();

        //create - sky
        initializeSky();

        // Initialize terrain boundaries
//    minX = Integer.MAX_VALUE;
//    maxX = Integer.MIN_VALUE;

        //create - ground
        initializeTerrain();

        //create - night
        night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND); // todo: ensure this is the correct layer

        //create - sun
        sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);

        //create - halo
        sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 1);

        //create - energy
        AvatarEnergy energy = new AvatarEnergy(new Vector2(100, 100), new Vector2(30, 30),
                new TextRenderable("100"));
        this.gameObjects().addGameObject(energy);

        // Compute initial avatar position
        float initialAvatarX = windowController.getWindowDimensions().x() / 2;
        float groundHeight = terrain.groundHeightAt(initialAvatarX);
        Vector2 initialAvatarPosition = new Vector2(initialAvatarX, groundHeight - 30); // Assuming avatar
        // height is 30
        //create - avatar
        avatar = new Avatar(initialAvatarPosition, inputListener, imageReader, energy::changeEnergy);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        //create - trees
        flora = new Flora(SEED, terrain, () -> avatar.setEnergy(AvatarEnergy.FRUIT_ENERGY));
        treeList = new ArrayList<>();
        // Initial tree creation
        createTreesInRange(0, (int) windowController.getWindowDimensions().x());


        setCamera(new Camera(avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarPosition),
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }


    public static void main(String[] args) {
        new PepseGameManager().run();
    }


}