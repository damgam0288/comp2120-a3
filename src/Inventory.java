import java.util.ArrayList;
import java.util.List;


public class Inventory {
    private List<Item> items;

    // Constructor
    public Inventory() {
        items = new ArrayList<>();
    }

    // Add an item to the inventory
    public void addItem(Item item) {
        items.add(item);
    }

    // Remove an item from the inventory
    public void removeItem(Item item) {
        items.remove(item);
    }

    // Get the list of items that are not equipped
    public List<Item> getItems() {
        List<Item> unequippedItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.isEquipped()) {
                unequippedItems.add(item);
            }
        }
        return unequippedItems;
    }
}