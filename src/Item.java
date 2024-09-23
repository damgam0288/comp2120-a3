/**
 * An abstract class that represents Items that can be held by Player or NPCs. This class
 * can be extended further to make weapons, shields, health items etc.
 */

public abstract class Item {

    String name = "";
    private int value;      // This can be AP or HP for the specific item classes (below)
    private ItemType type;
    /**
     * Creates item given a name.
     * @param name must be unique because it is used as an identifier when loading from a JSON file
     */
    public Item(String name, ItemType type) {
        this.name = name;
        this.type = type;
    }

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
}

/**
 * A specific type of Item used to attack opponents
 */
class Weapon extends Item {
    public Weapon(String name, int ap) {
        super(name, ItemType.WEAPON, ap);
    }
}

/**
 * A specific type of Item used to attack opponents
 */
class HealthPotion extends Item {
    public HealthPotion(String name, int hp) {
        super(name, ItemType.HEALTHPOTION, hp);
    }
}


enum ItemType {
    WEAPON,
    SHIELD,
    HEALTHPOTION
}