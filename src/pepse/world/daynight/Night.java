package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.components.Transition;

import java.awt.*;

public class Night {
    private static final String nightTag="night";
    private static final float initialValue=0;
    private static final Float MIDNIGHT_OPACITY = 0.5f;

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
        cycleLength/2, // transition fully over half a day
        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
        null
        );// nothing further to execute upon reaching final value

        return night;

    }
}

