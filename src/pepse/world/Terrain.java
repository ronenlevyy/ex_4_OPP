package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is responsible for the terrain generation in the game.
 * It uses Perlin noise to generate a realistic terrain.
 */
public class Terrain {

    private static final int TERRAIN_DEPTH = 20;
    private NoiseGenerator noiseGenerator;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private float groundHeightAtX0 ;
    private static final float GROUND_HEIGHT_MULTIPLIER = (float) 2/3;


    /**
     * Constructs a new Terrain instance.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed The seed for the noise generator, used to generate terrain.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = windowDimensions.y() *  GROUND_HEIGHT_MULTIPLIER;
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);

    }


    /**
     * Returns the ground height at a given x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The height of the ground at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 4);
        return groundHeightAtX0 + noise;
    }



    /**
     * Creates a single block at the specified coordinate.
     *
     * @param coordinate The top-left corner of the block's position.
     * @return A new Block instance.
     */
    private Block createABlock(Vector2 coordinate){
        Renderable rectangleRenderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Block block = new Block(coordinate, rectangleRenderable);
        block.setTag("ground");
        return block;
    }



    /**
     * Creates the terrain in the specified range.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return A list of blocks that make up the terrain in the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocksList = new ArrayList<>();
        int adjustedMinX = minX - (minX % Block.SIZE);
        while (adjustedMinX <= maxX) {
            float yCoord = groundHeightAt(adjustedMinX);
            int currentDepth = 0;
            while (currentDepth < TERRAIN_DEPTH) {
                Block block = createABlock(new Vector2(adjustedMinX, yCoord + currentDepth * Block.SIZE));
                blocksList.add(block);
                currentDepth++;
            }
            adjustedMinX += Block.SIZE;
        }
        return blocksList;
    }
}
