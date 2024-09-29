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

    /**
     * Retrieves the enemy's attack points (AP).
     *
     * @author Rifang Zhou
     *
     * @return the enemy's current attack points.
     */
    public int getAP() {
        return ap;
    }

    /**
     * Retrieves the enemy's health points (HP).
     *
     * @author Rifang Zhou
     *
     * @return the enemy's current health points.
     */
    public int getHP() {
        return hp;
    }

    /**
     * Sets the enemy's health points (HP).
     *
     * @author Rifang Zhou
     *
     * @param hp the new health points to set for the enemy.
     */
    public void setHP(int hp) {
        this.hp = hp;
    }

    /**
     * Attacks the player by reducing the player's HP based on the enemy's attack points (AP).
     * Displays the enemy's HP, AP, and the player's updated HP after the attack.
     *
     * @param player the player being attacked by the enemy.
     * @author Rifang Zhou
     */
    public void attack(Player player) {
        System.out.println("Enemy's Health Points (HP): " + getHP() + ", Enemy's Attack Points (AP): " + getAP());
        player.getAttacked(this);
        System.out.println("Enemy attacked you! Your HP is now: " + player.getHP());
    }

    /**
     * Calculates damage taken from the Player after an attack.
     * This method updates the enemy's HP based on the attack points of the player.
     *
     * @param player the player whose attack points are used to calculate damage.
     * @author Rifang Zhou
     */
    public void getAttacked(Player player) {
        int newHp = hp - player.getAP();
        hp = Math.max(newHp, 0);
    }

    /**
     * Levels up the enemy, increasing its health points (HP) and attack points (AP).
     * Displays the new HP and AP of the enemy after leveling up.
     *
     * @author Rifang Zhou
     */
    public void levelUp(){
        hp += 10;
        ap += 5;
        System.out.println("Enemies level up!"+"Enemies current hp:"+ hp + "   ap:"+ ap);
    }
}
