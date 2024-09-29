/**
 * A generic class that can be extended for Player, NPC and Enemy objects
 */
public abstract class Entity {

    private int x, y;
    private final char symbol;      // The char used to represent the Entity on the map

    /**
     * Constructor
     *
     * @param startX - starting x position on the game map
     * @param startY - starting y position on the game map
     * @param symbol - what char to use to represent this object on the game map;
     *               this cannot be changed
     */
    public Entity(int startX, int startY, char symbol) {
        this.x = startX;
        this.y = startY;
        this.symbol = symbol;
    }

    /**
     * Updates entity's position on the given game map
     * Note: recommend delta of only +/-1 to ensure accurate collision detection
     * @param deltaX - x direction movement
     * @param deltaY - y direction movement
     * @param map - game map on which this Entity has been placed
     */
    public void move(int deltaX, int deltaY, Map map) {
        int newX = x + deltaX;
        int newY = y + deltaY;
        map.moveEntity(this,newX,newY);
    }

    /**
     * Sets the x-coordinate of this entity
     *
     * @param x the x-coordinate to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of this entity
     *
     * @param y the y-coordinate to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the x-coordinate of this entity
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this entity
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the symbol representing this entity
     *
     * @return the symbol
     */
    public char getSymbol() {
        return symbol;
    }


}
