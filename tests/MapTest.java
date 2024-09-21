import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

/** Tests Map.java class draws floor, player, NPCs etc. correctly */
public class MapTest {

    private Player player;
    private Map map;
    private String entityFileOutOfBounds = "tests/resources/test-entity-bad-parameters.json";

    @Before
    public void setup() throws Exception {
        player = new Player(1,1,'P', 10, 100);
        map = new Map("test-map-2","tests/resources/test-map-2.json",player);
    }

    @Test
    public void testDrawPlainMap() {
        // Row 0 and 4
        for (int x = 0; x <= 9; x++) {
            assertEquals(map.getGridTile(x, 0), '#');
            assertEquals(map.getGridTile(x, 4), '#');
        }

        // All other rows in between
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 3; y++) {
                assertEquals(map.getGridTile(x, y), '.');
            }
        }
    }

    @Test
    public void testSetGridTile() {
        assertEquals(map.getGridTile(2,2),'.');
        map.setGridTile(2,2,'D');
        assertEquals(map.getGridTile(2,2),'D');
    }

    @Test
    public void testDrawWithPlayer() {
        map.draw();
        assertEquals(map.getGridTile(1,1),'P');
    }

    @Test
    public void testMovePlayerDrawMap() {
        player.move(3,1,map);
        map.draw();
        assertNotEquals(map.getGridTile(1,1),'P');
        assertEquals(map.getGridTile(4,2),'P');
    }

    @Test
    public void testAddRemoveEntity() {
        NPC npcThree = new NPC(5,1,'3');
        assertTrue(map.addEntity(npcThree));
        assertFalse(map.addEntity(npcThree));

        assertTrue(map.removeEntity(npcThree));
        assertFalse(map.removeEntity(npcThree));
    }

    @Test
    public void testDrawEntity() {
        NPC npcOne = new NPC(2,1,'1');
        NPC npcTwo = new NPC(3,1,'2');

        map.addEntity(npcOne);
        map.addEntity(npcTwo);
        map.draw();

        assertEquals(map.getGridTile(2,1),'1');
        assertEquals(map.getGridTile(3,1),'2');
    }

    @Test
    public void testMoveEntity() {
        NPC npcFour = new NPC(1,3,'4');
        map.addEntity(npcFour);
        npcFour.move(1,0,map);
        map.draw();

        assertEquals(map.getGridTile(2,3),'4');
    }

    @Test(expected = IOException.class)
    public void testFileDoesNotExistThrowsException() throws Exception {
        Map map1 = new Map("non-existent-map","resources/non-existent-file.json",player);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPlayerThrowsException() throws Exception {
        Map map1 = new Map("non-existent-map","resources/non-existent-file.json",null);
    }

    @Test(timeout = 1000, expected = IllegalArgumentException.class)
    public void entityXYIllegal() throws Exception {
        Map map1 = new Map("map1","tests/resources/test-map-1.json",player);

        NPC npc1 = NPCLoader.loadObject("npc1",entityFileOutOfBounds);
        map1.addEntity(npc1);

        NPC npc2 = NPCLoader.loadObject("npc2",entityFileOutOfBounds);
        map1.addEntity(npc2);

    }


}
