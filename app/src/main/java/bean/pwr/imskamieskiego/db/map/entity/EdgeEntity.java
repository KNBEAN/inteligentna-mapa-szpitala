package bean.pwr.imskamieskiego.db.map.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import bean.pwr.imskamieskiego.model.map.Edge;

@Entity(tableName = "edges")
public class EdgeEntity implements Edge {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "from_id")
    private int from;
    @ColumnInfo(name = "to_id")
    private int to;
    private int length;

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public int getFrom() {
        return from;
    }

    public void setFromId(int fromID) {
        this.from = fromID;
    }

    @Override
    public int getTo() {
        return to;
    }

    public void setToId(int toID) {
        this.to = toID;
    }

    @Override
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
