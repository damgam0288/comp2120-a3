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

    // Get the list of items
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }


    /**
     * Uses an item from the player's inventory based on the provided index.
     * If the specified item can be used (e.g., a Health Potion), the corresponding
     * action is performed. If the item cannot be used or the index is invalid,
     * an appropriate message is displayed.
     *
     * @param player    The player using the item.
     * @param itemIndex The index of the item in the inventory list to be used.
     *                  Must be within the range of the inventory list.
     */
    public void useItem(Player player, int itemIndex) {
        if (itemIndex < 0 || itemIndex >= items.size()) {
            System.out.println("Invalid item index.");
            return;
        }
        Item item = items.get(itemIndex);

        switch (item.getName()) {
            case "Health Potion":
                player.useHealthPotion(item);
                System.out.println("Used item: " + item.getName());
                break;
            default:
                System.out.println("can't use item: " + item.getName());
        }
    }

    /**
     * Equips an item from the player's inventory based on the provided index.
     * If the specified item can be equipped (e.g., a Sword), the player equips it.
     * If the item cannot be equipped or the index is invalid, an appropriate
     * message is displayed.
     *
     * @param player    The player equipping the item.
     * @param itemIndex The index of the item in the inventory list to be equipped.
     *                  Must be within the range of the inventory list.
     */
    public void equipItem(Player player, int itemIndex) {
        if (itemIndex < 0 || itemIndex >= items.size()) {
            System.out.println("Invalid item index.");
            return;
        }
        Item item = items.get(itemIndex);

        switch (item.getType()) {
            case WEAPON:
                player.equipItem(item);
                System.out.println("Equipped item: " + item.getName());
                break;
            default:
                System.out.println("Can't equip item: " + item.getName());
        }
    }
}