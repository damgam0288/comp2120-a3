import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapCollisionTest {

    private Player player;
    private Map testMap;
    private NPC collidingNPC;
    private NPC nonCollidingNPC;

    @Before
    public void setup() throws Exception {
        player = new Player(2,2,'P', 10, 100,1);

        testMap = new Map("test-map-1","tests/resources/test-map-1.json",player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);

        collidingNPC = new NPC(2,2, 'N');
        nonCollidingNPC = new NPC(3,3,'N');
        testMap.addEntity(collidingNPC);
        testMap.addEntity(nonCollidingNPC);
    }

    @Test
    public void testCorrectCollidingEntity() {
        assertEquals(collidingNPC, testMap.getCollidingEntity());
        assertNotEquals(nonCollidingNPC, testMap.getCollidingEntity());
    }

    @Test
    public void testCorrectEntityAfterMove() {
        // Colliding NPC no longer colliding with player
        player.move(1, 1, testMap);
        assertNotEquals(collidingNPC, testMap.getCollidingEntity());

        // Non-colliding NPC now colliding with player
        assertEquals(nonCollidingNPC, testMap.getCollidingEntity());
    }

    @Test
    public void testNoCollision() {
        // No collision at all
        player.move(2,2,testMap);
        assertNull(testMap.getCollidingEntity());
    }

    @Test
    public void testCollisionWithNoEntities() throws Exception {
        player = new Player(2,2,'P', 10, 100);
        testMap = new Map("test-map-1","tests/resources/test-map-1.json",player,
                GlobalConstants.MIN_MAP_WIDTH, GlobalConstants.MIN_MAP_HEIGHT,
                GlobalConstants.MAX_MAP_WIDTH, GlobalConstants.MAX_MAP_HEIGHT);

        assertNull(testMap.getCollidingEntity());
    }
}
