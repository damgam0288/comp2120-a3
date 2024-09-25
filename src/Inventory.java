import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    public List<Item> getUnequippedItems() {
        List<Item> unequippedItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.isEquipped()) {
                unequippedItems.add(item);
            }
        }
        return unequippedItems;
    }

    // Get the list of items that are equipped
    public List<Item> getEquippedItems() {
        if (items.isEmpty())
            return null;

        if (Objects.isNull(items))
            return null;

        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            if (item.isEquipped()) {
                list.add(item);
            }
        }

        if (list.isEmpty())
            return null;

        return list;
    }
}