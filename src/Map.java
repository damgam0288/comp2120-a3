import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private String name;
    private char[][] originalGrid = null;
    private char[][] grid = null;
    private int width;
    private int height;
    private List<Entity> entities;

    public Map(String name, char[][] grid) {
        this.name = name;
        this.grid = grid;
        this.originalGrid = grid;
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

    /*
        Option 1: update grid[] values whenever entities are added/moved
        and use the grid[] to draw the map

        Option 2: don't change grid[] values; just draw world[] char along with entity chars each time
     */

    public char getTile(int x, int y) {
        return grid[x][y];
    }

    public void setTile(int x, int y, char value) {
        grid[x][y] = value;
    }

    public boolean isWalkable(int x, int y) {
        return grid[x][y] != '#';
    }

    // Check if position is within the game map boundaries
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height;
    }

    /**
     * Adds (non-duplicate) entity to the list of entities on this map
     */
    public boolean addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);
            grid[e.getX()][e.getY()] = e.getSymbol();
            return true;
        }
        return false;
    }

    /**
     * Move the given entity on the map by re-drawing the entity's current position as the
     * original grid's x,y values, and then drawing the entity into the new x,y in the grid
     */
    public void moveEntity(Entity e, int newX, int newY) {
        if (isValidPosition(newX,newY)) {
            this.setTile(e.getX(),e.getY(),originalGrid[e.getX()][e.getY()]);
            this.setTile(newX,newY,e.getSymbol());
        }
    }

}