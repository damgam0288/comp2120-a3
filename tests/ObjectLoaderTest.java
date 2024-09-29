import org.json.JSONException;
import static org.junit.Assert.*;
import org.junit.Test;

import java.nio.file.NoSuchFileException;

/**
 * This test class tests all loaders e.g. for NPCs, Items etc.
 * Expected reasons for test failure include typos in the filepath, a JSON file with
 * the wrong KEY parameter and/or missing VALUE parameter
 */

// todo fix these tests for new NPC loader

public class ObjectLoaderTest {


    private static String configPath = "tests/resources/game-config.json";

    // Abstract Loader testing
    @Test(expected = NoSuchFileException.class, timeout=1000)
    public void abstractLoaderNoFileException() throws Exception {
        ObjectLoader.findObject
                ("npc1","npcs","name",
                        "file-doesnt-exist.json");
    }

    @Test(expected = JSONException.class, timeout=1000)
    public void abstractLoaderNoMatchingArrayString() throws Exception {
        ObjectLoader.findObject
                ("npc1","array-doesnt-exist","name",
                        configPath);
    }

    @Test(expected = JSONException.class, timeout=1000)
    public void abstractLoaderBadKeyString() throws Exception {
        ObjectLoader.findObject
                ("npc1","npcs","bad-keystring",
                        configPath);
    }

    @Test(expected = JSONException.class, timeout=1000)
    public void abstractLoaderNoMatchingTargetString() throws Exception {
        ObjectLoader.findObject
                ("bad-target","npcs","name",
                        configPath);
    }

    // Item Loader testing
    @Test(timeout=1000)
    public void itemLoaderReturnsCorrectItem() throws Exception {
        Weapon weapon1 = (Weapon) ItemLoader.loadObject("weapon1",
                configPath);

        assertNotNull(weapon1.getName());

        assertEquals("weapon1",weapon1.getName());
        assertEquals(ItemType.WEAPON, weapon1.getType());
        assertEquals(10, weapon1.getValue());

        // TODO Requires more tests to test different types of items
    }

    @Test(timeout=1000, expected = JSONException.class)
    public void itemLoaderItemNotFound() throws Exception {
        ItemLoader.loadObject("item-doesnt-exist", configPath);
    }

}
