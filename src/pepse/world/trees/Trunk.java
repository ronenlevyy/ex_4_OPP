package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;


/**
 * This class represents the trunk of a tree in the game world.
 * It can change its appearance when the avatar jumps.
 */
public class Trunk extends Block implements CallbackAvatarJump {
    // Constants
    private static final String TRUNK_TAG = "trunk";
    private static final Color trunkColor = new Color(100, 50, 20);


    /**
     * Constructs a new Trunk instance.
     *
     * @param topLeftCorner The top-left corner of the trunk's position.
     */
    public Trunk(Vector2 topLeftCorner) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(trunkColor)));
        setTag(TRUNK_TAG);
    }


    /**
     * Handles the jump action, changing the appearance of the trunk.
     */
    @Override
    public void onJump() {
        Renderable trunkRenderable = new RectangleRenderable(ColorSupplier.approximateColor(trunkColor));
        renderer().setRenderable(trunkRenderable);

    }
}
