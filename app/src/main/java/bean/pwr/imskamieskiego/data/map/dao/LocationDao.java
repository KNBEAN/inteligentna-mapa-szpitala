package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.data.map.entity.LocationTagEntity;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllLocations(List<LocationEntity> mapLocations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTags(List<LocationTagEntity> locationTags);

    @Query("SELECT * FROM locations WHERE id = :ID")
    LiveData<LocationEntity> getByID(int ID);

    @Query("SELECT id, name, description FROM  locations JOIN location_tags ON id = location_id WHERE tag LIKE :tag LIMIT :limit")
    LiveData<List<LocationEntity>> getListByTag(String tag, int limit);

    @Query("SELECT id, name, description FROM  locations JOIN location_tags ON id = location_id WHERE tag LIKE :tag LIMIT :limit")
    Cursor getCursorByTag(String tag, int limit);
}
