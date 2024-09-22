public class Item {
    private String name;

    // value represents the quantitative value of the item.
    // e.g. For a sword value represents its attacking power,
    // for a health potion value represents the amount of health it will regain.
    private int value;


    public Item(String name, int value) {

/**
 * An abstract class that represents Items that can be held by Player or NPCs. This class
 * can be extended further to make weapons, shields, health items etc.
 */

public abstract class Item {

    String name = "";

    public Item(String name) {
        this.name = name;
        this.value = value;
    }

    //
    public int getValue(){
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

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