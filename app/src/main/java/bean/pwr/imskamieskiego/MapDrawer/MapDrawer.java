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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Environment;
import android.graphics.Rect;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

import java.util.List;

import bean.pwr.imskamieskiego.MapDrawer.DottedPaint;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.MapPoint;



public class MapDrawer extends View {

    private Bitmap originalMap;
    private Context context;
    private PointF pointFlast;

    private PointF pointToShow;
    private HashSet<MapPoint> mapPoints;



    private Hashtable<MapPoint, Integer> mapPointsTypes;
    private List<MapPoint> pathPoints;
    private ArrayList<Bitmap> tackTextures;
    private int resourceTacksId[];
    private int measureWidth, measureHeight;
    private int currentlyDisplayedFloor;
    private float originalScale;
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
    private Paint paintPath;
    private Matrix canvasMatrix;
    private Matrix additonalMatrix;
    private Matrix additonalMatrixCopy;
    private MapDrawerGestureListener mapDrawerGestureListener;

    private Observer<RegionBitmap> regionObserver;
    private MapProvider mapProvider;
    private Rect displayedRect;
    private Rect displayedRectCopy;
    private Rect decodedRect;
    private Rect decodedRectCopy;
    private Point pointDecode;
    private boolean isNewBitmapDecoded;



    public MapDrawer(Context context) {
        super(context);
        this.context = context;
        SharedConstructing();
    }

    public MapDrawer(Context context, AttributeSet attrs){
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MapDrawer,
                0, 0);
        resourceTacksId = new int[a.length()];
        for (int i = 0; i < a.length(); i++) {
            resourceTacksId[i] = a.getResourceId(i, 0);
        }
        a.recycle();

