package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.List;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.LocationFactory;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;

public class NavigationSetupViewModel extends AndroidViewModel {

    private static final String TAG = "NavSetupViewModel";

    private MapRepository mapRepository;

    private MutableLiveData<MapPoint> startMapPointTrigger;
    private MutableLiveData<Location> startLocationTrigger;
    private MediatorLiveData<Location> startLocation;
    private MediatorLiveData<MapPoint> startMapPoint;

    private MutableLiveData<String> targetLocationName;

    private boolean startPointSelected = false;


    public NavigationSetupViewModel(@NonNull Application application, String targetName) {
        super(application);

        LocalDB dataBase = LocalDB.getDatabase(application);
        mapRepository = new MapRepository(dataBase);
        targetLocationName = new MutableLiveData<>();

        startLocationTrigger = new MutableLiveData<>();
        startMapPointTrigger = new MutableLiveData<>();
        startLocation = new MediatorLiveData<>();
        startMapPoint = new MediatorLiveData<>();


        //Location comes from search
        startLocation.addSource(startLocationTrigger, location -> startLocation.setValue(location));
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
            startLocation.setValue(location);
        });

        setTarget(targetName);
    }

    public void setTarget(String targetLocationName){
        this.targetLocationName.setValue(targetLocationName);
    }

    public LiveData<String> getTargetLocationName(){
        return targetLocationName;
    }

    public LiveData<MapPoint> getStartPoint() {
        return startMapPoint;
    }

    public LiveData<Location> getStartLocation() {
        return startLocation;
    }

    public void setStartPoint(MapPoint startMapPoint){
        startMapPointTrigger.setValue(startMapPoint);
    }


    public void setStartLocation(Location startLocation){
        startLocationTrigger.setValue(startLocation);
    }

    public boolean isStartPointSelected() {
        return startPointSelected;
    }

    public void clearStartPointSelection(){
        startLocation.setValue(null);
        startMapPoint.setValue(null);
        startPointSelected = false;
    }
}
