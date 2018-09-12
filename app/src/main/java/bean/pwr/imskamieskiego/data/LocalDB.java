package bean.pwr.imskamieskiego.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
    private static String MAP_POINT_MEMBER_NAME = "nodeList";
    private static String EDGE_MEMBER_NAME = "edgeList";
    private static String LOCATION_MEMBER_NAME = "locationList";

    private static LocalDB INSTANCE;

    private static JsonObject jsonObject;
    private static List<MapPointEntity> mapPointList;
    private static List<EdgeEntity> edgeList;
    private static List<LocationEntity> locationList;


    public static LocalDB getDatabase(final Context context) {

        jsonObject = getJsonFromAsset(context, "testmap_data.json");

        if (INSTANCE == null) {
            synchronized (LocalDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDB.class, DB_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Log.i("Instance", "NewCallback");
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MapPointDao mapPointDao;
        private final EdgeDao edgeDao;
        private final LocationDao locationDao;

        PopulateDbAsync(LocalDB db) {
            mapPointDao = db.getMapPointDao();
            edgeDao = db.getEdgeDao();
            locationDao = db.getLocationDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            setEnityList(MAP_POINT_MEMBER_NAME);
            setEnityList(EDGE_MEMBER_NAME);
            setEnityList(LOCATION_MEMBER_NAME);

            mapPointDao.insertAllPoints(mapPointList);
            edgeDao.insertAllEdges(edgeList);
            //locationDao.insertAllPoints(locationList);
            return null;
        }
    }

    private static JsonObject getJsonFromAsset(Context context, String jsonName) {
        JsonObject json = null;

        try {
            Gson gson = new Gson();
            InputStream inputStream = context.getAssets().open(jsonName);

            Reader reader = new InputStreamReader(inputStream);
            json = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    private static void setEnityList(String memberName) {

        mapPointList = new ArrayList<>();
        edgeList = new ArrayList<>();

        //JsonObject jsonObject = getJsonFromAsset(context, "testmap_data.json");
        JsonArray jsonArray = jsonObject.getAsJsonArray(memberName);

        if (memberName.equals("nodeList")) {
            for (JsonElement jsonElement : jsonArray) {
                int id = jsonElement.getAsJsonObject().get("id").getAsInt();
                int floor = jsonElement.getAsJsonObject().get("floor").getAsInt();
                int x = jsonElement.getAsJsonObject().get("x").getAsInt();
                int y = jsonElement.getAsJsonObject().get("y").getAsInt();
                int locationID = jsonElement.getAsJsonObject().get("locationID").getAsInt();

                MapPointEntity mapPointEntity = new MapPointEntity(id, floor, x, y, locationID);
                mapPointList.add(mapPointEntity);

            }
        } else if (memberName.equals("edgeList")) {

            for (JsonElement jsonElement : jsonArray) {
                int id = jsonElement.getAsJsonObject().get("id").getAsInt();
                int from_id = jsonElement.getAsJsonObject().get("from_id").getAsInt();
                int to_id = jsonElement.getAsJsonObject().get("to_id").getAsInt();
                int length = jsonElement.getAsJsonObject().get("length").getAsInt();

                EdgeEntity edgeEntity = new EdgeEntity(id, from_id, to_id, length);
                edgeList.add(edgeEntity);
            }
        } else if (memberName.equals("locationList")) {
            for (JsonElement jsonElement : jsonArray) {
                int id = jsonElement.getAsJsonObject().get("id").getAsInt();
                String name = jsonElement.getAsJsonObject().get("name").getAsString();
                //String description = jsonElement.getAsJsonObject().get("description").getAsString();

                LocationEntity locationEntity = new LocationEntity(id, name, null);
                locationList.add(locationEntity);
            }
        }
    }


}

