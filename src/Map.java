import java.io.IOException;

public class Map {

    private String name;
    private final char[][] originalGrid = null;
    private char[][] currentGrid = null;
    private int width;
    private int height;

    public Map(String name, String filePath) throws IOException {
        this.name = name;

        // TODO: Read grid from file
        // TODO: Calculate width and height from file
        this.width = -1;
        this.height = -1;
    }

    // Draw game map to terminal
    public void draw() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(currentGrid[y][x]);
            }
            System.out.println();
        }
    }



}