/** Player class is a specific type of Entity */
public class Player extends Entity {
    private int ap;  // Attack Power
    private int hp;  // Health Points

    /** Constructor */
    public Player(int startX, int startY, char symbol, int ap, int hp) {
        super(startX, startY, symbol);
        this.ap = ap;
        this.hp = hp;
    }

    // Getter and setter for AP and HP
    public int getAP() { return ap; }
    public int getHP() { return hp; }
    public void setAP(int ap) { this.ap = ap; }
    public void setHP(int hp) { this.hp = hp; }
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    // Method to attack the enemy
    public void attack(Enemy enemy) {
        enemy.setHP(enemy.getHP() - ap);
        System.out.println("Your Health Points (HP):"  + hp + ", Your Attack Points (AP): " + ap);
        System.out.println("You attacked the enemy. Enemy HP is now: " + enemy.getHP());
    }
}
