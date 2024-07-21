package pepse.world.AvatarManager;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;


/**
 * This class represents the energy display for the avatar.
 * It updates the displayed energy percentage based on the avatar's current energy level.
 */
public class AvatarEnergy extends GameObject {

    private final TextRenderable readable;
    private static final String PRECENTAGE = "%";

    /**
     * The energy gained from eating a fruit.
     */
    public static final int FRUIT_ENERGY = 10;

    /**
     * Constructs a new AvatarEnergy instance.
     *
     * @param topLeftCorner The initial position of the energy display.
     * @param dimensions    The dimensions of the energy display.
     * @param readable      The text renderable used to display the energy percentage.
     */
    public AvatarEnergy(Vector2 topLeftCorner, Vector2 dimensions, TextRenderable readable) {
        super(topLeftCorner, dimensions, readable);
        this.readable = readable;
    }

    /**
     * Updates the displayed energy percentage.
     *
     * @param str The new energy percentage to display.
     */
    public void changeEnergy(String str) {
        this.readable.setString(str + PRECENTAGE);
    }
}