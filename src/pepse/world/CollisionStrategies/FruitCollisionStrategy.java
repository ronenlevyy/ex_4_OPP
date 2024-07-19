package pepse.world.CollisionStrategies;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.util.Vector2;
import pepse.PepseGameManager;

public class FruitCollisionStrategy implements CollisionStrategy{
    private Runnable setEnergy;
    private boolean isEaten;
    private static final float CYCLE_LENGTH= PepseGameManager.CYCLE_LENGTH;

    public FruitCollisionStrategy(Runnable setEnergy){
        this.setEnergy = setEnergy;
        this.isEaten = false;
    }

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
