package bean.pwr.imskamieskiego.repository;

import android.graphics.Bitmap;

public interface MapImagesRepository {


    int getFloorCount();

    String[] getFloorNames();

    Bitmap getMapImage(int floor);

}
