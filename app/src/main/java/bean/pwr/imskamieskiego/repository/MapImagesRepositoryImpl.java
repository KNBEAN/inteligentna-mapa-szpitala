package bean.pwr.imskamieskiego.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.dao.FloorInfoDao;

public class MapImagesRepositoryImpl implements MapImagesRepository {

    private final String TAG = "MapImgRepository";
    private FloorInfoDao floorInfoDao;
    private MutableLiveData<InputStream> mapImageLiveData;
    private Context context;

    public MapImagesRepositoryImpl(LocalDB dataBase, Context context) {
        this.context = context;
        floorInfoDao = dataBase.getFloorInfoDao();
    }

    @Override
    public LiveData<String[]> getFloorNames() {
        return floorInfoDao.getFloorNames();
    }

    @Override
    public LiveData<InputStream> getMapImage(int floor) {

        if (mapImageLiveData == null){
            mapImageLiveData = new MutableLiveData<>();
        }

        mapImageLiveData.postValue(getMapStream(floor));

        return mapImageLiveData;
    }


    private InputStream getMapStream(int floor){
        final String MAP_IMAGE_DIR = "map-img/";
        final String MAP_IMG_TYPE = "png";

        InputStream mapImageStream = null;
        String imgPath = MAP_IMAGE_DIR + floor + "." + MAP_IMG_TYPE;

        Log.d(TAG, "Path: " + imgPath);

        try {
            mapImageStream = context.getAssets().open(imgPath, AssetManager.ACCESS_STREAMING);
        } catch (IOException e) {
            Log.w(TAG, "Problem with map image open.");
            e.printStackTrace();
        }

        return mapImageStream;
    }

}
