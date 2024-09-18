import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * NPC class is a specific Entity that can interact with the player
 * through conversation
 */
public class NPC extends Entity {
    private String name = null;
    private Item item = null;
    private String clue = null;

    /**
     * Constructor for the NPC class.
     *
     * @param startX the starting x-coordinate of the NPC.
     * @param startY the starting y-coordinate of the NPC.
     * @param symbol the character symbol representing the NPC on the map.
     */
    public NPC(int startX, int startY, char symbol) {
        super(startX, startY, symbol);
    }

    public NPC(int startX, int startY, char symbol, String name) {
        super(startX, startY, symbol);
        this.name = name;
    }

    /**
     * Getters and Setters
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void giveItem(Player player) {
        player.receiveItem(item);
        item=null;
    }

    public boolean hasItem() {
        return (Objects.nonNull(item));
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    /**
     * Handles the interaction between the NPC and the player based on the current map.
     *
     * If the player is on "map1" and has not yet received a weapon, the NPC gives the player a weapon
     * that increases their attack points (AP). If the player has already received the weapon,
     * the NPC gives a generic encouragement message. On "map2", the NPC provides a hint about the
     * location of the exit.
     *
     * @param player The player interacting with the NPC.
     * @param mapName The name of the current map where the interaction occurs.
     */
    public void interact(Player player, String mapName) {
        if (mapName.equals("map1")) {       // TODO: Incorporate this into the Game Config JSON file
            if (hasItem()) {
                System.out.println("NPC: Here's something to help!" + " ITEM RECEIVED: " + item.getName());

                // TODO: Change this to simply add the item to inventory, when inventory is implemented
                if (item.getClass().equals(Weapon.class)) {
                    player.setAP(player.getAP() + 10);
                    System.out.println("Your attack points (AP) is now: " + player.getAP());
                }

                this.giveItem(player);
            } else {
                System.out.println("NPC: Good luck out there!");
            }
        } else if (mapName.equals("map2")) {
            System.out.println("NPC: The exit is to the south.");
        }
    }
}

class GeneralLoader {

    public static JSONObject loadJSONObjectFromFile(String targetString, String arrayKeyword, String keyString, String filepath) throws IOException {

        // Read JSON file
        String content = new String(Files.readAllBytes(Paths.get(filepath)));

        // Search through all data in the JSON file
        JSONArray array = new JSONObject(content).getJSONArray(arrayKeyword);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);

            // Found target object
            if (Objects.nonNull(object)) {
                if (object.getString(keyString).equalsIgnoreCase(targetString)) {
                    return object;
                }
            }
        }
        return null;
    }
}

class NPCFileLoader {

    /**
     * Given a target NPC's name, and the file holding the NPC data, this method will return
     * a newly created NPC object with start x,y, the char value and the NPC's name
     */
    public static NPC loadNPCFromFile(String target, String npcFilePath) throws IOException {

        // Load entire JSON object for the NPC
        JSONObject npcRef =
                GeneralLoader.loadJSONObjectFromFile
                        (target, "npcs",
                                "name", npcFilePath);

        // Put data from JSON object into NPC
        if (Objects.nonNull(npcRef)) {
            NPC npc = new NPC(npcRef.getInt("startx"),
                    npcRef.getInt("starty"),
                    npcRef.getString("char").charAt(0),
                    npcRef.getString("name"));

            // Add other data to NPC
            String itemRef = npcRef.getString("item");
            if (Objects.nonNull(itemRef)) {
                Item item = ItemLoader.loadItemFromFile(itemRef, "assets/items.json");
                npc.setItem(item);
            }

//            String clueRef = npcRef.getString("clue");
//            if (Objects.nonNull(clueRef)) {
//                // TODO Add to NPC here
//            }
            return npc;
        }
        return null;
    }
}

