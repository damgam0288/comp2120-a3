/**
 * A generic class for Player, NPC and Enemy objects
 */
abstract class Entity {

    private int x, y;
    private final char symbol;

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
     * Getters and setters
    */

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSymbol() {
        return symbol;
    }

}
