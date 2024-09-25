public class Enemy extends Entity {
    private int hp;  // Health Points
    private int ap;  // Attack Power

    /**
     * Constructor for the Enemy class.
     *
     * @param startX the starting x-coordinate of the enemy.
     * @param startY the starting y-coordinate of the enemy.
     * @param symbol the character symbol representing the enemy on the map.
     * @param ap     the enemy's attack points.
     * @param hp     the enemy's health points.
     */
    public Enemy(int startX, int startY, char symbol, int ap, int hp) {
        super(startX, startY, symbol);
        this.ap = ap;
        this.hp = hp;
    }

    // Getter and setter for AP and HP

    /**
     * Retrieves the enemy's attack points (AP).
     *
     * @return the enemy's current attack points.
     */
    public int getAP() {
        return ap;
    }

    /**
     * Retrieves the enemy's health points (HP).
     *
     * @return the enemy's current health points.
     */
    public int getHP() {
        return hp;
    }

    /**
     * Sets the enemy's health points (HP).
     *
     * @param hp the new health points to set for the enemy.
     */
    public void setHP(int hp) {
        this.hp = hp;
    }

    /**
     * Updates the enemy's position on the map by setting the x and y coordinates.
     *
     * @param x the new x-coordinate to set for the enemy.
     * @param y the new y-coordinate to set for the enemy.
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Attacks the player by reducing the player's HP based on the enemy's AP.
     * Displays the enemy's HP, AP, and the player's updated HP after the attack.
     *
     * @param player the player being attacked by the enemy.
     */
    public void attack(Player player) {
        player.getAttacked(this);
        System.out.println("Enemy's Health Points (HP): " + getHP() + ", Enemy's Attack Points (AP): " + getAP());
        System.out.println("Enemy attacked you! Your HP is now: " + player.getHP());
    }

    /**
     * Calculates damage taken from the Player after an attack
     */
    public void getAttacked(Player player) {
        int newHp = hp - player.getAP();
        hp = Math.max(newHp, 0);
    }
}
