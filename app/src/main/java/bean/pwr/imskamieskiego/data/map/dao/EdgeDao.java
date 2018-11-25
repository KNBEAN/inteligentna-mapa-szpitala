/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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
    List<EdgeEntity> getOutgoingEdges(int fromID);

    @Query("SELECT * FROM edges WHERE from_id IN (:fromID)")
    List<EdgeEntity> getOutgoingEdges(List<Integer> fromID);

}

