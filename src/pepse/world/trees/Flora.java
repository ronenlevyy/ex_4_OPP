package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flora {
    private Random rand;
    private static final double TREE_PLANT_PROBABILITY = 0.1;
    private Terrain tet;
    private final Runnable setEnergy;
    private static final int MIN_DISTANCE_BETWEEN_TREES = 170;
    private static final float MIN_HEIGHT = 5.f;
    private static final float MAX_HEIGHT = 10.f;

    public Flora(int seed, Terrain tet, Runnable setEnergy){
        this.rand= new Random(seed);
        this.tet = tet;
        this.setEnergy = setEnergy;

    }

//    public ArrayList<Tree> createInRange(int minX, int maxX){
//        int distanceFromLastTree = MIN_DISTANCE_BETWEEN_TREES;
//        ArrayList<Tree> treeList = new ArrayList<>();
//        int cols= (int) Math.ceil((maxX-minX) / Block.SIZE); // the number of trees it is possible to add
//        for (int i=0;i<cols;i++){
//             if (rand.nextFloat() < TREE_PLANT_PROBABILITY &&
//                    distanceFromLastTree >= MIN_DISTANCE_BETWEEN_TREES) {
//                float x = minX - (minX % Block.SIZE) + i * Block.SIZE;
//                float groundHeight = tet.groundHeightAt(x);
//                float y = (int) ((Math.floor(groundHeight / Block.SIZE) - 1) * Block.SIZE);
//                Vector2 treePosition = new Vector2(x, y);
//                Tree tree = new Tree(treePosition, rand, setEnergy);
//                treeList.add(tree);
//                distanceFromLastTree = 0;
//             }
//             else {
//                distanceFromLastTree++;
//            }
//        }
//        return treeList;
//    }

//    public List<Tree> createInRange(int minX, int maxX) {
//        List<Tree> treeList = new ArrayList<>();
//        int distanceFromLastTree = MIN_DISTANCE_BETWEEN_TREES;
//        for (int x = minX; x < maxX; x += Block.SIZE) {
//            if (rand.nextFloat() > TREE_PLANT_PROBABILITY &&
//                    distanceFromLastTree >= MIN_DISTANCE_BETWEEN_TREES) {
//                int y = (int) ((Math.floor(tet.groundHeightAt(x) / Block.SIZE) - 1) * Block.SIZE);
//                Tree tree = new Tree(new Vector2(x, y), rand, setEnergy);
//                treeList.add(tree);
//                System.out.println("Tree created at position: " + x + ", " + y); // Debug print
//                distanceFromLastTree = 0;
//            } else {
//                distanceFromLastTree++;
//            }
//        }
//        System.out.println("Total trees created: " + treeList.size()); // Debug print
//        return treeList;
//    }
//

        public ArrayList<Tree> createInRange(int minX, int maxX){
            ArrayList<Tree> treeList = new ArrayList<>();
            int lastX= minX;
            int cols= (int) Math.floor((maxX-minX) / Block.SIZE); // the number of trees it is possible to add
            if (minX% Block.SIZE != 0){
                minX = minX - (minX % Block.SIZE);
            }

            for (int i=0;i<cols;i++){
                 if (rand.nextFloat() < TREE_PLANT_PROBABILITY ) {
                    float x = minX + i * Block.SIZE;
                    if (x - lastX < MIN_DISTANCE_BETWEEN_TREES){
                        continue;
                    }
                    int treeHeight=
                            (int) Math.ceil(MIN_HEIGHT + rand.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT));
                    lastX=(int) x;
                    float groundHeight = tet.groundHeightAt(x);
                    float y = groundHeight- treeHeight + Block.SIZE;
                    Vector2 treePosition = new Vector2(x, y);
                    Tree tree = new Tree(treePosition, rand, treeHeight, setEnergy);
                    treeList.add(tree);
                 }

            }
            return treeList;
    }
}

