package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.Random;

public class Flora {
    private Random rand;
    private static final double TREE_PLANT_PROBABILITY = 0.1;
    private static Terrain terrain;


    public Flora(int seed, Terrain tet){
        this.rand= new Random(seed);
        this.terrain = tet;

    }
    //todo: ensure
    public ArrayList<Tree> createInRange(int minX, int maxX){
        ArrayList<Tree> treeList = new ArrayList<>();
        int cols= (int) Math.ceil((maxX-minX) / Block.SIZE); // the number of trees it is possible to add
        for (int i=0;i<cols;i++){
             if (rand.nextDouble() < TREE_PLANT_PROBABILITY) {
                 float x = minX + i * Block.SIZE;
                 float y = terrain.groundHeightAt(x);
                 Vector2 treePosition = new Vector2(x, y);
                 Tree tree = new Tree(treePosition, rand);
                 treeList.add(tree);
             }
        }
        return treeList;
    }


//    public List<Tree> createInRange(int minX, int maxX) {
//        int numOfBlocks = (int) Math.ceil((double) (maxX - minX) / Block.SIZE);
//        List<Tree> treeList = new ArrayList<>();
//
//        int startingX = (minX) - ((minX % Block.SIZE) + Block.SIZE) % Block.SIZE;
//        int endX = startingX + numOfBlocks * Block.SIZE;
//        int distanceFromLastTree = DISTANCE_BETWEEN_TREES;
//        for (int x = startingX; x < endX; x += Block.SIZE) {
//            int y = (int) ((Math.floor(heightGetter.groundHeightAt(x) / Block.SIZE) - 1) * Block.SIZE);
//            if (random.nextInt(3) == 0 && ++distanceFromLastTree >= DISTANCE_BETWEEN_TREES) {
//                Tree tree = new Tree(new Vector2(x, y), random, fruitCollisionStrategyTask);
//                treeList.add(tree);
//                distanceFromLastTree = 0;
//            }
//        }
//
//        return treeList;
//    }
}
