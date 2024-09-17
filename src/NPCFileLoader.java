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

        // Read JSON file
        try {
            String content = new String(Files.readAllBytes(Paths.get(npcFilePath)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray npcArray = jsonObject.getJSONArray("npcs");

            // Put data into NPCs
            for (int i = 0; i < npcArray.length(); i++) {
                JSONObject npcRef = npcArray.getJSONObject(i);

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

    public static void main(String[] args) throws IOException {
        List<NPC> npcs = NPCFileLoader.makeNPCsFromFile("assets/npcs.json");

        for(NPC n : npcs) {
            System.out.println(n.getX() + ", " + n.getY());
        }
    }
}
