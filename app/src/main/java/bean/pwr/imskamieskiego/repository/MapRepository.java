package bean.pwr.imskamieskiego.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.data.map.dao.LocationDao;
import bean.pwr.imskamieskiego.data.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.model.map.Edge;
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
    public MapPoint getNearestPoint(int x, int y, int floor) {
        return mapPointDao.getNearest(x, y, floor);
    }

    @Override
    public List<MapPoint> getPointsByLocationID(int id) {
        return new ArrayList<MapPoint>(mapPointDao.getByLocationID(id));
    }

    @Override
    public Location getLocationByID(int id) {
        return locationDao.getByID(id);
    }

    @Override
    public LiveData<List<Location>> getLocationsListByName(String name, int limit) {
        return Transformations.map(
                locationDao.getListByTag(name.toLowerCase(), limit),
                list-> new ArrayList<>(list)
        );
    }

    @Override
    public Cursor getLocationsCursorByName(String name, int limit) {
        return locationDao.getCursorByTag(name.toLowerCase(), limit);
    }
}
