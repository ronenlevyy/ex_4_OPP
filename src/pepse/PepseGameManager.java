package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.util.*;

/**
 * The main game manager class for the Pepse game.
 * It handles the initialization, updates, and rendering of the game world.
 */
public class PepseGameManager extends GameManager {


    ///////////////
    // Constants //
    ///////////////
    public static final float CYCLE_LENGTH = 30;
    public static final int SEED = 0;
    private static final int TREE_CREATION_BUFFER = 70; // Create trees 70 pixels ahead
    private static final int BLOCK_SIZE = 30;
    private static final int INITIAL_TERRAIN_WIDTH = 1500;

    ////////////////////////
    // Instance Variables //
    ////////////////////////
    private WindowController windowController;
    private Terrain terrain;
    private Flora flora;
    private Avatar avatar;
    private AvatarEnergy energy;
    private List<Tree> treeList;
    private Map<Integer, List<Block>> createdBlocks;
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;

    /////////////////////////
    // Static Game Objects //
    /////////////////////////
    private static GameObject night;
    private static GameObject sun;
    private static GameObject sunHalo;

    /**
     * Initializes the sky in the game world.
     */
    private void initializeSky() {
        Sky sky = new Sky();
        this.gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()),
                Layer.BACKGROUND);
    }

    /**
     * Initializes the terrain in the game world.
     */
    private void initializeTerrain() {
        terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        List<Block> blockList = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Block block : blockList) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
            createdBlocks.computeIfAbsent((int) block.getTopLeftCorner().x(), k -> new ArrayList<>()).add(block);
        }
        minX = 0;
        maxX = (int) windowController.getWindowDimensions().x();
    }

    /**
     * Updates the position of the energy display to be relative to the camera's x position.
     */
    private void updateEnergyPosition() {
        float cameraX = avatar.getTopLeftCorner().x() - windowController.getWindowDimensions().x() / 2;
        Vector2 energyPosition = new Vector2(cameraX + 100, 100); // 100 is the original y-coordinate
        energy.setTopLeftCorner(energyPosition);
    }


    /**
     * Updates override of update to contain updateTerrain and updateEnergyPosition;
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTerrain();
        updateEnergyPosition();

    }


    /**
     * Updates the terrain and trees in the game world based on the avatar's position.
     */
    private void updateTerrain() {
        int[] newBounds = calculateNewBounds();
        updateTerrainBlocks(newBounds[0], newBounds[1]);
        createNewTrees(newBounds[0], newBounds[1]);
        updateBounds(newBounds[0], newBounds[1]);
    }

    /**
     * Calculates the new bounds for the terrain based on the avatar's position.
     * @return An array containing the new minimum and maximum x coordinates.
     */
    private int[] calculateNewBounds() {
        float avatarX = avatar.getTopLeftCorner().x();
        int avatarBlockX = (int) Math.floor(avatarX / BLOCK_SIZE) * BLOCK_SIZE;
        int newMinX = avatarBlockX - INITIAL_TERRAIN_WIDTH / 2;
        int newMaxX = avatarBlockX + INITIAL_TERRAIN_WIDTH / 2;
        return new int[]{newMinX, newMaxX};
    }


    /**
     * Updates the terrain blocks within the new bounds.
     * @param newMinX The new minimum x coordinate.
     * @param newMaxX The new maximum x coordinate.
     */
    private void updateTerrainBlocks(int newMinX, int newMaxX) {
        createNewTerrainBlocks(newMinX, newMaxX);
        removeOldTerrainBlocks(newMinX, newMaxX);
    }


    /**
     * Creates new terrain blocks within the specified range.
     * @param newMinX The new minimum x coordinate.
     * @param newMaxX The new maximum x coordinate.
     */
    private void createNewTerrainBlocks(int newMinX, int newMaxX) {
        if (newMinX < minX) {
            createBlocksInRange(newMinX, minX);
        }
        if (newMaxX > maxX) {
            createBlocksInRange(maxX, newMaxX);
        }
    }


    /**
     * Creates blocks within the specified range.
     * @param start The starting x coordinate.
     * @param end The ending x coordinate.
     */
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


    /**
     * Removes old terrain blocks outside the specified range.
     * @param newMinX The new minimum x coordinate.
     * @param newMaxX The new maximum x coordinate.
     */
    private void removeOldTerrainBlocks(int newMinX, int newMaxX) {
        removeBlocksOutsideRange(minX, newMinX);
        removeBlocksOutsideRange(newMaxX, maxX);
    }


    /**
     * Removes blocks outside the specified range.
     * @param start The starting x coordinate.
     * @param end The ending x coordinate.
     */
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


    /**
     * Creates new trees within the specified range.
     * @param newMinX The new minimum x coordinate.
     * @param newMaxX The new maximum x coordinate.
     */
    private void createNewTrees(int newMinX, int newMaxX) {
        if (newMinX < minX) {
            createTreesInRange(newMinX - TREE_CREATION_BUFFER, minX);
        }
        if (newMaxX > maxX) {
            createTreesInRange(maxX, newMaxX + TREE_CREATION_BUFFER);
        }
    }


    /**
     * Updates the bounds for the terrain and trees.
     * @param newMinX The new minimum x coordinate.
     * @param newMaxX The new maximum x coordinate.
     */
    private void updateBounds(int newMinX, int newMaxX) {
        minX = newMinX;
        maxX = newMaxX;
    }


    /**
     * Creates trees within the specified range.
     * @param minX The minimum x coordinate.
     * @param maxX The maximum x coordinate.
     */
    private void createTreesInRange(int minX, int maxX) {
        List<Tree> newTrees = flora.createInRange(minX, maxX);
        for (Tree tree : newTrees) {
            tree.addTree(gameObjects());
            avatar.addJumpCallback(tree);
            treeList.add(tree);
        }
    }


    /**
     * Initializes the game by setting up the sky, terrain, night, sun, halo, energy display, and avatar.
     * It also initializes the trees and sets the camera to follow the avatar.
     *
     * @param imageReader Used to read images from the disk.
     * @param soundReader Used to read sound effects and background music from the disk.
     * @param inputListener Used to receive user input from the keyboard and mouse.
     * @param windowController Used to control the window properties such as dimensions.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener
            inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        createdBlocks = new HashMap<>();

        //create - sky
        initializeSky();

        //create - ground
        initializeTerrain();

        //create - night
        night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND);

        //create - sun
        sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);

        //create - halo
        sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 1);

        //create - energy
        energy = new AvatarEnergy(new Vector2(100, 100), new Vector2(30, 30), new TextRenderable("100"));
        this.gameObjects().addGameObject(energy);

        // Compute initial avatar position
        float initialAvatarX = windowController.getWindowDimensions().x() / 2;
        float groundHeight = terrain.groundHeightAt(initialAvatarX);
        Vector2 initialAvatarPosition = new Vector2(initialAvatarX, groundHeight - 30);
        //create - avatar
        avatar = new Avatar(initialAvatarPosition, inputListener, imageReader, energy::changeEnergy);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        //create - trees
        flora = new Flora(SEED, terrain, () -> avatar.setEnergy(AvatarEnergy.FRUIT_ENERGY));
        treeList = new ArrayList<>();
        // Initial tree creation
        createTreesInRange(minX, maxX);


        setCamera(new Camera(avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarPosition),
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }

    /**
     * The main method to start the Pepse game.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
