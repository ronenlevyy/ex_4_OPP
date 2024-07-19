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
    private int lastTreeX;

    public Flora(int seed, Terrain tet, Runnable setEnergy) {
        this.rand = new Random(seed);
        this.tet = tet;
        this.setEnergy = setEnergy;
        this.lastTreeX = 0;
    }

    public ArrayList<Tree> createInRange(int minX, int maxX) {
        ArrayList<Tree> treeList = new ArrayList<>();
        int lastX = minX;
        int cols = (int) Math.floor((maxX - minX) / Block.SIZE); // the number of trees it is possible to add
        if (minX % Block.SIZE != 0) {
            minX = minX - (minX % Block.SIZE);
        }

        for (int i = 0; i < cols; i++) {
            float x = minX + i * Block.SIZE;
            if (x - lastTreeX >= MIN_DISTANCE_BETWEEN_TREES && rand.nextFloat() < TREE_PLANT_PROBABILITY) {
                if (x - lastX < MIN_DISTANCE_BETWEEN_TREES) {
                    continue;
                }
                int treeHeight =
                        (int) Math.ceil(MIN_HEIGHT + rand.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT));
                lastX = (int) x;
                float groundHeight = tet.groundHeightAt(x);
                float y = groundHeight - treeHeight + Block.SIZE;
                Vector2 treePosition = new Vector2(x, y);
                Tree tree = new Tree(treePosition, rand, treeHeight, setEnergy);
                treeList.add(tree);
                lastTreeX = (int) x;
            }

        }
        return treeList;
    }

    public void setLastTreeX(int x) {
        lastTreeX = x;
    }
}

