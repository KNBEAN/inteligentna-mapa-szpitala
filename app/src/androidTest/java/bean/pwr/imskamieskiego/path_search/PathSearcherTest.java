package bean.pwr.imskamieskiego.path_search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public class PathSearcherTest {

    private PathSearcher pathSearcher;
    @Mock
    private PathSearchAlgorithm searchAlgorithm;
    @Mock
    Observer observer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void callObserverWhenSearchEnded() {

        pathSearcher = new PathSearcher(InstrumentationRegistry.getContext());
        LiveData<List<MapPoint>> trace = pathSearcher.getPath();
        trace.observeForever(observer);
        pathSearcher.startSearch(searchAlgorithm);

        //It looks a bit like an ugly hack.
        while (pathSearcher.isInProgress()){};

        verify(observer, times(1)).onChanged(anyList());
    }
}