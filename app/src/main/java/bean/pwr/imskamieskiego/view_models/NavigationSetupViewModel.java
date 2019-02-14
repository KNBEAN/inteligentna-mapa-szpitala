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

/**
 * View model for navigation setup fragment.
 */
public class NavigationSetupViewModel extends AndroidViewModel {

    private static final String TAG = "NavSetupViewModel";

    private MapRepository mapRepository;

    private MutableLiveData<MapPoint> startMapPointTrigger;
    private MutableLiveData<Location> startLocationTrigger;
    private MediatorLiveData<Location> startLocation;
    private MediatorLiveData<MapPoint> startMapPoint;
    private MutableLiveData<String> targetLocationName;

    /**
     * Custom ViewModel for navigation setup view model.
     * @param application application context
     * @param targetName name of target location
     */
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
        LiveData<MapPoint> tmpStartPoint = Transformations.switchMap(startLocationTrigger, location ->
                location != null ? mapRepository.getPointByLocationID(location.getId()) : null
        );
        startMapPoint.addSource(tmpStartPoint, mapPoint -> startMapPoint.setValue(mapPoint));

        //MapPoint comes from touch the map
        LiveData<MapPoint> nearestStartPoint = Transformations.switchMap(startMapPointTrigger,
                (mapPoint) -> mapRepository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor())
        );

        startMapPoint.addSource(nearestStartPoint, mapPoint -> startMapPoint.setValue(mapPoint));

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

    /**
     * Sets target location name
     * @param targetLocationName name of target location
     */
    public void setTarget(String targetLocationName){
        this.targetLocationName.setValue(targetLocationName);
    }

    /**
     * Returns target location name as LiveData
     * @return LiveData with target location name
     */
    public LiveData<String> getTargetLocationName(){
        return targetLocationName;
    }

    /**
     * Returns selected start point as LiveData
     * @return LiveData with actual selected start point
     */
    public LiveData<MapPoint> getStartPoint() {
        return startMapPoint;
    }

    /**
     * Returns the selected starting location. If the starting point was selected from the map and
     * this starting point is not assigned to any location, the location will be generated as the
     * location with the default name.
     * @return selected location as LiveData
     */
    public LiveData<Location> getStartLocation() {
        return startLocation;
    }

    /**
     * Set start point. If passed map point isn't exact point in database,
     * the nearest point will be set as the start point.
     * @param startMapPoint start point
     */
    public void setStartPoint(MapPoint startMapPoint){
        startMapPointTrigger.setValue(startMapPoint);
    }

    /**
     * Sets start location
     * @param startLocation start location
     */
    public void setStartLocation(Location startLocation){
        startLocationTrigger.setValue(startLocation);
    }

}
