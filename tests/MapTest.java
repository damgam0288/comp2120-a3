import exceptions.InvalidEntityPlacementException;
import org.junit.Before;
import org.junit.Test;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import static org.junit.Assert.*;

/** Tests that Map.java draws floor, player, NPCs etc. correctly */
public class MapTest {

    private Player player;
    private Map map1;
    private Map map2;
    private String entityJsonBadParameters = "tests/resources/test-entity-bad-parameters.json";

    @Before
    public void setup() throws Exception {
        player = new Player(1, 1, 'P', 10, 100, 1);
        map1 = new Map("test-map-1","tests/resources/test-map-1.json",player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
        map2 = new Map("test-map-2", "tests/resources/test-map-2.json", player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
    }

    @Test
    public void testDrawPlainMapCorrectly() {
        for (int x = 0; x <= 9; x++) {
            assertEquals(map2.getGridTile(x, 0), '#');
            assertEquals(map2.getGridTile(x, 5), '#');
        }

        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 3; y++) {
                assertEquals(map2.getGridTile(x, y), '.');
            }
        }
    }

    @Test
    public void testSetAndRetrieveGridTile() {
        assertEquals(map2.getGridTile(2, 2), '.');
        map2.setGridTile(2, 2, 'D');
        assertEquals(map2.getGridTile(2, 2), 'D');
    }

    @Test
    public void testDrawMapWithPlayer() {
        map2.draw();
        assertEquals(map2.getGridTile(1, 1), 'P');
    }

    @Test
    public void testMovePlayerAndRedrawMap() {
        player.move(3, 1, map2);
        map2.draw();
        assertNotEquals(map2.getGridTile(1, 1), 'P');
        assertEquals(map2.getGridTile(4, 2), 'P');
    }

    @Test(timeout = 1000)
    public void testAddAndRemoveEntity() throws Exception {
        NPC npcThree = new NPC(5, 1, '3');
        assertTrue(map2.addEntity(npcThree));
        assertFalse(map2.addEntity(npcThree));

        assertTrue(map2.removeEntity(npcThree));
        assertFalse(map2.removeEntity(npcThree));
    }

    @Test(timeout = 1000)
    public void testDrawEntitiesCorrectly() throws Exception {
        NPC npc1 = new NPC(2, 1, '1');
        NPC npc2 = new NPC(3, 1, '2');

        map2.addEntity(npc1);
        map2.addEntity(npc2);
        map2.draw();

        assertEquals(map2.getGridTile(2, 1), '1');
        assertEquals(map2.getGridTile(3, 1), '2');
    }

    @Test(timeout = 1000)
    public void testMovePlayerWithinMap() throws Exception {
        map2.addEntity(player);
        player.move(3, 2, map2);
        map2.draw();
        assertEquals(map2.getGridTile(4, 3), player.getSymbol());
    }

    @Test(timeout = 1000)
    public void testBoundaryPreventsPlayerFromMovingTooFarHorizontally() throws Exception {
        map2.addEntity(player);
        player.move(1, 0, map2); player.move(1, 0, map2);
        player.move(1, 0, map2); player.move(1, 0, map2);
        player.move(1, 0, map2); player.move(1, 0, map2);
        player.move(1, 0, map2); player.move(1, 0, map2);
        map2.draw();
        assertEquals(map2.getGridTile(8, 1), player.getSymbol());
    }

    @Test(timeout = 1000)
    public void testBoundaryStopsPlayerMovingTooFarVertically() throws Exception {
        map2.addEntity(player);
        player.move(0, 1, map2);
        player.move(0, 1, map2);
        player.move(0, 1, map2);
        player.move(0, 1, map2);
        player.move(0, 1, map2);
        player.move(0, 1, map2);
        map2.draw();
        assertEquals(map2.getGridTile(1, 4), player.getSymbol());
    }


    // ** Wrong/missing files **
    @Test(expected = IOException.class)
    public void testFileDoesNotExistThrowsException() throws Exception {
        Map map = new Map("non-existent-map", "resources/non-existent-file.json", player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPlayerThrowsException() throws Exception {
        Map map = new Map("non-existent-map", "resources/non-existent-file.json", null,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
    }

    // ** Entity placement tests **
    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityIllegalX() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc1", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityIllegalY() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc2", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityOutOfBoundsX() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc3", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityOutOfBoundsY() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc4", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityOnLeftWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc5", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityOnRightWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc6", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityOnTopWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc7", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityOnBottomWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc8", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntityOnCentreObstacle() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc9", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void testEntitiesOverlapping() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc10", entityJsonBadParameters));
        map1.addEntity(NPCLoader.loadObject("npc11", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void testVeryWideMapThrowsError() throws Exception {
        Map map = new Map("wide map", "tests/resources/very-wide-map.json", player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void testVeryTallMapThrowsError() throws Exception {
        Map map = new Map("tall map", "tests/resources/very-tall-map.json", player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void testVeryNarrowMapThrowsError() throws Exception {
        Map map = new Map("narrow map", "tests/resources/very-narrow-map.json", player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void testVeryShortMapThrowsError() throws Exception {
        Map map = new Map("short map", "tests/resources/very-short-map.json", player,
                GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
    }
}

