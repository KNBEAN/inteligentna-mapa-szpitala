package bean.pwr.imskamieskiego.db.map.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import javax.annotation.Nullable;
import bean.pwr.imskamieskiego.data.map.Location;

@Entity(tableName = "locations")
public class LocationEntity implements Location {

    @PrimaryKey
    private int id;
    private String name;
    private String description;


    @Override
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
