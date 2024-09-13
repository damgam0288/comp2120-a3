import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class MapParameterTest {

    private Player player;
    private Map map;

    @Test(expected = IOException.class)
    public void testFileDoesNotExist() {
        fail();
    }

}
