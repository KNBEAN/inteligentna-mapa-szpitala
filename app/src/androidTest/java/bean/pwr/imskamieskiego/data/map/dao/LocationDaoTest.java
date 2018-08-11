package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class LocationDaoTest {

    private LocalDB database;
    private LocationDao locationDao;


    @Before
    public void setUp() throws Exception {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        locationDao = database.getLocationDao();
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void getLocationByID() {
        List<LocationEntity> locations = Arrays.asList(
                new LocationEntity(
                        1,
                        "Place 1",
                        "Very nice place."));

        locationDao.insertAllPoints(locations);

        LocationEntity result = locationDao.getByID(locations.get(0).getId());

        assertEquals(locations.get(0), result);
    }

    @Test
    public void getLocationWithNullDescription() {
        List<LocationEntity> locations = Arrays.asList(
                new LocationEntity(
                        1,
                        "Place 1",
                        null));

        locationDao.insertAllPoints(locations);

        LocationEntity result = locationDao.getByID(locations.get(0).getId());

        assertEquals(locations.get(0), result);
    }

    @Test
    public void getLocationByNonExistID() {
        List<LocationEntity> locations = Arrays.asList(
                new LocationEntity(
                        1,
                        "Place 1",
                        null));

        locationDao.insertAllPoints(locations);

        LocationEntity result = locationDao.getByID(2);

        assertNull(result);
    }
}