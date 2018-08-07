package bean.pwr.imskamieskiego.model.map;

/**
 * This interface represents a point on the map (so-called nodes). The nodes are part of the graph.
 * If more nodes on the toolbar_menu, navigation on the toolbar_menu will be smoother.
 */
public interface MapPoint {
    /**
     * Returns unique ID of point of the map
     * @return ID
     */
    int getId();

    /**
     * Returns floor number. Number of floors is count from 0. It has to be non negative value.
     * @return floor number
     */
    int getFloor();

    /**
     * X coordinate of point on map.
     * @return X coordinate
     */
    int getX();

    /**
     * Y coordinate of point on map.
     * @return Y coordinate
     */
    int getY();

    /**
     * ID of the place associated with the given point on the map.
     * @return ID of location
     */
    int getLocationID();
}
