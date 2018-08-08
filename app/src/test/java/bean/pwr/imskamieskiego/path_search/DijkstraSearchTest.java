package bean.pwr.imskamieskiego.path_search;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.MapRepository;

import static org.junit.Assert.*;

public class DijkstraSearchTest {

    private DijkstraSearch dijkstraSearch;
    private MapRepository mapRepository;

    @Before
    public void setUp() {
        mapRepository = new StaubGraphMapRepository();
    }

    @Test
    public void searchPathToReachableTarget() {
        MapPoint startPoint = mapRepository.getPointByID(2);
        MapPoint endPoint = mapRepository.getPointByID(7);
        List<MapPoint> expectedTrace = Arrays.asList(
                startPoint,
                mapRepository.getPointByID(3),
                mapRepository.getPointByID(4),
                mapRepository.getPointByID(5),
                mapRepository.getPointByID(6),
                endPoint
        );
        
        
        dijkstraSearch = new DijkstraSearch(mapRepository, startPoint, endPoint);
        dijkstraSearch.startSearch();
        List<MapPoint> trace = dijkstraSearch.getPatch();
        
        assertEquals(expectedTrace, trace);
    }

    @Test
    public void searchPathToUnReachableTarget() {
        MapPoint startPoint = mapRepository.getPointByID(2);
        MapPoint endPoint = mapRepository.getPointByID(8);

        dijkstraSearch = new DijkstraSearch(mapRepository, startPoint, endPoint);
        dijkstraSearch.startSearch();
        List<MapPoint> trace = dijkstraSearch.getPatch();

        assertTrue(trace.isEmpty());
    }

    @Test
    public void startFromOrphanedPoint() {
        MapPoint startPoint = mapRepository.getPointByID(8);
        MapPoint endPoint = mapRepository.getPointByID(2);

        dijkstraSearch = new DijkstraSearch(mapRepository, startPoint, endPoint);
        dijkstraSearch.startSearch();
        List<MapPoint> trace = dijkstraSearch.getPatch();

        assertTrue(trace.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void startAndEndPointAreTheSame() {
        MapPoint startPoint = mapRepository.getPointByID(2);
        MapPoint endPoint = mapRepository.getPointByID(2);

        dijkstraSearch = new DijkstraSearch(mapRepository, startPoint, endPoint);
    }
}