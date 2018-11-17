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

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.LocationFactory;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;
import bean.pwr.imskamieskiego.utils.EventWrapper;


public class LocationViewModel extends AndroidViewModel {

    private static final String TAG = "LocationViewModel";

    private MapRepository mapRepository;

    private MutableLiveData<MapPoint> targetMapPointTrigger;
    private MutableLiveData<Location> targetLocationTrigger;
    private MediatorLiveData<EventWrapper<Location>> targetLocation;
    private MediatorLiveData<List<MapPoint>> targetMapPoint;

    private boolean targetPointSelected = false;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        LocalDB dataBase = LocalDB.getDatabase(application.getApplicationContext());
        mapRepository = new MapRepository(dataBase);

        targetLocationTrigger = new MutableLiveData<>();
        targetMapPointTrigger = new MutableLiveData<>();
        targetLocation = new MediatorLiveData<>();
        targetMapPoint = new MediatorLiveData<>();


        //Location comes from search
        targetLocation.addSource(targetLocationTrigger, location -> targetLocation.setValue(new EventWrapper<>(location)));
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
            targetLocation.setValue(new EventWrapper<>(location));
        });
    }

    public LiveData<List<MapPoint>> getTargetPoint() {
        return targetMapPoint;
    }

    public LiveData<EventWrapper<Location>> getTargetLocation() {
        return targetLocation;
    }

    public void setTargetPoint(MapPoint destinationMapPoint){
        targetMapPointTrigger.setValue(destinationMapPoint);
    }

    public void setTargetLocation(Location targetLocation){
        targetLocationTrigger.setValue(targetLocation);
    }

    public boolean isTargetPointSelected() {
        return targetPointSelected;
    }

    public void clearTargetPointSelection(){
        targetLocation.setValue(null);
        targetMapPoint.setValue(null);
        targetPointSelected = false;
    }

}
