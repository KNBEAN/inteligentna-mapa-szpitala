package bean.pwr.imskamieskiego.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

import bean.pwr.imskamieskiego.TestObserver;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.data.map.dao.LocationDao;
import bean.pwr.imskamieskiego.data.map.entity.LocationEntity;
import bean.pwr.imskamieskiego.data.map.entity.LocationTagEntity;
import bean.pwr.imskamieskiego.model.map.Location;

import static org.junit.Assert.*;

public class MapRepositoryTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private MapRepository repository;

    private LocalDB database;
    private LocationDao locationDao;

    private List<Location> resultForLowerCase;
    private List<Location> resultForUpperCase;

    @Mock private LifecycleOwner lifecycleOwner;
    private LifecycleRegistry lifecycle;

    private List<LocationEntity> locationEntities;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                LocalDB.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        locationDao = database.getLocationDao();
        repository = new MapRepository(database);

        lifecycle = new LifecycleRegistry(lifecycleOwner);
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        when(lifecycleOwner.getLifecycle()).thenReturn(lifecycle);

        locationEntities = Arrays.asList(
                new LocationEntity(1, "Koński ogon",null),
                new LocationEntity(2, "kończyna",null),
                new LocationEntity(3, "OkoŃ",null));
        addDataToDB(locationEntities);
    }

    @Test
    public void getLocationsByCaseInsensitiveName() {
        repository.getLocationsListByName("%koń%", 5).observe(lifecycleOwner,
                locations -> resultForLowerCase = locations);

        repository.getLocationsListByName("%KOŃ%", 5).observe(lifecycleOwner,
                locations -> resultForUpperCase = locations);

        assertTrue(resultForLowerCase.containsAll(locationEntities));
        assertTrue(resultForUpperCase.containsAll(locationEntities));
    }

    @Test
    public void getLocationsByEmptyName() {
        LiveData<List<Location>> result = repository.getLocationsListByName("%%", 5);
        TestObserver<List<Location>> observer = new TestObserver<>();
        result.observeForever(observer);

        assertTrue(observer.observedValues.get(0).containsAll(locationEntities));
    }

    @Test
    public void getLocationsWhenNoMatch() {
        LiveData<List<Location>> result = repository.getLocationsListByName("potato", 5);
        TestObserver<List<Location>> observer = new TestObserver<>();
        result.observeForever(observer);

        assertTrue(observer.observedValues.get(0).isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void getLocationsWhenNameIsNull() {

        LiveData<List<Location>> result = repository.getLocationsListByName(null, 5);
        TestObserver<List<Location>> observer = new TestObserver<>();
        result.observeForever(observer);

    }

    private void addDataToDB(List<LocationEntity> locationEntities){
        List<LocationTagEntity> locationTags = new ArrayList();
        for (LocationEntity location:locationEntities) {
            locationTags.add(new LocationTagEntity(location.getName().toLowerCase(), location.getId()));
        }
        locationDao.insertAllLocations(locationEntities);
        locationDao.insertAllTags(locationTags);
    }

    @After
    public void tearDown() {
        database.close();
    }
}