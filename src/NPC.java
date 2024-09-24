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
        item = null;
    }

    public boolean hasItem() {
        return (Objects.nonNull(item));
    }

    /**
     * Handles the interaction between the NPC and the player based on the current map.
     * <p>
     * If the npc has an item, it will give the item to the Player. Otherwise it will wish
     * the Player well
     *
     * @param player  The player interacting with the NPC.
     * @param mapName The name of the current map where the interaction occurs. todo no longer required field?
     */
    public void interact(Player player, String mapName) {
        if (hasItem()) {
            System.out.println("NPC: Here's something to help! " + item.getName());
            this.giveItem(player);
        }
        else {
            System.out.println("NPC: Good luck out there!");
        }
    }
}

