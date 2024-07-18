package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private static final int TERRAIN_DEPTH = 20;
    private NoiseGenerator noiseGenerator;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private Vector2 windowDimensions;
    private float groundHeightAtX0 ;
    private static final float GROUND_HEIGHT_MULTIPLIER = (float) 2/3;


    //todo- seed isnt important until the unlimited part of the game
    public Terrain(Vector2 windowDimensions, int seed){
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.y() *  GROUND_HEIGHT_MULTIPLIER;
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);

    }



    ////////todo- the func isnt ready- we need to implement perline noise
    ////////todo- the func isnt ready- we need to implement perline noise
    ////////todo- the func isnt ready- we need to implement perline noise
    ////////todo- the func isnt ready- we need to implement perline noise
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7); // תוכל לשחק עם הפקטור כדי לקבל תוצאות טובות יותר
        return groundHeightAtX0 + noise;
    }




    private Block createABlock(Vector2 coordinate){
        Renderable rectangleRenderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Block block = new Block(coordinate, rectangleRenderable);
        block.setTag("ground");
        return block;
    }



    ///todo- keep working on this 2 funcs
    private float calculateMinX(int initialX){
        float supply = (float) Math.floor( initialX/ (double) Block.SIZE);
        return supply*Block.SIZE;
    }
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocksList = new ArrayList<>();
        float newMinX = calculateMinX(minX);
        for (float x = newMinX; x <= maxX; x += Block.SIZE) {
            float yCoord = groundHeightAt(x);
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                Block block = createABlock(new Vector2(x, yCoord + i * Block.SIZE));
                blocksList.add(block);
            }
        }
        return blocksList;
    }

}
