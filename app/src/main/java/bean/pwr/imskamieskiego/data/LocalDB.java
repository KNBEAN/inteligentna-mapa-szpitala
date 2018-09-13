package bean.pwr.imskamieskiego.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.concurrent.Executors;

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
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {

                                            MapPointDao mapPointDao = INSTANCE.getMapPointDao();
                                            EdgeDao edgeDao = INSTANCE.getEdgeDao();
                                            LocationDao locationDao = INSTANCE.getLocationDao();


                                            ArrayList<MapPointEntity> mapPointArrayList = getEntityArrayList(context,"nodeList.json",new TypeToken<ArrayList<MapPointEntity>>() {});
                                            mapPointDao.insertAllPoints(mapPointArrayList);

                                            ArrayList<EdgeEntity> edgeArrayList = getEntityArrayList(context, "edgeList.json",new TypeToken<ArrayList<EdgeEntity>>() {});
                                            edgeDao.insertAllEdges(edgeArrayList);

                                            ArrayList<LocationEntity> locationArrayList = getEntityArrayList(context,"locationList.json",new TypeToken<ArrayList<LocationEntity>>() {});
                                            locationDao.insertAllPoints(locationArrayList);

                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    /**
     *
     * @param context
     * @param jsonName json file name
     * @param typeToken must be specify in entry of method, when it wil not be specify in compile-time,
     *                  GSON can't specify what type of ArrayList will be and GSON creates a StringMap
     * @param <T> it represents array type
     * @return ArrayList of specify entity
     */

    private static <T> ArrayList<T> getEntityArrayList(Context context, String jsonName, TypeToken<ArrayList<T>> typeToken) {

        ArrayList<T> arrayList = null;

        try (InputStream inputStream = context.getAssets().open(jsonName)) {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(inputStream);
            arrayList = gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

}

