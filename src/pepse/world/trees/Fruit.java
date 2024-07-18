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
import java.util.HashSet;
import java.util.Set;

import java.awt.*;
import java.util.Random;

public class Fruit extends GameObject implements CallbackAvatarJump {
    private static final float FRUIT_SIZE=Block.SIZE* 0.5f;
    private static final Color FRUIT_COLOR= Color.RED;
    private boolean isEaten;
    private FruitCollisionStrategy collisionStrategy;


    public Fruit(Vector2 topLeftCorner){
        super(topLeftCorner, new Vector2(FRUIT_SIZE,FRUIT_SIZE),new OvalRenderable(FRUIT_COLOR));
        this.isEaten=false;
        this.collisionStrategy = new FruitCollisionStrategy();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("avatar")){
            isEaten=true;
            collisionStrategy.onCollision(this, other);

        }
    }


    @Override
    public void onJump() {
        Renderable fruitRenderable = new OvalRenderable(ColorSupplier.approximateColor(FRUIT_COLOR));
        renderer().setRenderable(fruitRenderable);

    }

}
