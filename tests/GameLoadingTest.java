import org.junit.Before;
import org.junit.Test;

public class GameLoadingTest {

    // run game.main and check it throws the error?

    private String pathToConfig = "tests/resources/test-config-too-many-npcs.json";
    private String pathToConfig2 = "tests/resources/test-config-too-many-enemies.json";

    @Test(timeout = 1000, expected = IllegalArgumentException.class)
    public void tooManyNpcs() throws Exception {
        // TODO after changing config file to load NPCs and Player directly
        //  from config file instead of referencing a separate file
    }
}
