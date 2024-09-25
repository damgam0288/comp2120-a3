/**
 * This class will load the game world, NPCs, enemies etc. from files and create a Map object
 */

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MapLoader {

    public static void loadMapWorldFromFile(String mapFilePath, Map map,
                                            int minWidth, int minHeight,
                                            int maxWidth, int maxHeight) throws Exception {

        // Read JSON file
        List<String> lines = Files.readAllLines(Paths.get(mapFilePath));
        lines = lines.stream()
                .map(line -> line.replaceAll("[\\[\\],\"]", "").trim())
                .filter(line -> !line.trim().isEmpty())
                .toList();

        // Check map size
        int mapMaxWidth = lines.stream().mapToInt(String::length).max().orElse(-1);
        int mapMinWidth = lines.stream().mapToInt(String::length).min().orElse(-1);

        System.out.println(mapMaxWidth);
        System.out.println(mapMinWidth);

        if (mapMaxWidth > maxWidth || lines.size() > maxHeight)
            throw new SizeLimitExceededException("Map too big");

        if (mapMinWidth < minWidth || lines.size() < minHeight)
            throw new SizeLimitExceededException("Map too small");

        // Set map parameters
        map.setHeight(lines.size());
        map.setWidth(mapMaxWidth);      // Width is the length of the longest string
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
