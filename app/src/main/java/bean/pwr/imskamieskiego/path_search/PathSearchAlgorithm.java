package bean.pwr.imskamieskiego.path_search;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public interface PathSearchAlgorithm {

    void startSearch();

    List<MapPoint> getPatch();

}
