/**
 * Items can be held by Player or NPCs
 */

public interface Item {

    String name = "";

    public String getName();

    public void setName(String name);

}

class Weapon implements Item {

    private String name;
    private int ap;     // Attack points provided by this weapon

    public Weapon(String name, int ap) {
        this.name = name;
        this.ap = ap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

}
