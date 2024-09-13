import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.fail;

/**
 * Tests whether Map.java throws Exceptions with bad file names or parameters
 */
public class MapParameterTest {

    private Player player;
    private Map map;

    @Test(expected = IOException.class)
    public void testFileDoesNotExist() {
        fail();
    }

}
