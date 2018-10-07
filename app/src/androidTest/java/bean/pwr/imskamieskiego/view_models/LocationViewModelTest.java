package bean.pwr.imskamieskiego.view_models;


import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;;
import android.support.test.InstrumentationRegistry;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.LocationFactory;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.model.map.MapPointFactory;
import bean.pwr.imskamieskiego.repository.MapRepository;

public class LocationViewModelTest {
    private LocationViewModel mViewModel;

    @Mock
    private MapRepository mapRepository;
    @Mock
    private Observer<Location> observer;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test()
    public void getLocationLiveDataByMapPoint_withMapPoint() {
        mViewModel = new LocationViewModel((Application) InstrumentationRegistry.getTargetContext().getApplicationContext());
        MutableLiveData<Location> locationLoc= new MutableLiveData<>();

        LocationFactory locationFactory = new LocationFactory();
        MapPointFactory mapPointFactory = new MapPointFactory();
        MapPoint clickedMapPoint = mapPointFactory.create(1, 2, 3, 4, 5);
        MapPoint nearestMapPoint = mapPointFactory.create(1, 2, 3, 4, 2);
        Location location = locationFactory.create(2, "SOR", "super-place");


        when(mapRepository.getNearestPoint(anyInt(), anyInt(), anyInt())).thenReturn(nearestMapPoint);
        when(mapRepository.getLocationByID(anyInt())).thenReturn(location);
        locationLoc.observeForever(observer);
        locationLoc = mViewModel.getLocationLiveData(clickedMapPoint);
        Location output = locationLoc.getValue();
        Assert.assertEquals(location, output);


    }
}