import java.io.IOException;

public class Map {

    private String name;
    private final char[][] originalGrid = null;
    private char[][] grid = null;
    private int width;
    private int height;

    public Map(String name, char[][] grid) {
        this.name = name;
        this.grid = grid;
        // TODO: Read grid from file
        // TODO: Calculate width and height from file
        this.width  = 5;
        this.height = 5;
    }

    // Draw game map to terminal
    public void draw() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
    }

    public char getTile(int x, int y) {
        return grid[x][y];
    }

    public void setTile(int x, int y, char value) {
        grid[x][y] = value;
    }

}