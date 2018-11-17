package bean.pwr.imskamieskiego.view_models;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public class MapViewModel extends ViewModel {

    List<MapPoint> trace;
    List<MapPoint> targets;
    MapPoint startPoint;

    public List<MapPoint> getTrace() {
        return trace;
    }

    public List<MapPoint> getTargets() {
        return targets;
    }

    public MapPoint getStartPoint() {
        return startPoint;
    }

    public void setTrace(List<MapPoint> trace) {
        this.trace = trace;
    }

    public void setTargets(List<MapPoint> targets) {
        this.targets = targets;
    }

    public void setStartPoint(MapPoint startPoint) {
        this.startPoint = startPoint;
    }
}
