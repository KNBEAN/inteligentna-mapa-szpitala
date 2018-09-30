package bean.pwr.imskamieskiego.repository;

import android.arch.lifecycle.LiveData;
import android.database.Cursor;
import java.util.List;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;


/**
 * This interface represents repository of data related to map and navigation graph.
 */
public interface IMapRepository {

    /**
     * Returns the point on the map which is nearest to given coordinates on specified floor.
     * Points from other floors are omit.
     * @param x coordinate on the map
     * @param y coordinate on the map
     * @param floor floor on which points will be searched
     * @return MapPoint object. Can be null, when specified floor haven't any point.
     */
    LiveData<MapPoint> getNearestPoint(int x, int y, int floor);

    /**
     * Returns list of points on map associated with location.
     * @param id location ID
     * @return return list of MapPoint. List can be empty if location hasn't any associated point.
     */
    LiveData<List<MapPoint>> getPointsByLocationID(int id);

    /**
     * Returns location represented by the given ID.
     * @param id ID of location
     * @return Location object. Cab be null when object for passed ID not exist.
     */
    LiveData<Location> getLocationByID(int id);

    /**
     * Returns a list of locations whose text given as an argument is a substring of the location
     * names.
     * @param name The search string in the location names.
     * @param limit Max length of list
     * @return LiveData with list of locations
     */
    LiveData<List<Location>> getLocationsListByName(String name, int limit);

}