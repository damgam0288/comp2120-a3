import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    private static Player player;

    @Before
    public void setup() {
        player = new Player(1,1,'P',10,100,1);
    }

    

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
