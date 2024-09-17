/**
 * This class will use a JSON reader to load the game world, NPCs, enemies etc. from files
 */

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MapLoader {
    public static void main(String[] args) {
        try {
            // Read the JSON file into a String
            String content = new String(Files.readAllBytes(Paths.get("assets/game-config.json")));

            // Parse the JSON String into a JSONObject
            JSONObject jsonObject = new JSONObject(content);

            // Accessing map files via keys
            String map1 = jsonObject.getString("map1");
            System.out.println("Map 1 path: " + map1);

            String map2 = jsonObject.getString("blech");
            System.out.println("Map 2 path: " + map2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
