/**
 * NPC class is a specific Entity that can interact with the player
 * through conversation
 */
public class NPC extends Entity {
    private boolean hasGivenWeapon = false;
    /** Constructor */
    public NPC(int startX, int startY, char symbol) {
        super(startX, startY, symbol);
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public void interact(Player player,String mapName) {
        if (mapName.equals("map1")) {
            if (!hasGivenWeapon) {
                System.out.println("NPC: Here's a weapon to help you!");
                player.setAP(player.getAP() + 10);  // Increase player's AP
                System.out.println("Your attack points (AP) is now: " + player.getAP());
                hasGivenWeapon = true;
            } else {
                System.out.println("NPC: Good luck out there!");
            }
        }else if (mapName.equals("map2")) {
            System.out.println("NPC: The exit is to the east.");
        }
    }
}