        this.context = context;
        SharedConstructing();
    }


    private void SharedConstructing() {
        setLongClickable(true);
        setClickable(true);
        loadTackTextures();
        originalScale = 1;
        offsetX = 0;
        offsetY = 0;
        paintPath = new DottedPaint();
        pointFlast = new PointF();
        pointDecode= new Point();
        scaleDetector = 1.f;
        pathPoints = new ArrayList<>();
        mapPoints = new HashSet<>();
        mapPointsTypes = new Hashtable<>();
        decodedRect = new Rect();
        displayedRect = new Rect();
        displayedRectCopy = new Rect();
        decodedRectCopy = new Rect();
        additonalMatrix = new Matrix();
        additonalMatrixCopy = new Matrix();
        canvasMatrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector  = new GestureDetector(context,new GestureListener());
        RectM.setAdditionalSpace(ADDITIONAL_SPACE);

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
                if (regionBitmap.isDecodedAlready()){
                    if (displayedRect.isEmpty()){
                        displayedRect.set(RectM.insetRect(regionBitmap.getRect(),false));
                        displayedRectCopy.set(displayedRect);
                    }
                    if (regionBitmap.getBitmap()!=null){
                        setOriginalMap(regionBitmap.getBitmap());
                    }

                } else {
                    if (regionBitmap.getRect() == null) return;
                    decodedRect = regionBitmap.getRect();
                    decodedRect.set(RectM.fitRectToScreen(decodedRect,measureWidth,measureHeight));
                    decodedRect.set(RectM.insetRect(decodedRect,true));
                    decodedRectCopy.set(decodedRect);
                    mapProvider.setRegionBitmap(decodedRect);
                }

            }
        };
        regionBitmapLiveData.observe((LifecycleOwner) context, regionObserver);
    }



    private Rect fitDecodingToScreen(Rect rect){
        Rect fitRect = new Rect();
        fitRect.left = Math.round((rect.right - measureWidth )/2);
        fitRect.top = Math.round((rect.bottom - measureHeight) / 2);
        fitRect.right = fitRect.left + measureWidth;
        fitRect.bottom = fitRect.top + measureHeight;
        return fitRect;
    }

    private Rect insetRect(Rect rect, boolean makeWider){
        Rect broadenRect = new Rect();
        broadenRect.set(rect);
        if (makeWider) {
            broadenRect.inset(-ADDITIONAL_SPACE,-ADDITIONAL_SPACE);
        } else {
            broadenRect.inset(ADDITIONAL_SPACE,ADDITIONAL_SPACE);
        }
        return broadenRect;

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

    private void setOriginalMap(Bitmap bitmap){
        originalMap = bitmap;
        isNewBitmapDecoded = true;
        invalidate();
    }







    private Bitmap convertBitmapToMutable(Bitmap bitmap){

        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap.Config type = bitmap.getConfig();

            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, bitmap.getRowBytes()*height);
            bitmap.copyPixelsToBuffer(map);

            bitmap.recycle();

            bitmap = Bitmap.createBitmap(width, height, type);
            map.position(0);

            bitmap.copyPixelsFromBuffer(map);

            channel.close();
            randomAccessFile.close();

            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }







    @Override
    protected void onDraw(Canvas canvas) {
        if (originalMap == null) return;
        canvasMatrix.reset();
        int moveX = 0;
        int moveY = 0;
        canvasMatrix.postTranslate(-ADDITIONAL_SPACE,-ADDITIONAL_SPACE);
        displayedRect.set(displayedRectCopy);
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
                displayedRect.offset(-moveX,-moveY);
                canvasMatrix.postTranslate(moveX,moveY);
                break;

            case ZOOM:

                break;

        }
        if (isNewBitmapDecoded) {
            pointDecode = getChangedPointOfDecoding();
            askIfNewMapNeeded();

        }
        canvas.concat(canvasMatrix);
        Log.i(MAP_DRAWER, "onDraw: PointDecode.x =" + pointDecode.x+ " .y =" +pointDecode.y);
        canvas.drawBitmap(originalMap,pointDecode.x,pointDecode.y , null);
    }

    private void askIfNewMapNeeded(){
        if (!decodedRect.contains(displayedRect)) {
            Log.i(MAP_DRAWER, "onDraw: DISPLAY OUT OF DECODED IMAGE");
            isNewBitmapDecoded = false;
            decodedRect.set(RectM.insetRect(displayedRect, true));
            mapProvider.setRegionBitmap(decodedRect);
        }
    }

    private Point getChangedPointOfDecoding(){
        return new Point(-(decodedRectCopy.left - decodedRect.left)
                ,-(decodedRectCopy.top-decodedRect.top));

    }



    /**
     * If resources for tack hadn't been
     * initialized in .xml file or
     * constructor was only with Context argument
     * this method allows to set up Tack textures after
     * creating instance of MapDrawer.
     * @param startTackId   id of drawable which will be set to starting points
     * @param endTackId     id of drawable which will be set to ending points
     * @param defaultTackId id of drawable which will be set for default points
     */
    public void addTackResources(int defaultTackId, int startTackId, int endTackId) {
        resourceTacksId = new int[3];
        resourceTacksId[0] = defaultTackId;
        resourceTacksId[1] = startTackId;
        resourceTacksId[2] = endTackId;
        loadTackTextures();
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

    private void loadTackTextures() {
        Bitmap texture;
        int tackWidth = 100;
        int tackHeight = 100;
        tackTextures = new ArrayList<>();
        try {
            for (int resourceName : resourceTacksId) {
                Resources res = context.getResources();
                texture = BitmapDecoder.decodeSampledBitmapFromResource(res
                        , resourceName
                        , tackWidth
                        , tackHeight);
                tackTextures.add(Bitmap.createScaledBitmap(texture,
                        (tackWidth),
                        (tackHeight),
                        false));
            }
        } catch (Exception o) {

        }
    }

    private Bitmap getTackTexture(int type) {
        return tackTextures.get(type);
    }



    /**
     * Add mappoint which will be drawn
     * immediately with previous added tacks.
     * @param tack mappoint with x and y coordinates
     * @param type determines the texture set to given point basing on previously sent attributes
     *  in xml file or resources in method addTackResources
     */
    public void addMapPoint(MapPoint tack, Integer type) {
        mapPoints.add(tack);
        mapPointsTypes.put(tack, type);
        invalidate();
    }

    /**
     * Clear mappoint arraylist
     */
    public void removeAllMapPoints() {
        mapPoints.clear();
        mapPointsTypes.clear();
        invalidate();
    }


    /**
     * Remove object from map
     * @param mapPoint object to remove
     */
    public void removeMapPoint(MapPoint mapPoint) {
        mapPoints.remove(mapPoint);
        mapPointsTypes.remove(mapPoint);
        invalidate();
    }

    /**
     * Set trace to be drawn as path to destined place
     * @param trace array of mappoints to be drawn
     */
    public void setTrace(List<MapPoint> trace) {
        pathPoints = trace;
        invalidate();
    }

    /**
     * Clear trace to be drawn as path to destined place
     */
    public void removeTrace() {
        pathPoints = new ArrayList<>();
        invalidate();
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

