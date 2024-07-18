package pepse.world.trees;

import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Leaf extends Block implements CallbackAvatarJump {
    private static final String leafTag="leaf";
    private static final Color leafColor = new Color(50,200,30);
    private static final float initialValueAngle = 0;
    private static final float finalValueAngle = 10;
    private static final float initialValueWidth = 1;
    private static final float finalValueWidth = 1.1f;
    private static final float cycleLength = 3;
    private static final float jumpSpinAngle = 90;
    private static final float jumpSpinDuration = 1;

    public Leaf(Vector2 topLeftCorner){
        super(topLeftCorner, new OvalRenderable(ColorSupplier.approximateColor(leafColor)));
        setTag(leafTag);
    }

    public void swayLeaf(){

        // angle
        new Transition<Float>(
                this, // the game object being changed
                (Float angle) -> renderer().setRenderableAngle(angle),// the method to call
                initialValueAngle, // initial transition value
                finalValueAngle,// final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, //
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null
        );// nothing further to execute upon reaching final value

        // dimensions
        new Transition<Float>(
                this, // the game object being changed
                (Float widthFactor) -> this.setDimensions(new Vector2(getDimensions()).mult(widthFactor)),
                // the method to call
                initialValueWidth, // initial transition value
                finalValueWidth,// final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, //
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null
        );// nothing further to execute upon reaching final value
    }


    @Override
    public void onJump() {

        new Transition<Float>(
                this, // the game object being changed
                (Float angle) -> renderer().setRenderableAngle(angle), // the method to call
                renderer().getRenderableAngle(), // initial transition value
                renderer().getRenderableAngle() + jumpSpinAngle, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                jumpSpinDuration, // duration of the transition
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null // nothing further to execute upon reaching final value
        ); // nothing further to execute upon reaching final value
    }
}
