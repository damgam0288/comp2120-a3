/** Player class is a specific type of Entity */
public class Player extends Entity {
    private int ap;  // Attack Power
    private int hp;  // Health Points
    private Item item;      // TODO 3 replace this with proper inventory

    private Item equippedItem;

    private Inventory inventory;


    /**
     * Constructor
     * @param startX the starting x-coordinate of the player.
     * @param startY the starting y-coordinate of the player.
     * @param symbol the character symbol representing the player on the map.
     * @param ap the player's initial attack points.
     * @param hp the player's initial health points.
     */
    public Player(int startX, int startY, char symbol, int ap, int hp) {
        super(startX, startY, symbol);
        this.ap = ap;
        this.hp = hp;
        this.inventory = new Inventory();
    }

    /**
     * Retrieves the player's attack points (AP).
     *
     * @return the player's current attack points.
     */
    public int getAP() {
        return ap;
    }

    /**
     * Retrieves the player's health points (HP).
     *
     * @return the player's current health points.
     */
    public int getHP() {
        return hp;
    }

    /**
     * Sets the player's attack points (AP).
     *
     * @param ap the new attack points to set for the player.
     */
    public void setAP(int ap) {
        this.ap = ap;
    }

    /**
     * Sets the player's health points (HP).
     *
     * @param hp the new health points to set for the player.
     */
    public void setHP(int hp) {
        this.hp = hp;
    }

    /**
     * Updates the player's position on the map by setting the x and y coordinates.
     *
     * @param x the new x-coordinate to set for the player.
     * @param y the new y-coordinate to set for the player.
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public void equipItem(Item item){
        this.equippedItem = item;
    }

    // TODO unequip item too? and method to retrieve equipped item?

    public void useHealthPotion(Item item){
        hp += item.getValue();      // TODO consider adding a max-health and increase hp up to that number?
        getInventory().removeItem(item);
    }


    public Item getItem() {
        return item;
    }

    public void receiveItem(Item item) {
        this.item = item;
    }

    /**
     * Attacks the enemy by reducing its HP based on the player's AP.
     * Displays the player's HP, AP, and the enemy's updated HP after the attack.
     *
     * @param enemy the enemy being attacked by the player.
     */
    public void attack(Enemy enemy) {
        enemy.setHP(enemy.getHP() - ap);
        System.out.println("Your Health Points (HP): " + hp + ", Your Attack Points (AP): " + ap);
        System.out.println("You attacked the enemy. Enemy HP is now: " + enemy.getHP());
    }

    /**
     * Initializes the player's inventory with a set of predefined items.
     * This method is typically called during game setup to populate the
     * inventory with starter items. Additional items can be added manually
     * or loaded from an external source.
     *
     * Currently adds the following example items:
     * - Health Potion
     * - Sword
     */
    public void initInventory() {
        inventory.addItem(new Item("Health Potion", 20));
        inventory.addItem(new Item("Sword", 10));
    }

    /**
     * Retrieves the player's inventory.
     *
     * @return The player's inventory object, which contains the list of items
     *         the player currently possesses and methods for interacting with them.
     */
    public Inventory getInventory() {
        return inventory;
    }
}
