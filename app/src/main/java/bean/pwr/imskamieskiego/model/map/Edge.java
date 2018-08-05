package bean.pwr.imskamieskiego.model.map;

/**
 * This interface represents directional edge on the graph. Edge is a object that make connection
 * between nodes.
 */
public interface Edge {

    /**
     * ID of start point of edge
     * @return
     */
    int getFrom();

    /**
     * ID of end point of edge
     * @return
     */
    int getTo();

    /**
     * Length of edge.
     * @return
     */
    int getLength();

}
