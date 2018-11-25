/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.Executors;

import bean.pwr.imskamieskiego.data.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.data.map.dao.FloorInfoDao;
import bean.pwr.imskamieskiego.data.map.dao.LocationDao;
import bean.pwr.imskamieskiego.data.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.data.map.entity.EdgeEntity;
import bean.pwr.imskamieskiego.data.map.entity.FloorInfoEntity;
import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.data.map.entity.LocationTagEntity;
import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;


@Database(entities =
        {
                MapPointEntity.class,
                LocationEntity.class,
                EdgeEntity.class,
                FloorInfoEntity.class,
                LocationTagEntity.class
        },
        version = 1)
@TypeConverters({IntegerConverter.class})
public abstract class LocalDB extends RoomDatabase {

    private static final String TAG = "LocalDB";

    private static final String NODE_LIST_FILE = "nodeList.json";
    private static final String EDGE_LIST_FILE = "edgeList.json";
    private static final String LOCATION_LIST_FILE = "locationList.json";
    private static final String TAG_LIST_FILE = "tagList.json";
    private static final String FLOOR_LIST_FILE = "floorList.json";

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
                    INSTANCE = createDatabaseInstance(context);
                }
            }
        }
        return INSTANCE;
    }

    private static LocalDB createDatabaseInstance(Context context){
        return Room.databaseBuilder(context.getApplicationContext(), LocalDB.class, DB_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {

                            MapPointDao mapPointDao = INSTANCE.getMapPointDao();
                            EdgeDao edgeDao = INSTANCE.getEdgeDao();
                            LocationDao locationDao = INSTANCE.getLocationDao();
                            FloorInfoDao floorInfoDao = INSTANCE.getFloorInfoDao();


                            List<MapPointEntity> mapPointArrayList = getEntityListFromJsonFile(context, NODE_LIST_FILE, new TypeToken<List<MapPointEntity>>() {});
                            mapPointDao.insertAllPoints(mapPointArrayList);

                            List<EdgeEntity> edgeList = getEntityListFromJsonFile(context, EDGE_LIST_FILE, new TypeToken<List<EdgeEntity>>() {});
                            edgeDao.insertAllEdges(edgeList);

                            List<LocationEntity> locationList = getEntityListFromJsonFile(context, LOCATION_LIST_FILE, new TypeToken<List<LocationEntity>>() {});
                            locationDao.insertAllLocations(locationList);

                            List<LocationTagEntity> locationTagList = getEntityListFromJsonFile(context, TAG_LIST_FILE, new TypeToken<List<LocationTagEntity>>() {});
                            locationDao.insertAllTags(locationTagList);

                            List<FloorInfoEntity> floorInfoList = getEntityListFromJsonFile(context, FLOOR_LIST_FILE, new TypeToken<List<FloorInfoEntity>>() {});
                            floorInfoDao.insertAllFloors(floorInfoList);

                        });
                        Log.d(TAG, "onCreate: data loaded into database");
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        Log.d(TAG, "onOpen: database open");
                    }
                })
                .build();
    }

    private static <T> List<T> getEntityListFromJsonFile(Context context, String jsonName, TypeToken<List<T>> typeToken) {

        List<T> list = null;

        try (InputStream inputStream = context.getAssets().open(jsonName)) {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(inputStream);
            list = gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Can't load data from file: " + jsonName, e);
        }

        return list;
    }

}

