package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPoints(List<LocationEntity> mapPoints);

    @Query("SELECT * FROM locations WHERE id = :ID")
    LocationEntity getByID(int ID);

    /**
     * TODO This query doesn't work properly in this shape, because limitations of SQLite (Only ASCII)
     * TODO I will be created with using additional table
    **/
//    @Query("SELECT * FROM locations WHERE lower(name) LIKE lower(:name) LIMIT :limit")
//    List<LocationEntity> getByName(String name, int limit);

}
