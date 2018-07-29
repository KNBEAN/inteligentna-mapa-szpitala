package bean.pwr.imskamieskiego.path_search;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public class DijkstraSearch implements PathSearchAlgorithm {

    private MapPoint startPoint;
    private MapPoint endPoint;
    private List<MapPoint> trace;


    public DijkstraSearch(MapPoint startPoint, MapPoint endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    /**
     * Start search patch between points given in constructor. Searching is a blocking operation
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
