import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Reads JSON files to create objects like NPCs, items etc. This is an abstract class
 * only that holds common functionality that goes into loading aforementioned objects
 */
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

/**
 * A custom version of the Abstract Loader to find an NPC in a JSON file
 */
class NPCLoader extends AbstractLoader {

    /**
     * Returns a newly created NPC object given the target-string and Json file.
     * Return JSONExceptions if the any keys are missing/malformed.
     * Return NoSuchFieldException if the specified NPC cannot be found.
     */
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
        Item item = ItemLoader.loadObject(itemRef);
        npc.setItem(item);

        return npc;

    }
}

/**
 * A custom version of the Abstract Loader to find an Item in a JSON file
 */
class ItemLoader extends AbstractLoader {

    /**
     * Returns a newly created Item object given the target-string and Json file.
     * Will return JSONExceptions if the KEY-string cannot be found,
     * or a NoSuchFieldException if the specified item cannot be found.
     */
    public static Item loadObject(String target) throws Exception {
        JSONObject itemRef = findObject(target, "items", "name", "assets/items.json");
        ItemType itemType = ItemType.stringToItemType(itemRef.getString("type"));

        if (itemType==null)
            throw new IllegalArgumentException("Cannot recognise type");

        return switch (itemType) {
            case WEAPON
                    -> new Weapon(itemRef.getString("name"),itemRef.getInt("value"));
            case SHIELD
                    -> new Shield(itemRef.getString("name"),itemRef.getInt("value"));
            case HEALTHPOTION
                    -> new HealthPotion(itemRef.getString("name"),itemRef.getInt("value"));
        };
    }
}

class ScreenLoader {

    public static List<String> jsonContentToStringList(String filepath) throws IOException {

        // Read JSON file
        List<String> lines = Files.readAllLines(Paths.get(filepath));
        lines = lines.stream()
                .map(line -> line.replaceAll("[\\[\\],\"]", "").trim())
                .filter(line -> !line.trim().isEmpty())
                .toList();

        return lines;
    }

}


