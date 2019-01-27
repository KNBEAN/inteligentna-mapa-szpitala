/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.path_search.DijkstraSearch;
import bean.pwr.imskamieskiego.path_search.PathSearcher;
import bean.pwr.imskamieskiego.repository.IMapGraphRepository;
import bean.pwr.imskamieskiego.repository.MapGraphRepository;

public class PathSearchViewModel extends AndroidViewModel {

    private static final int FAST_PATH = DijkstraSearch.DEFAULT_PENALIZATION;
    private static final int OPTIMAL_PATH = 5;
    private static final int COMFORTABLE_PATH = 10;

    public enum SearchMode {
        FAST_PATH,
        OPTIMAL_PATH,
        COMFORTABLE_PATH
    }

    private IMapGraphRepository graphRepository;
    private MediatorLiveData<List<MapPoint>> searchedRoute;
    private PathSearcher pathSearcher;

    public PathSearchViewModel(@NonNull Application application) {
        super(application);
        graphRepository = new MapGraphRepository(LocalDB.getDatabase(application));
        searchedRoute = new MediatorLiveData<>();
        pathSearcher = new PathSearcher(application);
        searchedRoute.addSource(pathSearcher.getPath(), searchedRoute::setValue);
    }

    /**
     * Gets LiveData with route that was found. LiveData will be update, when new route will be ready.
     *
     * @return LiveData with list of MapPoints
     */
    public LiveData<List<MapPoint>> getSearchedRoute() {
        return searchedRoute;
    }

    /**
     * Starts search of new route between given points using default options.
     *
     * @param startPoint start point
     * @param targets    list of possible targets
     */
    public void startPathSearch(MapPoint startPoint, List<MapPoint> targets) {
        DijkstraSearch dijkstraAlgorithm = new DijkstraSearch(graphRepository, startPoint, targets);
        dijkstraAlgorithm.setPenalizationFactor(FAST_PATH);
        pathSearcher.startSearch(dijkstraAlgorithm);
    }


    /**
     * Starts search of new route between given points. The mode changes the behavior of
     * the algorithm. Searching in fast mode finds the fastest path. The optimal mode avoids
     * places that are difficult to access but does not exclude more difficult routes.
     * Comfort mode more strongly avoids hard-to-reach locations than the optimal mode.
     *
     * @param startPoint start point
     * @param targets    list of possible targets
     * @param mode       mode of algorithm behavior
     */
    public void startPathSearch(MapPoint startPoint, List<MapPoint> targets, SearchMode mode) {
        DijkstraSearch dijkstraAlgorithm = new DijkstraSearch(graphRepository, startPoint, targets);
        int penalization;
        switch (mode) {
            case FAST_PATH:
                penalization = FAST_PATH;
                break;
            case OPTIMAL_PATH:
                penalization = OPTIMAL_PATH;
                break;
            case COMFORTABLE_PATH:
                penalization = COMFORTABLE_PATH;
                break;
            default:
                penalization = FAST_PATH;
        }
        dijkstraAlgorithm.setPenalizationFactor(penalization);
        pathSearcher.startSearch(dijkstraAlgorithm);
    }

    /**
     * Clear last search result
     */
    public void clearRoute() {
        searchedRoute.setValue(null);
    }
}
