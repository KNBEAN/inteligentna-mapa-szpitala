package bean.pwr.imskamieskiego.path_search;

import android.support.annotation.NonNull;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public interface PathSearchAlgorithm {

    void startSearch();

    @NonNull List<MapPoint> getPatch();

}
