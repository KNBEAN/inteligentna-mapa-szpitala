/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;
import bean.pwr.imskamieskiego.data.map.entity.QuickAccessEntity;
import bean.pwr.imskamieskiego.model.map.MapPoint;

@Dao
public interface MapPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPoints(List<MapPointEntity> mapPoints);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuickAccess(List<QuickAccessEntity> quickAccessLocations);

    @Query("SELECT * FROM nodes WHERE id = :id")
    MapPointEntity getByID(int id);

    @Query("SELECT * FROM nodes WHERE id = :id")
    LiveData<MapPointEntity> getByIDLiveData(int id);

    @Query("SELECT * FROM nodes WHERE id IN (:id)")
    List<MapPointEntity> getByID(List<Integer> id);

    @Query("SELECT nodes.id, nodes.floor, nodes.x, nodes.y, nodes.locationID, nodes.hardToReach " +
            "FROM nodes JOIN locations_points ON mapPointID = id " +
            "WHERE locations_points.locationID = :locationID")
    MapPointEntity getByLocationID(int locationID);

    @Query("SELECT nodes.id, nodes.floor, nodes.x, nodes.y, nodes.locationID, nodes.hardToReach " +
            "FROM nodes JOIN locations_points ON mapPointID = id " +
            "WHERE locations_points.locationID = :locationID")
    LiveData<MapPointEntity> getByLocationIDLiveData(int locationID);

    @Query("SELECT * FROM nodes " +
            "WHERE floor = :floor " +
            "ORDER BY (:xPos - x)*(:xPos - x) + (:yPos - y)*(:yPos - y) " +
            "LIMIT 1")
    MapPointEntity getNearest(int xPos, int yPos, int floor);

    @Query("SELECT * FROM nodes " +
            "WHERE floor = :floor " +
            "ORDER BY (:xPos - x)*(:xPos - x) + (:yPos - y)*(:yPos - y) " +
            "LIMIT 1")
    LiveData<MapPointEntity> getNearestLiveData(int xPos, int yPos, int floor);

    @Query("SELECT nodes.id, floor, x, y, locationID, hardToReach FROM nodes JOIN quick_access_locations ON locationID = location_id WHERE quick_access_type = :quickAccessType")
    LiveData<List<MapPointEntity>> getQuickAccessPoints(int quickAccessType);

}
