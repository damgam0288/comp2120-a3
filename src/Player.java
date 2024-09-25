import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Player class is a specific type of Entity */
public class Player extends Entity {
    private int ap;  // Attack Power
    private int hp;  // Health Points
    private int dp; // Defence Points
    private Inventory inventory;
    private HashMap<ItemType, Item> equippedItems;

    /**
     * Constructor
     *
     * @param startX the starting x-coordinate of the player.
     * @param startY the starting y-coordinate of the player.
     * @param symbol the character symbol representing the player on the map.
     * @param ap     the player's initial attack points.
     * @param hp     the player's initial health points.
     */
    public Player(int startX, int startY, char symbol, int ap, int hp) {
        super(startX, startY, symbol);
        this.ap = ap;
        this.hp = hp;
        this.inventory = new Inventory();
        this.equippedItems = new HashMap<>();

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
     * Sets the player's defence points (DP).
     *
     * @param dp the new defence points to set for the player.
     */
    public void setDP(int dp){
        this.dp = dp;
    }

    /**
     * Retrieves the player's defence points (DP).
     *
     * @return the player's current defence points.
     */
    public int getDp() {
        return dp;
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

    public void useHealthPotion(Item item) {
        hp += item.getValue();      // TODO consider adding a max-health and increase hp up to that number?
        getInventory().removeItem(item);
    }

    public void receiveItem(Item item) {
        inventory.addItem(item);
    }

    /**
     * Attacks the enemy by reducing its HP based on the player's AP.
     * Displays the player's HP, AP, and the enemy's updated HP after the attack.
     *
     * @param enemy the enemy being attacked by the player.
     */
    public void attack(Enemy enemy) {
        enemy.getAttacked(this);
        System.out.println("Your Health Points (HP): " + hp + ", Your Attack Points (AP): " + ap);
        System.out.println("You attacked the enemy. Enemy HP is now: " + enemy.getHP());
    }

    /**
     * Calculates damage taken from the Player after an attack.
     * If the player has a shield equipped, the damage will be mitigated by the shield's value.
     */
    public void getAttacked(Enemy enemy) {
        Item shield = getEquippedShield();

        // Total damage the player is going to take
        int totalDamage = enemy.getAP();

        if (shield != null) {
            int shieldValue = shield.getValue();

            // Calculate damage absorbed by the shield
            if (shieldValue >= totalDamage) {
                // Shield absorbs all the damage
                shield.setValue(shieldValue - totalDamage);
                totalDamage = 0;
                System.out.println("Shield absorbed the damage. Shield value left: " + shield.getValue());
            } else {
                // Shield is destroyed, taking full damage
                totalDamage -= shieldValue;
                shield.setValue(0); // Set shield value to 0, it’s broken now
                System.out.println("Shield is broken. Remaining damage: " + totalDamage);
            }
        }

        // Apply remaining damage to player's health
        int newHp = this.hp - totalDamage;
        this.setHP(Math.max(newHp, 0));

        System.out.println("Player took damage. New HP: " + this.hp);
    }


    /**
     * Initializes the player's inventory with a set of predefined items.
     * This method is typically called during game setup to populate the
     * inventory with starter items. Additional items can be added manually
     * or loaded from an external source.
     * <p>
     * Currently adds the following example items:
     * - Health Potion
     * - Sword
     */
    public void initInventory() {
        // Todo: Remove this method?
    }

    /**
     * Retrieves the player's inventory.
     *
     * @return The player's inventory object, which contains the list of items
     * the player currently possesses and methods for interacting with them.
     */
    public Inventory getInventory() {
        return inventory;
    }


    /**
     * Equips an item from the player's inventory based on the provided index.
     * The method checks if the item is a type that can be equipped (e.g., a weapon or shield).
     * If an item of the same type is already equipped, it will be unequipped first.
     *
     * @param itemIndex The index of the item in the inventory list that the player wants to equip.
     *                  Must be within the range of the inventory list.
     *                  If the index is out of bounds, an error message is displayed, and no item is equipped.
     */
    public void equipItem(int itemIndex) {
        List<Item> items = inventory.getItems();

        // Validate item index
        if (itemIndex < 0 || itemIndex >= items.size()) {
            System.out.println("Invalid item index.");
            return;
        }

        Item item = items.get(itemIndex);
        ItemType itemType = item.getType();

        // Check if the item can be equipped based on its type
        switch (itemType) {
            case WEAPON:
            case SHIELD:

                // If there's already an item of this type equipped, unequip it first
                if (equippedItems.containsKey(itemType)) {
                    unequipItem(item);
                }

                // Equip the new item
                equippedItems.put(itemType, item);
                item.setEquipped(true); // Assuming setEquipped(true) marks the item as equipped
                System.out.println("Equipped item: " + item.getName());
                if (item.getType() == ItemType.WEAPON) {
                    System.out.println("AP increased by: " + item.getValue());
                    ap += item.getValue();
                }
                else if (item.getType() == itemType.SHIELD) {
                    System.out.println("HP increased by: " + item.getValue());
                }
                break;

            default:
                // If the item type cannot be equipped, display a message
                System.out.println("Can't equip item: " + item.getName());
        }
    }

    /**
     * Unequips an item currently equipped by the player based on its type.
     * The method checks if an item of the given type (e.g., weapon, shield) is currently equipped.
     * If such an item is found, it will be removed from the equipped items list and marked as unequipped.
     * If no item of the specified type is equipped, a message is displayed indicating this.
     *
     * @param item The item to unequip (e.g., WEAPON, SHIELD).
     *                 The itemType must correspond to a type of item that can be equipped by the player.
     */
    public void unequipItem(Item item) {
        ItemType itemType = item.getType();
        if (equippedItems.containsKey(itemType)) {
            Item unequippedItem = equippedItems.remove(itemType);
            unequippedItem.setEquipped(false);
            System.out.println("Unequipped item: " + unequippedItem.getName());
            if (itemType == ItemType.SHIELD){
                dp -= item.getValue();
            } else if ( itemType == ItemType.WEAPON){
                hp -= item.getValue();
            }
        }
        else {
            System.out.println("No item of type " + itemType + " is currently equipped.");
        }
    }


    /**
     * Uses an item from the player's inventory based on the provided index.
     * If the specified item can be used (e.g., a Health Potion), the corresponding
     * action is performed. If the item cannot be used or the index is invalid,
     * an appropriate message is displayed.
     *
     * @param itemIndex The index of the item in the inventory list to be used.
     *                  Must be within the range of the inventory list.
     */
    public void useItem(int itemIndex) {
        List<Item> items = inventory.getItems();

        if (itemIndex < 0 || itemIndex >= items.size()) {
            System.out.println("Invalid item index.");
            return;
        }
        Item item = items.get(itemIndex);

        switch (item.getType()) {
            case HEALTHPOTION:
                System.out.println("Used item: " + item.getName());
                hp += item.getValue();
                System.out.println("HP increased by: " + item.getValue());
                break;
            default:
                System.out.println("can't use item: " + item.getName());
        }
    }

    /**
     * Retrieves the list of items that are currently equipped by the player.
     * This method returns a list of items that are equipped, including weapons and shields.
     *
     * @return a list of equipped items.
     */
    public List<Item> getEquippedItems() {
        return new ArrayList<>(equippedItems.values());
    }

    /**
     * Retrieves the equipped shield for the player.
     *
     * @return the equipped shield item, or null if no shield is equipped.
     */
    public Item getEquippedShield() {
        return equippedItems.get(ItemType.SHIELD);
    }


}
