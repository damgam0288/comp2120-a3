/**
 * This class will load the game world, NPCs, enemies etc. from files and create a Map object
 */
import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NPCFileLoader {

    public static List<NPC> makeNPCsFromFile(String npcFilePath) throws IOException {
        List<NPC> npcs = new ArrayList<>();

        try {
            // Read JSON file
            String content = new String(Files.readAllBytes(Paths.get(npcFilePath)));
            JSONObject jsonObject = new JSONObject(content);

            // NPC data
            JSONArray npcBaseData = jsonObject.getJSONArray("npcs");

            for (int i = 0; i < npcBaseData.length(); i++) {
                JSONObject npcRef = npcBaseData.getJSONObject(i);

                npcs.add(new NPC(npcRef.getInt("startx"),
                            npcRef.getInt("starty"),
                            npcRef.getString("char").charAt(0)));
            }

            return npcs;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
