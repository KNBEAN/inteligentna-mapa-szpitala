package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.entity.EdgeEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EdgeDaoTest {

    private LocalDB database;
    private EdgeDao edgeDao;


    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        edgeDao = database.getEdgeDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void getEdgesListForOnePoint() {
        List<EdgeEntity> edges = Arrays.asList(
                new EdgeEntity(1, 1, 2, 1),
                new EdgeEntity(2, 1, 3, 1),
                new EdgeEntity(5, 3, 1, 1),
                new EdgeEntity(6, 3, 2, 1),
                new EdgeEntity(7, 3, 4, 1),
                new EdgeEntity(8, 4, 3, 1));

        List<EdgeEntity> expectedEdges = Arrays.asList(
                new EdgeEntity(3, 2, 1, 1),
                new EdgeEntity(4, 2, 3, 1));

        edgeDao.insertAllEdges(edges);
        edgeDao.insertAllEdges(expectedEdges);

        List<EdgeEntity> result = edgeDao.getOutgoingEdges(2);
        assertEquals(expectedEdges, result);
    }

    @Test
    public void getEdgesListForListOfPoints() {
        List<EdgeEntity> edges = Arrays.asList(
                new EdgeEntity(1, 1, 2, 1),
                new EdgeEntity(2, 1, 3, 1),
                new EdgeEntity(3, 2, 1, 1),
                new EdgeEntity(4, 2, 3, 1),
                new EdgeEntity(5, 3, 1, 1),
                new EdgeEntity(6, 3, 2, 1),
                new EdgeEntity(7, 3, 4, 1),
                new EdgeEntity(8, 4, 3, 1));

        List<Integer> fromIDs = new ArrayList<>();
        fromIDs.add(1);
        fromIDs.add(2);
        fromIDs.add(3);
        fromIDs.add(4);

        edgeDao.insertAllEdges(edges);

        List<EdgeEntity> result = edgeDao.getOutgoingEdges(fromIDs);

        assertEquals(edges, result);
    }

    @Test
    public void getEdgesListWhenListOfPointsIsEmpty() {

        List<EdgeEntity> result = edgeDao.getOutgoingEdges(new ArrayList<Integer>());
        assertTrue(result.isEmpty());
    }
}