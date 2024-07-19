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
    public static final float CYCLE_LENGTH=30;

    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private static GameObject night;
    private static GameObject sun;
    private static GameObject sunHalo;
    private static Avatar avatar;
    public static final int SEED = 0;



    ///todo- in order to make infinite world we need this
    private static final int BLOCK_SIZE = 30;
    private static final int INITIAL_TERRAIN_WIDTH = 1500;
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private Terrain terrain;
    private Map<Integer, List<Block>> createdBlocks;
    //////////////////////////////////////////////////////




    private void initializeSky(){
        Sky sky = new Sky();
        this.gameObjects().addGameObject(sky.create(windowController.getWindowDimensions()),
                Layer.BACKGROUND);

    }




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



    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTerrain();
    }

    private void updateTerrain() {
        float avatarX = avatar.getTopLeftCorner().x();
        int avatarBlockX = (int) Math.floor(avatarX / BLOCK_SIZE) * BLOCK_SIZE;
        int newMinX = avatarBlockX - INITIAL_TERRAIN_WIDTH / 2;
        int newMaxX = avatarBlockX + INITIAL_TERRAIN_WIDTH / 2;

        // עדכון התחום התחתון
        if (newMinX < minX) {
            for (int x = newMinX; x < minX; x += BLOCK_SIZE) {
                if (!createdBlocks.containsKey(x)) {
                    List<Block> blocks = terrain.createInRange(x, x + BLOCK_SIZE);
                    createdBlocks.put(x, blocks);
                    for (Block block : blocks) {
                        gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
                    }
                }
            }
            minX = newMinX;
        }

        // עדכון התחום העליון
        if (newMaxX > maxX) {
            for (int x = maxX; x < newMaxX; x += BLOCK_SIZE) {
                if (!createdBlocks.containsKey(x)) {
                    List<Block> blocks = terrain.createInRange(x, x + BLOCK_SIZE);
                    createdBlocks.put(x, blocks);
                    for (Block block : blocks) {
                        gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
                    }
                }
            }
            maxX = newMaxX;
        }

        // מחיקת אובייקטים מחוץ לטווח התחום התחתון
        for (int x = minX; x < newMinX; x += BLOCK_SIZE) {
            if (createdBlocks.containsKey(x)) {
                List<Block> blocks = createdBlocks.get(x);
                for (Block block : blocks) {
                    gameObjects().removeGameObject(block, Layer.STATIC_OBJECTS);
                }
                createdBlocks.remove(x);
            }
        }

        // מחיקת אובייקטים מחוץ לטווח התחום העליון
        for (int x = maxX; x > newMaxX; x -= BLOCK_SIZE) {
            if (createdBlocks.containsKey(x)) {
                List<Block> blocks = createdBlocks.get(x);
                for (Block block : blocks) {
                    gameObjects().removeGameObject(block, Layer.STATIC_OBJECTS);
                }
                createdBlocks.remove(x);
            }
        }

        minX = newMinX;
        maxX = newMaxX;
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
//        minX = Integer.MAX_VALUE;
//        maxX = Integer.MIN_VALUE;

        //create - ground
        initializeTerrain();

        //create - night
        night= Night.create(windowController.getWindowDimensions(),CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND); // todo: ensure this is the correct layer

        //create - sun
        sun= Sun.create(windowController.getWindowDimensions(),CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND+1);

        //create - halo
        sunHalo= SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND+1);

        //create - energy
        AvatarEnergy energy = new AvatarEnergy(new Vector2(100, 100), new Vector2(30, 30),
                new TextRenderable("100"));
        this.gameObjects().addGameObject(energy);


        //create - avatar
        avatar = new Avatar(new Vector2(850,500),inputListener,imageReader,energy::changeEnergy); //todo: this is a demo avatar we will need to make it more specific to the instructions
        gameObjects().addGameObject(avatar,Layer.DEFAULT);

        //create - trees
        createTrees(terrain, windowController.getWindowDimensions());

        //todo- we will set the camera when the infinite world will be ready to go
        setCamera(new Camera(avatar, Vector2.ZERO, windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

    }

    private void createTrees(Terrain tet, Vector2 windowDimensions){
        Runnable setEnergy = () -> avatar.setEnergy(AvatarEnergy.FRUIT_ENERGY);
        Flora flora= new Flora(SEED, tet, setEnergy);
        List<Tree> treeList = flora.createInRange(0, (int) windowDimensions.x());
        for (Tree tree:treeList){
            avatar.addJumpCallback(tree);
            tree.addTree(gameObjects());

        }
    }



    public static void main(String[] args) {
        new PepseGameManager().run();
    }


}