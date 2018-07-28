package bean.pwr.imskamieskiego.data;

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

        List<MapPoint> result = new ArrayList<>();
        result.addAll(mapPointDao.getByID(id));
        return result;
    }

    @Override
    public MapPoint getNearestPoint(int x, int y, int floor) {
        return mapPointDao.getNearest(x, y, floor);
    }

    @Override
    public List<MapPoint> getPointsByLocationID(int id) {
        List<MapPoint> result = new ArrayList<>();
        result.addAll(mapPointDao.getByLocationID(id));
        return result;
    }

    @Override
    public Location getLocationByID(int id) {
        return locationDao.getByID(id);
    }

    @Override
    public List<Edge> getOutgoingEdges(int pointID) {
        List<Edge> result = new ArrayList<>();
        result.addAll(edgeDao.getOngoingEdges(pointID));
        return result;
    }

    @Override
    public Map<Integer, List<Edge>> getOutgoingEdges(@NonNull List<Integer> pointID) {
        List<Edge> edges = new ArrayList<>();
        edges.addAll(edgeDao.getOngoingEdges(pointID));

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
