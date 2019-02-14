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


@Entity(
        tableName = "locations_points",
        foreignKeys = {
                @ForeignKey(
                        entity = LocationEntity.class,
                        parentColumns = "id",
                        childColumns = "locationID",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = MapPointEntity.class,
                        parentColumns = "id",
                        childColumns = "mapPointID",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = @Index("mapPointID")
)
public class LocationPointEntity {

    @PrimaryKey
    private int locationID;
    private int mapPointID;

    public LocationPointEntity(int locationID, int mapPointID) {
        this.locationID = locationID;
        this.mapPointID = mapPointID;
    }

    public int getLocationID() {
        return locationID;
    }

    public int getMapPointID() {
        return mapPointID;
    }
}
