package pepse.world.daynight;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class is responsible for creating the sun halo GameObject.
 */
public class SunHalo {

    //Constants
    private static final String SUN_HALO_TAG = "sunHalo";
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float HALO_MULTIPLIER = 1.5f;

    /**
     * Empty constructor.
     */
    private SunHalo() {
    }

    /**
     * Creates a sun halo around the sun.
     *
     * @param sun
     * @return
     */
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(Vector2.ZERO, sun.getDimensions().mult(HALO_MULTIPLIER),
                new OvalRenderable(SUN_HALO_COLOR));
        sunHalo.addComponent((deltaTime -> sunHalo.setCenter(sun.getCenter())));
        sunHalo.setTag(SUN_HALO_TAG);
        return sunHalo;
    }
}
