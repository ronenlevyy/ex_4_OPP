package pepse.world.CollisionStrategies;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.util.Vector2;
import pepse.PepseGameManager;

/**
 * This class defines the behavior for fruit collision in the game.
 * When the avatar collides with a fruit, the fruit is "eaten" (made invisible),
 * energy is restored, and the fruit reappears after a set cycle length.
 */
public class FruitCollisionStrategy implements CollisionStrategy{
    private Runnable setEnergy;
    private boolean isEaten;
    private static final float CYCLE_LENGTH= PepseGameManager.CYCLE_LENGTH;


    /**
     * Constructs a new FruitCollisionStrategy instance.
     *
     * @param setEnergy A Runnable that sets the energy of the avatar.
     */
    public FruitCollisionStrategy(Runnable setEnergy){
        this.setEnergy = setEnergy;
        this.isEaten = false;
    }


    /**
     * Handles the collision between the fruit and another game object.
     * If the fruit is not already eaten, it becomes invisible and reappears
     * after a certain period, restoring energy to the avatar.
     *
     * @param firstObject  The fruit object.
     * @param secondObject The object that collides with the fruit.
     */
    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        if (!isEaten){
            isEaten = true;

            firstObject.physics().preventIntersectionsFromDirection(null);
            firstObject.renderer().setOpaqueness(0f); // make the fruit invisible

            setEnergy.run();

            new ScheduledTask(firstObject,CYCLE_LENGTH , false, () -> {
                firstObject.renderer().setOpaqueness(1f); // make the fruit visible
                firstObject.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                isEaten = false;
            });
        }

    }
}
