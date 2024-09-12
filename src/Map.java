import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Conceptually, the game is played on a "playable grid". This class
 * represents the entire "playable grid" including the world, all entities
 * and any inventory items
 */
public class Map {

    private final String name;
    private char[][] world = null;  // Actual map of the world only
    private char[][] grid = null;   // Playable grid containing game map, player, items, NPCs etc.
    private final int width;
    private final int height;
    private final List<Entity> entities;

    /**
     * Constructor
     *
     * @param n        - name of the map
     * @param filePath - path to json file containing world
     * @throws IOException - in case cannot find json file
     */
    public Map(String n, String filePath) throws IOException {
        name = n;
        entities = new ArrayList<>();

        // Load JSON file
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lines = lines.stream()
                .map(line -> line.replaceAll("[\\[\\],\"]", "").trim())
                .filter(line -> !line.trim().isEmpty())
                .toList();

        // Put JSON data into the game world[][] array
        height = lines.size();
        width = lines.get(0).length();
        world = new char[width][height];
        grid = new char[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                world[j][i] = lines.get(i).charAt(j);
            }
        }
    }

    /**
     * Draws game map together with all entities, items etc. to the terminal
     */
    public void draw() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // First make the "floor"
                grid[x][y] = world[x][y];

                // Next put the entities on the "floor"
                for (Entity e : entities) {
                    if (e.getX() == x && e.getY() == y) {
                        grid[x][y] = e.getSymbol();
                        break;
                    }
                }

                // Finally, draw game grid
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
    }

    /**
     * Returns the char from the grid given x and y coordinates
     */
    public char getTile(int x, int y) {
        return grid[x][y];
    }

    /**
     * Sets the char of the play grid given x,y coordinates and the char
     */
    public void setTile(int x, int y, char value) {
        grid[x][y] = value;
    }

    /**
     * Checks if given x,y position is not over the game world boundaries
     * or over an obstacle
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height &&
                !isObstacle(x, y);
    }

    /**
     * Helper method for checking if given x,y coordinates overlaps a
     * char that stands for an obstacle e.g. a wall
     */
    private boolean isObstacle(int x, int y) {
        return getTile(x, y) == '#';
    }

    /**
     * Adds (non-duplicate) entity to the list of entities on this map
     * NOTE: does NOT re-draw the game world to the terminal
     */
    public void addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);
            setTile(e.getX(),e.getY(),e.getSymbol());
        }
    }

    /**
     * Move a given entity around the map if it exists in this game world
     * and if the move is valid.
     * NOTE: Does NOT re-draw the play grid to the terminal
     */
    public boolean moveEntity(Entity e, int newX, int newY) {
        if (isValidPosition(newX, newY)) {
            e.setX(newX); e.setY(newY);
            return true;
        }
        else {
            System.out.println("Can't move there!");
            return false;
        }
    }

    public boolean isColliding(Entity e1, Entity e2) {
        if (Objects.isNull(e1) || Objects.isNull(e2))
            return false;

        if (entities.isEmpty())
            return false;

        if (!(entities.contains(e1) && entities.contains(e2)))
            return false;

        return true;
    }


}