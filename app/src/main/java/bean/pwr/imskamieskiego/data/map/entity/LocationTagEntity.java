package bean.pwr.imskamieskiego.data.map.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "location_tags",
        foreignKeys = @ForeignKey(
                entity = LocationEntity.class,
                parentColumns = "id",
                childColumns = "location_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("location_id"))
public class LocationTagEntity {

    @PrimaryKey
    @NonNull
    private String tag;

    private int location_id;


    public LocationTagEntity(@NonNull String tag, int location_id) {
        this.tag = tag;
        this.location_id = location_id;
    }

    public int getLocation_id(){
        return location_id;
    }

    public String getTag(){
        return tag;
    }
}
