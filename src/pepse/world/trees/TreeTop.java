package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.Random;

public class TreeTop implements CallbackAvatarJump {
    private static final int MAX_TOP_LEAVES = 10;
    private static final int MAX_TOP_FRUITS = 5;
    private final Vector2 topLeftCorner;
    private Random rand;
    private ArrayList <Leaf> leaves;
    private ArrayList <Fruit> fruits;
    private static final double LEAF_PLANT_PROBABILITY = 0.2;
    private static final double FRUIT_PLANT_PROBABILITY = 0.02;
    private static final int SIZE= 5;



    public TreeTop(Vector2 topLeftCorner, float treeHeight, Random rand){
        this.topLeftCorner= topLeftCorner;
        this.rand = rand;
    }

    public void createLeaves(){
        leaves = new ArrayList<>();
        int leafCount = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int j = 0; j < SIZE; j++) {
                if (rand.nextFloat() < LEAF_PLANT_PROBABILITY && leafCount < MAX_TOP_LEAVES) {
                    float x = topLeftCorner.x() + j * Block.SIZE;
                    float y = topLeftCorner.y() + row * Block.SIZE;
                    Vector2 leafPosition = new Vector2(x, y);
                    Leaf leaf = new Leaf(leafPosition);
                    leaves.add(leaf);
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
                if (rand.nextFloat() < FRUIT_PLANT_PROBABILITY && fruitCount < MAX_TOP_FRUITS){
                    float x = topLeftCorner.x() + j * Block.SIZE;
                    float y = topLeftCorner.y() + i * Block.SIZE;
                    Vector2 fruitPosition = new Vector2(x, y);
                    Fruit fruit = new Fruit(fruitPosition);
                    fruits.add(fruit);
                    fruitCount++;
                }
            }
        }

    }

    public ArrayList<Leaf> getLeaves(){
        return leaves;
    }

    public ArrayList<Fruit> getFruits(){
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
