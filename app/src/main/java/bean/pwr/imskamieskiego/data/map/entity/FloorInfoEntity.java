package bean.pwr.imskamieskiego.data.map.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "floors")
public class FloorInfoEntity {

    @PrimaryKey
    private int floorNumber;
    @NonNull
    private String floorName;
    @NonNull
    private String floorMap;

    public FloorInfoEntity(int floorNumber, String floorName, String floorMap) {
        this.floorNumber = floorNumber;
        this.floorName = floorName;
        this.floorMap = floorMap;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public String getFloorName() {
        return floorName;
    }

    public String getFloorMap() {
        return floorMap;
    }
}
