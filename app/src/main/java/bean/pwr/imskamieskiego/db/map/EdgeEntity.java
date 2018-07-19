package bean.pwr.imskamieskiego.db.map;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "edges")
public class EdgeEntity {

    @PrimaryKey
    private int id;
    private int fromId;
    private int toId;
    private int length;

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
