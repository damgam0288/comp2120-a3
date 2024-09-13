import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;

/** Tests whether Map.java keeps track of entities correctly */
public class MapEntityTest {
    private Player player;
    private Map testMap;

    @Before
    public void setup() throws Exception {
        player = new Player(2,2,'P');

        testMap = new Map("test-map-1","resources/test-map-1.json",player);

    }

    @Test
    public void testAddEntity() {
        fail();
    }

    @Test
    public void testMoveEntity() {
        fail();
    }

    @Test
    public void testRemoveEntity() {
        fail();
    }
}
