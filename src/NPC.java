import java.util.Objects;

/**
 * NPC class is a specific Entity that can interact with the player
 * through conversation
 */
public class NPC extends Entity {
    private Item item;
    private String clue;

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

    public NPC(int startX, int startY, char symbol, String name, Item item) {
        super(startX, startY, symbol);
    }

    public NPC(int startX, int startY, char symbol, String clue) {
        super(startX, startY, symbol);
    }

    /**
     * Getters and Setters
     */
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
                System.out.println("NPC: Here's something to help you!");

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
