package bean.pwr.imskamieskiego.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.data.map.dao.LocationDao;
import bean.pwr.imskamieskiego.data.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.model.map.Edge;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;

/**
 * This class is implementation of MapRepository. It should be used to get access to data related
 * to map and navigation graph. Methods of this class must be call from thread other
 * than main thread.
 */
public class MapRepositoryImpl implements MapRepository {

    private MapPointDao mapPointDao;
    private LocationDao locationDao;
    private EdgeDao edgeDao;

    public MapRepositoryImpl(LocalDB dataBase) {
        mapPointDao = dataBase.getMapPointDao();
        locationDao = dataBase.getLocationDao();
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
    public MapPoint getNearestPoint(int x, int y, int floor) {
        return mapPointDao.getNearest(x, y, floor);
    }

    @Override
    public List<MapPoint> getPointsByLocationID(int id) {
        return new ArrayList<MapPoint>(mapPointDao.getByLocationID(id));
    }

    @Override
    public Location getLocationByID(int id) {
        return locationDao.getByID(id);
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
