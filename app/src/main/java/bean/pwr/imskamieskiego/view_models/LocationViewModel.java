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

    private  MapRepository repository;

    private MutableLiveData<MapPoint> targetMapPoint = new MutableLiveData<>();

    private LocalDB dataBase;



    private LiveData<Location> currentLocation = Transformations.switchMap(targetMapPoint, mapPoint ->
        getLocationLiveData(mapPoint));




    public LiveData<Location> getLocationLiveData(MapPoint mapPoint) {

            MapPoint nearestPoint = repository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor());
            Location location = repository.getLocationByID(nearestPoint.getId());

            };

    }

    //Target location
    public LocationViewModel(@NonNull Application application) {
        super(application);
        dataBase = LocalDB.getDatabase(application);
        repository = new MapRepository(dataBase);

//        LiveData locationLiveData = Transformations.switchMap(targetMapPointLiveData, mapPoint ->
//               setLocationLiveData(mapPoint));

    }



}
