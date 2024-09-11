import java.io.IOException;
import java.util.Scanner;

public class Game {


    private final Map currentMap;
    private final Entity player;

    public Game() throws IOException {
        // Load map from assets, load Entity, NPCs, Enemies, inventory etc.
        char[][] grid = new char[5][5];
        grid[0][0] = '#';
        grid[1][0] = '#';
        grid[2][0] = '#';
        grid[3][0] = '#';
        grid[4][0] = '#';
        for (int i = 0; i <= 4; i++) {
            for (int j = 1; j <= 3; j++) {
                grid[i][j] = '.';
            }
        }
        grid[0][4] = '#';
        grid[1][4] = '#';
        grid[2][4] = '#';
        grid[3][4] = '#';
        grid[4][4] = '#';
        currentMap = new Map("map-default", grid);
        player = new Entity(1, 2, 'P');
        currentMap.addEntity(player);

        Entity npc = new Entity(3, 3, 'N');
        currentMap.addEntity(npc);
    }

    // Main game "loop" - handle user inputs through Scanner
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            currentMap.draw();
            System.out.println("W for Up, S for Down, A for Left, D for Right: ");
            handleMovement(scanner.nextLine());
        }
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
