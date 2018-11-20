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
     * Gets live data with found route. Live data will be update, when new route will be ready.
     * @return live data with list of MapPoints
     */
    public LiveData<List<MapPoint>> getSearchedRoute(){
        return searchedRoute;
    }

    /**
     * Starts search of new route between given points.
     * @param startPoint start point
     * @param targets list of possible targets
     */
    public void startPathSearch(MapPoint startPoint, List<MapPoint> targets){
        DijkstraSearch dijkstraAlgorithm = new DijkstraSearch(graphRepository, startPoint, targets);
        pathSearcher.startSearch(dijkstraAlgorithm);
    }

    /**
     * Clear last search result
     */
    public void clearRoute(){
        searchedRoute.setValue(null);
    }
}
