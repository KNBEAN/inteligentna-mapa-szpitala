package bean.pwr.imskamieskiego.MapGenerator;

import android.graphics.Bitmap;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public interface MapModel {

    int getFloorCount();
    Bitmap getFloorMap(int floor);
    MapPoint getPointImage();
    MapPoint getPathPointImage();




}
