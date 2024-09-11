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

        if (map.isWalkable(newX, newY)) {

            map.moveEntity(this, newX, newY);

            x = newX;
            y = newY;
        }
        else {
            System.out.println("You can't move there!");
        }
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
