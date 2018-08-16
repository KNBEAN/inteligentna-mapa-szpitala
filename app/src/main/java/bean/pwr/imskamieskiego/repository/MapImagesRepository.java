package bean.pwr.imskamieskiego.repository;

import android.arch.lifecycle.LiveData;
import android.graphics.Bitmap;

public interface MapImagesRepository {

    LiveData<String[]> getFloorNames();

    LiveData<Bitmap> getMapImage(int floor);

}
