package bean.pwr.imskamieskiego.path_search;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;

public class DijkstraSearch implements PathSearchAlgorithm {

    private MapPoint startPoint;
    private MapPoint endPoint;
    private MapRepository mapRepository;
    private List<MapPoint> trace;


    public DijkstraSearch(MapRepository mapRepository, MapPoint startPoint, MapPoint endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.mapRepository = mapRepository;
    }

    /**
     * Start patch search between points given in constructor. Searching is a blocking operation
     * that can take a long time. Therefore, do not call it directly in the main thread.
     */
    @Override
    public void startSearch() {

    }

    /**
     * Return the shortest path between points given in constructor. If searching didn't end,
     * it returns null;
     * @return
     */
    @Override
    public List<MapPoint> getPatch() {
        return null;
    }
}
