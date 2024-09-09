import java.io.IOException;

public class Game {



    public Game() throws IOException {
        // Load map from assets, load Player, NPCs, Enemies, inventory etc.
    }

    public void start() {
        // Main game "loop" - handle user inputs through Scanner
    }

    private void handleMap() {
        // Display and update the map according to user inputs
    }

    private void handleMovement() {
        // Handle player movement within the current map
    }

    private void handleInteraction() {
        // Handle  NPCs / enemy interaction here
    }

    private void handleInventory() {
        // Handle inventory system
    }

    public static void main(String[] args) throws IOException {
        new Game().start();
    }
}
