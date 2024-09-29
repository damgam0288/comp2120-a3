import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import java.util.ArrayList;
import java.util.List;

/**
 * The Inventory class represents a player's inventory in the game.
 * It manages a collection of items, allowing the player to add, remove,
 * and retrieve equipped or unequipped items.
 *
 * @author Noah Martin
 */
public class Inventory {
    private List<Item> items;

    /**
     * Constructor for the Inventory class.
     * Initializes an empty list of items.
     */
    public Inventory() {
        items = new ArrayList<>();
    }

    /**
     * Adds an item to the player's inventory.
     *
     * @param item The item to be added to the inventory.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the player's inventory.
     *
     * @param item The item to be removed from the inventory.
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Retrieves a list of unequipped items from the inventory.
     *
     * @return A list of unequipped items.
     */
    public List<Item> getItems() {
        List<Item> unequippedItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.isEquipped()) {
                unequippedItems.add(item);
            }
        }
        return unequippedItems;
    }

    /**
     * Retrieves a list of equipped items from the inventory.
     *
     * @return A list of equipped items, or {@code null} if there are no items equipped.
     */
    public List<Item> getEquippedItems() {
        if (items.isEmpty())
            return null;

        List<Item> equippedItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isEquipped()) {
                equippedItems.add(item);
            }
        }

        if (equippedItems.isEmpty())
            return null;

        return equippedItems;
    }
}