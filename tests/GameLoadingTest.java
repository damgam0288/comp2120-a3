import exceptions.TooManyEntitiesException;
import org.junit.Before;
import org.junit.Test;


public class GameLoadingTest {

    private String pathToConfig = "tests/resources/test-config-too-many-npcs.json";
    private String pathToConfig2 = "tests/resources/test-config-too-many-enemies.json";

    @Test(timeout = 1000, expected = TooManyEntitiesException.class)
    public void tooManyNpcs() throws Exception {
        Game.main(new String[] {pathToConfig});
    }

    @Test(timeout = 1000, expected = TooManyEntitiesException.class)
    public void tooManyEnemies() throws Exception {
        Game.main(new String[] {pathToConfig2});
    }
}
