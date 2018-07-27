package bean.pwr.imskamieskiego.MapGenerator;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


import static android.content.ContentValues.TAG;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.i(TAG, "onSingleTapConfirmed: TRUE");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i(TAG, "onDoubleTap: TRUE");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.i(TAG, "onDoubleTapEvent: TRUE");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown: TRUE");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onShowPress: TRUE");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp: TRUE");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(TAG, "onScroll: TRUE");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress: TRUE");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG, "onFling: TRUE");
        return false;
    }
}
