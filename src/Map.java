import exceptions.InvalidEntityPlacementException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Conceptually, the game is played on a "playable grid". This class
 * represents the entire "playable grid" including the world, all entities
 * and any inventory items
 */
public class Map {

    private String name;
    private char[][] world;     // Actual map of the world only
    private char[][] grid;      // Playable grid containing game map, player, items, NPCs etc.
    private int width;
    private int height;
    private Player player;
    private List<Entity> entities;

    /**
     * Constructor
     *
     * @param n        - name of the map
     * @param filePath - path to json file containing world
     * @throws IOException - in case cannot find json file
     *
     * @author Damian Gamlath
     */
    public Map(String n, String filePath, Player p,
                    int minWidth, int minHeight,
                    int maxWidth, int maxHeight) throws Exception {
        // Player != null to check position once here, instead of in .draw()
        if (Objects.isNull(p)) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        name = n;
        entities = new ArrayList<>();
        player = p;

        MapLoader.loadMapWorldFromFile(filePath, this,
                minWidth, minHeight, maxWidth, maxHeight);
    }

    /**
     * Draws game map together with all entities, items etc. to the terminal
     *
     * @author Damian Gamlath
     */
    public void draw() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // First make the "floor"
                grid[x][y] = world[x][y];

                // Then add the player
                if (player.getX() == x && player.getY() == y) grid[x][y] = player.getSymbol();

                // Next add the entities
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
     * Returns the char from the playable grid given x and y coordinates
     * @param x position of the tile
     * @param y position of the tile
     * @return the playable world's grid tile e.g. if 'P' is located above '.' floor tile
     * then it would return 'P'
     *
     * @author Damian Gamlath
     */
    public char getGridTile(int x, int y) {
        return grid[x][y];
    }

    /**
     * Sets the char of the play grid given x,y coordinates and the char
     * @param x position of the tile
     * @param y position of the tile
     * @value char symbol to set this tile to
     *
     * @author Damian Gamlath
     */
    public void setGridTile(int x, int y, char value) {
        grid[x][y] = value;
    }

    /**
     * The world tile is actually the floor tile of the game world (i.e. not including
     * any symbols for Player, NPC etc.)
     * @param x position of the tile
     * @param y position of the tile
     * @param value char value you want to change the floor tile to
     *
     * @author Damian Gamlath
     */
    public void setWorldTile(int x, int y, char value) {world[x][y] = value; }

    /**
     * Returns the width of the map
     *
     * @return the width of the map
     *
     * @author Damian Gamlath
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the map
     *
     * @param width the width to set
     *
     * @author Damian Gamlath
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the height of the map
     *
     * @return the height of the map
     *
     * @author Damian Gamlath
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the map
     *
     * @param height the height to set
     *
     * @author Damian Gamlath
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the array representing the floor of the map
     *
     * @param world char[x][y] array representing the game world
     *
     * @author Damian Gamlath
     */
    public void setWorld(char[][] world) {
        this.world = world;
    }

    /**
     * Sets the playable grid for this map
     *
     * @param grid char[x][y] array representing the playable world
     *
     * @author Damian Gamlath
     */
    public void setGrid(char[][] grid) {
        this.grid = grid;
    }

    /**
     * Returns the name of the map
     *
     * @return the name of the map
     *
     * @author Damian Gamlath
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the map
     *
     * @param name the name to set
     *
     * @author Damian Gamlath
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the player associated with this map
     *
     * @return the player object
     *
     * @author Damian Gamlath
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player for this map
     *
     * @param player the player to set
     *
     * @author Damian Gamlath
     */
    public void setPlayer(Player player) {
        this.player = player;
    }


    /**
     * Checks if given x,y position is not over the game world boundaries
     * or over an obstacle
     * @return {@code true} if position is NOT outside boundaries/on an obstacle
     * {@code false} if position IS outside boundary or on an obstacle
     *
     * @author Damian Gamlath
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height &&
                !isObstacle(x, y);
    }

    /**
     * Private helper method for checking if given x,y coordinates overlaps a
     * char that stands for an obstacle e.g. a wall
     *
     * @author Damian Gamlath
     */
    private boolean isObstacle(int x, int y) {
        return getGridTile(x, y) == '#';
    }

