package bean.pwr.imskamieskiego.repository;

import android.arch.lifecycle.LiveData;
import android.graphics.Bitmap;

import java.io.InputStream;

public interface IFloorDataRepository {

    LiveData<String[]> getFloorNames();

    LiveData<InputStream> getMapImage(int floor);

}
