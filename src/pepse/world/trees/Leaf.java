package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.awt.*;
import java.util.Random;



/**
 * Represents a leaf in the game world.
 * The leaf can sway and change dimensions over time and can spin when the avatar jumps.
 */
public class Leaf extends Block implements CallbackAvatarJump {
    private static final String LEAF_TAG ="leaf";
    private static final Color LEAF_COLOR = new Color(50,200,30);
    private static final float INITIAL_VALUE_ANGLE = 0;
    private static final float FINAL_VALUE_ANGLE = 10;
    private static final float CYCLE_LENGTH = 3;
    private static final float JUMP_SPIN_ANGLE = 90;
    private static final float JUMP_SPIN_DURATION = 3;
    private static final float SIZE = 28;
    private static final float MAX_WIDTH = 33;


    /**
     * Constructs a new Leaf instance.
     *
     * @param topLeftCorner The top-left corner of the leaf's position.
     */
    public Leaf(Vector2 topLeftCorner){
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
        Random rand = new Random();
        new ScheduledTask(this, rand.nextFloat(), false, this::swayLeaf);
        setTag(LEAF_TAG);
    }


    /**
     * Initiates the swaying motion of the leaf.
     * The leaf sways back and forth and changes its width over time.
     */
    public void swayLeaf(){

        // angle
        new Transition<Float>(
                this, // the game object being changed
                (Float angle) -> renderer().setRenderableAngle(angle),// the method to call
                INITIAL_VALUE_ANGLE, // initial transition value
                FINAL_VALUE_ANGLE,// final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                CYCLE_LENGTH, //
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null
        );// nothing further to execute upon reaching final value

    new Transition<>(
            this, // the object to be related with the transition
            this::setDimensions, // the function that will change the width
            new Vector2(SIZE, SIZE), //initialized value
            new Vector2(MAX_WIDTH, SIZE), //final value
            Transition.CUBIC_INTERPOLATOR_VECTOR, // moving strategy
            CYCLE_LENGTH, // cycle time
            Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // the transition type
            null// the function to call
    );
    }

    /**
     * Spins the leaf when the avatar jumps.
     */
    @Override
    public void onJump() {
        new Transition<Float>(
                this, // the game object being changed
                (Float angle) -> renderer().setRenderableAngle(angle), // the method to call
                renderer().getRenderableAngle(), // initial transition value
                renderer().getRenderableAngle() + JUMP_SPIN_ANGLE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                JUMP_SPIN_DURATION, // duration of the transition
                Transition.TransitionType.TRANSITION_ONCE, // Choose appropriate ENUM value
                null // nothing further to execute upon reaching final value
        ); // nothing further to execute upon reaching final value
    }
}
