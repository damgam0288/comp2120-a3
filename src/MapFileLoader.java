/**
 * This class will load the game world, NPCs, enemies etc. from files and create a Map object
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MapFileLoader {

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

    /*

    public static void main(String[] args) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("assets/game-config.json")));
            JSONObject jsonObject = new JSONObject(content);

            JSONArray maps = jsonObject.getJSONArray("maps");

            if (Objects.nonNull(maps)) {
                loadMapsFromJSONArray(maps);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Queue<Map> loadMapsFromJSONArray(JSONArray dataArray) throws Exception {
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject mapRef = dataArray.getJSONObject(i);
            allMaps.add(
                    new Map(mapRef.getString("name"),
                            mapRef.getString("filepath"),
                            new Player(1, 1, 'P', 10, 100)));
        }

        return allMaps;
    }
    */

}
