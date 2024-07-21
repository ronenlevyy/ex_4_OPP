package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.AvatarManager.CallbackAvatarJump;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents the avatar character in the Pepse game.
 * The avatar can move left, right, and jump based on user input, and its energy
 * level is affected by its actions.
 */
public class Avatar extends GameObject {

    ///////////////////////////////
    // Animation and image paths //
    ///////////////////////////////
    private static final String NO_MOVEMENT_IDLE_0_IMAGE = "assets/idle_0.png";
    private static final String NO_MOVEMENT_IDLE_1_IMAGE = "assets/idle_1.png";
    private static final String NO_MOVEMENT_IDLE_2_IMAGE = "assets/idle_2.png";
    private static final String NO_MOVEMENT_IDLE_3_IMAGE = "assets/idle_3.png";
    private static final String JUMP_0_IMAGE = "assets/jump_0.png";
    private static final String JUMP_1_IMAGE = "assets/jump_1.png";
    private static final String JUMP_2_IMAGE = "assets/jump_2.png";
    private static final String JUMP_3_IMAGE = "assets/jump_3.png";
    private static final String RUN_0_IMAGE = "assets/run_0.png";
    private static final String RUN_1_IMAGE = "assets/run_1.png";
    private static final String RUN_2_IMAGE = "assets/run_2.png";
    private static final String RUN_3_IMAGE = "assets/run_3.png";
    private static final String RUN_4_IMAGE = "assets/run_4.png";
    private static final String RUN_5_IMAGE = "assets/run_5.png";

    // Constants
    private static final float START_ENERGY = 100;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final String AVATAR_TAG = "avatar";
    private static final int FACTOR_50 = 50;
    private static final int AVATAR_ENERGY = 100;
    private static final float TEN = 10.0f;
    private static final float HALF = 0.5f;
    private static final float FRAME_TIME = 0.1f;


    private final UserInputListener inputListener;
    private float avatarEnergy;
    private boolean isJumping;
    private final List<CallbackAvatarJump> callbackJump;

    private AnimationRenderable noMoveAnimation;
    private AnimationRenderable jumpAnimation;
    private AnimationRenderable runAnimation;
    private final Consumer<String> stringEnergy;

    /**
     * Constructs a new Avatar instance.
     *
     * @param topLeftCorner The initial position of the avatar.
     * @param inputListener Listener for user input to control the avatar.
     * @param imageReader   Reader to load images for the avatar animations.
     * @param stringEnergy  Callback to update the energy display.
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener,
                  ImageReader imageReader, Consumer<String> stringEnergy) {
        super(topLeftCorner, Vector2.ONES.mult(FACTOR_50), imageReader.readImage(NO_MOVEMENT_IDLE_0_IMAGE,
                false));
        this.inputListener = inputListener;
        this.stringEnergy = stringEnergy;
        this.avatarEnergy = START_ENERGY;
        this.isJumping = false;
        this.callbackJump = new ArrayList<>();
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        createAnimations(imageReader);
        this.setTag(AVATAR_TAG);

    }

    /**
     * Updates the avatar's state based on user input and the current game state.
     *
     * @param deltaTime Time since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        moveAvatar();

    }

    /**
     * Sets the avatar's energy to a new value, ensuring it stays within the range [0, 100].
     *
     * @param newEnergy The new energy value to set.
     */
    public void setEnergy(float newEnergy) {
        if (this.avatarEnergy + newEnergy <= AVATAR_ENERGY) {
            this.avatarEnergy += newEnergy;

        } else {
            this.avatarEnergy = AVATAR_ENERGY;
        }
        stringEnergy.accept(Integer.toString((int) this.avatarEnergy));
    }

    /**
     * Handles the movement of the avatar based on user input and
     * updates the avatar's energy accordingly.
     */
    private void moveAvatar() {
        float xVel = 0;

        // Ensure the energy stays within the range [0, 100]
        if (this.avatarEnergy > AVATAR_ENERGY) {
            this.avatarEnergy = AVATAR_ENERGY;
        }
        if (this.avatarEnergy < 0) {
            this.avatarEnergy = 0;
        }
        // Check for left and right movement and update energy
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && this.avatarEnergy > HALF) {
            xVel -= VELOCITY_X;
            this.avatarEnergy -= HALF;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && this.avatarEnergy > HALF) {
            xVel += VELOCITY_X;
            this.avatarEnergy -= HALF;
        }
        // Check if the avatar is jumping and update energy
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 &&
                this.avatarEnergy >= TEN) {
            this.avatarEnergy -= 10f;
            this.isJumping = true;
            renderer().setRenderable(this.jumpAnimation);
            transform().setVelocityY(VELOCITY_Y);
            onJump();

        } else if (getVelocity().y() == 0) {
            this.isJumping = false;
        }

        // Set the appropriate animation based on the avatar's state
        if (this.isJumping) {
            renderer().setRenderable(this.jumpAnimation);
        } else if (xVel != 0) {
            renderer().setRenderable(this.runAnimation);
        } else if (xVel == 0 && ((inputListener.isKeyPressed(KeyEvent.VK_LEFT) ||
                inputListener.isKeyPressed(KeyEvent.VK_RIGHT)))) {
            renderer().setRenderable(this.noMoveAnimation);
        } else {
            renderer().setRenderable(this.noMoveAnimation);
            if (this.avatarEnergy < START_ENERGY) {
                this.avatarEnergy += 1;
            }
        }
        transform().setVelocityX(xVel);
        stringEnergy.accept(Integer.toString((int) this.avatarEnergy));


    }

    /**
     * Creates the animations for the avatar using the provided image reader.
     *
     * @param imageReader Reader to load images for the avatar animations.
     */
    private void createAnimations(ImageReader imageReader) {
        this.noMoveAnimation = createAnimationRenderable(imageReader, new String[]{
                NO_MOVEMENT_IDLE_0_IMAGE,
                NO_MOVEMENT_IDLE_1_IMAGE,
                NO_MOVEMENT_IDLE_2_IMAGE,
                NO_MOVEMENT_IDLE_3_IMAGE
        }, FRAME_TIME);

        this.jumpAnimation = createAnimationRenderable(imageReader, new String[]{
                JUMP_0_IMAGE,
                JUMP_1_IMAGE,
                JUMP_2_IMAGE,
                JUMP_3_IMAGE
        }, FRAME_TIME);

        this.runAnimation = createAnimationRenderable(imageReader, new String[]{
                RUN_0_IMAGE,
                RUN_1_IMAGE,
                RUN_2_IMAGE,
                RUN_3_IMAGE,
                RUN_4_IMAGE,
                RUN_5_IMAGE
        }, FRAME_TIME);
    }


    /**
     * Creates an AnimationRenderable from the given image paths and frame duration.
     *
     * @param imageReader   Reader to load images.
     * @param imagePaths    Array of image paths for the animation frames.
     * @param frameDuration Duration of each frame in the animation.
     * @return A new AnimationRenderable instance.
     */
    private AnimationRenderable createAnimationRenderable(ImageReader imageReader, String[] imagePaths,
                                                          float frameDuration) {
        Renderable[] images = new Renderable[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = imageReader.readImage(imagePaths[i], false);
        }
        return new AnimationRenderable(images, frameDuration);
    }

    /**
     * Adds a jump callback to the list of callbacks.
     *
     * @param callback The callback to add.
     */
    public void addJumpCallback(CallbackAvatarJump callback) {
        callbackJump.add(callback);
    }


    /**
     * Adds a jump callback to the list of callbacks.
     */
    private void onJump() {
        for (CallbackAvatarJump callback : callbackJump) {
            callback.onJump();
        }
    }
}


