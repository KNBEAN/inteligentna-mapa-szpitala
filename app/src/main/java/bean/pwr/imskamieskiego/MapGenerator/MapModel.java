package bean.pwr.imskamieskiego.MapGenerator;

import android.graphics.Bitmap;

public interface MapModel {

    int getFloorCount();

    Bitmap getFloorMap(int floor);

    TackObject getPointImage();

    TackObject getPathPointImage();




}
