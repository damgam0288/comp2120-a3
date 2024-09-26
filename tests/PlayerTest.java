import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;
    private Item sword;
    private Item shield;
    private Item healthPotion;
    private Enemy enemy;

    @Before
    public void setUp() {

        player = new Player(1,1,'P',10,100, 1);

        // Create items
        sword = new Weapon("Sword", 20);
        shield = new Shield("Shield", 30);
        healthPotion = new HealthPotion("Health Potion", 50);

        // Create a basic enemy with AP = 15, HP = 50
        enemy = new Enemy(0, 0, 'E', 15, 50);
    }

    @Test
    public void testInitialPlayerStats() {
        assertEquals(10, player.getAP());
        assertEquals(100, player.getHP());
    }

    @Test
    public void testEquipItem_weapon() {
        player.receiveItem(sword);
        player.equipItem(0);

        // Check if the player's AP increased by the weapon's value
        assertEquals(30, player.getAP());

        // Check if the sword is in the equipped items list
        List<Item> equippedItems = player.getEquippedItems();
        assertTrue(equippedItems.contains(sword));
    }

    @Test
    public void testEquipItem_shield() {
        player.receiveItem(shield);
        player.equipItem(0);

        // Check if the shield is equipped
        assertNotNull(player.getEquippedShield());
        assertEquals(shield, player.getEquippedShield());
    }

    @Test
    public void testUnequipItem() {
        player.receiveItem(sword);
        System.out.println(player.getAP());
        player.equipItem(0);
        System.out.println(player.getAP());
        player.unequipItem(sword);
        System.out.println(player.getAP());

        // Check that the sword is unequipped
        assertFalse(player.getEquippedItems().contains(sword));
        assertEquals(10, player.getAP());  // Player's base AP
    }

    @Test
    public void testUseHealthPotion() {
        player.receiveItem(healthPotion);
        player.useItem(0);

        // Check that the player's HP has increased
        assertEquals(150, player.getHP());

        // Check that the health potion is removed from the inventory
        assertFalse(player.getInventory().getItems().contains(healthPotion));
    }

    @Test
    public void testAttackEnemy() {
        player.receiveItem(sword);
        player.equipItem(0);

        // Player attacks the enemy
        player.attack(enemy);

        // Enemy HP should decrease based on player's AP (30 in this case)
        assertEquals(20, enemy.getHP());
    }

    @Test
    public void testGetAttackedWithShield() {
        player.receiveItem(shield);
        player.equipItem(0);

        // Enemy attacks player, shield should absorb damage
        player.getAttacked(enemy);

        // Check shield value after absorbing damage
        assertEquals(15, shield.getValue());  // Shield started with 30, absorbed 15 AP

        // Player's HP should not change since the shield absorbed all damage
        assertEquals(100, player.getHP());
    }

    @Test
    public void testGetAttackedWithoutShield() {
        // Enemy attacks player directly, no shield equipped
        player.getAttacked(enemy);

        // Player HP should be reduced by the enemy's AP (15)
        assertEquals(85, player.getHP());
    }

    // todo Test adding/removing items from inventory

    @Test(timeout = 1000)
    public void healthPotionHealsCorrectly() {
        player.getInventory().addItem(new HealthPotion("health", 10));
        player.getInventory().addItem(new HealthPotion("health", 10));

        // Hurt player
        player.setHP(90);
        assertEquals(90, player.getHP());

        // Heal player
        player.useItem(0);
        assertEquals(100,player.getHP());
    }

    @Test(timeout = 1000)
    public void healthPotionDoesntGoAboveMaxHP() {
        player.getInventory().addItem(new HealthPotion("health", 10));
        player.getInventory().addItem(new HealthPotion("health", 10));

        // "over" heal player
        player.useItem(0);
        player.useItem(0);
        assertEquals(100,player.getHP());
    }

    // Todo check shield affects damage points correctly

    @Test(timeout = 1000)
    public void returnCorrectItemsEquipped() {
        Weapon weapon = new Weapon("weapon1",25);
        player.getInventory().addItem(weapon);

        // Before equipping
        assertNull(player.getInventory().getEquippedItems());

        // After equipping
        player.equipItem(0);
        assertTrue(player.getInventory().getEquippedItems().contains(weapon));
    }

    @Test(timeout = 1000)
    public void returnCorrectAPWithAndWithoutWeapons() {
        // Without weapon
        assertEquals(10, player.getAP());

        // With weapon
        Weapon weapon = new Weapon("weapon1",25);
        player.getInventory().addItem(weapon);
        player.equipItem(0);

        assertEquals(35, player.getAP());
    }
}
