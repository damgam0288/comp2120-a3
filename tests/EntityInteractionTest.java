import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class EntityInteractionTest {
    @Before
    public void setup() {
        GlobalConstants.setConfigFilePath("tests/resources/game-config.json");
    }


    @Test
    public void testPlayerAttackEnemy() {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        Enemy enemy = new Enemy(1, 1, 'E', 5, 20);

        assertEquals(20, enemy.getHP());
        player.attack(enemy);
        assertEquals(10, enemy.getHP());
        player.attack(enemy);
        assertEquals(0, enemy.getHP());
        assertEquals(1, player.getEnemiesDefeated());
    }

    @Test
    public void testPlayerGetAttacked() {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        Enemy enemy = new Enemy(1, 1, 'E', 5, 20);

        assertEquals(50, player.getHP());
        player.getAttacked(enemy);
        assertEquals(45, player.getHP());
        player.getAttacked(enemy);
        assertEquals(40, player.getHP());
    }
    @Test
    public void testEnemyAttackPlayer() {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        Enemy enemy = new Enemy(1, 1, 'E', 5, 20);

        assertEquals(50, player.getHP());
        enemy.attack(player);
        assertEquals(45, player.getHP());
        enemy.attack(player);
        assertEquals(40, player.getHP());
    }

    @Test
    public void testEnemyGetAttackedByPlayer() {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        Enemy enemy = new Enemy(1, 1, 'E', 5, 20);

        assertEquals(20, enemy.getHP());
        enemy.getAttacked(player);
        assertEquals(10, enemy.getHP());
        enemy.getAttacked(player);
        assertEquals(0, enemy.getHP());
    }

    @Test
    public void testInteractWithItem() throws Exception {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        NPC npc = new NPC(1, 1, 'N',"a","weapon2");
        Map currentMap = new Map("test-map-1","tests/resources/test-map-1.json",player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);

        npc.interact(player);
        assertFalse(npc.hasItem());
        assertEquals(1, player.getInventory().getItems().size());
        assertEquals("weapon2", player.getInventory().getItems().get(0).getName());
    }

    @Test
    public void testInteractWithoutItem() throws Exception {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        NPC npc = new NPC(1, 1, 'N', "b");
        Map currentMap = new Map("test-map-1","tests/resources/test-map-1.json",player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);

        assertFalse(npc.hasItem());
        npc.interact(player);
        assertTrue(player.getInventory().getItems().isEmpty());
    }
    @Test
    public void testPlayerHealthCannotGoBelowZero() {
        Player player = new Player(1, 1, 'P', 10, 50, 1);
        Enemy enemy = new Enemy(1, 1, 'E', 5, 100);

        player.getAttacked(enemy);
        player.getAttacked(enemy);
        player.getAttacked(enemy);
        player.getAttacked(enemy);
        player.getAttacked(enemy);

        assertTrue(player.getHP() >= 0);
    }

    @Test
    public void testMultipleInteractionsWithNPC() throws Exception {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        NPC npc = new NPC(1, 1, 'N', "a", "weapon2");
        Map currentMap = new Map("test-map-1", "tests/resources/test-map-1.json", player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);

        assertTrue(npc.hasItem());
        npc.interact(player);
        assertFalse(npc.hasItem());
        assertEquals(1, player.getInventory().getItems().size());

        npc.interact(player); // Interact again, should have no item
        assertFalse(npc.hasItem());
        assertTrue(player.getInventory().getItems().size() <= 1);
    }
}