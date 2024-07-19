package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is responsible for creating the tree GameObject.
 */
public class Tree implements CallbackAvatarJump {
    private static final String treeTag="tree";
    private Vector2 topLeftCorner;
    private static final float MIN_HEIGHT = 5.f;
    private static final float MAX_HEIGHT = 8.f;
    private int treeHeight;
    private Random rand;
    private List<Trunk> trunk;
    private TreeTop treeTop;
    private Runnable setEnergy;

    public Tree(Vector2 topLeftCorner, Random rand, Runnable setEnergy) {
        this.topLeftCorner = topLeftCorner;
        this.rand = rand;
        this.treeHeight = (int) Math.ceil(MIN_HEIGHT + rand.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT));
        this.setEnergy=setEnergy;

        this.trunk = new ArrayList<>();
        for (int i = 0; i < treeHeight; i++) {
            Vector2 placementVector = new Vector2(topLeftCorner.x(), topLeftCorner.y() - i * Block.SIZE);
            trunk.add(new Trunk(placementVector));
        }

        this.treeTop = new TreeTop(topLeftCorner, rand, treeHeight, setEnergy);


    }


    @Override
    public void onJump() {
        for (Trunk t : trunk){
            t.onJump();
        }
        //treeTop.onJump();
    }

    public List<Leaf> getLeaves(){
        return treeTop.getLeaves();
    }

    public List<Fruit> getFruits(){
        return treeTop.getFruits();
    }

    public List<Trunk> getTrunk(){
        return trunk;
    }

    public void addTree(GameObjectCollection gameObjects){
        for (Trunk t : trunk){
            gameObjects.addGameObject(t);
        }
        for (Leaf l : getLeaves()){
            gameObjects.addGameObject(l);
        }
        for (Fruit f : getFruits()){
            gameObjects.addGameObject(f);
        }

    }
}
