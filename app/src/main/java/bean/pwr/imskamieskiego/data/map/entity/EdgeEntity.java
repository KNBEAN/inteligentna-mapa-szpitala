package bean.pwr.imskamieskiego.data.map.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

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

    public EdgeEntity(int id, int from, int to, int length) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getFrom() {
        return from;
    }

    public void setFrom(int fromID) {
        this.from = fromID;
    }

    @Override
    public int getTo() {
        return to;
    }

    public void setTo(int toID) {
        this.to = toID;
    }

    @Override
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeEntity that = (EdgeEntity) o;
        return id == that.id &&
                from == that.from &&
                to == that.to &&
                length == that.length;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, from, to, length);
    }
}
