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
    private MediatorLiveData<List<MapPoint>> searchedTrace;
    private PathSearcher pathSearcher;

    public PathSearchViewModel(@NonNull Application application) {
        super(application);
        graphRepository = new MapGraphRepository(LocalDB.getDatabase(application));
        searchedTrace = new MediatorLiveData<>();
        pathSearcher = new PathSearcher(application);
        searchedTrace.addSource(pathSearcher.getPath(), searchedTrace::setValue);
    }

    public LiveData<List<MapPoint>> getSearchedTrace(){
        return searchedTrace;
    }

    public void startPathSearch(MapPoint startPoint, List<MapPoint> targets){
        DijkstraSearch dijkstraAlgorithm = new DijkstraSearch(graphRepository, startPoint, targets);
        pathSearcher.startSearch(dijkstraAlgorithm);
    }

    public void clearTrace(){
        searchedTrace.setValue(null);
    }
}
