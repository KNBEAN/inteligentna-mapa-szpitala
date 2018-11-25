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

import bean.pwr.imskamieskiego.data.map.entity.FloorInfoEntity;

@Dao
public interface FloorInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllFloors(List<FloorInfoEntity> floors);

    @Query("SELECT floorName FROM floors ORDER BY floorNumber ASC")
    LiveData<String[]> getFloorNames();

}
