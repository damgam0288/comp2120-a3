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
                player.getInventory().addItem(item);
            } else {
                System.out.println("NPC: Good luck out there!");
            }
        } else if (mapName.equals("map2")) {
            System.out.println("NPC: The exit is to the south.");
        }
    }
}

