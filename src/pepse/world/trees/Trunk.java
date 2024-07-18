package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

public class Trunk extends Block {
    private static final String trunkTag="trunk";

    private static final Color trunkColor = new Color(100, 50, 20);

    public Trunk(Vector2 topLeftCorner){
        super(topLeftCorner, new RectangleRenderable(trunkColor));
        setTag(trunkTag);
    }

}
