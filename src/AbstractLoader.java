import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

abstract class AbstractLoader {

    public static JSONObject findJsonObjectInFile
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
                " in the JSON reference:" +
                filepath + "/" + arrayKeyword + "/" + keyString);
    }
}

class NPCLoader extends AbstractLoader {

    public static NPC loadNPCFromFile(String target, String npcFilePath) throws Exception {

        JSONObject jsonObject = findJsonObjectInFile(target, "npcs",
                                "name", npcFilePath);

        // Put data from JSON object into NPC
        if (Objects.nonNull(jsonObject)) {
            NPC npc = new NPC(jsonObject.getInt("startx"),
                                jsonObject.getInt("starty"),
                                jsonObject.getString("char").charAt(0),
                                jsonObject.getString("name"));

            // Add item data to NPC
            String itemRef = jsonObject.getString("item");
            if (Objects.nonNull(itemRef)) {
                Item item = ItemLoader.loadItemFromFile(itemRef, "assets/items.json");
                npc.setItem(item);
            }

            return npc;
        }

        return null;
    }
}

class ItemLoader extends AbstractLoader {
    public static Item loadItemFromFile(String target, String filepath) throws Exception {
        JSONObject itemRef = findJsonObjectInFile(target, "items", "name", filepath);

        if (Objects.nonNull(itemRef)) {
            if (itemRef.getString("type").equalsIgnoreCase("weapon")) {
                return new Weapon(itemRef.getString("name"),
                        itemRef.getInt("ap"));
            }
            if (itemRef.getString("type").equalsIgnoreCase("shield")) {
                // TODO
            }
        }

        return null;
    }
}
