package bean.pwr.imskamieskiego.db;

import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;

import bean.pwr.imskamieskiego.db.map.dao.LocationDao;
import bean.pwr.imskamieskiego.db.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.db.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.db.map.entity.MapPointEntity;


@Database(entities =
        {
                MapPointEntity.class,
                LocationEntity.class
        },
        version = 1)
public abstract class LocalDB extends RoomDatabase {

    public abstract MapPointDao getMapPointDao();
    public abstract LocationDao getLocationDao();


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
