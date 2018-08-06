package bean.pwr.imskamieskiego.MapGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;

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
    private int bitmapWidth;
    private int bitmapHeight;
    private int originalBitmapWidth;
    private int originalBitmapHeight;
    private int currentlyDisplayedFloor;
    private float originalScale;
    private float deltaX, deltaY;
    private float offsetX, offsetY;
    private ScaleGestureDetector ScaleDetector;
    private float scaleDetector;
    private float scaleDetectorXCenter;
    private float scaleDetectorYCenter;
    private boolean isViewHorizontal;
    private float scaleDetectorMAX = 3.f;
    private float scaleDetectorMIN = 0.5f;
    private float previousScaleDetectorXCenter = 0;
    private float previousScaleDetectorYCenter = 0;
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private final int ZOOM_POINT = 3;
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

        ScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ScaleDetector.onTouchEvent(event);

                if (ScaleDetector.isInProgress()) return true;

                PointF pointF = new PointF(event.getX(), event.getY());

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
     *
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
        bitmapHeight = originalMap.getHeight();
        bitmapWidth = originalMap.getWidth();
        originalBitmapWidth = bitmapWidth;
        originalBitmapHeight = bitmapHeight;

    }

    public void showPoint(MapPoint point) {
        mode = ZOOM_POINT;
        showFloor(point.getFloor());
        WindowManager windowManager =
                (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        display.getRealSize(outPoint);
        Log.i(TAG, "onDraw: DISPLAY MET x = " + outPoint.x);
        Log.i(TAG, "onDraw: DISPLAY MET x = " + outPoint.y);
        scaleDetectorXCenter = point.getX() - outPoint.x;
        scaleDetectorYCenter = point.getY() - outPoint.y;
        scaleDetector = scaleDetectorMAX;

    }

    private Bitmap layerMap(Bitmap originalMap, int xFrom, int yFrom, float scale) {
        Matrix tempMatrix = new Matrix();
        Bitmap result;

        tempMatrix.postScale(scale, scale);
        try {
            result = Bitmap.createBitmap(originalMap,
                    xFrom, yFrom,
                    originalBitmapWidth,
                    originalBitmapHeight,
                    tempMatrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        bitmapWidth = result.getWidth();
        bitmapHeight = result.getHeight();
        return result;
    }


    private Bitmap layerTacks(ArrayList<MapPoint> mapObjects, float scale) {
        Bitmap result = Bitmap.createBitmap(measureWidth, measureHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        if (!mapObjects.isEmpty()) {
            for (MapPoint point : mapObjects) {
                if (point.getFloor() == currentlyDisplayedFloor) {
                    if (isViewHorizontal == true) {
                        canvas.drawBitmap(getTackTexture(mapPointsTypes.get(point)),
                                point.getX() * (originalBitmapWidth / measureHeight)
                                        * scale - (getTackTexture(mapPointsTypes.get(point)).getWidth() / 2),
                                point.getY() * (originalBitmapHeight / measureWidth)
                                        * scale - (getTackTexture(mapPointsTypes.get(point)).getHeight() / 2),
                                null);
                    } else {
                        canvas.drawBitmap(getTackTexture(mapPointsTypes.get(point)),
                                calculatePoint(point).x- (getTackTexture(mapPointsTypes.get(point)).getWidth() / 2),
                                calculatePoint(point).y - (getTackTexture(mapPointsTypes.get(point)).getHeight() / 2),
                                null);

                    }
                }
            }
        }
        return result;
    }

    private PointF calculatePoint(MapPoint mapPoint){
        PointF point = new PointF();
        point.x = mapPoint.getX()* (originalBitmapWidth / measureWidth) * originalScale;
        point.y = mapPoint.getY() * (originalBitmapHeight / measureHeight) * originalScale;
        return point;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (originalMap == null) return;
        Bitmap layer;
        fitBitmapToScreen(originalMap);

        switch (mode) {

            case NONE:
                Log.i(TAG, "onDraw: NONE");

                break;

            case DRAG:
                Log.i(TAG, "onDraw: DRAG");
                canvas.scale(scaleDetector,
                        scaleDetector,
                        scaleDetectorXCenter,
                        scaleDetectorYCenter);
                canvas.translate(deltaX + offsetX, deltaY + offsetY);
                Log.i(TAG, "onDraw: DRAG x = " + deltaX + " offset x = " +
                        offsetX + " y = " + deltaY + " offset x = " + offsetY);
                break;

            case ZOOM:
                Log.i(TAG, "onDraw: ZOOM");
                canvas.scale(scaleDetector,
                        scaleDetector,
                        scaleDetectorXCenter,
                        scaleDetectorYCenter);
                canvas.translate(offsetX, offsetY);
                previousScaleDetectorXCenter = scaleDetectorXCenter;
                previousScaleDetectorYCenter = scaleDetectorYCenter;
                Log.i(TAG, "onDraw: ZOOM : scaleFromDetector = " + scaleDetector
                        + " scaleFromDetectorCenterX = " + scaleDetectorXCenter
                        + " scaleFromDetectorCenterY = " + scaleDetectorYCenter);
                Log.i(TAG, "onDraw: ZOOM OFFSET : X = " + offsetX + " Y = " + offsetY);
                break;
            case ZOOM_POINT:
                scaleDetectorXCenter = scaleDetectorXCenter * (originalBitmapWidth / measureWidth)
                        * originalScale + measureWidth;
                scaleDetectorYCenter = scaleDetectorYCenter * (originalBitmapHeight / measureHeight)
                        * originalScale + measureHeight;
                Log.i(TAG, "onDraw: ZOOM_POINT");
                canvas.scale(scaleDetector,
                        scaleDetector,
                        scaleDetectorXCenter,
                        scaleDetectorYCenter);
                previousScaleDetectorXCenter = scaleDetectorXCenter;
                previousScaleDetectorYCenter = scaleDetectorYCenter;
                offsetX = 0;
                offsetY = 0;
                Log.i(TAG, "onDraw: ZOOM_POINT : scaleFromDetector = " + scaleDetector
                        + " scaleFromDetectorCenterX = " + scaleDetectorXCenter
                        + " scaleFromDetectorCenterY = " + scaleDetectorYCenter);
        }

        layer = layerMap(originalMap, 0, 0, originalScale);
        canvas.translate((canvas.getWidth() - layer.getWidth()) / 2,
                (canvas.getHeight() - layer.getHeight()) / 2);
        canvas.drawBitmap(layer, 0, 0, null);
        layer = layerTacks(mapPoints, originalScale);
        canvas.drawBitmap(layer, 0, 0, null);
    }

    private void fitBitmapToScreen(Bitmap bitmap) {
        if ((measureWidth == 0) || (measureHeight == 0)) return;
        float origScaleX = (float) measureHeight / bitmap.getHeight();
        float origScaleY = (float) measureWidth / bitmap.getWidth();
        originalScale = Math.min(origScaleX, origScaleY);

    }


    /**
     * Setting max scale.
     * Keep in mind that too big
     * scale can cause problems with rendering
     * on canvas.
     *
     * @param maxScale scale which will be set a maximal
     */
    public void setMaxScale(float maxScale) {
        scaleDetectorMAX = maxScale;
    }

    /**
     * Setting minimum scale allowed
     *
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
        switch (type) {
            case 1:
                texture = BitmapFactory.decodeResource(context.getResources(), R.drawable.start_tack);
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
     * get maps textures
     *
     * @param model object that implements MapModel interface
     */
    public void setModel(MapModel model) {
        this.model = model;
    }


    /**
     * Add mappoint which will be drawn
     * immediately with previous added tacks.
     *
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
     *
     * @param mapPoint object to remove
     */
    public void removeMapPoint(MapPoint mapPoint) {
        mapPoints.remove(mapPoint);
        mapPointsTypes.remove(mapPoint);
        invalidate();
    }

    /**
     * Set trace to be drawn as path to destined place
     *
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
            if (previousScaleDetectorXCenter == 0) {
                scaleDetectorXCenter = detector.getFocusX();
                scaleDetectorYCenter = detector.getFocusY();
            } else {
                scaleDetectorXCenter = detector.getFocusX() + (previousScaleDetectorXCenter - detector.getFocusX());
                scaleDetectorYCenter = detector.getFocusY() + (previousScaleDetectorYCenter - detector.getFocusY());
            }
            Log.i(TAG, "onScale: scaleFdetector: " + scaleDetector);
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

