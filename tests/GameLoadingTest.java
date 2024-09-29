import exceptions.TooManyEntitiesException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GameLoadingTest {

    private String pathToConfig = "tests/resources/test-config-too-many-npcs.json";
    private String pathToConfig2 = "tests/resources/test-config-too-many-enemies.json";

    private Game game;
    private Player player;
    private Map map;

    @Before
    public void setup() throws Exception {
        game = new Game("tests/resources/game-config.json");
        map = game.getCurrentMap();
        player = game.getPlayer();
    }

    @Test(timeout = 1000)
    public void testPlayerMoveWithoutCollision() {
        game.handleMovement("sss");
        assertEquals(1, player.getX());
        assertEquals(4, player.getY());
    }

    @Test(timeout = 1000)
    public void testComplexPlayerMove() {
        game.handleMovement("ssssdddddwwdd");
        assertEquals(8, player.getX());
        assertEquals(3, player.getY());
    }

    @Test(timeout = 1000)
    public void testPlayerMoveStoppedByObstacle() {
        int oldy = player.getY();
        game.handleMovement("ssssssss");
        assertEquals(oldy+6, player.getY());
    }

    @Test(timeout = 1000)
    public void testPlayerMoveStoppedByEntity() {
        game.handleMovement("sssdddddwwwaaaaaaaa");
        assertEquals(5, player.getX());
        assertEquals(1, player.getY());
    }



    @Test(timeout = 1000, expected = TooManyEntitiesException.class)
    public void tooManyNpcs() throws Exception {
        Game.main(new String[] {pathToConfig});
    }

    @Test(timeout = 1000, expected = TooManyEntitiesException.class)
    public void tooManyEnemies() throws Exception {
        Game.main(new String[] {pathToConfig2});
    }
}
