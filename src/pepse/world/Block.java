package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * A class for Block game object
 */
public class Block extends GameObject {

    /**
     * size of a block side size
     */
    public static final int SIZE = 30;

    /**
     * Constructor
     * @param topLeftCorner - the top left corner
     * @param renderable - renderable object
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }


}