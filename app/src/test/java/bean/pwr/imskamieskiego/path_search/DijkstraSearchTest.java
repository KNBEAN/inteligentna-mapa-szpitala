package bean.pwr.imskamieskiego.path_search;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.IMapGraphRepository;

import static org.junit.Assert.*;

public class DijkstraSearchTest {

    private DijkstraSearch dijkstraSearch;
    private IMapGraphRepository graphRepository;

    @Before
    public void setUp() {
        graphRepository = new StubGraphMapRepository();
    }

    @Test
    public void searchPathToReachableTarget() {
        MapPoint startPoint = graphRepository.getPointByID(2);
        MapPoint endPoint = graphRepository.getPointByID(7);
        List<MapPoint> expectedTrace = Arrays.asList(
                startPoint,
                graphRepository.getPointByID(3),
                graphRepository.getPointByID(4),
                graphRepository.getPointByID(5),
                graphRepository.getPointByID(6),
                endPoint
        );
        
        
        dijkstraSearch = new DijkstraSearch(graphRepository, startPoint, endPoint);
        dijkstraSearch.startSearch();
        List<MapPoint> trace = dijkstraSearch.getPath();
        
        assertEquals(expectedTrace, trace);
    }

    @Test
    public void searchPathToUnReachableTarget() {
        MapPoint startPoint = graphRepository.getPointByID(2);
        MapPoint endPoint = graphRepository.getPointByID(8);

        dijkstraSearch = new DijkstraSearch(graphRepository, startPoint, endPoint);
        dijkstraSearch.startSearch();
        List<MapPoint> trace = dijkstraSearch.getPath();

        assertTrue(trace.isEmpty());
    }

    @Test
    public void startFromOrphanedPoint() {
        MapPoint startPoint = graphRepository.getPointByID(8);
        MapPoint endPoint = graphRepository.getPointByID(2);

        dijkstraSearch = new DijkstraSearch(graphRepository, startPoint, endPoint);
        dijkstraSearch.startSearch();
        List<MapPoint> trace = dijkstraSearch.getPath();

        assertTrue(trace.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void startAndEndPointAreTheSame() {
        MapPoint startPoint = graphRepository.getPointByID(2);
        MapPoint endPoint = graphRepository.getPointByID(2);

        dijkstraSearch = new DijkstraSearch(graphRepository, startPoint, endPoint);
    }


    @Test
    public void searchPathToTargetList() {
        MapPoint startPoint = graphRepository.getPointByID(2);
        List<MapPoint> endPoints = Arrays.asList(
                graphRepository.getPointByID(7),
                graphRepository.getPointByID(5));
        
        List<MapPoint> expectedTrace = Arrays.asList(
                startPoint,
                graphRepository.getPointByID(3),
                graphRepository.getPointByID(4),
                graphRepository.getPointByID(5)
        );


        dijkstraSearch = new DijkstraSearch(graphRepository, startPoint, endPoints);
        dijkstraSearch.startSearch();
        List<MapPoint> trace = dijkstraSearch.getPath();

        assertEquals(expectedTrace, trace);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startPointOnTargetList() {
        MapPoint startPoint = graphRepository.getPointByID(2);
        List<MapPoint> endPoints = Arrays.asList(
                graphRepository.getPointByID(7),
                graphRepository.getPointByID(2));

        dijkstraSearch = new DijkstraSearch(graphRepository, startPoint, endPoints);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyListAsTargetList() {
        MapPoint startPoint = graphRepository.getPointByID(2);
        List<MapPoint> endPoints = new ArrayList<>();

        dijkstraSearch = new DijkstraSearch(graphRepository, startPoint, endPoints);
    }

}