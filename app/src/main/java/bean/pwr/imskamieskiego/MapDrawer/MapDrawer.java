/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.MapDrawer;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Environment;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;



public class MapDrawer extends View {

    private Bitmap originalMap;
    private Context context;
    private PointF pointFlast;
    private int measureWidth, measureHeight;
    private float deltaX, deltaY;
    private float offsetX, offsetY;
    private final int ADDITIONAL_SPACE = 400;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleDetector = 1;
    private float scaleDetectorMAX = 3.f;
    private float scaleDetectorMIN = 0.5f;

    private final int START = -1;
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private final int ZOOM_POINT = 3;
    private int mode;

    private final String MAP_DRAWER = "map_drawer";
    private Matrix canvasMatrix;
    private MapDrawerGestureListener mapDrawerGestureListener;

    private Observer<RegionBitmap> regionObserver;
    private MapProvider mapProvider;
    private Point pointDecode;



    public MapDrawer(Context context) {
        super(context);
        this.context = context;
        SharedConstructing();
    }

    public MapDrawer(Context context, AttributeSet attrs){
        super(context, attrs);
        SharedConstructing();
    }


    private void SharedConstructing() {
        setLongClickable(true);
        setClickable(true);
        offsetX = 0;
        offsetY = 0;
        pointFlast = new PointF();
        pointDecode= new Point();
        scaleDetector = 1.f;
        canvasMatrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector  = new GestureDetector(context,new GestureListener());


        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                gestureDetector.onTouchEvent(event);


                if (scaleGestureDetector.isInProgress()) return true;

                PointF pointF = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        pointFlast = pointF;

                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (mode == DRAG) {
                            deltaX = pointF.x - pointFlast.x;
                            deltaY = pointF.y - pointFlast.y;
                            invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX += deltaX;
                        offsetY += deltaY;
                        deltaX = 0;
                        deltaY = 0;
                        break;
                }
                return true;
            }
        });
    }


    private void setRegionObserver(LiveData<RegionBitmap> regionBitmapLiveData){
        regionObserver = new Observer<RegionBitmap>() {
            @Override
            public void onChanged(@Nullable RegionBitmap regionBitmap) {
                Log.i(MAP_DRAWER, "RegionObserver: RegionMapChanged!");


            }
        };
        regionBitmapLiveData.observe((LifecycleOwner) context, regionObserver);
    }






    /**
     * Set data with bitmap to be observed. MapDrawer will observe
     * RegionBitmap in delivered MapProvider and use methods by reference
     *  @param mapProvider mapProvider connected to database
     */
    public void setMapProvider(MapProvider mapProvider){
     this.mapProvider = mapProvider;
     setRegionObserver(mapProvider.getRegionBitmap());
    }







    @Override
    protected void onDraw(Canvas canvas) {
        if (originalMap == null) return;
        canvasMatrix.reset();
        int moveX = 0;
        int moveY = 0;
        canvasMatrix.postTranslate(-ADDITIONAL_SPACE,-ADDITIONAL_SPACE);
        switch (mode) {

            case START:
                mode = NONE;
                break;
            case NONE:
                Log.i(MAP_DRAWER, "onDraw: MODE:NONE");
                break;

            case DRAG:
                moveX = (int) offsetX + (int)deltaX;
                moveY = (int) offsetY + (int) deltaY;
                canvasMatrix.postTranslate(moveX,moveY);
                break;

            case ZOOM:

                break;

        }

        canvas.concat(canvasMatrix);
        Log.i(MAP_DRAWER, "onDraw: PointDecode.x =" + pointDecode.x+ " .y =" +pointDecode.y);
        canvas.drawBitmap(originalMap,pointDecode.x,pointDecode.y , null);
    }






    /**
     * Setting max scale.
     * Keep in mind that too big
     * scale can cause problems with rendering
     * on canvas.
     * @param maxScale scale which will be set a maximal
     */
    public void setMaxScale(float maxScale) {
        scaleDetectorMAX = maxScale;
    }

    /**
     * Setting minimum scale allowed.
     * @param minScale scale which will be set as minimal
     */
    public void setMinScale(float minScale) {
        scaleDetectorMIN = minScale;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
        if (mode != ZOOM_POINT) {
            mode = START;
        }
    }




    public void showFloor(int floor, Bitmap bitmap){}

    /**
     * Add mappoint which will be drawn
     * immediately with previous added tacks.
     * @param tack mappoint with x and y coordinates
     * @param type determines the texture set to given point basing on previously sent attributes
     *  in xml file or resources in method addTackResources
     */
    public void addMapPoint(MapPoint tack, Integer type) {
    }

    /**
     * Clear mappoint arraylist
     */
    public void removeAllMapPoints() {
    }


    /**
     * Remove object from map
     * @param mapPoint object to remove
     */
    public void removeMapPoint(MapPoint mapPoint) {
    }

    /**
     * Set trace to be drawn as path to destined place
     * @param trace array of mappoints to be drawn
     */
    public void setTrace(List<MapPoint> trace) {
    }

    /**
     * Clear trace to be drawn as path to destined place
     */
    public void removeTrace() {

    }

    /**
     * Set observer for MapDrawer to get ongoing
     * gestures in widget
     * @param mapDrawerGestureListener
     */
    public void setOnLongPressListener(MapDrawerGestureListener mapDrawerGestureListener){
        this.mapDrawerGestureListener = mapDrawerGestureListener;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            if ((scaleDetectorMAX < scaleDetector * detector.getScaleFactor())
                    || (scaleDetectorMIN > scaleDetector * detector.getScaleFactor()))
                return true;
            scaleDetector = scaleDetector * detector.getScaleFactor();
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.i(MAP_DRAWER, "onScaleBegins");
            mode = ZOOM;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.i(MAP_DRAWER, "onScaleEnds");
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {


        }

    }


}

