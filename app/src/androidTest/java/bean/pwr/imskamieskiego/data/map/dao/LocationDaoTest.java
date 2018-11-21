package bean.pwr.imskamieskiego.data.map.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.data.map.entity.LocationTagEntity;
import bean.pwr.imskamieskiego.model.map.Location;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LocationDaoTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private LocalDB database;
    private LocationDao locationDao;

    @Mock
    private Observer observer;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        locationDao = database.getLocationDao();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void getLocationByID() {
        List<LocationEntity> locations = Arrays.asList(
                new LocationEntity(
                        1,
                        "Place 1",
                        "Very nice place."));

        locationDao.insertAllLocations(locations);

        LiveData<LocationEntity> result = locationDao.getByID(locations.get(0).getId());
        result.observeForever(observer);

        assertEquals(locations.get(0), result.getValue());
    }

    @Test
    public void getLocationWithNullDescription() {
        List<LocationEntity> locations = Arrays.asList(
                new LocationEntity(
                        1,
                        "Place 1",
                        null));

        locationDao.insertAllLocations(locations);

        LiveData<LocationEntity> result = locationDao.getByID(locations.get(0).getId());
        result.observeForever(observer);

        assertEquals(locations.get(0), result.getValue());
    }

    @Test
    public void getLocationByNonExistID() {
        List<LocationEntity> locations = Arrays.asList(
                new LocationEntity(
                        1,
                        "Place 1",
                        null));

        locationDao.insertAllLocations(locations);

        LiveData<LocationEntity> result = locationDao.getByID(2);
        result.observeForever(observer);

        assertNull(result.getValue());
    }


    @Test
    public void getListOfSimilarLocationNames() {
        List<LocationEntity> similarLocations = Arrays.asList(
                new LocationEntity(1, "Koński ogon",null),
                new LocationEntity(2, "kończyna",null),
                new LocationEntity(3, "OkoŃ",null));
        List<LocationEntity> nonSimilarLocations = Arrays.asList(
                new LocationEntity(4, "pietruszka",null),
                new LocationEntity(5, "kiełbasa",null));
        List<LocationTagEntity> locationTags = new ArrayList();
        for (LocationEntity location:similarLocations) {
            locationTags.add(new LocationTagEntity(location.getName().toLowerCase(), location.getId()));
        }
        for (LocationEntity location:nonSimilarLocations) {
            locationTags.add(new LocationTagEntity(location.getName().toLowerCase(), location.getId()));
        }
        locationDao.insertAllLocations(similarLocations);
        locationDao.insertAllLocations(nonSimilarLocations);
        locationDao.insertAllTags(locationTags);


        LiveData<List<LocationEntity>> locations = locationDao.getListByTag("%koń%", 5);
        locations.observeForever(observer);
        assertTrue(locations.getValue().containsAll(similarLocations));
    }
}