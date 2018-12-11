/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.data.map.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "quick_access_locations",
        foreignKeys = @ForeignKey(
                entity = LocationEntity.class,
                parentColumns = "id",
                childColumns = "location_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("location_id"))
public class QuickAccessEntity {

    @PrimaryKey
    private int id;
    private int location_id;
    private int quick_access_type;

    public QuickAccessEntity(int id, int location_id, int quick_access_type) {
        this.id = id;
        this.location_id = location_id;
        this.quick_access_type = quick_access_type;
    }

    public int getId() {
        return id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public int getQuick_access_type() {
        return quick_access_type;
    }
}
