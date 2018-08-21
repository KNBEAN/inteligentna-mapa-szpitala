package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.entity.FloorInfoEntity;

import static org.junit.Assert.*;

public class FloorInfoDaoTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private LocalDB database;
    private FloorInfoDao floorInfoDao;

    @Mock
    private Observer observer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        floorInfoDao = database.getFloorInfoDao();
        List<FloorInfoEntity> floorsList = Arrays.asList(
                new FloorInfoEntity(0, "ground floor", "mapFile1"),
                new FloorInfoEntity(1, "1st floor", "mapFile2"),
                new FloorInfoEntity(2, "2nd floor", "mapFile3")
        );
        floorInfoDao.insertAllPoints(floorsList);
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test(expected = SQLiteConstraintException.class)
    public void addElementWithNullName() {
        List<FloorInfoEntity> floorList = Collections.singletonList(
                new FloorInfoEntity(4, null, "mapFile4")
        );
        floorInfoDao.insertAllPoints(floorList);
    }

    @Test
    public void getListOfFloorNames() {
        String[] expectedNames = {"ground floor", "1st floor", "2nd floor"};

        LiveData<String[]> floorNames = floorInfoDao.getFloorNames();
        floorNames.observeForever(observer);


        assertArrayEquals(expectedNames, floorNames.getValue());
    }

    @Test
    public void getMapResourcePath() {
        String expectedNames = "mapFile2";

        LiveData<String> floorResourceName = floorInfoDao.getFloorImagePath(1);
        floorResourceName.observeForever(observer);


        assertEquals(expectedNames, floorResourceName.getValue());

    }

    @Test
    public void getMapResourcePathFromNonExistFloor() {
        LiveData<String> floorResourceName = floorInfoDao.getFloorImagePath(-1);
        floorResourceName.observeForever(observer);

        assertNull(floorResourceName.getValue());
    }
}