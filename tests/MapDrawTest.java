import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapDrawTest {

    private Player player;
    private Map map;

    @Before
    public void setup() throws Exception {
        player = new Player(1,1,'P');

        map = new Map("test-map-1","resources/test-map-1.json",player);
    }

    @Test
    public void testDrawMapOne() {
        System.out.println(map.getTile(0,0));

    }

    @Test
    public void testDrawMapTwo() {

    }

    @Test
    public void testMovePlayerDrawMap() {

    }

    @Test
    public void testGetTile() {

    }

    @Test
    public void testSetTile() {

    }

    @Test
    public void testFileDoesNotExist() {

    }
}
