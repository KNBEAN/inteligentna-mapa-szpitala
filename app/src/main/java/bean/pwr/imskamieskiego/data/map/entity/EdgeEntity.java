/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public int getTo() {
        return to;
    }

    @Override
    public int getLength() {
        return length;
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
