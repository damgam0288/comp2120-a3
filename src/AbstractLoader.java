import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

abstract class AbstractLoader {

    public static JSONObject findObject
            (String targetString, String arrayKeyword, String keyString, String filepath)
            throws Exception {

        // Read JSON file
        String content = new String(Files.readAllBytes(Paths.get(filepath)));

        // Search through all data in the JSON file
        JSONArray array = new JSONObject(content).getJSONArray(arrayKeyword);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);

            // Find target object
            String keyvalue = object.getString(keyString);
            if (keyvalue.equalsIgnoreCase(targetString)) {
                return object;
            }
        }

        // Missing object in a JSON configuration file means game shouldn't run
        throw new NoSuchFieldException("Could not find " + targetString +
                " in the given reference: " +
                filepath + "/" + arrayKeyword + "/" + keyString);
    }
}

class NPCLoader extends AbstractLoader {

    public static NPC loadObject(String target, String filepath) throws Exception {

        JSONObject jsonObject = findObject(target, "npcs",
                                "name", filepath);

        // Put data from JSON object into NPC
        NPC npc = new NPC(jsonObject.getInt("startx"),
                jsonObject.getInt("starty"),
                jsonObject.getString("char").charAt(0),
                jsonObject.getString("name"));

        // Add item data to NPC
        String itemRef = jsonObject.getString("item");
        Item item = ItemLoader.loadObject(itemRef, "assets/items.json");
        npc.setItem(item);

        return npc;

    }

    public static void main(String[] args) throws Exception {
        NPCLoader.loadObject("npc4","tests/resources/npcfile-right-values.json");

    }
}

class ItemLoader extends AbstractLoader {
    public static Item loadObject(String target, String filepath) throws Exception {
        JSONObject itemRef = findObject(target, "items", "name", filepath);

        // Weapons
        if (itemRef.getString("type").equalsIgnoreCase("weapon")) {
            return new Weapon(itemRef.getString("name"),
                    itemRef.getInt("ap"));
        }

        // Shields
        if (itemRef.getString("type").equalsIgnoreCase("shield")) {
            // TODO
        }

        // Missing object in a JSON file means game shouldn't run
        throw new NoSuchFieldException("Could not find " + target +
                " in the given reference: " + filepath);
    }
}
