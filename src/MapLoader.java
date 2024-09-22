/**
 * This class will load the game world, NPCs, enemies etc. from files and create a Map object
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MapLoader {

    public static void loadMapWorldFromFile(String mapFilePath, Map map) throws IOException {

        // Read JSON file
        List<String> lines = Files.readAllLines(Paths.get(mapFilePath));
        lines = lines.stream()
                .map(line -> line.replaceAll("[\\[\\],\"]", "").trim())
                .filter(line -> !line.trim().isEmpty())
                .toList();

        // Set map parameters
        map.setHeight(lines.size());
        map.setWidth(lines.get(0).length());
        map.setWorld(new char[map.getWidth()][map.getHeight()]);
        map.setGrid(new char[map.getWidth()][map.getHeight()]);

        // Put world data into the map
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                map.setWorldTile(j,i,lines.get(i).charAt(j));
                map.setGridTile(j,i,lines.get(i).charAt(j));     // Init play grid with world
            }
        }
    }



}
