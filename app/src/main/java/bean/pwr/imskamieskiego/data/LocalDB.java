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
import java.lang.reflect.Type;
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


                                            ArrayList<MapPointEntity> mapPointArrayList = getEntityArrayList(context,"nodeList.json",MapPointEntity.class);
                                            mapPointDao.insertAllPoints(mapPointArrayList);

                                            ArrayList<EdgeEntity> edgeArrayList = getEntityArrayList(context, "edgeList.json",EdgeEntity.class);
                                            edgeDao.insertAllEdges(edgeArrayList);

                                            ArrayList<LocationEntity> locationArrayList = getEntityArrayList(context,"locationList.json",LocationEntity.class);
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

    private static <T> ArrayList<T> getEntityArrayList(Context context, String jsonName, final Class<T> typeOfList) {

        ArrayList<T> arrayList = null;

        try (InputStream inputStream = context.getAssets().open(jsonName)) {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(inputStream);
            Type typeToken = new TypeToken<ArrayList<T>>() {}.getType();

            arrayList = gson.fromJson(reader, typeToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

}

