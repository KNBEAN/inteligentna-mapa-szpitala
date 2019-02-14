package bean.pwr.imskamieskiego.path_search;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.repository.IMapGraphRepository;
import bean.pwr.imskamieskiego.repository.MapGraphRepository;
import bean.pwr.imskamieskiego.data.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.data.map.dao.MapPointDao;
import bean.pwr.imskamieskiego.data.map.entity.EdgeEntity;
import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;
import bean.pwr.imskamieskiego.model.map.MapPoint;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class PathSearcherIntegrationTest {


    private LocalDB database;
    private MapPointDao mapPointDao;
    private EdgeDao edgeDao;
    private PathSearcher pathSearcher;
    private IMapGraphRepository repository;
    private List<MapPoint> pathSearchResult;
    private PathSearchAlgorithm algorithm;

    @NonNull
    private List<EdgeEntity> edges = Arrays.asList(
            new EdgeEntity(1, 1,2,5),
            new EdgeEntity(2, 1, 2, 5),
            new EdgeEntity(3, 1, 3, 3),
            new EdgeEntity(4, 2, 3, 6),
            new EdgeEntity(5, 3, 4, 1),
            new EdgeEntity(6, 5, 6, 1),
            new EdgeEntity(7, 6, 7, 4),
            new EdgeEntity(8, 4, 5, 1)
    );

    @NonNull
    private List<MapPointEntity> nodes = Arrays.asList(
            new MapPointEntity(1, 0, 1,1,-1, false),
            new MapPointEntity(1, 0, 1, 1, -1, false),
            new MapPointEntity(2, 0, 6, 1, -1, false),
            new MapPointEntity(3, 0, 1, 4, -1, false),
            new MapPointEntity(4, 0, 0, 4, -1, false),

            new MapPointEntity(5, 1, 0, 4, -1, false),
            new MapPointEntity(6, 1, 1, 4, -1, false),
            new MapPointEntity(7, 1, 4, 2, -1, false),
            //add orphaned node
            new MapPointEntity(8, 2, 0, 0, -1, false)
    );


    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                .build();

        mapPointDao = database.getMapPointDao();
        edgeDao = database.getEdgeDao();

        mapPointDao.insertAllPoints(nodes);
        edgeDao.insertAllEdges(edges);

        repository = new MapGraphRepository(database);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void callObserverWhenSearchEnded() {

        pathSearcher = new PathSearcher(InstrumentationRegistry.getContext());
        LiveData<List<MapPoint>> path = pathSearcher.getPath();
        path.observeForever(mapPoints -> pathSearchResult = mapPoints);


        MapPoint startPoint = repository.getPointByID(2);
        MapPoint endPoint = repository.getPointByID(7);
        List<MapPoint> expectedPath = Arrays.asList(
                startPoint,
                repository.getPointByID(3),
                repository.getPointByID(4),
                repository.getPointByID(5),
                repository.getPointByID(6),
                endPoint
        );

        algorithm = new DijkstraSearch(repository, startPoint, endPoint);
        algorithm.setPenalizationFactor(5);

        pathSearcher.startSearch(algorithm);

        //It looks a bit like an ugly hack.
        while (pathSearcher.isInProgress()){}

        assertEquals(expectedPath, pathSearchResult);

    }


    @Test
    public void searchPathToTargetList() {

        pathSearcher = new PathSearcher(InstrumentationRegistry.getContext());
        LiveData<List<MapPoint>> path = pathSearcher.getPath();
        path.observeForever(mapPoints -> pathSearchResult = mapPoints);

        MapPoint startPoint = repository.getPointByID(2);
        List<MapPoint> endPoints = Arrays.asList(
                repository.getPointByID(7),
                repository.getPointByID(5));

        List<MapPoint> expectedPath = Arrays.asList(
                startPoint,
                repository.getPointByID(3),
                repository.getPointByID(4),
                repository.getPointByID(5)
        );


        algorithm = new DijkstraSearch(repository, startPoint, endPoints);

        pathSearcher.startSearch(algorithm);

        //It looks a bit like an ugly hack.
        while (pathSearcher.isInProgress()){}

        assertEquals(expectedPath, pathSearchResult);
    }

}