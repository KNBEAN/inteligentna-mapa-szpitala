package bean.pwr.imskamieskiego.data;

import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;

import bean.pwr.imskamieskiego.data.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.data.map.dao.FloorInfoDao;
import bean.pwr.imskamieskiego.data.map.dao.LocationDao;
import bean.pwr.imskamieskiego.data.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.data.map.entity.EdgeEntity;
import bean.pwr.imskamieskiego.data.map.entity.FloorInfoEntity;
import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;


@Database(entities =
        {
                MapPointEntity.class,
                LocationEntity.class,
                EdgeEntity.class,
                FloorInfoEntity.class
        },
        version = 1)
@TypeConverters({IntegerConverter.class})
public abstract class LocalDB extends RoomDatabase {

    public abstract MapPointDao getMapPointDao();
    public abstract LocationDao getLocationDao();
    public abstract EdgeDao getEdgeDao();
    public abstract FloorInfoDao getFloorInfoDao();

    private static String DB_NAME = "IMS_database";
    private static LocalDB INSTANCE;


    public static LocalDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDB.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
