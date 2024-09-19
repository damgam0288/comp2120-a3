import org.json.JSONException;
import static org.junit.Assert.*;
import org.junit.Test;

import java.nio.file.NoSuchFileException;

/**
 * This test class tests all loaders e.g. for NPCs, Items etc. to ensure JSON files are
 * read correctly, and any illegal/missing parameters are dealt with.
 * Expected reasons for test failure include typos in the filepath, a JSON file with
 * the wrong KEY parameter and/or missing VALUE parameter
 */
public class ObjectLoaderTest {

    private static String npcJsonFilePath = "tests/resources/npcfile-right-values.json";

    // Abstract Loader testing
    @Test(expected = NoSuchFileException.class, timeout=1000)
    public void abstractLoaderNoFileException() throws Exception {
        AbstractLoader.findJsonObjectInFile
                ("npc1","npcs","name",
                        "file-doesnt-exist.json");
    }

    @Test(expected = JSONException.class, timeout=1000)
    public void abstractLoaderNoMatchingArrayString() throws Exception {
        AbstractLoader.findJsonObjectInFile
                ("npc1","array-doesnt-exist","name",
                        npcJsonFilePath);
    }

    @Test(expected = JSONException.class, timeout=1000)
    public void abstractLoaderBadKeyString() throws Exception {
        AbstractLoader.findJsonObjectInFile
                ("npc1","npcs","bad-keystring",
                        npcJsonFilePath);
    }

    @Test(expected = NoSuchFieldException.class, timeout=1000)
    public void abstractLoaderNoMatchingTargetString() throws Exception {
        AbstractLoader.findJsonObjectInFile
                ("bad-target","npcs","name",
                        npcJsonFilePath);
    }

    // NPC Loader testing
    @Test(timeout=1000)
    public void npcLoaderReturnsCorrectNPC() throws Exception {
        NPC npc = NPCLoader.loadNPCFromFile("npc1",npcJsonFilePath);

        assertNotNull(npc.getName());
        assertNotNull(npc.getX());
        assertNotNull(npc.getY());
        assertNotNull(npc.getSymbol());

        assertEquals("npc1",npc.getName());
        assertEquals(4,npc.getX());
        assertEquals(1,npc.getY());
        assertEquals('1',npc.getSymbol());
    }

    @Test(timeout=1000)
    public void npcWrongKeysInJsonFile() throws Exception {
        // Bad array key
        // Bad name key
        // Bad start x/y key
        // Bad item key
    }

    @Test(timeout=1000)
    public void npcWrongValuesInJsonFile() throws Exception {
        // Corrupt name value
        // Corrupt start x/y value
        // Corrupt item value
    }
    @Test(timeout=1000)
    public void npcLoaderNoFileException() throws Exception {

    }



    // Item Loader testing


}
