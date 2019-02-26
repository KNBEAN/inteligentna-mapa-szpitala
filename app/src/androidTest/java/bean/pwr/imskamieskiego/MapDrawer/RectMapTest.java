package bean.pwr.imskamieskiego.MapDrawer;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.graphics.Point;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;


public class RectMapTest {
    private MapRect mapRect;
    private String TAG = "TEST-RECTMAP";

    @Before
    public void setUp() throws Exception {
        mapRect = new MapRect(0,0,100,100);
    }

    @Test
    public void shouldMakeRectWider() {
        Log.i(TAG, "Rectmap:  " + mapRect.getRect());
        MapRect afterInsetion = new MapRect(0,0,100,100);
        afterInsetion.inset(100,true);
        Log.i(TAG, "Rectmap after inset:  " + afterInsetion.getRect());
        assertTrue(afterInsetion.getRect().width()> mapRect.getRect().width());
        assertTrue(afterInsetion.getRect().height()> mapRect.getRect().height());
    }

    @Test
    public void shouldMakeRectSmaller(){
        Log.i(TAG, "Rectmap:  " + mapRect.getRect());
        MapRect afterInsetion = new MapRect(0,0,100,100);
        afterInsetion.inset(50,false);
        Log.i(TAG, "Rectmap after inset:  " + afterInsetion.getRect());
        assertTrue(afterInsetion.getRect().width()< mapRect.getRect().width());
        assertTrue(afterInsetion.getRect().height()< mapRect.getRect().height());
    }

    @Test
    public void shouldScaleProperlyWithMaintainingCenterPoint(){

        Point centerBeforeScaling = mapRect.getCenter();
        mapRect.scaleCentrally(0.5f);
        assertEquals(mapRect.getCenter(), centerBeforeScaling);

        mapRect.getRect().offsetTo(-50,-50);
        centerBeforeScaling = mapRect.getCenter();
        mapRect.scaleCentrally(2f);
        assertEquals(centerBeforeScaling,mapRect.getCenter());

        mapRect.getRect().set(0,0,50,50);
        centerBeforeScaling = mapRect.getCenter();
        mapRect.scaleCentrally(2f);
        assertEquals(centerBeforeScaling,mapRect.getCenter());

    }


    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionDueToSmallAdditionalSpace(){
        mapRect.inset(100,false);
    }


}