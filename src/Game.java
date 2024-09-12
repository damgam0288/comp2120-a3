import java.io.IOException;
import java.util.Scanner;

public class Game {

    private final Map currentMap;
    private final Entity player;

    // Game initiation
    public Game() throws IOException {

        currentMap = new Map("map2", "assets/map2.json");

        // Dummy entities: can move this to a config file later
        player = new Player(1, 2, 'P');
        currentMap.addEntity(player);

        Entity npc = new NPC(3, 3, 'N');
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

            handleMovement(input); // Handle player movement
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

    private void handleInteraction() {
        // Handle  NPCs / enemy interaction here
    }

    public static void main(String[] args) throws IOException {
        new Game().start();
    }
}
