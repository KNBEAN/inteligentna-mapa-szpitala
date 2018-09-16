package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;

@Dao
public interface MapPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPoints(List<MapPointEntity> mapPoints);

    @Query("SELECT * FROM nodes WHERE id = :id")
    MapPointEntity getByID(int id);
  
    @Query("SELECT * FROM nodes WHERE id IN (:id)")
    List<MapPointEntity> getByID(List<Integer> id);

    @Query("SELECT * FROM nodes WHERE locationID = (:locationID)")
    List<MapPointEntity> getByLocationID(int locationID);

    @Query("SELECT * FROM nodes WHERE locationID = (:locationID)")
    LiveData<List<MapPointEntity>> getByLocationIDLiveData(int locationID);

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

}
