package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.CollisionStrategies.FruitCollisionStrategy;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Represents a fruit in the game world. The fruit can change color on jump and has collision behavior with the avatar.
 */
public class Fruit extends GameObject implements CallbackAvatarJump {
    // Constants
    private static final float FRUIT_SIZE = Block.SIZE * 0.8f;
    private static final Color FRUIT_COLOR = Color.RED;
    private static final Set<Color> FRUIT_COLORS = new HashSet<>();

    static {
        FRUIT_COLORS.add(Color.RED);
        FRUIT_COLORS.add(Color.YELLOW);
        FRUIT_COLORS.add(Color.ORANGE);
        FRUIT_COLORS.add(Color.PINK);
        FRUIT_COLORS.add(Color.MAGENTA);
    }

    private final FruitCollisionStrategy collisionStrategy;


    /**
     * Constructs a new Fruit instance.
     *
     * @param topLeftCorner The top-left corner of the fruit's position.
     * @param setEnergy     The runnable to set the energy when the fruit is collected.
     */
    public Fruit(Vector2 topLeftCorner, Runnable setEnergy) {
        super(topLeftCorner, new Vector2(FRUIT_SIZE, FRUIT_SIZE), new OvalRenderable(FRUIT_COLOR));
        this.collisionStrategy = new FruitCollisionStrategy(setEnergy);
    }


    /**
     * Handles the collision behavior when the fruit collides with another game object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("avatar")) {
            collisionStrategy.onCollision(this, other);

        }
    }

    /**
     * Returns a random color for the fruit from the set of predefined colors.
     *
     * @return A random color.
     */
    private Color getRandomColor() {
        Random rand = new Random();
        int index = rand.nextInt(FRUIT_COLORS.size());
        return (Color) FRUIT_COLORS.toArray()[index];
    }

    /**
     * Changes the color of the fruit when the avatar jumps.
     */
    @Override
    public void onJump() {
        Renderable fruitRenderable = new OvalRenderable(ColorSupplier.approximateColor(getRandomColor()));
        renderer().setRenderable(fruitRenderable);
    }

}
