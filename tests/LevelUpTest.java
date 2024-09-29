import org.junit.Test;
import static org.junit.Assert.*;

public class LevelUpTest {

    @Test
    public void testPlayerLevelUp() {
        Player player = new Player(0, 0, 'P', 10, 50, 1);
        int initialMaxHp = player.getMaxHp();
        int initialAp = player.getAP();

        player.levelUp();
        assertEquals(2, player.getLevel());
        assertEquals(initialMaxHp + GlobalConstants.PLAYER_HP_INCREASE_PER_LEVEL, player.getMaxHp());
        assertEquals(initialAp + GlobalConstants.PLAYER_AP_INCREASE_PER_LEVEL, player.getAP());
    }

    @Test
    public void testPlayerLevelUpAtMaxLevel() {
        Player player = new Player(0, 0, 'P', 10, 50, GlobalConstants.PLAYER_MAX_LEVEL);
        player.levelUp();

        assertEquals(GlobalConstants.PLAYER_MAX_LEVEL, player.getLevel());
        assertEquals(50, player.getMaxHp());
        assertEquals(10, player.getAP());
    }

    @Test
    public void testEnemyLevelUp() {
        Enemy enemy = new Enemy(1, 1, 'E', 5, 20);
        int initialHp = enemy.getHP();
        int initialAp = enemy.getAP();

        enemy.levelUp();
        assertEquals(initialHp + 10, enemy.getHP());
        assertEquals(initialAp + 5, enemy.getAP());
    }
}

