package bean.pwr.imskamieskiego.data.map.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import bean.pwr.imskamieskiego.data.map.entity.EdgeEntity;

@Dao
public interface EdgeDao {

    @Insert
    void insertAllEdges(List<EdgeEntity> edges);

    @Query("SELECT * FROM edges WHERE from_id = :fromID")
    List<EdgeEntity> getOngoingEdges(int fromID);

    @Query("SELECT * FROM edges WHERE from_id IN (:fromID)")
    List<EdgeEntity> getOngoingEdges(List<Integer> fromID);

}
