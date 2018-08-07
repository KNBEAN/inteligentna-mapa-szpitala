package bean.pwr.imskamieskiego.MapGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.Hashtable;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.MapPoint;

import static android.content.ContentValues.TAG;


public class MapDrawer extends View {

    private Bitmap originalMap;
    private MapModel model;
    private Context context;
    private PointF pointFlast;
    private ArrayList<MapPoint> mapPoints;
    private Hashtable<MapPoint, Integer> mapPointsTypes;
    private ArrayList<MapPoint> pathPoints;
    private int measureWidth, measureHeight;
    private int deasiredWidth;
    private int deasiredHeight;
    private int currentlyDisplayedFloor;
    private float originalScale;
    private float deltaX, deltaY;
    private float offsetX, offsetY;
    private ScaleGestureDetector ScaleDetector;
    private float scaleDetector;
    private boolean isViewHorizontal;
    private float scaleDetectorMAX = 3.f;
    private float scaleDetectorMIN = 0.5f;
    private PointF pointToShow;
    private float scaleFactorPoint =1;
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private final int ZOOM_POINT = 3;
    private final int DENISITY_CONVERTER = 160;
    private int mode;


    public MapDrawer(Context context) {
        super(context);
        this.context = context;
        SharedConstructing();
    }

    public MapDrawer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedConstructing();
    }


    private void SharedConstructing() {

        setClickable(true);
        originalScale = 1;
        offsetX = 0;
        offsetY = 0;
        pointFlast = new PointF();
        scaleDetector = 1.f;
        pathPoints = new ArrayList<>();
        mapPoints = new ArrayList<>();
        mapPointsTypes = new Hashtable<>();
        pointToShow = new PointF();

        ScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ScaleDetector.onTouchEvent(event);

                if (ScaleDetector.isInProgress()) return true;

                PointF pointF = new PointF(event.getX(), event.getY());
                Log.i(TAG, "onTouch: event.getX() = " + event.getX() + " event.getY() = " + event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        pointFlast = pointF;
                        offsetX += deltaX;
                        offsetY += deltaY;
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (mode == DRAG) {
                            deltaX = pointF.x - pointFlast.x;
                            deltaY = pointF.y - pointFlast.y;
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
    }


    /**
     * Setting map to be drawn on canvas
     * and getting dimensions from bitmap
     * to set up scale parameters
     * If floor exceeds size of
     * floors list method gets the last one floor
     * map possible
     * @param floor number of the floor to be drawn
     */
    public void showFloor(int floor) {
        if (floor < 0) throw new IllegalArgumentException("Positive value needed");
        if (floor >= model.getFloorCount()) {
            originalMap = model.getFloorMap(model.getFloorCount() - 1);
            currentlyDisplayedFloor = model.getFloorCount() - 1;
        } else {
            originalMap = model.getFloorMap(floor);
            currentlyDisplayedFloor = floor;
        }
        if (originalMap == null) throw new NullPointerException();
        invalidate();
    }

    /**
     * Zoom view on given point. Floor will be
     * changed if needed. Zoom is equal to scaleDetectorMAX
     * @param point point to zoom
     */
    public void showPoint(MapPoint point) {
        mode = ZOOM_POINT;
        showFloor(point.getFloor());
        scaleDetector = scaleDetectorMAX;
        pointToShow.x = point.getX();
        pointToShow.y = point.getY();
        invalidate();
    }

    private Bitmap layerMap(Bitmap originalMap) {
        Bitmap result;
        Matrix matrix = new Matrix();
        try {
                result = Bitmap.createScaledBitmap(originalMap
                        , deasiredWidth
                        , deasiredHeight
                        , false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }


    private Bitmap layerTacks(ArrayList<MapPoint> mapObjects) {
        Bitmap result = Bitmap.createBitmap(measureWidth, measureHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        if (!mapObjects.isEmpty()) {
            for (MapPoint point : mapObjects) {
                if (point.getFloor() == currentlyDisplayedFloor) {
                        canvas.drawBitmap(getTackTexture(mapPointsTypes.get(point)),
                                point.getX() / scaleFactorPoint
                                        - (getTackTexture(mapPointsTypes.get(point)).getWidth() / 2),
                                point.getY() / scaleFactorPoint
                                        - (getTackTexture(mapPointsTypes.get(point)).getHeight() / 2),
                                null);
                } } }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (originalMap == null) return;
        Bitmap layer;
        fitMapToScreen(originalMap);
        layer = layerMap(originalMap);

        switch (mode) {

            case NONE:
                Log.i(TAG, "onDraw: NONE");
                break;

            case DRAG:
                Log.i(TAG, "onDraw: DRAG");
                canvas.scale(scaleDetector,
                        scaleDetector, measureWidth/2,measureHeight/2);
                canvas.translate(deltaX + offsetX, deltaY + offsetY);
                Log.i(TAG, "onDraw: DRAG x = " + deltaX + " offset x = " +
                        offsetX + " y = " + deltaY + " offset x = " + offsetY);
                break;

            case ZOOM:
                Log.i(TAG, "onDraw: ZOOM");
                canvas.scale(scaleDetector,
                        scaleDetector,measureWidth/2,measureHeight/2);
                canvas.translate(offsetX, offsetY);

                Log.i(TAG, "onDraw: ZOOM : scaleDetector = " + scaleDetector);
                Log.i(TAG, "onDraw: ZOOM OFFSET : X = " + offsetX + " Y = " + offsetY);
                break;
            case ZOOM_POINT:
                Log.i(TAG, "onDraw: ZOOM_POINT");
                    pointToShow.x =( measureWidth/2 - pointToShow.x/scaleFactorPoint );
                    pointToShow.y = ( measureHeight/2 - pointToShow.y/scaleFactorPoint );
                Log.i(TAG, "onDraw: ZOOM_POINT point to show = " +pointToShow);

                canvas.scale(scaleDetector,
                        scaleDetector,
                        measureWidth/2,measureHeight/2);
                canvas.translate(pointToShow.x ,
                        pointToShow.y);

            //    canvas.translate(p);
                offsetX = pointToShow.x;
                offsetY = pointToShow.y;
                Log.i(TAG, "onDraw: ZOOM_POINT : scaleFromDetector = " + scaleDetector);
        }

    //  canvas.translate((canvas.getWidth() - layer.getWidth()) / 2,
    //           (canvas.getHeight() - layer.getHeight()) / 2);
        canvas.drawBitmap(layer, 0, 0, null);
        layer = layerTacks(mapPoints);
        canvas.drawBitmap(layer, 0, 0, null);
    }

    private void fitMapToScreen(Bitmap bitmap) {
        if ((measureWidth == 0) || (measureHeight == 0)) return;

           float originalBitmapWidth = bitmap.getWidth();
           float originalBitmapHeight = bitmap.getHeight();
           float pixelDensity = bitmap.getDensity()/DENISITY_CONVERTER;
            originalScale = Math.max(
                 originalBitmapHeight / (float) measureHeight
                ,originalBitmapWidth / (float) measureWidth);

        float pointScale;

        deasiredWidth = (int)( originalBitmapWidth / originalScale);
        deasiredHeight = (int)( originalBitmapHeight / originalScale);

        scaleFactorPoint = originalScale/pixelDensity;



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

        Log.i(TAG, "onMeasure: START");
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
        if (mode != ZOOM_POINT) {
            mode = NONE;
        }
        if (measureWidth > measureHeight) isViewHorizontal = true;
        if (measureHeight > measureWidth) isViewHorizontal = false;
    }


    private Bitmap getTackTexture(int type) {
        Bitmap texture;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        switch (type) {
            case 1:
                texture = BitmapFactory.decodeResource(context.getResources(), R.drawable.start_tack,opt);
                texture = Bitmap.createBitmap(texture, 0, 0, 100, 100);
                break;

            case 2:
                texture = BitmapFactory.decodeResource(context.getResources(), R.drawable.end_tack);
                texture = Bitmap.createBitmap(texture, 0, 0, 100, 100);
                break;

            default:
                texture = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_tack);
                texture = Bitmap.createBitmap(texture, 0, 0, 100, 100);
                break;
        }
        return texture;
    }

    /**
     * Method sets model
     * which will be used to
     * get maps textures.
     * @param model object that implements MapModel interface
     */
    public void setModel(MapModel model) {
        this.model = model;
    }


    /**
     * Add mappoint which will be drawn
     * immediately with previous added tacks.
     * @param tack mappoint with x and y coordinates
     * @param type determines the texture set to given point
     */
    public void addMapPoint(MapPoint tack, Integer type) {
        mapPoints.add(tack);
        mapPointsTypes.put(tack, type);
        Log.i(TAG, "addMapPoint: Point Added");
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
    public void setTrace(ArrayList<MapPoint> trace) {
        pathPoints = trace;
        invalidate();
    }

    /**
     * Clear trace to be drawn as path to destined place
     */
    public void removeTrace() {
        pathPoints = null;
        invalidate();
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
            Log.i(TAG, "onScaleBegins");
            mode = ZOOM;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.i(TAG, "onScaleEnds");
        }

    }


}

