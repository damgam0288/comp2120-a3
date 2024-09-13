import java.io.IOException;
import java.util.Scanner;

public class Game {

    private Map currentMap;
    private final Player player;
    private NPC npc;
    private Enemy enemy;

    // Game initiation
    public Game() throws Exception {

        player = new Player(1, 2, 'P',10,100);

        currentMap = new Map("map1", "assets/map1.json", player);

        // Dummy entities: can move this to a config file later
        npc = new NPC(3, 3, 'N');
        enemy = new Enemy(4, 4, 'E', 5, 20);
        currentMap.addEntity(enemy);
        currentMap.addEntity(npc);
    }

    // Main game "loop" - handle user inputs through Scanner
    public void start() {
        currentMap.draw();

        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println("Enter move (W for Up, S for Down, A for Left, D for Right, Q to quit): ");
            input = scanner.nextLine();

            handleMovement(input);      // Handle player movement
            handleNPCInteraction();     // Handle interaction with NPCs
            handleEnemicInteraction();

            currentMap.draw();
        } while (!input.equalsIgnoreCase("q"));

        scanner.close();
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

    private void handleEnemicInteraction() {
        Entity collidingEntity = currentMap.getCollidingEntity();

        if (collidingEntity instanceof Enemy enemy) {
            System.out.println("You've encountered an enemy!");
            System.out.println("Press 'F' to fight, or move away.");
            Scanner scanner = new Scanner(System.in);
            String action = scanner.nextLine();
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

    private void fight(Enemy enemy) {
        Scanner scanner = new Scanner(System.in);
        while (player.getHP() > 0 && enemy.getHP() > 0) {
            // Player attacks the enemy
            player.attack(enemy);

            // Check if the enemy is defeated
            if (enemy.getHP() <= 0) {
                System.out.println("You defeated the enemy!");
                currentMap.removeEntity(enemy);
                return; // Exit the fight
            }

            // Enemy attacks the player
            enemy.attack(player);

            // Check if the player is defeated
            if (player.getHP() <= 0) {
                System.out.println("You were defeated...");
                return; // Exit the fight
            }

            // Prompt the player to continue fighting or move away
            System.out.println("Press 'F' to fight, or move away.");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("f")) {
                continue; // Continue fighting
            } else {
                System.out.println("You chose to move away.");
                handleMovement(action); // Call handleMovement to process the move
                return; // Exit the fight
            }
        }
    }

    private void handleNPCInteraction() {
        // Handle  NPCs / enemy interaction here
            Entity collidingEntity = currentMap.getCollidingEntity();
            if (collidingEntity instanceof NPC npc) {
                npc.interact(player, currentMap.getMapNumber());
            }

        // Testing method only - TODO remove later
        System.out.println("TEST: Who's player colliding: " + currentMap.getCollidingEntity());

    }

    public static void main(String[] args) throws Exception {
        new Game().start();
    }
}
