/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.dao.LocationDao;
import bean.pwr.imskamieskiego.data.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;

/**
 * This class is implementation of IMapRepository. It should be used to get access to data related
 * to map location ect. Methods of this class must be call from thread other than main thread.
 */
public class MapRepository implements IMapRepository {

    private MapPointDao mapPointDao;
    private LocationDao locationDao;

    public MapRepository(LocalDB dataBase) {
        mapPointDao = dataBase.getMapPointDao();
        locationDao = dataBase.getLocationDao();
    }

    @Override
    public LiveData<MapPoint> getNearestPoint(int x, int y, int floor) {
        return Transformations.map(
                mapPointDao.getNearestLiveData(x, y, floor),
                point -> point
        );
    }

    @Override
    public LiveData<List<MapPoint>> getPointsByLocationID(int id) {
        return Transformations.map(
                mapPointDao.getByLocationIDLiveData(id),
                list-> new ArrayList<MapPoint>(list)
        );
    }

    @Override
    public LiveData<Location> getLocationByID(int id) {
        return Transformations.map(
                locationDao.getByID(id),
                locationEntity-> locationEntity
        );
    }

    @Override
    public LiveData<List<Location>> getLocationsListByName(@NonNull String name, int limit) {
        return Transformations.map(
                locationDao.getListByTag(name.toLowerCase(), limit),
                list-> new ArrayList<>(list)
        );
    }

    @Override
    public LiveData<List<MapPoint>> getPointsByQuickAccessType(int quickAccessType) {
        return Transformations.map(
                mapPointDao.getQuickAccessPoints(quickAccessType),
                list -> new ArrayList<>(list)
        );
    }
}
