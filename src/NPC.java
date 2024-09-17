import java.util.Objects;

/**
 * NPC class is a specific Entity that can interact with the player
 * through conversation
 */
public class NPC extends Entity {
    private boolean hasGivenItem = false;
    private Item item;

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

    /**
     * Getters and Setters
     */
    public void setItem(Item item) {
        this.item = item;
    }

    public Item giveItem() {
        Item output = this.item;
        item = null;
        return output;
    }

    public boolean hasItem() {
        return (Objects.nonNull(item));
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
        if (mapName.equals("map1")) {
            // NPC gives the player a weapon if not already given
            if (!hasGivenItem) {
                System.out.println("NPC: Here's something to help you!");
                player.setAP(player.getAP() + 10);  // Increase player's AP  TODO: Change this to add an item to inventory when inventory is implemented
                System.out.println("Your attack points (AP) is now: " + player.getAP()); // TODO: Change this to either increase AP or improve HP based on the item given
                hasGivenItem = true;
            } else {
                System.out.println("NPC: Good luck out there!");
            }
        } else if (mapName.equals("map2")) {
            System.out.println("NPC: The exit is to the south.");
        }
    }

    public void interact2(Player player) {
        if (hasItem()) {
            System.out.println("NPC: Here's something to help you!");
            player.setItem(this.giveItem());
        } else {
            System.out.println("NPC: Good luck out there!");
        }
    }

    public static void main(String[] args) {
        Player player = new Player(1,1,'P',10,100);
        NPC npc = new NPC(1,1,'N');

        npc.setItem(new Weapon("weapon1",10));

        System.out.println(player.getItem());
        npc.interact2(player);
        System.out.println(player.getItem());
        npc.interact2(player);
    }
}
