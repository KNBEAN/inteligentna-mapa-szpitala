package bean.pwr.imskamieskiego.GUI;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bean.pwr.imskamieskiego.MapDrawer.MapDrawer;
import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.MapPoint;


public class MapFragment extends Fragment {

    private MapDrawer mapDrawer;

    private List<MapPoint> targetPoints;
    private MapPoint startPoint;
    private List<MapPoint> trace;

    private OnMapInteractionListener listener;

    public MapFragment() {}

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapDrawer = view.findViewById(R.id.mapdrawer);
        mapDrawer.setOnLongPressListener(listener::onMapClick);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapInteractionListener) {
            listener = (OnMapInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void showFloor(int floorNumber, Bitmap mapImage){
        if (mapDrawer != null){
            mapDrawer.showFloor(floorNumber, mapImage);
        }
    }

    public void setStartPoint(MapPoint startPoint){
        clearStartPoint();
        this.startPoint = startPoint;
        if (mapDrawer != null){
            mapDrawer.addMapPoint(startPoint, 1);
        }
    }

    public void setTargetPoints(List<MapPoint> targetPoints){
        clearTargetPoints();
        this.targetPoints = targetPoints;
        for (MapPoint mapPoint:targetPoints) {
            mapDrawer.addMapPoint(mapPoint, 0);
        }
    }

    public void setTrace(List<MapPoint>trace){
        clearTrace();
        this.trace = trace;
        mapDrawer.setTrace(trace);
    }

    public boolean isStartPointSet(){
        return startPoint != null;
    }

    public boolean isTargetPointsSet(){
        return targetPoints != null;
    }

    public boolean isTraceSet(){
        return trace != null;
    }

    public boolean clearTrace(){
        if (trace == null) return false;
        if (mapDrawer != null){
            mapDrawer.removeTrace();
        }
        trace = null;
        return true;
    }

    public boolean clearStartPoint(){
        if (startPoint == null) return false;
        if (mapDrawer != null){
            mapDrawer.removeMapPoint(startPoint);
        }
        startPoint = null;
        return true;
    }

    public boolean clearTargetPoints(){
        if(targetPoints == null) return false;
        if (mapDrawer != null){
            for (MapPoint mapPoint:targetPoints) {
                mapDrawer.removeMapPoint(mapPoint);
            }
            targetPoints = null;
        }

        return true;
    }

    public interface OnMapInteractionListener {
        void onMapClick(MapPoint clickPoint);
    }
}
