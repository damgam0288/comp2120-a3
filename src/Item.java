import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Items can be held by Player or NPCs
 */

public abstract class Item {

    String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

class Weapon extends Item {

    private String name;
    private int ap;     // Attack points provided by this weapon

    public Weapon(String name, int ap) {
        this.name = name;
        this.ap = ap;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

}

class ItemLoader {
    /** Given a target item name and the file holding the data, this method will return
     * an Item object with the written parameters */
    public static Item loadItemFromFile(String target, String filepath) throws IOException {

        // Read JSON file
        String content = new String(Files.readAllBytes(Paths.get(filepath)));

        // Search through all objects in the JSON file
        JSONArray array = new JSONObject(content).getJSONArray("items");

        for (int i = 0; i < array.length(); i++) {
            JSONObject ref = array.getJSONObject(i);

            if (Objects.nonNull(ref)) {
                String name = ref.getString("name");

                // Found target
                if (name.equalsIgnoreCase(target)) {
                    if (ref.getString("type").equalsIgnoreCase("weapon")) {
                        return new Weapon(ref.getString("name"),
                                            ref.getInt("ap"));
                    }

//                    if (ref.getString("type").equalsIgnoreCase("shield")) {
                        // TODO
//                    }
                }
            }
        }

        return null;
    }
}