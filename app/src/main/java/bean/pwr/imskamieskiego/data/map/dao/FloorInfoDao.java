package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bean.pwr.imskamieskiego.data.map.entity.FloorInfoEntity;

@Dao
public interface FloorInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPoints(List<FloorInfoEntity> mapPoints);

    @Query("SELECT floorName FROM floors ORDER BY floorNumber ASC")
    LiveData<String[]> getFloorNames();

    @Query("SELECT imagePath FROM floors WHERE floorNumber = :floorNumber")
    LiveData<String> getFloorImagePath(int floorNumber);

}
