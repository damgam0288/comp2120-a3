import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapDrawTest {

    private Player player;
    private Map testMap;

    @Before
    public void setup() throws Exception {
        player = new Player(2,2,'P');

        testMap = new Map("test-map-1","resources/test-map-1.json",player);

    }

    @Test
    public void testDrawMapOne() {

    }

    @Test
    public void testDrawMapTwo() {

    }

    @Test
    public void testMovePlayerDrawMap() {

    }

    @Test
    public void testCannotLoadMap() {

    }

    @Test
    public void testGetTile() {

    }

    @Test
    public void testSetTile() {

    }
}
