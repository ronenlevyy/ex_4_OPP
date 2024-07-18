package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.CollisionStrategies.CollisionStrategy;
import pepse.world.CollisionStrategies.FruitCollisionStrategy;

import java.awt.*;

public class Fruit extends GameObject {
    private static final float FRUIT_SIZE=Block.SIZE* 0.5f;
    private static final Color FRUIT_COLOR= Color.RED;
    private boolean isEaten;
    private GameObjectCollection collection;
    private FruitCollisionStrategy collisionStrategy;

    public Fruit(Vector2 topLeftCorner){
        super(topLeftCorner, new Vector2(FRUIT_SIZE,FRUIT_SIZE),new OvalRenderable(FRUIT_COLOR));
        this.isEaten=false;
        this.collection=collection;
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
}
