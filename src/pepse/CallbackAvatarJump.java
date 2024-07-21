package pepse;

/**
 * This interface defines a callback mechanism for when the avatar jumps.
 * Implementing classes should define what actions to take when the onJump method is called.
 */
public interface CallbackAvatarJump {

    /**
     * Method to be called when the avatar jumps.
     */
    void onJump();
}