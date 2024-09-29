import java.util.*;

import exceptions.TooManyEntitiesException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Game {

    private Map currentMap;
    private List<Map> maps;
    private final Player player;

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
                playerJson.getInt("ap"),playerJson.getInt("hp"), playerJson.getInt("level"));

        // Levels, NPCs, enemies
        maps = new ArrayList<>();
        JSONArray levels = gameConfigJson.getJSONArray("levels");
        enemies = new ArrayList<>();

        if (levels.isEmpty())
            throw new Exception("Levels are missing");

        for (int i = 0; i < levels.length(); i++) {
            JSONObject mapRef = levels.getJSONObject(i);
            Map map = new Map(mapRef.getString("name"), mapRef.getString("filepath"), player,
                    GlobalConstants.MAP_MIN_WIDTH, GlobalConstants.MAP_MIN_HEIGHT,
                    GlobalConstants.MAP_MAX_WIDTH, GlobalConstants.MAP_MAX_HEIGHT);
            maps.add(map);
            loadEntities(map, mapRef);
            enemies.addAll(map.getEnemies());
        }

        // Current map
        currentMap = this.maps.get(0);
    }

    /**
     * Helper method to load all NPCs, enemies etc. into each Game Level
     *
     * @author Rifang Zhou
     *
     * @param map    the map into which we want to put associated entities
     * @param mapRef JSON object taken from the main config file containing the current level's data
     * @throws Exception if JSON refs are not found
     */
    private void loadEntities(Map map, JSONObject mapRef) throws Exception {
        // Load NPCs
        JSONArray npcRefs = mapRef.getJSONArray("npcs");

        if (npcRefs.length()> GlobalConstants.GAME_MAX_NPC_PER_LEVEL)       // Apply max limit  per level
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

        if (enemyRefs.length()> GlobalConstants.GAME_MAX_ENEMIES_PER_LEVEL)       // Apply max limit per level
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
            System.out.println(player.displayStats() + ", " + player.displayEquippedItems());
            System.out.println("ENTER W for Up, S for Down, A for Left, D for Right, I for Inventory, P to pause, Q to quit: ");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("p")) {
                handlePaused();
            }
            if (input.equalsIgnoreCase("i")) {
                openInventory();
            } else {
                handleMovement(input);
                handleNPCInteraction();
                handleEnemyInteraction();

                if (currentMap.canMoveToNextMap())
                    handleNextMap();
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
     *
     * @author Noah Martin
     */
    private void openInventory() {
        boolean inventoryOpen = true;

        while (inventoryOpen) {
            // Get the player's equipped items
            List<Item> equippedItems = player.getEquippedItems();

            // Display equipped items
            if (equippedItems.isEmpty()) {
                System.out.println("No items are currently equipped.");
            } else {
                System.out.println("Equipped Items:");
                for (Item equippedItem : equippedItems) {
                    String points;
                    if (equippedItem.getType() == ItemType.WEAPON) {
                        points = "AP";
                    }
                    else {
                        points = "HP";
                    }
                    System.out.println(equippedItem.getName() + " +" + equippedItem.getValue() + points + "");
                }
            }

            // Another line
            System.out.println();

            // No more items to show
            List<Item> items = player.getInventory().getItems();
            if (items.isEmpty()) {
                System.out.println("Your inventory is empty.");
                return;
            }

            // Unequipped items
            System.out.println("Inventory:");
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                String type = (item.getType().equals(ItemType.WEAPON) ? "AP"
                        : (item.getType().equals(ItemType.SHIELD) ? "HP"
                        : ""));
                String value = String.valueOf(item.getValue());

                System.out.println((i + 1) + ") " + item.getName()
                        + " (+" + value + type + ")");

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
     *
     *
     * @author Noah Martin
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

    /** Given a string containing a single/combination move, this method
     * moves Player on the Map. If the Player collides with another Entity in the move,
     * movement is interrupted, but it is not interrupted when colliding with obstacle
     * @param move e.g. "wwwww", "wwwsssadd" */
    public void handleMovement(String move) {
        for (char c : move.toLowerCase().toCharArray()) {
            if (!isValidMove(c))
                break;

            movePlayer(c);

            if (currentMap.isCollidingWithEntity(player))
                break;
        }
    }

    /** Helper method: executes the move given a valid char for a move */
    private void movePlayer(char move) {
        switch (move) {
            case 'w' -> player.move(0, -1, currentMap);
            case 's' -> player.move(0, 1, currentMap);
            case 'a' -> player.move(-1, 0, currentMap);
            case 'd' -> player.move(1, 0, currentMap);
        }
    }

    /** Helper method: returns true if the given char is a movement char */
    private boolean isValidMove(char c) {
        return c == 'w' || c == 's' || c == 'a' || c == 'd';
    }

    /**
     * Handles interactions with enemies on the current map.
     * If the player encounters an enemy, they are given the option to fight.
     * After all enemies on the map are defeated, the player can proceed to the next map.
     * Also checks if the player has won the game.
     *
     * @author Rifang Zhou
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
     * @author Rifang Zhou
     *
     * @param enemy The enemy the player is fighting
     */
    private void fight(Enemy enemy) {
        while (player.getHP() > 0 && enemy.getHP() > 0) {
            player.attack(enemy);

            // Enemy dies
            if (enemy.getHP() <= 0) {
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

            if (!action.equalsIgnoreCase("f")) {
                System.out.println("You chose to move away.");
                handleMovement(action);
                return;
            }
        }
    }

    /**
     * Checks the player's level and upgrades all enemies if the player's level has increased.
     * This method compares the current player level with the initial level.
     * If the player has leveled up, it upgrades all enemies and updates the initial player level.
     *
     * @author Rifang Zhou
     */
    private void checkPlayerLevelAndUpgradeEnemies() {
        int currentPlayerLevel = player.getLevel();
        if (currentPlayerLevel > initialPlayerLevel) {
            upgradeEnemies();
            initialPlayerLevel = currentPlayerLevel;
        }
    }

    /**
     * Upgrades all enemies by increasing their levels.
     * This method iterates through the list of enemies and calls the levelUp method on each enemy.
     *
     * @author Rifang Zhou
     */
    private void upgradeEnemies() {
        for (Enemy enemy : enemies) {
            enemy.levelUp();
        }
    }

    /**
     * Prints the current map number to the console.
     * This is used to indicate which map the player is currently on.
     *
     * @author Rifang Zhou
     */
    private void printCurrentMap() {
        System.out.println("Current map: " + currentMap.getMapNumber());
    }

    /**
     * Handles interactions with NPCs on the current map.
     * When the player collides with an NPC, the NPC interacts with the player.
     *
     * @author Rifang Zhou
     */
    private void handleNPCInteraction() {
        Entity collidingEntity = currentMap.getCollidingEntity();
        if (collidingEntity instanceof NPC npc) {
            npc.interact(player);
        }
    }

    /**
     * Handles the transition to the next map in the game.
     * This method checks the current map index and moves the player to the next map if available.
     * If there are no more maps, it shows the victory screen and exits the game.
     * It also handles any exceptions that may occur during the map transition.
     *
     * @author Rifang Zhou
     */
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

    /** Private helper method to display whether the game has been
     * won or the player lost
     * @param state Victory or Defeat in this method
     * @author Damian Gamlath
     */
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

    /** Getter mainly for testing purposes */
    public Map getCurrentMap() {
        return currentMap;
    }

    /** Getting mainly for testing purposes */
    public Player getPlayer() {
        return player;
    }

    /**
     * Main execution point for Game.java
     * @param args args[0] must be the path to main game configuration file
     * @throws Exception from Game constructor
     */
    public static void main(String[] args) throws Exception {

        if (Objects.nonNull(args) && args.length>0) {
            new Game(args[0]).start();
        }
        else {
            GlobalConstants.setConfigFilePath("assets/game-config.json");
            new Game("assets/game-config.json").start();
        }
    }

    // For pausing/unpausing and completion/defeat of the game
    enum GameState {
        RUNNING, PAUSED, DEFEAT, VICTORY
    }


}
