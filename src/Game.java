import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Game {

    private Map currentMap;
    private final Player player;
    private Scanner scanner;
    private NPC npc;
    private Enemy enemy;
    // Game initiation
    public Game() throws Exception {
        player = new Player(1, 2, 'P', 10, 100);
        player.initInventory(); // Initialize inventory with items
        currentMap = new Map("map1", "assets/map1.json", player);
        npc = new NPC(3, 3, 'N');
        enemy = new Enemy(4, 4, 'E', 5, 20);
        currentMap.addEntity(enemy);
        currentMap.addEntity(npc);
        scanner = new Scanner(System.in); // Initialize scanner here
    }

    public void start() {
        currentMap.draw();

        String input;
        do {
            System.out.println("Enter move (W for Up, S for Down, A for Left, D for Right, I for Inventory, Q to quit): ");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("i")) {
                openInventory();
            } else {
                handleMovement(input);
                handleNPCInteraction();
                handleEnemicInteraction();
            }

            currentMap.draw();
            printCurrentMap();
        } while (!input.equalsIgnoreCase("q"));

        scanner.close(); // Close scanner at the end
    }

    /**
     * Opens the player's inventory, allowing them to view and interact with their items.
     * This method displays the list of items in the player's inventory and provides
     * options to use, equip, or drop items. The player can also exit the inventory.
     *
     * While the inventory is open:
     * - The player can select an item by its number to interact with it.
     * - Options for interacting include using the item, equipping it, or dropping it.
     * - If the inventory is empty, a message is displayed, and the method exits.
     *
     * The method continues to run in a loop until the player chooses to exit
     * the inventory by selecting option 0 or 4.
     */
    private void openInventory() {
        boolean inventoryOpen = true;

        while (inventoryOpen) {
            List<Item> items = player.getInventory().getItems();
            if (items.isEmpty()) {
                System.out.println("Your inventory is empty.");
                return;
            }

            System.out.println("Inventory:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ") " + items.get(i).getName());
            }

            System.out.println("Select an item number to interact with, or 0 to exit:");
            int itemIndex;
            try {
                itemIndex = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (itemIndex == -1) {
                inventoryOpen = false;
                continue;
            }

            if (itemIndex < 0 || itemIndex >= items.size()) {
                System.out.println("Invalid item number. Try again.");
                continue;
            }

            Item selectedItem = items.get(itemIndex);

            System.out.println("Selected Item: " + selectedItem.getName());
            System.out.println("Choose an option: 1) Use Item  2) Equip Item  3) Drop Item  4) Exit Inventory");
            String action = scanner.nextLine();

            switch (action) {
                case "1":
                    player.getInventory().useItem(player, itemIndex);
                    break;
                case "2":
                    player.getInventory().equipItem(player, itemIndex);
                    break;
                case "3":
                    player.getInventory().removeItem(selectedItem);
                    System.out.println("Dropped " + selectedItem.getName());
                    break;
                case "4":
                    inventoryOpen = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Game().start();
    }

    // Handle player movement within the current map
    private void handleMovement(String move) {
        switch (move.toLowerCase()) {
            case "w" -> player.move(0, -1, currentMap);
            case "s" -> player.move(0, 1, currentMap);
            case "a" -> player.move(-1, 0, currentMap);
            case "d" -> player.move(1, 0, currentMap);
        }
    }

    /**
     * Handles interactions with enemies on the current map.
     * If the player encounters an enemy, they are given the option to fight.
     * After all enemies on the map are defeated, the player can proceed to the next map.
     * Also checks if the player has won the game.
     */
    private void handleEnemicInteraction() {
        Entity collidingEntity = currentMap.getCollidingEntity();
        if (collidingEntity instanceof Enemy enemy) {
            System.out.println("You've encountered an enemy!");
            System.out.println("Press 'F' to fight, or move away.");
            String action = scanner.nextLine(); // Use class-level scanner
            if (action.equalsIgnoreCase("f")) {
                fight(enemy);
            }
        }
        // Check if the player can move to the next map
        if (currentMap.canMoveToNextMap()) {
            System.out.println("You've defeated all enemies on this map. Moving to the next map...");
            // Load the next map
            try {
                currentMap = new Map("map2", "assets/map2.json", player);
                player.setPosition(1, 2);
                npc = new NPC(3, 3, 'N');
                enemy = new Enemy(3, 5, 'E', 10, 30);
                currentMap.addEntity(enemy);
                currentMap.addEntity(npc);

            } catch (Exception e) {
                System.out.println("Error loading next map: " + e.getMessage());
                System.exit(1);
            }
        }
        // Check if the player has won the game
        if (currentMap.isVictory()) {
            System.out.println("Congratulations! You've won the game!");
            System.exit(0); // End the game
        }
    }

    /**
     * Handles the combat between the player and an enemy.
     * The player and enemy take turns attacking each other.
     * The player can choose to continue fighting or move away.
     * The fight ends when either the player or enemy is defeated.
     *
     * @param enemy The enemy the player is fighting
     */
    private void fight(Enemy enemy) {
        while (player.getHP() > 0 && enemy.getHP() > 0) {
            player.attack(enemy);
            if (enemy.getHP() <= 0) {
                System.out.println("You defeated the enemy!");
                currentMap.removeEntity(enemy);
                return;
            }
            enemy.attack(player);
            if (player.getHP() <= 0) {
                System.out.println("You were defeated...");
                return;
            }
            System.out.println("Press 'F' to fight, or move away.");
            String action = scanner.nextLine(); // Use class-level scanner
            if (action.equalsIgnoreCase("f")) {
                continue; // Continue fighting
            } else {
                System.out.println("You chose to move away.");
                handleMovement(action); // Call handleMovement to process the move
                return;
            }
        }
    }

    /**
     * Prints the current map number to the console.
     * This is used to indicate which map the player is currently on.
     */
    private void printCurrentMap() {
        System.out.println("Current map: " + currentMap.getMapNumber());
    }

    /**
     * Handles interactions with NPCs on the current map.
     * When the player collides with an NPC, the NPC interacts with the player.
     */
    private void handleNPCInteraction() {
        // Handle  NPCs / enemy interaction here
        Entity collidingEntity = currentMap.getCollidingEntity();
        if (collidingEntity instanceof NPC npc) {
            npc.interact(player, currentMap.getMapNumber());
        }

        // Testing method only - TODO remove later
        System.out.println("TEST: Who's player colliding: " + currentMap.getCollidingEntity());

    }

    public void handleNextMap() {
        // TODO - if (!isVictory) this method should load the next map using the game-config file
        //  Use the map number fields to track which map to move into
        //  Use a separate mapLoader class to do the work of loading the map
    }

}