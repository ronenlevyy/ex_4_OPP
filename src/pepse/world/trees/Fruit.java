package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.CallbackAvatarJump;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.CollisionStrategies.FruitCollisionStrategy;
import java.awt.*;

public class Fruit extends GameObject implements CallbackAvatarJump {
    private static final float FRUIT_SIZE=Block.SIZE* 0.5f;
    private static final Color FRUIT_COLOR= Color.RED;
    private FruitCollisionStrategy collisionStrategy;


    public Fruit(Vector2 topLeftCorner, Runnable setEnergy){
        super(topLeftCorner, new Vector2(FRUIT_SIZE,FRUIT_SIZE),new OvalRenderable(FRUIT_COLOR));
        this.collisionStrategy = new FruitCollisionStrategy(setEnergy);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        System.out.println("Fruit collided with "+other.getTag());
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("avatar")){
            collisionStrategy.onCollision(this, other);

        }
    }


    @Override
    public void onJump() {
        Renderable fruitRenderable = new OvalRenderable(ColorSupplier.approximateColor(FRUIT_COLOR));
        renderer().setRenderable(fruitRenderable);

    }

}
