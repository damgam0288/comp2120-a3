public class Entity {

    private int x, y;
    private char symbol;

    public Entity(int startX, int startY, char symbol) {
        this.x = startX;
        this.y = startY;
        this.symbol = symbol;
    }

    public void move(int deltaX, int deltaY, Map map) {
        int newX = x + deltaX;
        int newY = y + deltaY;
        map.moveEntity(this,newX,newY);
    }

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
        // TODO: Replace with Symbol enum and .toString()
        return symbol;
    }

    // TODO: Additional player functionality...
}
