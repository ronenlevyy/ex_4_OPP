package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import danogl.components.Transition;

import java.awt.*;



/**
 * This class handles the night cycle in the game, transitioning between day and night by adjusting
 * the opacity of a black rectangle overlay.
 */
public class Night {
    private static final String nightTag="night";
    private static final float initialValue=0;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final float TWO=2;

    /**
     * Creates a night cycle GameObject that transitions between day and night.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The length of the day-night cycle.
     * @return A GameObject representing the night overlay.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(nightTag);

        new Transition<Float>(night, // the game object being changed
        night.renderer()::setOpaqueness, // the method to call
        initialValue, // initial transition value
        MIDNIGHT_OPACITY,// final transition value
        Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
        cycleLength/TWO, // transition fully over half a day
        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
        null
        );// nothing further to execute upon reaching final value

        return night;
    }
}

