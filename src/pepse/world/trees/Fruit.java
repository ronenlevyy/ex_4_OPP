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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Fruit extends GameObject implements CallbackAvatarJump {
    private static final float FRUIT_SIZE=Block.SIZE* 0.8f;
    private static final Color FRUIT_COLOR= Color.RED;
    private FruitCollisionStrategy collisionStrategy;
    private static final Set<Color> FRUIT_COLORS = new HashSet<>();

    static {
        FRUIT_COLORS.add(Color.RED);
        FRUIT_COLORS.add(Color.YELLOW);
        FRUIT_COLORS.add(Color.ORANGE);
        FRUIT_COLORS.add(Color.PINK);
        FRUIT_COLORS.add(Color.MAGENTA);
    }


    public Fruit(Vector2 topLeftCorner, Runnable setEnergy){
        super(topLeftCorner, new Vector2(FRUIT_SIZE,FRUIT_SIZE),new OvalRenderable(FRUIT_COLOR));
        this.collisionStrategy = new FruitCollisionStrategy(setEnergy);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("avatar")){
            collisionStrategy.onCollision(this, other);

        }
    }
    private Color getRandomColor() {
        Random rand = new Random();
        int index = rand.nextInt(FRUIT_COLORS.size());
        return (Color) FRUIT_COLORS.toArray()[index];
    }

    @Override
    public void onJump() {
        Renderable fruitRenderable = new OvalRenderable(ColorSupplier.approximateColor(getRandomColor()));
        renderer().setRenderable(fruitRenderable);

    }

}
