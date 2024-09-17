import java.util.LinkedList;
import java.util.Objects;

/**
 * This singleton class holds all the maps that the entire game will use. It determines which map the player will move into after completing each one.
 * It adds NPCs and Enemies to the Map based on the given configuration file. But it does not any NPC/enemy etc. interactions.
 */
public class MapController {

    private LinkedList<Map> maps;
    private static MapController instance;

    private MapController() {
        maps = new LinkedList<>();
    }

    public static MapController getInstance() {
        if (Objects.isNull(instance))
            instance = new MapController();

        return instance;
    }

    public boolean nextMap() {
        if (Objects.isNull(maps))
            return false;

        if (maps.isEmpty())
            return false;

        maps.poll();

        return true;
    }

    public boolean isLastMap() {
        if (Objects.isNull(maps))
            return true;

        return (maps.isEmpty());
    }

    public int mapsRemaining() {
        if (Objects.nonNull(maps)) {
            return maps.size();
        }

        return -1;
    }

    public Map currentMap() {
        if (Objects.nonNull(maps)) {
            return maps.peek();
        }
        return null;
    }

}
