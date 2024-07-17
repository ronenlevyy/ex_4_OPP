package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * This class is responsible for creating the sun halo GameObject.
 */
public class SunHalo {
    private static final String sunHaloTag="sunHalo";
    private static final Color sunHaloColor=new Color(255,255,0,20);
    private static final float HALO_MULTIPLIER=1.5f;

    /**
     * Empty constructor.
     */
    private SunHalo(){
    }

    /**
     * Creates a sun halo around the sun.
     * @param sun
     * @return
     */
    public static GameObject create(GameObject sun){
        GameObject sunHalo= new GameObject(Vector2.ZERO, sun.getDimensions().mult(HALO_MULTIPLIER)
                ,new OvalRenderable(sunHaloColor));
        sunHalo.addComponent((deltaTime -> sunHalo.setCenter(sun.getCenter())));
        sunHalo.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(sunHaloTag);
        return sunHalo;
    }

}
