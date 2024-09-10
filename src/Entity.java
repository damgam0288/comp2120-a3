public class Entity {

    private int x, y;

    public Entity(int startX, int startY) {
        this.x = startX;
        this.y = startY;
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
        return 'P';
    }

    // TODO: Additional player functionality...
}
