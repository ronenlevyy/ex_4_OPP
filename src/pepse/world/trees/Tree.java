package pepse.world.trees;

import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is responsible for creating the tree GameObject.
 */
public class Tree implements CallbackAvatarJump {
    private static final String treeTag="tree";
    private static Vector2 topLeftCorner;
    private static final int MIN_HEIGHT = 5;
    private static final int MAX_HEIGHT = 10;
    private static float treeHeight;
    private static Random rand;
    private static ArrayList<Trunk> trunk;
    private static TreeTop treeTop;

    public Tree(Vector2 topLeftCorner, Random rand){
        this.topLeftCorner= topLeftCorner;
        this.rand = rand;
        this.treeHeight = MIN_HEIGHT + rand.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT);
        this.trunk = new ArrayList<>();
        for (int i = 0; i < treeHeight; i++){
            Vector2 placementVector= new Vector2(topLeftCorner.x(), topLeftCorner.y() - i* Block.SIZE);
            trunk.add(new Trunk(placementVector));
        }
        this.treeTop = new TreeTop(topLeftCorner, treeHeight, rand);

    }


    @Override
    public void onJump() {
        for (Trunk t : trunk){
            t.onJump();
        }
        treeTop.onJump();
    }
}
