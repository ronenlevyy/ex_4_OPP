package pepse;

import danogl.GameManager; // ודא שיש לך את ההפניה הנכונה ל-GameManager
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.util.List;


public class PepseGameManager extends GameManager {
    private static final float cycleLength=30;

    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private static GameObject night;
    private static GameObject sun;
    private static GameObject sunHalo;
    private static GameObject avatar;
    //public static final int SEED = 69420;




    private void initializeSky(){
        Sky sky = new Sky();
        this.gameObjects().addGameObject(sky.create(windowController.getWindowDimensions()),
                Layer.BACKGROUND);
    }


    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener
            inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;

        //create - sky
        initializeSky();


        //create - ground
        Terrain tet = new Terrain(windowController.getWindowDimensions(),0);
        List<Block> blocklist = tet.createInRange(0, (int) windowController.getWindowDimensions().x());
        for(Block block : blocklist){
            gameObjects().addGameObject(block);
        }

        //create - night
        night= Night.create(windowController.getWindowDimensions(),cycleLength);
        gameObjects().addGameObject(night, Layer.FOREGROUND); // todo: ensure this is the correct layer

        //create - sun
        sun= Sun.create(windowController.getWindowDimensions(),cycleLength);
        gameObjects().addGameObject(sun, Layer.BACKGROUND+1); // todo: ensure this is the correct layer

        //create - holo
        sunHalo= SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND+1); // todo: ensure this is the correct layer



        //create - energy
        AvatarEnergy energy = new AvatarEnergy(new Vector2(100, 100), new Vector2(30, 30),
                new TextRenderable("100"));
        this.gameObjects().addGameObject(energy);


        //create - avatar
        avatar = new Avatar(new Vector2(850,650),inputListener,imageReader,energy::changeEnergy); //todo: this is a demo avatar we will need to make it more specific to the instructions
        gameObjects().addGameObject(avatar);


        //todo- we will set the camera when the infinite world will be ready to go
//        setCamera(new Camera(avatar, Vector2.ZERO, windowController.getWindowDimensions(),
//                windowController.getWindowDimensions()));

    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }


}