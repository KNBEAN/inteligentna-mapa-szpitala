package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.TestObserver;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;
import bean.pwr.imskamieskiego.data.map.entity.QuickAccessEntity;
import bean.pwr.imskamieskiego.model.map.MapPoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapPointDaoTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
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
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        database.close();
    }


    private List<MapPointEntity> getTestData(){
        int records = 4;
        List<MapPointEntity> pointsList = new ArrayList<>();
        for (int i = 0; i < records; i++){
            MapPointEntity point = new MapPointEntity(1+i, 0, i+2, i+1, i%4);
            pointsList.add(point);
        }
        return pointsList;
    }

    @Test
    public void getMapPointByID() {
        List<MapPointEntity> data = getTestData();
        mapPointDao.insertAllPoints(data);

        MapPointEntity point = mapPointDao.getByID(data.get(1).getId());
        assertEquals(data.get(1), point);
    }

    @Test
    public void getListOfMapPointByIDs() {
        List<MapPointEntity> data = getTestData();
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
        List<MapPointEntity> data = new ArrayList<>();
        for (MapPointEntity mapPoint:getTestData()) {
            if (mapPoint.getLocationID() == 0)
                data.add(mapPoint);
        }
        mapPointDao.insertAllPoints(data);

        List<MapPointEntity> result = mapPointDao.getByLocationID(0);
        assertEquals(data, result);
    }

    @Test
    public void getNearestToPoint() {
        int nearestID = 2;
        List<MapPointEntity> data = getTestData();
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
        List<MapPointEntity> data = getTestData();
        mapPointDao.insertAllPoints(data);

        MapPointEntity point = mapPointDao.getNearest(4, 1, 5);
        assertNull(point);
    }

    @Test
    public void getByNotExistingLocationID() {
        List<MapPointEntity> result = mapPointDao.getByLocationID(5);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getPointsForQuickAccessCategory() {

        List<LocationEntity> locationList = Arrays.asList(
                new LocationEntity(1, "A",null),
                new LocationEntity(2, "B",null),
                new LocationEntity(3, "C",null),
                new LocationEntity(4, "D",null)
        );
        List<QuickAccessEntity> quickAccessList = Arrays.asList(
                new QuickAccessEntity(1, 1, 1),
                new QuickAccessEntity(2, 2, 2),
                new QuickAccessEntity(3, 3, 2),
                new QuickAccessEntity(4, 3, 3)
        );


        List<MapPointEntity> pointsForQAType3 = new ArrayList<>();
        List<MapPointEntity> pointsForQAType2 = new ArrayList<>();

        for(MapPointEntity mapPoint:getTestData()){
            if (mapPoint.getLocationID() == 3){
                pointsForQAType3.add(mapPoint);
            }
            if (mapPoint.getLocationID() == 2 || mapPoint.getLocationID() == 3){
                pointsForQAType2.add(mapPoint);
            }
        }

        LocationDao locationDao = database.getLocationDao();
        locationDao.insertAllLocations(locationList);
        mapPointDao.insertAllQuickAccess(quickAccessList);

        TestObserver<List<MapPointEntity>> observer = new TestObserver<>();
        mapPointDao.getQuickAccessPoints(2).observeForever(observer);
        mapPointDao.getQuickAccessPoints(3).observeForever(observer);
        mapPointDao.getQuickAccessPoints(4).observeForever(observer);

        assertTrue(observer.observedValues.get(0).containsAll(pointsForQAType2));
        assertTrue(observer.observedValues.get(1).containsAll(pointsForQAType3));
        assertTrue(observer.observedValues.get(2).isEmpty());
    }
}