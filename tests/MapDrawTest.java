import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapDrawTest {

    private Player player;
    private Map map;
    private NPC npcOne;
    private NPC npcTwo;

    @Before
    public void setup() throws Exception {
        player = new Player(1,1,'P');
        npcOne = new NPC(2,1,'1');
        npcTwo = new NPC(3,1,'2');

        map = new Map("test-map-2","resources/test-map-2.json",player);
    }

    @Test
    public void testDrawPlainMap() {
        // Row 0 and 4
        for (int x = 0; x <= 9; x++) {
            assertEquals(map.getTile(x, 0), '#');
            assertEquals(map.getTile(x, 4), '#');
        }

        // All other rows in between
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 3; y++) {
                assertEquals(map.getTile(x, y), '.');
            }
        }
    }

    @Test
    public void testSetTile() {
        assertEquals(map.getTile(2,2),'.');
        map.setTile(2,2,'D');
        assertEquals(map.getTile(2,2),'D');
    }

    @Test
    public void testDrawWithPlayer() {
        map.draw();
        assertEquals(map.getTile(1,1),'P');
    }

    @Test
    public void testMovePlayerDrawMap() {
        player.move(3,1,map);
        map.draw();
        assertNotEquals(map.getTile(1,1),'P');
        assertEquals(map.getTile(4,2),'P');
    }

    @Test
    public void testDrawNPC() {
        map.addEntity(npcOne);
        map.addEntity(npcTwo);
        map.draw();

        assertEquals(map.getTile(2,1),'1');
        assertEquals(map.getTile(3,1),'2');
    }

    // TODO later - test show/hide level exit door

}
