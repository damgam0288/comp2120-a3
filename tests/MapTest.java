import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class MapTest {

    private Player player;
    private Map testMap;

    @Before
    public void setup() throws Exception {
        player = new Player(2,2,'P');
        testMap = new Map("test-map-1","resources/test-map-1.json",player);
    }

    @Test
    public void testGetCollidingEntity() {
        NPC collidingNPC = new NPC(2,2, 'N');
        NPC nonCollidingNPC = new NPC(3,3,'N');

        testMap.addEntity(collidingNPC);
        testMap.addEntity(nonCollidingNPC);

        assertEquals(collidingNPC, testMap.getCollidingEntity());
        assertNotEquals(nonCollidingNPC, testMap.getCollidingEntity());
    }
}
