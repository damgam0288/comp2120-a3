/**
 * NPC class is a specific Entity that can interact with the player
 * through conversation
 */
public class NPC extends Entity {
    private boolean hasGivenWeapon = false;
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
    public void interact(Player player,String mapName) {
        if (mapName.equals("map1")) {
            // NPC gives the player a weapon if not already given
            if (!hasGivenWeapon) {
                System.out.println("NPC: Here's a weapon to help you!");
                player.setAP(player.getAP() + 10);  // Increase player's AP
                System.out.println("Your attack points (AP) is now: " + player.getAP());
                hasGivenWeapon = true;
            } else {
                System.out.println("NPC: Good luck out there!");
            }
        }else if (mapName.equals("map2")) {
            System.out.println("NPC: The exit is to the south.");
        }
    }
}
