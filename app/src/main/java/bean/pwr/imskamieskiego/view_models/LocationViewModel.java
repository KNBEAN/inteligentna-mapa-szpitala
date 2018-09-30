package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;

/**
 * The ViewModel used in [InfoSheetWindow]
 */

public class LocationViewModel extends AndroidViewModel {

    private  MapRepository repository;
    //The MapPoint choosen by a user on the map
    private MutableLiveData<MapPoint> targetMapPointLiveData = new MutableLiveData<>();
    //Target location
    private MutableLiveData<Location> locationLiveData = new MutableLiveData<>();



    public void setTargetMapPointLiveData(MutableLiveData<MapPoint> targetMapPointLiveData) {
        this.targetMapPointLiveData = targetMapPointLiveData;
    }


    public MutableLiveData<Location> getLocationLiveData() {
        return locationLiveData;
    }



    //Target location
    public LocationViewModel(@NonNull Application application) {
        super(application);
        LiveData locationLiveData = Transformations.switchMap(targetMapPointLiveData, mapPoint ->
               setLocationLiveData(mapPoint));

    }



    public MutableLiveData<Location> setLocationLiveData(MapPoint mapPoint) {
        MapPoint nearestPoint = repository.getNearestPoint(mapPoint.getX(),mapPoint.getY(),mapPoint.getFloor());
        // Returns a LiveData object directly from the database.
        locationLiveData.postValue(repository.getLocationByID(nearestPoint.getId()));
        return locationLiveData;
    }


    public void setTargetMapPointLiveData(MapPoint mapPoint) {
        if (mapPoint!=null){
        targetMapPointLiveData.setValue(mapPoint);

    }}

}
