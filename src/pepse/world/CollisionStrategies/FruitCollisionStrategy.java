package pepse.world.CollisionStrategies;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.util.Vector2;
import pepse.PepseGameManager;

public class FruitCollisionStrategy implements CollisionStrategy{
    private Runnable setEnergy;
    private boolean isEaten;
    private float CYCLE_LENGTH= PepseGameManager.CYCLE_LENGTH;

    public FruitCollisionStrategy(Runnable setEnergy){
        this.setEnergy = setEnergy;
        this.isEaten = false;
    }

    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        if (!isEaten){
            setEnergy.run();
            firstObject.renderer().setOpaqueness(0f);
            firstObject.physics().preventIntersectionsFromDirection(null);
            isEaten = true;
            new ScheduledTask(firstObject,CYCLE_LENGTH , false, () -> {
                firstObject.renderer().setOpaqueness(1f);
                firstObject.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                isEaten = false;
            });
        }

    }
}
