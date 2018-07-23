package bean.pwr.imskamieskiego.db.map.entity;

import android.arch.persistence.room.*;
import android.support.annotation.Nullable;

import bean.pwr.imskamieskiego.data.map.MapPoint;

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

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
