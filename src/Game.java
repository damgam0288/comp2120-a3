import java.util.*;

import exceptions.TooManyEntitiesException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Game {

    private static final int MAX_ENEMIES_PER_LEVEL = 3;
    private static final int MAX_NPCs_PER_LEVEL = 3;

    private Map currentMap;
    private List<Map> maps;
    private final Player player;

    private Map pausedState;        // TODO 2
    private GameState currentState = GameState.RUNNING;
    private Scanner scanner;
    private List<Enemy> enemies;
    private int initialPlayerLevel = 1;

    // Game initiation
    public Game(String pathToConfig) throws Exception {

        // Load game config
        if (Objects.isNull(pathToConfig))
            throw new IllegalArgumentException("Game.java pathToConfig cannot be null");

        if (pathToConfig.isEmpty())
            throw new IllegalArgumentException("Game.java pathToConfig is empty");

        String gameConfigContent = new String(Files.readAllBytes(Paths.get(pathToConfig)));
        JSONObject gameConfigJson = new JSONObject(gameConfigContent);

        if (gameConfigJson.isEmpty())
            throw new Exception("Game config file cannot be empty");

        // Player
        JSONObject playerJson = gameConfigJson.getJSONObject("player");
        player = new Player(playerJson.getInt("startx"), playerJson.getInt("starty"),
                playerJson.getString("symbol").charAt(0),
                playerJson.getInt("ap"), playerJson.getInt("hp"));
        player.initInventory();


        // Levels, NPCs, enemies
        maps = new ArrayList<>();
        JSONArray mapRefs = gameConfigJson.getJSONArray("levels");
        enemies = new ArrayList<>();

        for (int i = 0; i < mapRefs.length(); i++) {
            JSONObject mapRef = mapRefs.getJSONObject(i);
            Map map = new Map(mapRef.getString("name"), mapRef.getString("filepath"), player);
            maps.add(map);
            loadEntities(map, mapRef);
            enemies.addAll(map.getEnemies());
        }

        // Current map
        currentMap = this.maps.get(0);       // TODO 2 Replace with MapController later
    }

    /**
     * Helper method to load all NPCs, enemies etc. into each Game Level
     *
     * @param map    the map into which we want to put associated entities
     * @param mapRef JSON object taken from the main config file containing the current level's data
     * @throws Exception if JSON refs are not found
     */
    private void loadEntities(Map map, JSONObject mapRef) throws Exception {
        // Load NPCs
        JSONArray npcRefs = mapRef.getJSONArray("npcs");

        if (npcRefs.length() > MAX_NPCs_PER_LEVEL)       // Apply max limit  per level
            throw new TooManyEntitiesException("Too many npcs loaded into map");

        for (int j = 0; j < npcRefs.length(); j++) {
            JSONObject npcData = npcRefs.getJSONObject(j);

            NPC npc = new NPC(
                    npcData.getInt("startx"),
                    npcData.getInt("starty"),
                    npcData.getString("char").charAt(0),
                    npcData.getString("name"),
                    npcData.getString("item"));

            map.addEntity(npc);
        }

        // Load Enemies
        JSONArray enemyRefs = mapRef.getJSONArray("enemies");

        if (enemyRefs.length() > MAX_ENEMIES_PER_LEVEL)       // Apply max limit per level
            throw new TooManyEntitiesException("Too many enemies loaded into map");

        for (int j = 0; j < enemyRefs.length(); j++) {
            JSONObject enemyData = enemyRefs.getJSONObject(j);
            Enemy enemy = new Enemy(
                    enemyData.getInt("startx"),
                    enemyData.getInt("starty"),
                    enemyData.getString("char").charAt(0),
                    enemyData.getInt("ap"),
                    enemyData.getInt("hp")
            );

            map.addEntity(enemy);
        }
    }

    // Game 'loop'
    public void start() {
        currentMap.draw();

        scanner = new Scanner(System.in);
        String input;

        do {
            System.out.println("ENTER W for Up, S for Down, A for Left, D for Right, I for Inventory, P to pause, Q to quit: ");
            input = scanner.nextLine();

            // TODO 2 replace with a state field?
            if (input.equalsIgnoreCase("p")) {
                handlePaused();
            }
            if (input.equalsIgnoreCase("i")) {
                openInventory();
            } else {
                handleMovement(input);
                handleNPCInteraction();
                handleEnemyInteraction();
            }

            currentMap.draw();
            printCurrentMap();
        } while (!input.equalsIgnoreCase("q"));

        scanner.close();
    }

    /**
     * Opens the player's inventory, allowing them to view and interact with their items.
     * This method displays the list of items in the player's inventory and provides
     * options to use, equip, or drop items. The player can also exit the inventory.
     * <p>
     * While the inventory is open:
     * - The player can select an item by its number to interact with it.
     * - Options for interacting include using the item, equipping it, or dropping it.
     * - If the inventory is empty, a message is displayed, and the method exits.
     * <p>
     * The method continues to run in a loop until the player chooses to exit
     * the inventory by selecting option 0 or 4.
     */
    private void openInventory() {
        boolean inventoryOpen = true;

        while (inventoryOpen) {
            // Get the player's equipped items
            List<Item> equippedItems = player.getEquippedItems();

            // Display equipped items

            if (equippedItems.isEmpty()) {
//                System.out.println("No items are currently equipped.");
            } else {
                System.out.println("Equipped Items:");
                for (int i = 0; i < equippedItems.size(); i++) {
                    String points;
                    if (equippedItems.get(i).getType() == ItemType.WEAPON) {
                        points = "AP";
                    } else {
                        points = "HP";
                    }
                    System.out.println(equippedItems.get(i).getName() + " +" + equippedItems.get(i).getValue() + points + "");
                }
            }

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
                    player.useItem(itemIndex);
                    break;
                case "2":
                    player.equipItem(itemIndex);
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


    /**
     * Handles the game's pause functionality.
     * <p>
     * This method checks if the game is currently in the RUNNING state. If so, it pauses the game by
     * switching the game state to PAUSED and displays a pause menu. While the game is paused,
     * it enters a loop waiting for user input to either resume the game or quit.
     * <p>
     * - When the user presses 'P', the game resumes by switching the game state back to RUNNING.
     * - When the user presses 'Q', the game quits by exiting the application.
     * - Any other input is considered invalid and the pause menu is re-displayed.
     * <p>
     * The method ensures that no game actions can be taken while the game is paused.
     */
    public void handlePaused() {
        if (currentState == GameState.RUNNING) {
            currentState = GameState.PAUSED;
            System.out.println("#######################");
            System.out.println("#                     #");
            System.out.println("#     GAME PAUSED     #");
            System.out.println("# Press 'P' to resume #");
            System.out.println("# Press 'Q' to quit   #");
            System.out.println("#                     #");
            System.out.println("#######################");

            // Loop to wait for input to resume or quit
            boolean paused = true;
            while (paused) {
                String input = scanner.nextLine().trim().toLowerCase();
                switch (input) {
                    case "p":
                        paused = false;
                        currentState = GameState.RUNNING;
                        System.out.println("Game resumed.");
                        break;
                    case "q":
                        System.out.println("Quitting the game...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input. Press 'P' to resume or 'Q' to quit.");
                }
            }
        }
    }

    private void handleMovement(String move) {
        switch (move.toLowerCase()) {
            case "w" -> player.move(0, -1, currentMap);
            case "s" -> player.move(0, 1, currentMap);
            case "a" -> player.move(-1, 0, currentMap);
            case "d" -> player.move(1, 0, currentMap);
        }
        if (currentMap.canMoveToNextMap()) {
            System.out.println("You've reached the exit! Moving to the next map...");
            handleNextMap();
        }
        if (currentMap.isVictory()) {
            System.out.println("Congratulations! You've won the game!");
            System.exit(0);
        }
    }

    /**
     * Handles interactions with enemies on the current map.
     * If the player encounters an enemy, they are given the option to fight.
     * After all enemies on the map are defeated, the player can proceed to the next map.
     * Also checks if the player has won the game.
     */
    private void handleEnemyInteraction() {
        Entity collidingEntity = currentMap.getCollidingEntity();
        if (collidingEntity instanceof Enemy enemy) {
            System.out.println("You've encountered an enemy!");
            System.out.println("Press 'F' to fight, or move away.");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("f")) {
                fight(enemy);
            }
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

            // Enemy dies
            if (enemy.getHP() <= 0) {
                System.out.println("You defeated the enemy!");
                currentMap.removeEntity(enemy);
                enemies.remove(enemy);
                checkPlayerLevelAndUpgradeEnemies();
                return;
            }

            enemy.attack(player);

            // Player dies
            if (player.getHP() <= 0) {
                showScreen(GameState.DEFEAT);
                System.exit(0);
            }

            System.out.println("Press 'F' to fight, or move away.");

            String action = scanner.nextLine();

            if (action.equalsIgnoreCase("f")) {
                continue; // Continue fighting  TODO Dead code?
            } else {
                System.out.println("You chose to move away.");
                handleMovement(action);
                return;
            }
        }
    }

    private void checkPlayerLevelAndUpgradeEnemies() {
        int currentPlayerLevel = player.getLevel();
        if (currentPlayerLevel > initialPlayerLevel) {
            upgradeEnemies();
            initialPlayerLevel = currentPlayerLevel;
        }
    }

    private void upgradeEnemies() {
        for (Enemy enemy : enemies) {
            enemy.levelUp();
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
        Entity collidingEntity = currentMap.getCollidingEntity();
        if (collidingEntity instanceof NPC npc) {
            npc.interact(player, currentMap.getMapNumber());
        }
    }

    public void handleNextMap() {
        try {
            int currentMapIndex = maps.indexOf(currentMap);
            if (currentMapIndex + 1 < maps.size()) {
                System.out.println("Moving to the next map...");
                currentMap = maps.get(currentMapIndex + 1);
                player.setPosition(1, 1); // player position on the new map
            } else {
                showScreen(GameState.VICTORY);
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Error loading next map: " + e.getMessage());
            System.exit(1);
        }
    }

    private void showScreen(GameState state) {
        String filepath = "";

        if (state==GameState.VICTORY) {
            filepath = "assets/victoryScreen.json";
        } else if (state==GameState.DEFEAT) {
            filepath = "assets/defeatScreen.json";
        }

        try {
            List<String> screen = ScreenLoader.jsonContentToStringList(filepath);
            for(String line : screen) {
                System.out.println(line);
            }
        }
        catch (Exception e) {
            System.out.println("Could not load screen" + e);
        }

    }

    /**
     * Main execution point for Game.java
     * @param args args[0] must be the path to main game configuration file
     * @throws Exception from Game constructor
     */
    public static void main(String[] args) throws Exception {

        if (Objects.nonNull(args) && args.length>0)
            new Game(args[0]).start();
        else
            new Game("assets/game-config.json").start();
    }

    // For pausing/unpausing and completion/defeat of the game
    enum GameState {
        RUNNING, PAUSED, DEFEAT, VICTORY
    }


}
