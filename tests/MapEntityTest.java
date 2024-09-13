import org.junit.Before;
import org.junit.Test;

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

    }

    @Test
    public void testMoveEntity() {

    }

    @Test
    public void testRemoveEntity() {

    }
}
