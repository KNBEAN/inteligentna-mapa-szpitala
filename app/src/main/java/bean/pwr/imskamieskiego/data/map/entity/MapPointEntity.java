/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.data.map.entity;

import android.arch.persistence.room.*;

import java.util.Objects;

import bean.pwr.imskamieskiego.model.map.MapPoint;

@Entity(tableName = "nodes")
public class MapPointEntity implements MapPoint {

    @PrimaryKey
    private int id;
    private int floor;
    private int x;
    private int y;
    private int locationID;

    public MapPointEntity(int id, int floor, int x, int y, int locationID) {
        this.id = id;
        this.floor = floor;
        this.x = x;
        this.y = y;
        this.locationID = locationID;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getFloor() {
        return floor;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getLocationID() {
        return locationID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapPointEntity that = (MapPointEntity) o;
        return id == that.id &&
                floor == that.floor &&
                x == that.x &&
                y == that.y &&
                locationID == that.locationID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, floor, x, y, locationID);
    }
}
