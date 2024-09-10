import java.io.IOException;

public class Game {



    public Game() throws IOException {
        // Load map from assets, load Entity, NPCs, Enemies, inventory etc.
    }

    public void start() {
        // Main game "loop" - handle user inputs through Scanner

        char[][] grid = new char[5][5];
        grid[0][0] = '#'; grid[1][0] = '#'; grid[2][0] = '#'; grid[3][0]='#'; grid[4][0]='#';
        for (int i = 0; i <= 4; i++) {
            for (int j = 1; j <= 3 ; j++) {
                grid[i][j]='.';
            }
        }
        grid[0][4] = '#'; grid[1][4] = '#'; grid[2][4] = '#'; grid[3][4]='#'; grid[4][4]='#';

        Map map = new Map("map-default",grid);
        Entity player = new Entity(1,2);
        map.addEntity(player);

        map.draw();

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
