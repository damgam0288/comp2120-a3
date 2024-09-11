import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Map {

    private final String name;
    private char[][] world = null;
    private char[][] grid = null;   // Playable grid containing game map, player, items, NPCs etc.
    private final int width;
    private final int height;

    private final List<Entity> entities;

    public Map(String n, String filePath) throws IOException {
        name = n;
        entities = new ArrayList<>();

        // Load JSON file
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lines = lines.stream()
                .map(line -> line.replaceAll("[\\[\\],\"]", "").trim())
                .filter(line -> !line.trim().isEmpty())
                .toList();

        // Create world using JSON data
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
                // Put world map into the playable grid
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

    /**
     * Checks if given x,y position is not over the game world boundaries
     * or over an obstacle
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height &&
                !isObstacle(x, y);
    }

    private boolean isObstacle(int x, int y) {
        return getTile(x, y) == '#';
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

    // TODO: Create tests, add tests to CI/CD pipeline

}