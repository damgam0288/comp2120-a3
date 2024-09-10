import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private String name;
    private final char[][] originalGrid = null;
    private char[][] grid = null;
    private int width;
    private int height;
    private List<Entity> entities;
    public Map(String name, char[][] grid) {
        this.name = name;
        this.grid = grid;
        this.width  = 5;
        this.height = 5;
        this.entities = new ArrayList<>();

        // TODO: Read grid from file
        // TODO: Calculate width and height from file

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

    public boolean isWalkable(int x, int y) {
        return grid[x][y] != '#';
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height;
    }

    public void addEntity(Entity e) {
        this.entities.add(e);

        if (isValidPosition(e.getX(),e.getY()))
            grid[e.getX()][e.getY()] = e.getSymbol();
    }

    public void moveEntity(Entity e, int newX, int newY) {
        System.out.println(getTile(e.getX(), e.getY()));

    }

}