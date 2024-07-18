package pepse;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;


/**
 * This class represents the energy rendered on the Pepsi game.
 */
public class AvatarEnergy extends GameObject {

    private final TextRenderable readable;


    public AvatarEnergy(Vector2 topLeftCorner, Vector2 dimensions, TextRenderable readable) {
        super(topLeftCorner, dimensions, readable);
        this.readable = readable;
    }


    /**
     *  used as callback in the Avatar class. It is used to update the rendered energy of the avatar.
     * @param s - the avatars energy as a string
     */
    public void changeEnergy(String s){
        this.readable.setString(s +"%");
    }


}