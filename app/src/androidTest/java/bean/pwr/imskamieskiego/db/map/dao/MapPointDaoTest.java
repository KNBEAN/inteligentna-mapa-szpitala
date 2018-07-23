package bean.pwr.imskamieskiego.db.map.dao;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import bean.pwr.imskamieskiego.db.LocalDB;
import bean.pwr.imskamieskiego.db.map.entity.MapPointEntity;

import static org.junit.Assert.*;

public class MapPointDaoTest {

    private LocalDB database;
    private MapPointDao mapPointDao;


    @Before
    public void setUp() throws Exception {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        mapPointDao = database.getMapPointDao();
    }

    @After
    public void tearDown() throws Exception {
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
        assertEquals(data.get(1).getId(), point.getId());
    }

    @Test
    public void getMapPointByLocationID() {
        List<MapPointEntity> data = testData();
        mapPointDao.insertAllPoints(data);

        List<MapPointEntity> result = mapPointDao.getByLocationID(data.get(1).getLocationID());
        assertEquals(data.size(), result.size());
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