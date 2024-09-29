/**
 * An abstract class that represents Items that can be held by Player or NPCs. This class
 * can be extended further to make weapons, shields, health items etc.
 *
 * @author Damian Gamlath
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

    /**
     * Returns the value of this item
     *
     * @return the value of the item
     */
    public int getValue(){
        return value;
    }

    /**
     * Sets the value of this item, which can be health points
     * provided by the item, or Attack Points of the item etc.
     *
     * @param value int value to set
     */
    public void setValue(int value){
        this.value = value;
    }

    /**
     * Returns the name of this item e.g. "Longsword" or "Wooden shield"
     *
     * @return String name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this item
     *
     * @return the item type e.g. WEAPON, SHIELD, HEALTHPOTION etc.
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Checks if the item is equipped
     *
     * @return {@code true} if the item is equipped, {@code false} otherwise
     */
    public boolean isEquipped() {
        return isEquipped;
    }

    /**
     * Sets the equipped status of this item
     *
     * @param isEquipped the equipped status to set, {@code true} to equip the item, {@code false} to unequip it
     */
    public void setEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

}

/**
 * A specific WEAPON type of item that increases Player's attack points
 */
class Weapon extends Item {
    public Weapon(String name, int ap) {
        super(name, ItemType.WEAPON, ap);
    }
}

/**
 * A consumable used to heal player by the given number of Health Points
 */
class HealthPotion extends Item {
    public HealthPotion(String name, int hp) {
        super(name, ItemType.HEALTHPOTION, hp);
    }
}

/**
 * An specific Item that increases Player's max health points
 */
class Shield extends Item {
    public Shield(String name, int dp) {
        super(name, ItemType.SHIELD, dp);
    }
}

/**
 * Types of items that the Player can have in inventory.
 * This is useful for knowing which items can be equipped vs used e.g. a weapon vs health potion
 */
enum ItemType {
    WEAPON,
    SHIELD,
    HEALTHPOTION;

    /**
     * Parse a string input into an ItemType
     * @param input e.g. "weapon", "shield", "healthpotion"
     * @return the respective item type, or {@code null} if the ItemType could not be recognised
     */
    public static ItemType stringToType(String input) {
        return switch (input.toLowerCase()) {
            case "weapon" -> ItemType.WEAPON;
            case "shield" -> ItemType.SHIELD;
            case "healthpotion" -> ItemType.HEALTHPOTION;
            default -> null;
        };
    }
}