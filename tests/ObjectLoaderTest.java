import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import static org.junit.Assert.*;

/**
 * This test class tests all loaders e.g. for NPCs, Items etc. to ensure JSON files are
 * read correctly, and any illegal/missing parameters are dealt with
 */
public class ObjectLoaderTest {

    private NPCLoader npcLoader;
    private ItemLoader itemLoader;

    // Test abstract loader can identify the right file
    // it should handle missing filepaths, bad parameters

    @Before
    public void setup() {
        npcLoader = new NPCLoader();
        itemLoader = new ItemLoader();
    }

    @Test(expected = NoSuchFileException.class)
    public void abstractLoaderNoFileException() throws Exception {
        AbstractObjectLoader.findJsonObjectInFile
                ("npc1","npcs","name",
                        "resources/file-doesnt-exist.json");
    }

    @Test(expected = JSONException.class)
    public void abstractLoaderNoMatchingArray() throws Exception {
        AbstractObjectLoader.findJsonObjectInFile
                ("npc1","array-doesnt-exist","name",
                        "resources/test-npcs.json");
    }

    @Test(expected = JSONException.class)
    public void abstractLoaderNoMatchingObject() throws Exception {
        AbstractObjectLoader.findJsonObjectInFile
                ("bad-target","npcs","name",
                        "resources/test-npcs.json");
    }

    @Test(expected = JSONException.class)
    public void abstractLoaderBadKeyString() throws Exception {
        AbstractObjectLoader.findJsonObjectInFile
                ("npc1","npcs","bad-keystring",
                        "resources/test-npcs.json");
    }

}
