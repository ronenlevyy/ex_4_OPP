package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 * This class is responsible for creating the sun GameObject.
 */
public class Sun {

    private static final String sunTag = "sun";
    private static final float TWO = 2;
    private static final float THREE = 3;
    private static final float initialValue = 0;
    private static final float finalValue = 360;
    private static final Vector2 sunDimensions = new Vector2(100, 100);

    /**
     * Empty constructor.
     */
    public Sun() {
    }

    /**
     * Creates a sun GameObject.
     *
     * @param windowDimensions
     * @param cycleLength
     * @return
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(Vector2.ZERO, sunDimensions, new OvalRenderable(Color.YELLOW));
        sun.setTag(sunTag);

        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / TWO, windowDimensions.y() / THREE);
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / TWO, windowDimensions.y() * (TWO / THREE));

        new Transition<>(
                sun, // the game object being changed
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter)
                        .rotated(angle).add(cycleCenter)), // the method to call
                initialValue, // initial transition value
                finalValue, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, // transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP, // Choose appropriate ENUM value
                null // nothing further to execute upon reaching final value
        );

        return sun;
    }
}
