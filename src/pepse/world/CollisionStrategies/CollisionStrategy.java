package pepse.world.CollisionStrategies;

import danogl.GameObject;

/**
 * This interface is responsible for the collision strategy
 */
public interface CollisionStrategy {

    /**
     * This method is called when a collision occurs
     * @param firstObject the first object
     * @param secondObject the second object
     */
    void onCollision(GameObject firstObject, GameObject secondObject);
}

