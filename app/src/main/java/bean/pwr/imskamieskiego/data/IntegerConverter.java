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
