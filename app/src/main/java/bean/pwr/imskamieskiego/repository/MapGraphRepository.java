package bean.pwr.imskamieskiego.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.data.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.model.map.Edge;
import bean.pwr.imskamieskiego.model.map.MapPoint;

/**
 * This class is implementation of IMapGraphRepository. It should be used to get access to data
 * related to graph. Methods of this class must be call from thread other than main thread.
 */
public class MapGraphRepository implements IMapGraphRepository {

    private MapPointDao mapPointDao;
    private EdgeDao edgeDao;

    public MapGraphRepository(LocalDB dataBase) {
        mapPointDao = dataBase.getMapPointDao();
        edgeDao = dataBase.getEdgeDao();
    }


    @Override
    public MapPoint getPointByID(int id) {
        return mapPointDao.getByID(id);
    }

    @Override
    public List<MapPoint> getPointByID(@NonNull List<Integer> id) {
        return new ArrayList<MapPoint>(mapPointDao.getByID(id));
    }

    @Override
    public List<Edge> getOutgoingEdges(int pointID) {
        return  new ArrayList<Edge>(edgeDao.getOutgoingEdges(pointID));
    }

    @Override
    public Map<Integer, List<Edge>> getOutgoingEdges(@NonNull List<Integer> pointID) {
        if (pointID == null) throw new NullPointerException();

        List<Edge> edges = new ArrayList<Edge>(edgeDao.getOutgoingEdges(pointID));

        Map<Integer, List<Edge>> map = new Hashtable<>();
        for (Edge edge:edges) {
            if (map.containsKey(edge.getFrom())){
                map.get(edge.getFrom()).add(edge);
            }else {
                ArrayList<Edge> edgeList = new ArrayList<>();
                edgeList.add(edge);
                map.put(edge.getFrom(), edgeList);
            }
        }

        return map;
    }

}
