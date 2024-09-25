import exceptions.InvalidEntityPlacementException;
import org.junit.Before;
import org.junit.Test;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import static org.junit.Assert.*;

/** Tests Map.java class draws floor, player, NPCs etc. correctly */
public class MapTest {

    private Player player;
    private Map map1;
    private Map map2;
    private String entityJsonBadParameters = "tests/resources/test-entity-bad-parameters.json";

    @Before
    public void setup() throws Exception {
        player = new Player(1, 1, 'P', 10, 100, 1);
        map1 = new Map("test-map-1","tests/resources/test-map-1.json",player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
        map2 = new Map("test-map-2", "tests/resources/test-map-2.json", player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
    }

    @Test
    public void testDrawPlainMap() {
        // Row 0 and 4
        for (int x = 0; x <= 9; x++) {
            assertEquals(map2.getGridTile(x, 0), '#');
            assertEquals(map2.getGridTile(x, 5), '#');
        }

        // All other rows in between
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 3; y++) {
                assertEquals(map2.getGridTile(x, y), '.');
            }
        }
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void veryWideMapThrowsError() throws Exception {
        Map map = new Map("wide map", "tests/resources/very-wide-map.json", player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void veryTallMapThrowsError() throws Exception {
        Map map = new Map("tall map", "tests/resources/very-tall-map.json", player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void veryNarrowMapThrowsError() throws Exception {
        Map map = new Map("narrow map", "tests/resources/very-narrow-map.json", player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
    }

    @Test(timeout = 1000, expected = SizeLimitExceededException.class)
    public void veryShortMapThrowsError() throws Exception {
        Map map = new Map("short map", "tests/resources/very-short-map.json", player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
    }

    // ** Map.java methods **
    @Test
    public void testSetGridTile() {
        assertEquals(map2.getGridTile(2, 2), '.');
        map2.setGridTile(2, 2, 'D');
        assertEquals(map2.getGridTile(2, 2), 'D');
    }

    @Test
    public void testDrawWithPlayer() {
        map2.draw();
        assertEquals(map2.getGridTile(1, 1), 'P');
    }

    @Test
    public void testMovePlayerDrawMap() {
        player.move(3, 1, map2);
        map2.draw();
        assertNotEquals(map2.getGridTile(1, 1), 'P');
        assertEquals(map2.getGridTile(4, 2), 'P');
    }

    @Test(timeout = 1000)
    public void testAddRemoveEntity() throws Exception {
        NPC npcThree = new NPC(5, 1, '3');
        assertTrue(map2.addEntity(npcThree));
        assertFalse(map2.addEntity(npcThree));

        assertTrue(map2.removeEntity(npcThree));
        assertFalse(map2.removeEntity(npcThree));
    }

    @Test(timeout = 1000)
    public void testDrawEntity() throws Exception {
        NPC npcOne = new NPC(2, 1, '1');
        NPC npcTwo = new NPC(3, 1, '2');

        map2.addEntity(npcOne);
        map2.addEntity(npcTwo);
        map2.draw();

        assertEquals(map2.getGridTile(2, 1), '1');
        assertEquals(map2.getGridTile(3, 1), '2');
    }

    @Test(timeout = 1000)
    public void testMoveEntity() throws Exception {
        NPC npcFour = new NPC(1, 3, '4');
        map2.addEntity(npcFour);
        npcFour.move(1, 0, map2);
        map2.draw();

        assertEquals(map2.getGridTile(2, 3), '4');
    }

    // ** Wrong/missing files **
    @Test(expected = IOException.class)
    public void testFileDoesNotExistThrowsException() throws Exception {
        Map map = new Map("non-existent-map", "resources/non-existent-file.json", player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPlayerThrowsException() throws Exception {
        Map map = new Map("non-existent-map", "resources/non-existent-file.json", null,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);
    }

    // ** Entity out of bounds **
    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityIllegalX() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc1", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityIllegalY() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc2", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityOutOfBoundsX() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc3", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityOutOfBoundsY() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc4", entityJsonBadParameters));
    }

    // ** Entity placed on an obstacle **
    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityOnLeftWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc5", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityOnRightWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc6", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityOnTopWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc7", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityOnBottomWall() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc8", entityJsonBadParameters));
    }

    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entityOnCentreObstacle() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc9", entityJsonBadParameters));
    }

    // ** Overlapping entities **
    @Test(timeout = 1000, expected = InvalidEntityPlacementException.class)
    public void entitiesOverlapping() throws Exception {
        map1.addEntity(NPCLoader.loadObject("npc10", entityJsonBadParameters));
        map1.addEntity(NPCLoader.loadObject("npc11", entityJsonBadParameters));
    }

}
