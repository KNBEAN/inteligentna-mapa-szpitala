package bean.pwr.imskamieskiego.path_search;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PathSearcherTest {

    private PathSearcher pathSearcher;
    @Mock
    private PathSearchAlgorithm searchAlgorithm;
    @Mock
    private SearchCompleteListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void callEndOfSearchListener() {

        pathSearcher = new PathSearcher(InstrumentationRegistry.getContext());
        pathSearcher.setListener(listener);
        pathSearcher.startSearch(searchAlgorithm);

        //It looks a bit like an ugly hack.
        while (pathSearcher.isInProgress()){};

        verify(listener, times(1)).searchComplete(anyList());
    }
}