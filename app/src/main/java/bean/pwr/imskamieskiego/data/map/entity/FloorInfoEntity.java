/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.data.map.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "floors")
public class FloorInfoEntity {

    @PrimaryKey
    private int floorNumber;
    @NonNull
    private String floorName;

    public FloorInfoEntity(int floorNumber, String floorName) {
        this.floorNumber = floorNumber;
        this.floorName = floorName;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public String getFloorName() {
        return floorName;
    }

}
