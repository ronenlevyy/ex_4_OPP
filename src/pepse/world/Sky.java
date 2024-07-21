package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * A class representing the sky in the game.
 * The sky is a simple rectangle that covers the entire window and serves as the background.
 */
public class Sky {
    // Constants
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5"); // The basic color of the sky.
    private static final String SKY_TAG = "sky";     // The tag to identify the sky game object.

    /**
     * Creates a sky game object.
     *
     * @param windowDimensions The dimensions of the game window.
     * @return A GameObject representing the sky.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}
