package bean.pwr.imskamieskiego.db.map;

import android.arch.persistence.room.*;
import bean.pwr.imskamieskiego.data.map.MapPoint;

@Entity(tableName = "nodes")
public class MapPointEntity implements MapPoint {

    @PrimaryKey
    private int id;
    private int floor;
    private int x;
    private int y;
    private int locationID;

    @Override
    public int getID() {
        return id;
    }

    public void setID(int id) {
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
