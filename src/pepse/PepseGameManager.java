package pepse;

import danogl.GameManager; // ודא שיש לך את ההפניה הנכונה ל-GameManager
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;


public class PepseGameManager extends GameManager {
    ImageReader imageReader;
    SoundReader soundReader;
    UserInputListener inputListener;
    WindowController windowController;



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
        initializeSky();
        Terrain tet = new Terrain(windowController.getWindowDimensions(),0);
        List<Block> blocklist = tet.createInRange(50,700);
        for(Block block : blocklist){
            gameObjects().addGameObject(block);
        }









    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }


}