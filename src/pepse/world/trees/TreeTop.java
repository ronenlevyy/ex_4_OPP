package pepse.world.trees;

import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeTop implements CallbackAvatarJump {
//    private static final int MAX_TOP_LEAVES = 10;
//    private static final int MAX_TOP_FRUITS = 5;
    private final Vector2 topLeftCorner;
    private Random rand;
    private List <Leaf> leaves;
    private List <Fruit> fruits;
    private static final double LEAF_PLANT_PROBABILITY = 0.7;
    private static final double FRUIT_PLANT_PROBABILITY = 0.5;
    private static final int SIZE= 5;
    private final Runnable setEnergy;
    private final int treeHeight;
    private static final int TREE_TOP_SIZE = 2;
    private boolean[][] is_occupied;


    public TreeTop(Vector2 topLeftCorner, Random rand, int treeHeight, Runnable setEnergy){
        this.topLeftCorner= topLeftCorner.subtract(new Vector2(Block.SIZE,Block.SIZE).mult(TREE_TOP_SIZE))
        .subtract(new Vector2(0,Block.SIZE* treeHeight));
        this.rand = rand;
        this.treeHeight = treeHeight;
        this.setEnergy = setEnergy;
        this.is_occupied = new boolean[SIZE][SIZE];
        initOccupation();

        createLeaves();
        createFruits();
    }



    private void initOccupation(){
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                this.is_occupied[i][j]=false;
            }
        }
    }

    public void createLeaves(){
        leaves = new ArrayList<>();
        int leafCount = 0; //todo: erase

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (rand.nextFloat() < LEAF_PLANT_PROBABILITY && !is_occupied[i][j]) {
                    float x = topLeftCorner.x() + j * Block.SIZE;
                    float y = topLeftCorner.y() + i * Block.SIZE;
                    Vector2 leafPosition = new Vector2(x, y);
                    Leaf leaf = new Leaf(leafPosition);
                    leaves.add(leaf);
                    is_occupied[i][j] = true;
                    leafCount++;
                }
            }
        }
    }

    public void createFruits(){
        fruits = new ArrayList<>();
        int fruitCount = 0;

        for (int i=0; i< SIZE; i++){
            for (int j=0; j< SIZE; j++){
                if (rand.nextFloat() < FRUIT_PLANT_PROBABILITY && !is_occupied[i][j]){
                    float x = topLeftCorner.x() + j * Block.SIZE;
                    float y = topLeftCorner.y() + i * Block.SIZE;
                    Vector2 fruitPosition = new Vector2(x, y);
                    Fruit fruit = new Fruit(fruitPosition, setEnergy);
                    fruits.add(fruit);
                    is_occupied[i][j] = true;
                    fruitCount++;
                }
            }
        }

    }

    public List<Leaf> getLeaves(){
        return leaves;
    }

    public List<Fruit> getFruits(){
        return fruits;
    }

    @Override
    public void onJump() {
        for (Leaf l : leaves){
            l.onJump();
        }
        for (Fruit f : fruits){
            f.onJump();
        }
    }
}




