import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;
public class InventoryTest {
    private Inventory inventory;
    private Item sword;

    private Item shield;

    @Before
    public void setup() {
        inventory = new Inventory();
        sword = new Weapon("Sword", 10);
        shield = new Shield("Shield", 5);
    }

    @Test
    public void testAddItem1() {
        inventory.addItem(sword);
        List<Item> items = inventory.getItems();
        assertTrue(items.contains(sword));
    }

    @Test
    public void testRemoveItem1() {
        inventory.addItem(sword);
        inventory.removeItem(sword);
        List<Item> items = inventory.getItems();
        assertFalse(items.contains(sword));
    }

    @Test
    public void testGetEquippedItems() {
        sword.setEquipped(true);
        inventory.addItem(sword);
        List<Item> equippedItems = inventory.getEquippedItems();
        assertTrue(equippedItems.contains(sword));
    }

    @Test
    public void testGetUnequippedItems() {
        inventory.addItem(sword);
        inventory.addItem(shield);
        shield.setEquipped(true);
        List<Item> unequippedItems = inventory.getItems();
        assertTrue(unequippedItems.contains(sword));
        assertFalse(unequippedItems.contains(shield));
    }

    @Test
    public void testGetEquippedItemsWhenEmpty() {
        assertNull(inventory.getEquippedItems());
    }

    @Test
    public void testNullSafetyInGetEquippedItems() {
        assertNull(inventory.getEquippedItems());
    }

    @Test
    public void testAddAndEquipItem() {
        inventory.addItem(sword);
        sword.setEquipped(true);
        List<Item> equippedItems = inventory.getEquippedItems();
        assertNotNull(equippedItems);
        assertEquals(1, equippedItems.size());
        assertTrue(equippedItems.contains(sword));
    }

    @Test(timeout = 1000)
    public void testAddItem() {
        Item item = new Weapon("Sword", 10);
        inventory.addItem(item);

        assertEquals(1, inventory.getItems().size());
        assertTrue(inventory.getItems().contains(item));
    }

    @Test(timeout = 1000)
    public void testRemoveItem() {
        Item item = new Shield("Shield", 5);
        inventory.addItem(item);
        inventory.removeItem(item);

        assertEquals(0, inventory.getItems().size());
        assertFalse(inventory.getItems().contains(item));
    }

    @Test(timeout = 1000)
    public void testGetItem() {
        Item item = new HealthPotion("Health Potion", 50);
        inventory.addItem(item);

        Item retrievedItem = inventory.getItems().get(0);
        assertEquals(item, retrievedItem);
    }

}