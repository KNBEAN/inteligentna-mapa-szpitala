package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import java.io.InputStream;


import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.repository.FloorDataRepository;

public class FloorViewModel extends AndroidViewModel {
    private FloorDataRepository floorDataRepository;
    private int currentFloor=0;

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    private MutableLiveData<Integer> selectedFloor = new MutableLiveData<>();

    public LiveData<String[]> getFloorList(){
       return floorDataRepository.getFloorNames();
    }

    private LiveData<InputStream> mapImageStream = Transformations.switchMap(selectedFloor,
            floor -> floorDataRepository.getMapImage(floor));

    private LiveData<Bitmap> floorBitmap = Transformations.map(mapImageStream, stream -> {
        if (stream != null) {
            return BitmapFactory.decodeStream(stream);
        }
        return null;

    });

    public LiveData<Bitmap> getFloorBitmap() {
        return floorBitmap;
    }


    public FloorViewModel(@NonNull Application application) {
        super(application);
        LocalDB localDB = LocalDB.getDatabase(application.getApplicationContext());
        floorDataRepository = new FloorDataRepository(localDB, application.getApplicationContext());
    }

    public void setSelectedFloor(int floor) {
        selectedFloor.postValue(floor);

    }
}
