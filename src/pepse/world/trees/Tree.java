package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
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
    private static final String treeTag = "tree";
    private Vector2 topLeftCorner;
    private int treeHeight;
    private Random rand;
    private List<Trunk> trunk;
    private TreeTop treeTop;
    private Runnable setEnergy;

    public Tree(Vector2 topLeftCorner, Random rand, int treeHeight, Runnable setEnergy) {
        this.topLeftCorner = topLeftCorner;
        this.rand = rand;
        this.treeHeight = treeHeight;
        this.setEnergy = setEnergy;

        this.trunk = new ArrayList<>();
        for (int i = 0; i < treeHeight; i++) {
            Vector2 placementVector = new Vector2(topLeftCorner.x(), topLeftCorner.y() - i * Block.SIZE);
            trunk.add(new Trunk(placementVector));
        }

        this.treeTop = new TreeTop(topLeftCorner, rand, treeHeight, setEnergy);


    }


    @Override
    public void onJump() {
        for (Trunk t : trunk) {
            t.onJump();
        }
        treeTop.onJump();
    }

//    public List<Leaf> getLeaves(){
//        return treeTop.getLeaves();
//    }
//
//    public List<Fruit> getFruits(){
//        return treeTop.getFruits();
//    }

//    public List<Trunk> getTrunk(){
//        return trunk;
//    }

    public void addTree(GameObjectCollection gameObjects) {
        for (Trunk t : trunk) {
            gameObjects.addGameObject(t, Layer.STATIC_OBJECTS);
        }
        for (Leaf l : treeTop.getLeaves()) {
            gameObjects.addGameObject(l, Layer.FOREGROUND);
        }
        for (Fruit f : treeTop.getFruits()) {
            gameObjects.addGameObject(f, Layer.STATIC_OBJECTS);
        }

    }

    public Vector2 getTopLeftCorner() {
        return topLeftCorner;
    }

    public void removeTree(GameObjectCollection gameObjects) {
        for (Trunk t : trunk) {
            gameObjects.removeGameObject(t, Layer.STATIC_OBJECTS);
        }
        for (Leaf l : treeTop.getLeaves()) {
            gameObjects.removeGameObject(l, Layer.FOREGROUND);
        }
        for (Fruit f : treeTop.getFruits()) {
            gameObjects.removeGameObject(f, Layer.STATIC_OBJECTS);
        }


    }

    public void setTopLeftCorner(Vector2 vector2) {
        this.topLeftCorner = vector2;
    }

    public float getTreeHeight() {
        return treeHeight;
    }
}
