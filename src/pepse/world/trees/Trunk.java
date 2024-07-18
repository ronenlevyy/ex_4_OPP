package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Trunk extends Block implements CallbackAvatarJump {
    private static final String trunkTag="trunk";

    private static final Color trunkColor = new Color(100, 50, 20);

    public Trunk(Vector2 topLeftCorner){
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(trunkColor)));
        setTag(trunkTag);
    }

    @Override
    public void onJump() {
        Renderable trunkRenderable = new RectangleRenderable(ColorSupplier.approximateColor(trunkColor));
        renderer().setRenderable(trunkRenderable);

    }
}
