package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class representing a single block in the game world.
 * Blocks are the building units of the terrain and other structures in the game.
 */
public class Block extends GameObject {

    public static final int SIZE = 30;     // The size of a block in pixels.

    /**
     * Constructs a new Block instance.
     *
     * @param topLeftCorner The top-left corner of the block's position.
     * @param renderable The renderable representing the block's appearance.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
