package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import bean.pwr.imskamieskiego.MapActivity;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.IMapRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;

/**
 * The ViewModel used in [InfoSheetWindow]
 */

public class LocationViewModel extends AndroidViewModel {

    private MapRepository repository;
    private LocalDB dataBase;

    private MutableLiveData<MapPoint> targetMapPoint = new MutableLiveData<>();


    private LiveData<MapPoint> nearestMapPoint = Transformations.switchMap(targetMapPoint,
            (mapPoint) -> {

                return repository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor());

            });

    public LiveData<MapPoint> getNearestMapPoint() {
        return nearestMapPoint;
    }

    private LiveData<Location> currentLocation = Transformations.switchMap(nearestMapPoint, mapPoint -> {


        if (mapPoint != null) {
            return repository.getLocationByID(mapPoint.getLocationID());
        }
        return null;
    });

    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void setMapPoint(MapPoint mapPoint) {
        targetMapPoint.postValue(mapPoint);
    }


    //Target location
    public LocationViewModel(@NonNull Application application) {
        super(application);
        dataBase = LocalDB.getDatabase(application.getApplicationContext());
        repository = new MapRepository(dataBase);


    }


}
