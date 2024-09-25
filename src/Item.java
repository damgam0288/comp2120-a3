/**
 * An abstract class that represents Items that can be held by Player or NPCs. This class
 * can be extended further to make weapons, shields, health items etc.
 */

public abstract class Item {

    String name;
    private int value;      // This can be AP or HP for the specific item classes (below)
    private ItemType type;
    private boolean isEquipped = false;

    /**
     * Creates item given name and value.
     * @param name name of the weapon. Must be unique since it is an identifier when loading items from a JSON file
     * @param value value represents the quantitative value of the item
     *              e.g. For a sword value represents its attacking power,
     *              for a health potion value represents the amount of health it  will regain.
     */
    public Item(String name, ItemType type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    // ** Getters **
    public int getValue(){
        return value;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    // Check if the item is equipped
    public boolean isEquipped() {
        return isEquipped;
    }

    // Set the equipped status
    public void setEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }
}

/**
 * An equippable Item that increases Player's attack points
 */
class Weapon extends Item {
    public Weapon(String name, int ap) {
        super(name, ItemType.WEAPON, ap);
    }
}

/**
 * A consumable used to health player
 */
class HealthPotion extends Item {
    public HealthPotion(String name, int hp) {
        super(name, ItemType.HEALTHPOTION, hp);
    }
}

/**
 * Types of items that the Player can have in inventory.
 * This is useful for knowing which items can be equipped vs used e.g. a weapon vs health potion
 */
enum ItemType {
    WEAPON,
    SHIELD,
    HEALTHPOTION
}