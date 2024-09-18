import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Game {

    private Map currentMap;
    // TODO Change this to use the MapController.java
    private List<Map> maps;
    private final Player player;
    private NPC npc;
    private Enemy enemy;

    // Game initiation
    public Game() throws Exception {

        player = new Player(1, 2, 'P', 10, 100);
        maps = new ArrayList<>();

        // Load configuration file
        String content =
                new String(Files.readAllBytes(Paths.get("assets/game-config.json")));
        JSONObject jsonObject =
                new JSONObject(content);

        // Get all Level data into an array
        JSONArray mapRefs = jsonObject.getJSONArray("levels");

        // For each level, load the map first
        for (int i = 0; i < mapRefs.length(); i++) {
            JSONObject mapRef = mapRefs.getJSONObject(i);

            Map map = new Map(mapRef.getString("name"),
                                mapRef.getString("filepath"));
            maps.add(map);

            // Then load NPCs
            JSONArray npcRefs = mapRef.getJSONArray("npcs");

            for (int j = 0; j < npcRefs.length(); j++) {
                JSONObject npcRef = npcRefs.getJSONObject(j);

                NPC npc = NPCFileLoader.loadNPCFromFile(
                        npcRef.getString("name"),
                        npcRef.getString("filepath"));

                // Add NPCs to the map
                map.addEntity(npc);
            }
        }

        // Set current map
        currentMap = this.maps.get(0);       // TODO Replace with MapController later

        // Enemies
        enemy = new Enemy(4, 4, 'E', 5, 20);
        currentMap.addEntity(enemy);

        // Finally add player
        currentMap.setPlayer(player);
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
            printCurrentMap();
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

    /**
     * Handles interactions with enemies on the current map.
     * If the player encounters an enemy, they are given the option to fight.
     * After all enemies on the map are defeated, the player can proceed to the next map.
     * Also checks if the player has won the game.
     */
    private void handleEnemicInteraction() {
        Entity collidingEntity = currentMap.getCollidingEntity();
        // If the colliding entity is an enemy, offer the player a chance to fight
        if (collidingEntity instanceof Enemy enemy) {
            System.out.println("You've encountered an enemy!");
            System.out.println("Press 'F' to fight, or move away.");
            Scanner scanner = new Scanner(System.in);
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("f")) {
                fight(enemy);
            }
        }
        // Check if the player can move to the next map TODO Adapt to handle several maps
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
        Scanner scanner = new Scanner(System.in);
        while (player.getHP() > 0 && enemy.getHP() > 0) {
            // Player attacks the enemy
            player.attack(enemy);
            // Check if the enemy is defeated
            if (enemy.getHP() <= 0) {
                System.out.println("You defeated the enemy!");
                currentMap.removeEntity(enemy);
                return;
            }
            // Enemy attacks the player
            enemy.attack(player);
            // Check if the player is defeated
            if (player.getHP() <= 0) {
                System.out.println("You were defeated...");
                return;
            }
            // Prompt the player to continue fighting or move away
            System.out.println("Press 'F' to fight, or move away.");
            String action = scanner.nextLine();
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

    public static void main(String[] args) throws Exception {
        new Game().start();

    }
}
