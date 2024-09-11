import java.io.IOException;
import java.util.Scanner;

public class Game {


    private final Map currentMap;
    private final Entity player;

    public Game() throws IOException {

        currentMap = new Map("map2", "assets/map2.json");

        player = new Entity(1, 2, 'P');
        currentMap.addEntity(player);

        Entity npc = new Entity(3, 3, 'N');
        currentMap.addEntity(npc);
    }

    // Main game "loop" - handle user inputs through Scanner
    public void start() throws IOException {
        currentMap.draw();
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("q")) {
            currentMap.draw();
            System.out.println("Enter move (W for Up, S for Down, A for Left, D for Right, Q to quit): ");
            String input = scanner.nextLine();
            handleMovement(input); // Handle player movement
        }
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
