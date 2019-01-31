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

import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.LocationFactory;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;
import bean.pwr.imskamieskiego.utils.EventWrapper;

/**
 * View model, which one control selection of target location/point for navigation.
 */
public class LocationViewModel extends AndroidViewModel {

    public static final int TOILET_QA = 1;
    public static final int FOOD_QA = 2;
    public static final int PATIENT_ASSISTANT_QA = 3;

    private static final String TAG = "LocationViewModel";

    private Application application;
    private MapRepository mapRepository;

    private MutableLiveData<MapPoint> targetMapPointTrigger;
    private MutableLiveData<Location> targetLocationTrigger;
    private MutableLiveData<Integer> targetQuickAccessType;
    private MediatorLiveData<EventWrapper<Location>> targetLocation;
    private MediatorLiveData<List<MapPoint>> targetMapPoints;

    private boolean targetPointSelected = false;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        LocalDB dataBase = LocalDB.getDatabase(application.getApplicationContext());
        mapRepository = new MapRepository(dataBase);

        targetLocationTrigger = new MutableLiveData<>();
        targetMapPointTrigger = new MutableLiveData<>();
        targetQuickAccessType = new MutableLiveData<>();
        targetLocation = new MediatorLiveData<>();
        targetMapPoints = new MediatorLiveData<>();


        //Location comes from search
        targetLocation.addSource(targetLocationTrigger, location -> targetLocation.setValue(new EventWrapper<>(location)));
        LiveData<MapPoint> tmpTargetPoint = Transformations.switchMap(targetLocationTrigger, location ->
                location != null ? mapRepository.getPointByLocationID(location.getId()) : null
        );
        targetMapPoints.addSource(tmpTargetPoint, mapPoint -> {
            if (mapPoint != null) {
                targetPointSelected = true;
            }
            targetMapPoints.setValue(Arrays.asList(mapPoint));
        });

        //Data from quick access
        LiveData<List<MapPoint>> quickAccessMapPoints = Transformations.switchMap(targetQuickAccessType,
                type -> type != null ? mapRepository.getPointsByQuickAccessType(type) : null
        );
        targetMapPoints.addSource(quickAccessMapPoints, mapPoints -> {
            if (mapPoints != null) {
                targetPointSelected = true;
            }
            targetMapPoints.setValue(mapPoints);
        });

        //MapPoint comes from touch the map
        LiveData<MapPoint> nearestTargetPoint = Transformations.switchMap(targetMapPointTrigger,
                (mapPoint) -> mapRepository.getNearestPoint(mapPoint.getX(), mapPoint.getY(), mapPoint.getFloor())
        );

        targetMapPoints.addSource(nearestTargetPoint, mapPoint -> {
            if (mapPoint != null) {
                targetPointSelected = true;
            }
            targetMapPoints.setValue(Arrays.asList(mapPoint));
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

    /**
     * Returns list of points for selected target.
     *
     * @return list of target points as LiveData
     */
    public LiveData<List<MapPoint>> getTargetPoint() {
        return targetMapPoints;
    }

    /**
     * Returns selected location as LiveData. Selected location is wrapped into EventWrapper object.
     * If the target point was selected from the map and this target point is not assigned to any
     * location, the location will be generated as the location with the default name.
     *
     * @return LiveData with location wrapped into EventWrapper
     */
    public LiveData<EventWrapper<Location>> getTargetLocation() {
        return targetLocation;
    }

    /**
     * Set target point. If passed map point isn't exact point in database,
     * the nearest point will be set as the destination.
     *
     * @param destinationMapPoint target point
     */
    public void setTargetPoint(MapPoint destinationMapPoint) {
        targetMapPointTrigger.setValue(destinationMapPoint);
    }

    /**
     * Set target location
     *
     * @param targetLocation target location
     */
    public void setTargetLocation(Location targetLocation) {
        targetLocationTrigger.setValue(targetLocation);
    }

    /**
     * Returns status of target point selection.
     *
     * @return if target is selected, returns true. Otherwise return false.
     */
    public boolean isTargetPointSelected() {
        return targetPointSelected;
    }

    public void getQuickAccessTarget(int quickAccessType) {
        String qaLocationName;
        String qaLocationDescription = null;
        switch (quickAccessType) {
            case TOILET_QA:
                qaLocationName = application.getString(R.string.wc_quick_access_button);
                qaLocationDescription = application.getString(R.string.quick_access_wc_desc);
                break;
            case FOOD_QA:
                qaLocationName = application.getString(R.string.food_quick_access_button);
                qaLocationDescription = application.getString(R.string.quick_access_food_desc);
                break;
            case PATIENT_ASSISTANT_QA:
                qaLocationName = application.getString(R.string.ap_quick_access_button);
                qaLocationDescription = application.getString(R.string.quick_access_ap_desc);
                break;
            default:
                qaLocationName = application.getString(R.string.default_place_name);

        }
        targetQuickAccessType.setValue(quickAccessType);
        targetLocation.setValue(new EventWrapper<>(LocationFactory.create(qaLocationName, qaLocationDescription)));
    }

    /**
     * Clear target selection
     */
    public void clearTargetPointSelection() {
        targetLocation.setValue(null);
        targetMapPoints.setValue(null);
        targetPointSelected = false;
    }

}
