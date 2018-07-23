package bean.pwr.imskamieskiego.db.map.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bean.pwr.imskamieskiego.db.map.entity.MapPointEntity;

@Dao
public interface MapPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPoints(List<MapPointEntity> mapPoints);

    @Query("SELECT * FROM nodes WHERE id = :ID")
    MapPointEntity getByID(int ID);

    @Query("SELECT * FROM nodes WHERE locationID = :locationID")
    List<MapPointEntity> getByLocationID(int locationID);

    @Query("SELECT * FROM nodes " +
           "WHERE floor = :floor " +
           "ORDER BY (:xPos - x)*(:xPos - x) + (:yPos - y)*(:yPos - y) " +
           "LIMIT 1")
    MapPointEntity getNearest(int xPos, int yPos, int floor);

}
