public class Item {
    private String name;

    // value represents the quantitative value of the item.
    // e.g. For a sword value represents its attacking power,
    // for a health potion value represents the amount of health it will regain.
    private int value;


    public Item(String name, int value) {
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
}