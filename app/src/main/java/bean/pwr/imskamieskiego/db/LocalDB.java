package bean.pwr.imskamieskiego.db;

import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;

import bean.pwr.imskamieskiego.db.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.db.map.dao.LocationDao;
import bean.pwr.imskamieskiego.db.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.db.map.entity.EdgeEntity;
import bean.pwr.imskamieskiego.db.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.db.map.entity.MapPointEntity;


@Database(entities =
        {
                MapPointEntity.class,
                LocationEntity.class,
                EdgeEntity.class
        },
        version = 1)
@TypeConverters({IntegerConverter.class})
public abstract class LocalDB extends RoomDatabase {

    public abstract MapPointDao getMapPointDao();
    public abstract LocationDao getLocationDao();
    public abstract EdgeDao getEdgeDao();


    private static LocalDB INSTANCE;


    static LocalDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDB.class, "IMS_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
