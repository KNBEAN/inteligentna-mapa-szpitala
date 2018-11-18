package bean.pwr.imskamieskiego.GUI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bean.pwr.imskamieskiego.MapDrawer.MapDrawer;
import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.view_models.MapViewModel;


public class MapFragment extends Fragment {

    private final static String TAG = "MapFragment";
    
    private MapDrawer mapDrawer;

    private MapViewModel viewModel;
    private LiveData<List<MapPoint>> trace;
    private LiveData<List<MapPoint>> targets;
    private LiveData<MapPoint> startPoint;

    private OnMapInteractionListener listener;

    public MapFragment() {}

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapDrawer = view.findViewById(R.id.mapdrawer);
        mapDrawer.setOnLongPressListener(listener::onMapClick);
        restoreMap();
        return view;
    }

    void restoreMap(){
        Log.d(TAG, "restoreMap: map restore");
        List<MapPoint> trace = viewModel.getTrace();
        if (trace != null){
            mapDrawer.setTrace(trace);
            mapDrawer.removeAllMapPoints();
            mapDrawer.addMapPoint(trace.get(0), 1);
            mapDrawer.addMapPoint(trace.get(trace.size()-1), 0);
            return;
        }

        List<MapPoint> targets = viewModel.getTargets();
        if (targets != null){
            for(MapPoint target:targets){
                mapDrawer.addMapPoint(target, 0);
            }
        }

        MapPoint startPoint = viewModel.getStartPoint();
        if (startPoint != null){
            mapDrawer.addMapPoint(startPoint, 1);
        }
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
        viewModel.setStartPoint(startPoint);
        if (mapDrawer != null){
            mapDrawer.addMapPoint(startPoint, 1);
        }
    }

    public void setTargetPoints(List<MapPoint> targetPoints){
        clearTargetPoints();
        viewModel.setTargets(targetPoints);
        for (MapPoint mapPoint:targetPoints) {
            mapDrawer.addMapPoint(mapPoint, 0);
        }
    }

    public void setTrace(List<MapPoint>trace){
        clearTrace();
        if (mapDrawer !=null && !trace.isEmpty()) {
            mapDrawer.setTrace(trace);
            mapDrawer.removeAllMapPoints();
            mapDrawer.addMapPoint(trace.get(0), 1);
            mapDrawer.addMapPoint(trace.get(trace.size()-1), 0);
        }
        viewModel.setTrace(trace);
    }

    public boolean isStartPointSet(){
        return viewModel.getStartPoint() != null;
    }

    public boolean isTargetPointsSet(){
        return viewModel.getTargets() != null;
    }

    public boolean isTraceSet(){
        return viewModel.getTrace() != null;
    }

    public boolean clearTrace(){
        if (viewModel.getTrace() == null) return false;
        if (mapDrawer != null){
            mapDrawer.removeTrace();
        }
        viewModel.setTrace(null);
        restoreMap();
        return true;
    }

    public boolean clearStartPoint(){
        if (viewModel.getStartPoint() == null) return false;
        if (mapDrawer != null){
            mapDrawer.removeMapPoint(viewModel.getStartPoint());
        }
        viewModel.setStartPoint(null);
        restoreMap();
        return true;
    }

    public boolean clearTargetPoints(){
        if(viewModel.getTargets() == null) return false;
        if (mapDrawer != null){
            for (MapPoint mapPoint:viewModel.getTargets()) {
                mapDrawer.removeMapPoint(mapPoint);
            }
            viewModel.setTargets(null);
        }
        restoreMap();
        return true;
    }

    public interface OnMapInteractionListener {
        void onMapClick(MapPoint clickPoint);
    }
}
