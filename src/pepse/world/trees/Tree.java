package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.AvatarManager.CallbackAvatarJump;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This class is responsible for creating the tree GameObject.
 */
public class Tree implements CallbackAvatarJump {
    private final List<Trunk> trunk;
    private final TreeTop treeTop;


    /**
     * Constructs a new Tree instance.
     *
     * @param topLeftCorner The top-left corner of the tree's position.
     * @param rand          Random instance for generating tree attributes.
     * @param treeHeight    The height of the tree.
     * @param setEnergy     A runnable to set the energy.
     */
    public Tree(Vector2 topLeftCorner, Random rand, int treeHeight, Runnable setEnergy) {
        this.trunk = new ArrayList<>();
        for (int i = 0; i < treeHeight; i++) {
            Vector2 placementVector = new Vector2(topLeftCorner.x(), topLeftCorner.y() - i * Block.SIZE);
            trunk.add(new Trunk(placementVector));
        }

        this.treeTop = new TreeTop(topLeftCorner, rand, treeHeight, setEnergy);


    }

    /**
     * Handles the jump action for the tree, making all its components react to the jump.
     */
    @Override
    public void onJump() {
        for (Trunk t : trunk) {
            t.onJump();
        }
        treeTop.onJump();
    }


    /**
     * Adds the tree and its components to the game objects.
     *
     * @param gameObjects The collection of game objects.
     */
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
}
