/**
 * An abstract class that represents Items that can be held by Player or NPCs. This class
 * can be extended further to make weapons, shields, health items etc.
 */

public abstract class Item {

    String name = "";
    private int value;

    /**
     * Creates item given a name.
     * @param name must be unique because it is used as an identifier when loading from a JSON file
     */
    public Item(String name) {
        this.name = name;
    }

    /**
     * Creates item given name and value.
     * @param name name of the weapon. Must be unique since it is an identifier when loading items from a JSON file
     * @param value value represents the quantitative value of the item
     *              e.g. For a sword value represents its attacking power,
     *              for a health potion value represents the amount of health it  will regain.
     */
    public Item(String name, int value) {
        this.name = name;
        this.value = value;
    }

    // ** Getters **
    public int getValue(){
        return value;
    }

    public String getName() {
        return name;
    }
}

/**
 * A specific type of Item used by the player to increase attack points which kills enemies easier
 */
class Weapon extends Item {

    private int ap;     // Attack points provided by this weapon
    private ItemType type;

    public Weapon(String name, int ap) {
        super(name);
        this.type = ItemType.WEAPON;
        this.ap = ap;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public ItemType getType() {
        return type;
    }
}

enum ItemType {
    WEAPON,
    SHIELD
}