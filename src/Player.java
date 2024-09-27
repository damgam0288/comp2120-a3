import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Player class is a specific type of Entity */
public class Player extends Entity {
    private int ap;                 // Attack points
    private int hp;                 // Health Points
    private int maxHp;
    private Inventory inventory;
    private HashMap<ItemType, Item> equippedItems;
    private int level;              // Player level
    private int enemiesDefeated;    // Count of defeated enemies

    /**
     * Constructor
     *
     * @param startX the starting x-coordinate of the player.
     * @param startY the starting y-coordinate of the player.
     * @param symbol the character symbol representing the player on the map.
     * @param ap     the player's initial attack points.
     * @param hp     the player's initial health points.
     */
    public Player(int startX, int startY, char symbol, int ap, int hp, int level) {
        super(startX, startY, symbol);
        this.ap = ap;
        this.hp = hp;
        this.maxHp = hp;
        this.inventory = new Inventory();
        this.equippedItems = new HashMap<>();
        this.level = level; // Initial level
        this.enemiesDefeated = 0;
    }

    /**
     * Retrieves total (base + weapon) player attack points (ap)
     *
     * @author Rifang Zhou
     *
     * @return the player's current attack points.
     */
    public int getAP() {
        return ap +
            (getEquippedWeapon()!=null ?
                    getEquippedWeapon().getValue() : 0);
    }

    /**
     * Retrieves the player's health points (HP).
     *
     * @author Rifang Zhou
     *
     * @return the player's current health points.
     */
    public int getHP() {
        return hp;
    }

    /**
     * Sets the player's attack points (AP).
     *
     *@author Rifang Zhou
     *
     * @param ap the new attack points to set for the player.
     */
    public void setAP(int ap) {
        this.ap = ap;
    }

    /**
     * Sets the player's health points (HP).
     *
     * @author Rifang Zhou
     *
     * @param hp the new health points to set for the player.
     */
    public void setHP(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public String toStringHP() {
        return "Health: " + getHP() + " / " + getMaxHp();
    }

    public String displayStats() {
        return "Health: " + getHP() + "/" + getMaxHp() +
                ", Attack: " + getAP() +
                ", Level: " + getLevel() + "/" + GlobalConstants.PLAYER_MAX_LEVEL;
    }

    public String displayEquippedItems() {
        if (equippedItems.isEmpty())
            return "";

        return "Equipped: " + equippedItems.values().stream()
                .map(item -> item.name)
                .collect(Collectors.joining(", "));
    }

    /**
     * Sets the position of the entity in the game world by updating its x and y coordinates.
     * This method calls the appropriate setter methods to change the entity's position.
     *
     * @author Rifang Zhou
     *
     * @param x the new x-coordinate of the entity
     * @param y the new y-coordinate of the entity
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public void receiveItem(Item item) {
        inventory.addItem(item);
    }

    /**
     * Executes an attack on the specified enemy. This method applies damage to the enemy,
     * prints the player's current health and attack points, and checks if the enemy has
     * been defeated. If the enemy's health points (HP) drop to zero or below, the player
     * is notified of the defeat, and the number of defeated enemies is tracked.
     * Additionally, the method checks if the player is eligible to level up based on the
     * number of enemies defeated.
     *
     * @author Rifang Zhou
     *
     * @param enemy the enemy that the player is attacking
     */
    public void attack(Enemy enemy) {
        enemy.getAttacked(this);
        System.out.println("Your Health Points (HP): " + getHP() + ", Your Attack Points (AP): " + getAP());
        System.out.println("You attacked the enemy. Enemy HP is now: " + enemy.getHP());

        if (enemy.getHP() <= 0) {
            enemiesDefeated++;
            System.out.println("You defeated an enemy!");

            // Check if it's time to level up
            int enemiesRequiredForNextLevel = (level + 1); // Level 1 requires 2 enemies, level 2 requires 3, and so on.

            if (enemiesDefeated >= enemiesRequiredForNextLevel) {
                levelUp();
                enemiesDefeated = 0; // Reset defeated enemies count for the next level
            }
        }
    }

    /**
     * Handles the player's response to an attack from an enemy. This method calculates
     * the damage the player takes based on the enemy's attack points (AP) and the
     * player's equipped shield (if any). If the shield can absorb the damage, it is
     * reduced accordingly. If the shield is destroyed, the remaining damage is applied
     * to the player's health points (HP).
     *
     * @author Rifang Zhou
     *
     * @param enemy the enemy that is attacking the player
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
                System.out.println("Shield absorbed the damage. Shield strength: " + shield.getValue());
            } else {
                // Shield is destroyed, taking full damage
                totalDamage -= shieldValue;
                shield.setValue(0); // Set shield value to 0, it’s broken now
                System.out.println("Shield is broken. Remaining damage: " + totalDamage);

                // Apply remaining damage to player's health
                int newHp = this.hp - totalDamage;
                this.setHP(Math.max(newHp, 0));

                System.out.println("Player took damage. New HP: " + this.hp);

            }
        } else {
            // Apply remaining damage to player's health
            int newHp = this.hp - totalDamage;
            this.setHP(Math.max(newHp, 0));

            System.out.println("Player took damage. New HP: " + this.hp);
        }
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
                    System.out.println("Base AP: " + ap + " Weapon AP: " + item.getValue());
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
        } else {
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
                int oldHp = hp;
                hp += item.getValue();
                hp = Math.min(hp, maxHp);
                inventory.removeItem(item);
                System.out.println("Used item: " + item.getName());
                System.out.println("HP increased by: " + (hp - oldHp));
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
     * @return the equipped shield item, or null if no shield is equipped.
     */
    public Item getEquippedShield() {
        return equippedItems.get(ItemType.SHIELD);
    }

    public Item getEquippedWeapon() {return equippedItems.get(ItemType.WEAPON); }

    /**
     * Represents a player in the game with attributes for level, health points (HP),
     * and attack points (AP). This class provides methods to get the current level,
     * level up the player, and retrieve the number of defeated enemies.
     *
     * @author Rifang Zhou
     *
     * @return the current level of the player
     */
    public int getLevel() {
        return level;
    }

    /**
     * Increases the player's level by 1, while also updating the player's
     * maximum HP and AP based on predefined constants. Prints a congratulatory
     * message upon leveling up. If the player is already at the maximum level,
     * a message is displayed indicating that the maximum level has been reached.
     *
     * @author Rifang Zhou
     */
    public void levelUp() {
        if (level < GlobalConstants.PLAYER_MAX_LEVEL) {
            level++;
            maxHp   += GlobalConstants.PLAYER_HP_INCREASE_PER_LEVEL; // Increase base HP by 20
            ap      += GlobalConstants.PLAYER_AP_INCREASE_PER_LEVEL;    // Increase base AP by 10
            System.out.println("Congratulations! You've reached level " + level + "!");
            System.out.println("Your new MaxHP: " + maxHp + ", Your new AP: " + ap);
        } else {
            System.out.println("You have reached the maximum level!"); // Print message if level is already max
        }
    }

    /**
     * Returns the number of enemies that have been defeated by the player.
     *
     * @author Rifang Zhou
     *
     * @return the number of enemies defeated
     */
    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }
}

