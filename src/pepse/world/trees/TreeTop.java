package pepse.world.trees;

import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This class is responsible for creating and managing the tree top,
 * including the leaves and fruits in the game.
 */
public class TreeTop implements CallbackAvatarJump {

    // Constants
    private static final double LEAF_PLANT_PROBABILITY = 0.7;
    private static final double FRUIT_PLANT_PROBABILITY = 0.5;
    private static final int SIZE = 5;
    private static final int TREE_TOP_SIZE = 2;

    private Vector2 topLeftCorner;
    private Random rand;
    private List<Leaf> leaves;
    private List<Fruit> fruits;
    private Runnable setEnergy;
    private int treeHeight;
    private boolean[][] is_occupied;


    /**
     * Constructs a new TreeTop instance.
     *
     * @param topLeftCorner The top-left corner of the tree's position.
     * @param rand          Random instance for generating tree attributes.
     * @param treeHeight    The height of the tree.
     * @param setEnergy     A runnable to set the energy.
     */
    public TreeTop(Vector2 topLeftCorner, Random rand, int treeHeight, Runnable setEnergy) {
        this.topLeftCorner = topLeftCorner.subtract(new Vector2(Block.SIZE, Block.SIZE).mult(TREE_TOP_SIZE))
                .subtract(new Vector2(0, Block.SIZE * treeHeight));
        this.rand = rand;
        this.treeHeight = treeHeight;
        this.setEnergy = setEnergy;
        this.is_occupied = new boolean[SIZE][SIZE];
        initOccupation();

        createLeaves();
        createFruits();
    }


    /**
     * Initializes the occupation matrix for the tree top.
     */
    private void initOccupation() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.is_occupied[i][j] = false;
            }
        }
    }


    /**
     * Creates the leaves of the tree top.
     */
    public void createLeaves() {
        leaves = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (rand.nextFloat() < LEAF_PLANT_PROBABILITY && !is_occupied[i][j]) {
                    float x = topLeftCorner.x() + j * Block.SIZE;
                    float y = topLeftCorner.y() + i * Block.SIZE;
                    Vector2 leafPosition = new Vector2(x, y);
                    Leaf leaf = new Leaf(leafPosition);
                    leaves.add(leaf);
                    is_occupied[i][j] = true;
                }
            }
        }
    }


    /**
     * Creates the fruits of the tree top.
     */
    public void createFruits() {
        fruits = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (rand.nextFloat() < FRUIT_PLANT_PROBABILITY && !is_occupied[i][j]) {
                    float x = topLeftCorner.x() + j * Block.SIZE;
                    float y = topLeftCorner.y() + i * Block.SIZE;
                    Vector2 fruitPosition = new Vector2(x, y);
                    Fruit fruit = new Fruit(fruitPosition, setEnergy);
                    fruits.add(fruit);
                    is_occupied[i][j] = true;
                }
            }
        }

    }

    /**
     * Returns the list of leaves.
     */
    public List<Leaf> getLeaves() {
        return leaves;
    }


    /**
     * Returns the list of fruits.
     */
    public List<Fruit> getFruits() {
        return fruits;
    }


    /**
     * Handles the jump action, making all leaves and fruits react to the jump.
     */
    @Override
    public void onJump() {
        for (Leaf l : leaves) {
            l.onJump();
        }
        for (Fruit f : fruits) {
            f.onJump();
        }
    }
}




