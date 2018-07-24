package bean.pwr.imskamieskiego.db.map.dao;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import bean.pwr.imskamieskiego.db.LocalDB;
import bean.pwr.imskamieskiego.db.map.entity.LocationEntity;

import static org.junit.Assert.*;

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
        List<LocationEntity> locations = new ArrayList<>();
        locations.add(new LocationEntity(
                1,
                "Place 1",
                "Very nice place."));

        locationDao.insertAllPoints(locations);

        LocationEntity result = locationDao.getByID(locations.get(0).getId());

        assertEquals(locations.get(0), result);
    }

    @Test
    @Ignore
    //Ignore this test, because this feature will not by implement in current sprint (25.07.18)
    public void getLocationByName() {
        //List of locations, because searching by name doesn't have to use explicit name.
        List<LocationEntity> similarLocations = new ArrayList<>();
        similarLocations.add(new LocationEntity(1, "Koński ogon",null));
        similarLocations.add(new LocationEntity(2, "kończyna",null));
        similarLocations.add(new LocationEntity(3, "OkoŃ",null));

        List<LocationEntity> nonSimilarLocations = new ArrayList<>();
        nonSimilarLocations.add(new LocationEntity(4, "pietruszka",null));
        nonSimilarLocations.add(new LocationEntity(5, "kiełbasa",null));

        locationDao.insertAllPoints(similarLocations);
        locationDao.insertAllPoints(nonSimilarLocations);

//        List<LocationEntity> results = locationDao.getByName("%kOń%", 5);
//        assertEquals(similarLocations, results);
    }

    @Test
    public void getLocationWithNullDescription() {
        List<LocationEntity> locations = new ArrayList<>();
        locations.add(new LocationEntity(
                1,
                "Place 1",
                null));

        locationDao.insertAllPoints(locations);

        LocationEntity result = locationDao.getByID(locations.get(0).getId());

        assertEquals(locations.get(0), result);
    }

    @Test
    public void getLocationByNonExistID() {
        List<LocationEntity> locations = new ArrayList<>();
        locations.add(new LocationEntity(
                1,
                "Place 1",
                null));

        locationDao.insertAllPoints(locations);

        LocationEntity result = locationDao.getByID(2);

        assertNull(result);
    }

    @Test
    @Ignore
    public void getLocationByNotMatchingName() {

        fail();
    }
}