
/**
 * An abstract class that represents Items that can be held by Player or NPCs. This class
 * can be extended further to make weapons, shields, health items etc.
 */

public abstract class Item {

    String name = "";

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

class Weapon extends Item {

    private String name;
    private int ap;     // Attack points provided by this weapon

    public Weapon(String name, int ap) {
        super(name);
        this.ap = ap;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

}