public class Enemy extends Entity {
    private int hp;  // Health Points
    private int ap;  // Attack Power

    public Enemy(int startX, int startY, char symbol, int ap, int hp) {
        super(startX, startY, symbol);
        this.ap = ap;
        this.hp = hp;
    }

    // Getter and setter for AP and HP
    public int getAP() { return ap; }
    public int getHP() { return hp; }
    public void setHP(int hp) { this.hp = hp; }
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    // Method for enemy to attack the player
    public void attack(Player player) {
        player.setHP(player.getHP() - ap);
        System.out.println("Enemy's Health Points (HP): " + hp + ", Enemy's Attack Points (AP): " + ap);
        System.out.println("Enemy attacked you! Your HP is now: " + player.getHP());
    }
}
