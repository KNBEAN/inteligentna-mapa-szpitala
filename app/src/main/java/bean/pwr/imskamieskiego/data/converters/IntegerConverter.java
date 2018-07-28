package bean.pwr.imskamieskiego.data.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class IntegerConverter {

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
