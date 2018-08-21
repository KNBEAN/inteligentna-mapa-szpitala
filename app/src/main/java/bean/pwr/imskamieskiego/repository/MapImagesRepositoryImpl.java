package bean.pwr.imskamieskiego.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.io.InputStream;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.dao.FloorInfoDao;

public class MapImagesRepositoryImpl implements MapImagesRepository {

    private FloorInfoDao floorInfoDao;

    private LiveData<InputStream> mapImageLiveData;
    private MutableLiveData<Integer> queryFloorNumber;


    public MapImagesRepositoryImpl(LocalDB dataBase) {
        floorInfoDao = dataBase.getFloorInfoDao();
        queryFloorNumber = new MutableLiveData<>();

    }

    @Override
    public LiveData<String[]> getFloorNames() {
        return floorInfoDao.getFloorNames();
    }

    @Override
    public LiveData<InputStream> getMapImage(int floor) {
        if (mapImageLiveData == null) {

            LiveData<String> imagePathLive = Transformations.switchMap(
                    queryFloorNumber, (floorNumber) ->
                    floorInfoDao.getFloorImagePath(queryFloorNumber.getValue())v
            );

            mapImageLiveData = Transformations.map(imagePathLive, this::getImageStream);
        }

        queryFloorNumber.postValue(floor);
        return mapImageLiveData;
    }



    private InputStream getImageStream(String path){

        return null;
    }
}
