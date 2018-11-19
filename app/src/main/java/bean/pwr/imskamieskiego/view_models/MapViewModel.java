package bean.pwr.imskamieskiego.view_models;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

/**
 * View model which contain actual selected start point, targets and trace. This view model is
 * created for preserving data for MapDrawer between orientation changes etc.
 */
public class MapViewModel extends ViewModel {

    List<MapPoint> trace;
    List<MapPoint> targets;
    MapPoint startPoint;

    /**
     * Returns trace as list of MapPoints.
     * @return list of MapPoints. If trace isn't set, returns null.
     */
    public List<MapPoint> getTrace() {
        return trace;
    }

    /**
     * Returns list of targets points.
     * @return list of targets point. If there are no targets, returns null.
     */
    public List<MapPoint> getTargets() {
        return targets;
    }

    /**
     * Returns current start point
     * @return start point. If start point is unset, returns null
     */
    public MapPoint getStartPoint() {
        return startPoint;
    }

    /**
     * Set new trace
     * @param trace list of MapPoints
     */
    public void setTrace(List<MapPoint> trace) {
        this.trace = trace;
    }

    /**
     * Set targets list
     * @param targets list of targets
     */
    public void setTargets(List<MapPoint> targets) {
        this.targets = targets;
    }

    /**
     * Set start point
     * @param startPoint start point
     */
    public void setStartPoint(MapPoint startPoint) {
        this.startPoint = startPoint;
    }
}
