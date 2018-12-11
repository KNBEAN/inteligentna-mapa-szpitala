/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.data;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Converter used with Room database library. It use to convert list of integer objects into
 * int table and vice versa. It is necessary, because SQLite work with simple data types.
 */
class IntegerConverter {

    @TypeConverter
    public int[] listToTable(List<Integer> list){
        int[] table = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            table[i] = list.get(i);
        }
        return table;
    }

    @TypeConverter
    public List<Integer> tableToList(int[] table){
        List<Integer> list = new ArrayList<>(table.length);
        for (int i = 0; i < table.length; i++) {
            list.add(table[i]);
        }
        return list;
    }
}
