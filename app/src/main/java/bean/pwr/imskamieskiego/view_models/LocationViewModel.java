package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.LocationFactory;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.path_search.DijkstraSearch;
import bean.pwr.imskamieskiego.path_search.PathSearcher;
import bean.pwr.imskamieskiego.repository.IMapGraphRepository;
import bean.pwr.imskamieskiego.repository.MapGraphRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;
import bean.pwr.imskamieskiego.utils.EventWrapper;


public class LocationViewModel extends AndroidViewModel {

    private static final String TAG = "LocationViewModel";

    private MapRepository mapRepository;
    private IMapGraphRepository graphRepository;

    private MutableLiveData<MapPoint> targetMapPointTrigger;
    private MutableLiveData<Location> targetLocationTrigger;
    private MediatorLiveData<EventWrapper<Location>> targetLocation;
    private MediatorLiveData<List<MapPoint>> targetMapPoint;

    private MutableLiveData<MapPoint> startMapPointTrigger;
    private MutableLiveData<Location> startLocationTrigger;
    private MediatorLiveData<EventWrapper<Location>> startLocation;
    private MediatorLiveData<MapPoint> startMapPoint;

    private PathSearcher pathSearcher;



    private boolean targetPointSelected = false;
    private boolean startPointSelected = false;



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
        targetLocation.addSource(targetLocationTrigger, location -> targetLocation.setValue(new EventWrapper(location)));
        LiveData<List<MapPoint>> tmpTargetPoints = Transformations.switchMap(targetLocationTrigger, location ->
                location != null ? mapRepository.getPointsByLocationID(location.getId()) : null
        );
        targetMapPoint.addSource(tmpTargetPoints, mapPoints -> {
            if (mapPoints != null){
                targetPointSelected = true;
            }
            targetMapPoint.setValue(mapPoints);
        });

        //MapPoint comes from touch the map
        LiveData<MapPoint> nearestTargetPoint = Transformations.switchMap(targetMapPointTrigger,
                (mapPoint) -> mapRepository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor())
        );

        targetMapPoint.addSource(nearestTargetPoint, mapPoint -> {
            if (mapPoint != null){
                targetPointSelected = true;
            }
            targetMapPoint.setValue(Arrays.asList(mapPoint));
        });

        LiveData<Location> tmpTargetLocation = Transformations.switchMap(nearestTargetPoint, mapPoint ->
                mapPoint != null ? mapRepository.getLocationByID(mapPoint.getLocationID()) : null
        );

        targetLocation.addSource(tmpTargetLocation, location -> {
            if (location == null)
                location = LocationFactory.create(application.getString(R.string.default_place_name), null);
            targetLocation.setValue(new EventWrapper(location));
        });




        //Location comes from search
        startLocation.addSource(startLocationTrigger, location -> startLocation.setValue(new EventWrapper(location)));
        LiveData<List<MapPoint>> tmpStartPoints = Transformations.switchMap(startLocationTrigger, location ->
                location != null ? mapRepository.getPointsByLocationID(location.getId()) : null
        );
        startMapPoint.addSource(tmpStartPoints, mapPoint -> {
            if (mapPoint != null && !mapPoint.isEmpty())
                startPointSelected = true;
                startMapPoint.setValue(mapPoint.get(0));
        });

        //MapPoint comes from touch the map
        LiveData<MapPoint> nearestStartPoint = Transformations.switchMap(startMapPointTrigger,
                (mapPoint) -> mapRepository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor())
        );

        startMapPoint.addSource(nearestStartPoint, mapPoint -> {
            if (mapPoint != null){
                startPointSelected = true;
            }
            startMapPoint.setValue(mapPoint);
        });

        LiveData<Location> tmpStartLocation = Transformations.switchMap(nearestStartPoint, mapPoint ->
                mapPoint != null ? mapRepository.getLocationByID(mapPoint.getLocationID()) : null
        );
        startLocation.addSource(tmpStartLocation, location -> {
            if (location == null)
                location = LocationFactory.create(application.getString(R.string.default_place_name), null);
            startLocation.setValue(new EventWrapper(location));
        });
    }

    public LiveData<List<MapPoint>> getTargetPoint() {
        return targetMapPoint;
    }

    public LiveData<EventWrapper<Location>> getTargetLocation() {
        return targetLocation;
    }

    public LiveData<MapPoint> getStartPoint() {
        return startMapPoint;
    }

    public LiveData<EventWrapper<Location>> getStartLocation() {
        return startLocation;
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
        Log.i(TAG, "searchPatch: S: " + startPoint + " T: " + targets);
        DijkstraSearch dijkstraAlgorithm = new DijkstraSearch(graphRepository, startPoint, targets);
        pathSearcher.startSearch(dijkstraAlgorithm);
    }


    public boolean isStartPointSelected() {
        return startPointSelected;
    }

    public boolean isTargetPointSelected() {
        return targetPointSelected;
    }

    public void clearStartPointSelection(){
//        setStartLocation(null);
//        setStartPoint(null);
        startLocation.setValue(null);
        startMapPoint.setValue(null);
    }

    public void clearTargetPointSelection(){
//        setTargetLocation(null);
//        setTargetPoint(null);
        targetLocation.setValue(null);
        targetMapPoint.setValue(null);
    }

}
