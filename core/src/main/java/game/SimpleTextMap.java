package game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class SimpleTextMap extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont font;
    private InputAdapter inputAdapter;

    // Define player position
    private int playerX;
    private int playerY;
    private boolean movementOn = true;

    // Define world size
    private int worldWidth;
    private int worldHeight;
    private String[][] world;

    // Text messages from NPC
    private String message = "Hello I'm an NPC!";
    private boolean showMessage = false;

    // Inventory
    private ArrayList<String> inventory;
    private String equippedItem;
    private boolean showInventory = false;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Default font
        inventory = new ArrayList<>();
        inventory.add("Sword"); inventory.add("Health potion"); // Dummy items for testing only
        inputAdapter = new InputAdapter();
        Gdx.input.setInputProcessor(inputAdapter);

        // TODO allow ability to change maps here?
        loadWorldFromJson("map-02.json");
    }

    private void loadWorldFromJson(String filePath) {
        Json json = new Json();
        FileHandle file = Gdx.files.internal(filePath);
        GameWorld gameWorld = json.fromJson(GameWorld.class, file);

        playerX = gameWorld.player.x;
        playerY = gameWorld.player.y;
        worldWidth = gameWorld.width;
        worldHeight = gameWorld.height;
        world = gameWorld.world;
    }

    @Override
    public void render() {
        if (movementOn) handleMovement();
        handleConversation();
        handleInventory();
        handleItemPickup();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Basic instructions to screen
        batch.begin();
            font.draw(batch,"Up/down/left/right movement",(worldWidth+2)*20, 20);
            font.draw(batch,"Enter to interact",(worldWidth+2)*20, 40);
            font.draw(batch,"I for inventory",(worldWidth+2)*20, 60);
        batch.end();

        // Draw the "game world"
        batch.begin();

        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                if (x == playerX && y == playerY) {
                    font.draw(batch, "P", x * 20, (worldHeight - y) * 20); // Player character
                } else {
                    font.draw(batch, world[y][x], x * 20, (worldHeight - y) * 20); // World from JSON
                }
            }
        }

        // Show messages (npc conversations, notices to player etc.)
        if (showMessage) {
            font.draw(batch, message, 20, 20 * (worldHeight + 2));

            // Hide message on movement
            for (int i = 19; i <= 22; i++) {
                if (Gdx.input.isKeyPressed(i)) showMessage=false;
            }

        }

        // Draw inventory
        if (showInventory) {
            movementOn = false;

            int y = 20*(worldHeight+6);
            font.draw(batch, "Inventory:", 20, y);
            for (int i = 0; i < inventory.size(); i++) {
                font.draw(batch, (i + 1) + ". " + inventory.get(i), 20, y -= ((i+1)*10));
            }
        } else
            movementOn = true;

        batch.end();
    }

    // Move the player with arrow keys, but prevent movement into walls ("#")
    private void handleMovement() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (playerX > 0 && !world[playerY][playerX - 1].equals("#")) playerX--;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (playerX < worldWidth - 1 && !world[playerY][playerX + 1].equals("#")) playerX++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (playerY < worldHeight - 1 && !world[playerY + 1][playerX].equals("#")) playerY++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (playerY > 0 && !world[playerY - 1][playerX].equals("#")) playerY--;
        }
    }

    private void handleConversation() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if ((world[playerY][playerX].equals("N"))) {
                message = "Hello! I'm an NPC!";
                showMessage = true;
            }
        }
    }

    private void handleInventory() {
        // Toggle inventory display with 'I'
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            showInventory = !showInventory;
        }

        if (showInventory) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                // To select item
                int itemNumber = getNumberKeyPressed();
                if (itemNumber != -1) {
                    equippedItem = getInventoryItem(itemNumber);
                    message = "You have equipped: " + equippedItem;
                    showMessage = true;
                }

                // To drop item
                if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                    System.out.println("delete");
                    if (inventory.contains(equippedItem)) {
                        message = "You have dropped: " + equippedItem;
                        showMessage = true;
                        inventory.remove(equippedItem);
                        equippedItem = "";
                    }
                }
            }
        }
    }

    private void addItemToInventory(String item) {
        if (!inventory.contains(item)) {
            inventory.add(item);
            message = "You have picked up a " + item + "!";
            showMessage = true;
        }
    }

    // Automatically pick up items on walking over them
    private void handleItemPickup() {
        if (world[playerY][playerX].equals("i")) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                addItemToInventory("Crossbow");

                world[playerY][playerX] = ".";

                batch.begin();
                font.draw(batch, ".", playerY, playerX);
                batch.end();
            }
        }
    }

    private int getNumberKeyPressed() {
        for (int i = 8; i <= 16; i++) {
            if (Gdx.input.isKeyJustPressed(i)) return i-7;
        }
        return -1;
    }

    private String getInventoryItem(int itemNumber) {
        if (itemNumber-1 < inventory.size() && itemNumber-1 >= 0)
            return inventory.get(itemNumber-1);
        return "nothing";
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    // GameWorld class to map JSON structure
    public static class GameWorld {
        public int width;
        public int height;
        public Player player;
        public String[][] world;
    }

    public static class Player {
        public int x;
        public int y;
    }
}
