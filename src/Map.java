import java.util.ArrayList;
import java.util.List;

public class Map {

    private String name;
    private char[][] world = null;
    private char[][] grid = null;
    private int width;
    private int height;

    private List<Entity> entities;

    public Map(String name, char[][] world) {
        this.name = name;
        this.world = world;                         // This is the game map including floor, walls, obstacles etc.
        this.width = 5;
        this.height = 5;

        this.grid = new char[width][height];        // This is the playable grid containing game map, player, items, NPCs etc.

        this.entities = new ArrayList<>();

        // TODO: Read grid from file and calculate width and height from file

    }

    /**
     * Draws game map together with all entities, items etc. to the terminal
     */
    public void draw() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Initialize grid with the game map
                grid[x][y] = world[x][y];

                // Put entities onto the grid
                for (Entity e : entities) {
                    if (e.getX() == x && e.getY() == y) {
                        grid[x][y] = e.getSymbol();
                        break;
                    }
                }

                // Draw play grid
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

    /**
     * Returns true if the given x,y position is not over the game world boundaries
     * or over an obstacle
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height;

        // TODO Check for collision with obstacles
    }

    /**
     * Adds (non-duplicate) entity to the list of entities on this map
     * but does not re-draw the game world to the terminal
     */
    public boolean addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);
            setTile(e.getX(),e.getY(),e.getSymbol());
            return true;
        }
        return false;
    }

    /**
     * Move a given entity around the map (if it exists in this game world)
     */
    public void moveEntity(Entity e, int newX, int newY) {
        if (isValidPosition(newX,newY)) {
            // TODO
        }
    }

}