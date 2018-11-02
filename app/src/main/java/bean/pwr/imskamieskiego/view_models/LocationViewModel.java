package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.path_search.DijkstraSearch;
import bean.pwr.imskamieskiego.path_search.PathSearcher;
import bean.pwr.imskamieskiego.repository.IMapGraphRepository;
import bean.pwr.imskamieskiego.repository.MapGraphRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;


public class LocationViewModel extends AndroidViewModel {

    private MapRepository mapRepository;
    private IMapGraphRepository graphRepository;

    private MutableLiveData<MapPoint> targetMapPointTrigger;
    private MutableLiveData<Location> targetLocationTrigger;
    private MediatorLiveData<Location> targetLocation;
    private MediatorLiveData<List<MapPoint>> targetMapPoint;

    private MutableLiveData<MapPoint> startMapPointTrigger;
    private MutableLiveData<Location> startLocationTrigger;
    private MediatorLiveData<Location> startLocation;
    private MediatorLiveData<MapPoint> startMapPoint;

    private PathSearcher pathSearcher;


    public LocationViewModel(@NonNull Application application) {
        super(application);
        LocalDB dataBase = LocalDB.getDatabase(application.getApplicationContext());
        mapRepository = new MapRepository(dataBase);
        graphRepository = new MapGraphRepository(dataBase);


        pathSearcher = new PathSearcher(application.getApplicationContext());

        startLocationTrigger = new MutableLiveData<>();
        startMapPointTrigger = new MutableLiveData<>();
        startLocation = new MediatorLiveData<>();
        startMapPoint = new MediatorLiveData<>();


        targetLocationTrigger = new MutableLiveData<>();
        targetMapPointTrigger = new MutableLiveData<>();
        targetLocation = new MediatorLiveData<>();
        targetMapPoint = new MediatorLiveData<>();


        //Location comes from search
        targetLocation.addSource(targetLocationTrigger, location -> targetLocation.setValue(location));
        LiveData<List<MapPoint>> tmpTargetPoints = Transformations.switchMap(targetLocationTrigger, location ->
                location != null ? mapRepository.getPointsByLocationID(location.getId()) : null
        );
        targetMapPoint.addSource(tmpTargetPoints, mapPoints -> targetMapPoint.setValue(mapPoints));

        //MapPoint comes from touch the map
        LiveData<MapPoint> nearestTargetPoint = Transformations.switchMap(targetMapPointTrigger,
                (mapPoint) -> mapRepository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor())
        );
        targetMapPoint.addSource(nearestTargetPoint, mapPoint -> targetMapPoint.setValue(Arrays.asList(mapPoint)));
        LiveData<Location> tmpTargetLocation = Transformations.switchMap(nearestTargetPoint, mapPoint ->
                mapPoint != null ? mapRepository.getLocationByID(mapPoint.getLocationID()) : null
        );
        targetLocation.addSource(tmpTargetLocation, location -> targetLocation.setValue(location));




        //Location comes from search
        startLocation.addSource(startLocationTrigger, location -> startLocation.setValue(location));
        LiveData<List<MapPoint>> tmpStartPoints = Transformations.switchMap(targetLocationTrigger, location ->
                location != null ? mapRepository.getPointsByLocationID(location.getId()) : null
        );
        startMapPoint.addSource(tmpStartPoints, mapPoint -> {
            if (mapPoint != null && !mapPoint.isEmpty())
                startMapPoint.setValue(mapPoint.get(0));
        });

        //MapPoint comes from touch the map
        LiveData<MapPoint> nearestStartPoint = Transformations.switchMap(startMapPointTrigger,
                (mapPoint) -> mapRepository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor())
        );
        startMapPoint.addSource(nearestStartPoint, mapPoint -> startMapPoint.setValue(mapPoint));
        LiveData<Location> tmpStartLocation = Transformations.switchMap(nearestStartPoint, mapPoint ->
                mapPoint != null ? mapRepository.getLocationByID(mapPoint.getLocationID()) : null
        );
        startLocation.addSource(tmpStartLocation, location -> startLocation.setValue(location));
    }


    public LiveData<List<MapPoint>> getTargetPoint() {
        return targetMapPoint;
    }

    public LiveData<Location> getTargetLocation() {
        return targetLocation;
    }

    public LiveData<List<MapPoint>> getTrace(){
        return pathSearcher.getPath();
    }


    public void setStartPoint(MapPoint startMapPoint){
        startMapPointTrigger.setValue(startMapPoint);
    }

    public void setTargetPoint(MapPoint destinationMapPoint){
        targetMapPointTrigger.setValue(destinationMapPoint);
    }

    public void setStartLocation(Location startLocation){
        startLocationTrigger.setValue(startLocation);
    }

    public void setTargetLocation(Location targetLocation){
        targetLocationTrigger.setValue(targetLocation);
    }

    public void searchPatch(){
        List<MapPoint> targets = targetMapPoint.getValue();
        MapPoint startPoint = startMapPoint.getValue();
        DijkstraSearch dijkstraAlgorithm = new DijkstraSearch(graphRepository, startPoint, targets);
        pathSearcher.startSearch(dijkstraAlgorithm);
    }
}
