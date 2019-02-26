/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI;

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
import bean.pwr.imskamieskiego.MapDrawer.MapProvider;
import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.FloorDataRepository;
import bean.pwr.imskamieskiego.view_models.MapViewModel;

/**
 * Fragment which contains view of map
 */
public class MapFragment extends Fragment {

    private final static String TAG = "MapFragment";
    
    private MapDrawer mapDrawer;

    private MapProvider mapProvider;

    private MapViewModel viewModel;

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
        FloorDataRepository floorDataRepository = new FloorDataRepository(LocalDB.getDatabase(getContext()),getContext());
        mapProvider = new MapProvider(getContext(), floorDataRepository.getMapImage(0));
        mapDrawer.setMapProvider(mapProvider);
        restoreMap();
        return view;
    }

    private void restoreMap(){
        Log.d(TAG, "restoreMap: map restore");
        List<MapPoint> route = viewModel.getRoute();
        if (route != null){
            mapDrawer.setTrace(route);
            mapDrawer.removeAllMapPoints();
            mapDrawer.addMapPoint(route.get(0), 2);
            mapDrawer.addMapPoint(route.get(route.size()-1), 1);
            return;
        }

        List<MapPoint> targets = viewModel.getTargets();
        if (targets != null){
            for(MapPoint target:targets){
                mapDrawer.addMapPoint(target, 1);
            }
        }

        MapPoint startPoint = viewModel.getStartPoint();
        if (startPoint != null){
            mapDrawer.addMapPoint(startPoint, 2);
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

    /**
     * Set floor to show
     * @param floorNumber the floor number that will be shown
     * @param mapImage the image of floor
     */
    public void showFloor(int floorNumber, Bitmap mapImage){
        if (mapDrawer != null){
            mapDrawer.showFloor(floorNumber, mapImage);
        }
    }

    /**
     * Set start point to show on map
     * @param startPoint start point
     */
    public void setStartPoint(MapPoint startPoint){
        clearStartPoint();
        viewModel.setStartPoint(startPoint);
        if (mapDrawer != null){
            mapDrawer.addMapPoint(startPoint, 2);
        }
    }

    /**
     * Set list of target points to show on map
     * @param targetPoints target points
     */
    public void setTargetPoints(List<MapPoint> targetPoints){
        clearTargetPoints();
        viewModel.setTargets(targetPoints);
        for (MapPoint mapPoint:targetPoints) {
            mapDrawer.addMapPoint(mapPoint, 1);
        }
    }

    /**
     * Set route to show on map
     * @param route route to show
     */
    public void setRoute(List<MapPoint>route){
        clearRoute();
        if (mapDrawer !=null && !route.isEmpty()) {
            mapDrawer.setTrace(route);
            mapDrawer.removeAllMapPoints();
            mapDrawer.addMapPoint(route.get(0), 2);
            mapDrawer.addMapPoint(route.get(route.size()-1), 1);
        }
        viewModel.setRoute(route);
    }

    /**
     * Returns true, when start point is set. Otherwise returns false
     * @return
     */
    public boolean isStartPointSet(){
        return viewModel.getStartPoint() != null;
    }

    /**
     * Returns true, when target points are set. Otherwise returns false
     * @return
     */
    public boolean isTargetPointsSet(){
        return viewModel.getTargets() != null;
    }

    /**
     * Return true, when route is set. Otherwise return false
     * @return
     */
    public boolean isRouteSet(){
        return viewModel.getRoute() != null;
    }

    /**
     * Removes route from map
     * @return true if route was set
     */
    public boolean clearRoute(){
        if (viewModel.getRoute() == null) return false;
        if (mapDrawer != null){
            mapDrawer.removeTrace();
        }
        viewModel.setRoute(null);
        restoreMap();
        return true;
    }

    /**
     * Removes start point from map
     * @return true if start point was set
     */
    public boolean clearStartPoint(){
        if (viewModel.getStartPoint() == null) return false;
        if (mapDrawer != null){
            mapDrawer.removeMapPoint(viewModel.getStartPoint());
        }
        viewModel.setStartPoint(null);
        restoreMap();
        return true;
    }

    /**
     * Removes target points from map
     * @return true if target points was set
     */
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
        /**
         * Called after clicking the map
         * @param clickPoint the point where the map was clicked
         */
        void onMapClick(MapPoint clickPoint);
    }
}