    /**
     * Adds (non-duplicate) entity to the list of entities on this map
     * NOTE: does NOT re-draw the game world to the terminal
     * @throws Exception if position is invalid or is colliding with an existing entity
     * @return {@code true} if the entity did not already exist
     * {@code false} if entity already exists in the list
     *
     * @author Damian Gamlath
     */
    public boolean addEntity(Entity e) throws Exception {
        if (entities.contains(e))
            return false;

        if (!isValidPosition(e.getX(), e.getY()))
            throw new InvalidEntityPlacementException("Map.addEntity: Bad start position");

        if (isCollidingWithEntity(e))
            throw new InvalidEntityPlacementException("Map.addEntity: Entities cannot overlap");

        entities.add(e);
        return true;
    }

    /**
     * Updates entity position on the map IF it exists in this game world
     * and if the move is valid.<br>
     * NOTE: Does NOT re-draw the play grid to the terminal - other classes
     * must do that separately if required
     *
     * @author Damian Gamlath
     */
    public void moveEntity(Entity e, int newX, int newY) {
        if (isValidPosition(newX, newY)) {
            e.setX(newX); e.setY(newY);
        }
        else {
            System.out.println("Can't move there!");
        }
    }

    /**
     * The entity that the Player is colliding with
     *
     * @return Null if player is null <br>
     * Null if no entities on the map <br>
     *
     * @author Damian Gamlath
     */
    public Entity getCollidingEntity() {
        if (Objects.isNull(player))
            return null;

        if (entities.isEmpty())
            return null;

        for (Entity e : entities) {
            if (entitiesOverlap(player,e)) return e;
        }

        return null;
    }

    /**
     * Checks if all enemies on the map have been defeated
     *
     * @author Rifang Zhou
     *
     * @return true if there are no enemies left on the map, false otherwise.
     */
    public boolean allEnemiesDefeated() {
        return entities.stream().noneMatch(e -> e instanceof Enemy);
    }

    /**
     * Determines if the player can move to the next map.
     * The player can only move to the next map if all enemies are defeated
     * and the player is standing on the designated 'O' tile (exit point).
     *
     * @author Rifang Zhou
     *
     * @return true if the player can move to the next map, false otherwise.
     */
    public boolean canMoveToNextMap() {
        return allEnemiesDefeated() && getGridTile(player.getX(), player.getY()) == 'O';
    }

    /**
     * Removes a specified entity from the map and resets the tile where it was located.
     * After removing the entity, the tile it occupied is set back to a default state,
     *
     * @author Rifang Zhou
     *
     * @param e The entity to be removed from the map.
     */
    public boolean removeEntity(Entity e) {
        if (entities.contains(e)) {
            entities.remove(e);
            setGridTile(e.getX(), e.getY(), world[e.getX()][e.getY()]);   // Reset the tile to floor value
            return true;
        }
        return false;
    }

    /**
     * Method to check the given entity is colliding
     * with another existing entity
     * @return {@code true} if the entity is colliding with another one
     * {@code false} if the entity is not colliding with another entity
     *
     * @author Damian Gamlath
     */
    public boolean isCollidingWithEntity(Entity entity) {
        if (entities.isEmpty())
            return false;

        for(Entity e : entities) {
            if (entitiesOverlap(e,entity))
                return true;
        }

        return false;
    }

    /**
     * Helper method to check NPC / Enemy collision
     * @param e1 first entity
     * @param e2 second entity
     * @return true if first and second entities are colliding
     *
     * @author Damian Gamlath
     */
    private boolean entitiesOverlap(Entity e1, Entity e2) {
        return (e1.getX() == e2.getX() &&
                e1.getY() == e2.getY());
    }

    /**
     * Retrieves the map number associated with the current map.
     * This method returns the name of the map, which serves as a placeholder for the map number.
     *
     * @author Rifang Zhou
     * @return the name of the map.
     */
    public String getMapNumber() {
        return name;
    }

    /**
     * Retrieves a list of enemies present in the current map.
     * This method iterates through the entities in the map and collects those that are instances of the Enemy class.
     *
     * @author Rifang Zhou
     * @return a list of enemies in the current map.
     */
    public List<Enemy> getEnemies(){
        List<Enemy> enemies = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Enemy) {
                enemies.add((Enemy) entity);
            }
        }
        return enemies;
    }
}