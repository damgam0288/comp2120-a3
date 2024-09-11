import java.util.ArrayList;
import java.util.List;

public class Map {

    private final String name;
    private char[][] world = null;
    private char[][] grid = null;
    private final int width;
    private final int height;

    private final List<Entity> entities;

    public Map(String name, char[][] world) {
        this.name = name;
        this.world = world;                         // This is only the game map with the floor, walls, obstacles etc.
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
                // Initialize game grid with the game map
                grid[x][y] = world[x][y];

                // Put entities onto the grid
                for (Entity e : entities) {
                    if (e.getX() == x && e.getY() == y) {
                        grid[x][y] = e.getSymbol();
                        break;
                    }
                }

                // Draw game grid
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
        System.out.println();
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
    public void addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);
            setTile(e.getX(),e.getY(),e.getSymbol());
        }
    }

    /**
     * Move a given entity around the map if it exists in this game world
     * and if the move is valid
     */
    public boolean moveEntity(Entity e, int newX, int newY) {
        if (isValidPosition(newX,newY)) {
            e.setX(newX);
            e.setY(newY);
            return true;
        }
        else {
            System.out.println("Can't move there!");
            return false;
        }
    }

}