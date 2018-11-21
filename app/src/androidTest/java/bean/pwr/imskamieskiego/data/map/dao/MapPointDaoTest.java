package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;
import bean.pwr.imskamieskiego.model.map.MapPoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapPointDaoTest {

    private LocalDB database;
    private MapPointDao mapPointDao;


    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        mapPointDao = database.getMapPointDao();
    }

    @After
    public void tearDown() {
        database.close();
    }


    public List<MapPointEntity> testData(){
        int records = 4;
        List<MapPointEntity> pointsList = new ArrayList<>();
        for (int i = 0; i < records; i++){
            MapPointEntity point = new MapPointEntity(1+i, 0, i+2, i+1, 1);
            pointsList.add(point);
        }
        return pointsList;
    }

    @Test
    public void getMapPointByID() {
        List<MapPointEntity> data = testData();
        mapPointDao.insertAllPoints(data);

        MapPointEntity point = mapPointDao.getByID(data.get(1).getId());
        assertEquals(data.get(1), point);
    }

    @Test
    public void getListOfMapPointByIDs() {
        List<MapPointEntity> data = testData();
        mapPointDao.insertAllPoints(data);

        List<Integer> pointIDs = new ArrayList<>();
        for (MapPoint point:data) {
            pointIDs.add(point.getId());
        }

        List<MapPointEntity> point = mapPointDao.getByID(pointIDs);
        assertEquals(data, point);
    }

    @Test
    public void getMapPointByLocationID() {
        List<MapPointEntity> data = testData();
        mapPointDao.insertAllPoints(data);

        List<MapPointEntity> result = mapPointDao.getByLocationID(data.get(1).getLocationID());
        assertEquals(data, result);
    }

    @Test
    public void getNearestToPoint() {
        int nearestID = 2;
        List<MapPointEntity> data = testData();
        mapPointDao.insertAllPoints(data);

        MapPointEntity point = mapPointDao.getNearest(4, 1, 0);

        assertEquals(nearestID, point.getId());
    }

    @Test
    public void getByNotExistingID() {
        MapPointEntity point = mapPointDao.getByID(-1);
        assertNull(point);
    }

    @Test
    public void getNearestFromNotExistingFloor() {
        List<MapPointEntity> data = testData();
        mapPointDao.insertAllPoints(data);

        MapPointEntity point = mapPointDao.getNearest(4, 1, 5);
        assertNull(point);
    }

    @Test
    public void getByNotExistingLocationID() {
        List<MapPointEntity> result = mapPointDao.getByLocationID(5);
        assertTrue(result.isEmpty());
    }
}