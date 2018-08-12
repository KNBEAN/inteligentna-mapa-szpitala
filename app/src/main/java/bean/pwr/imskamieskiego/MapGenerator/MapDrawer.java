package bean.pwr.imskamieskiego.MapGenerator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
<<<<<<< HEAD
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
=======
import android.graphics.Matrix;
>>>>>>> Proper position on map sent to listener
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
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
    private int desiredWidth;
    private int desiredHeight;
    private int currentlyDisplayedFloor;
    private float originalScale;
    private float deltaX, deltaY;
    private float offsetX, offsetY;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleDetector = 1;
    private ArrayList<Bitmap> tackTextures;
    private float scaleDetectorMAX = 3.f;
    private float scaleDetectorMIN = 0.5f;
    private PointF pointToShow;
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private final int ZOOM_POINT = 3;
    private int mode;
    private int resourceTacksId[];
<<<<<<< HEAD

    private Paint paintPath;

=======
    private int tackWidth = 100;
    private int tackHeight = 100;
    Matrix canvasMatrix, invertedCanvasMatrix;
>>>>>>> Proper position on map sent to listener
    MapDrawerGestureListener mMapDrawerGestureListener;


    public MapDrawer(Context context) {
        super(context);
        this.context = context;
        SharedConstructing();
    }

    public MapDrawer(Context context, AttributeSet attrs) {
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
        scaleDetector = 1.f;
        pathPoints = new ArrayList<>();
        mapPoints = new ArrayList<>();
        mapPointsTypes = new Hashtable<>();
        pointToShow = new PointF();
<<<<<<< HEAD


        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
=======
        canvasMatrix = new Matrix();
>>>>>>> Proper position on map sent to listener
        gestureDetector  = new GestureDetector(context,new GestureListener());


        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                gestureDetector.onTouchEvent(event);


                if (scaleGestureDetector.isInProgress()) return true;

                PointF pointF = new PointF(event.getX(), event.getY());
             //   Log.i(TAG, "onTouch: event.getX() = " + event.getX() + " event.getY() = " + event.getY());

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
                        Log.i(TAG, "onTouch: ACTION_UP");
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
     * @param point point to center on and zoom
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
        try {
            result = Bitmap.createScaledBitmap(originalMap,
                    desiredWidth,
                    desiredHeight,
                    false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }


    private Bitmap layerPathAndTacks(ArrayList<MapPoint> mapObjects, ArrayList<MapPoint> pathPoints, Bitmap floorToDrawOn) {
        Canvas canvas = new Canvas(floorToDrawOn);

        if (!pathPoints.isEmpty()) {
            Path path = new Path();
            for (MapPoint point : pathPoints) {
                if (point.getFloor() == currentlyDisplayedFloor) {
                    if (path.isEmpty()) {
                        path.moveTo(point.getX() / originalScale, point.getY() / originalScale);
                    } else {
                        path.lineTo(point.getX() / originalScale, point.getY() / originalScale);
                    }
                }
            }
            paintPath.setStrokeWidth(17f);
            paintPath.setShadowLayer(5f, 0, 0, Color.BLACK);
            paintPath.setColor(Color.WHITE);
            canvas.drawPath(path, paintPath);
            paintPath.clearShadowLayer();
            paintPath.setColor(Color.rgb(59,196,226));
            paintPath.setStrokeWidth(12f);
            canvas.drawPath(path, paintPath);
        }


        if (!mapObjects.isEmpty()) {
            for (MapPoint point : mapObjects) {
                if (point.getFloor() == currentlyDisplayedFloor) {
                    canvas.drawBitmap(getTackTexture(mapPointsTypes.get(point)),
                            point.getX() / originalScale
                                    - (getTackTexture(mapPointsTypes.get(point)).getWidth() / 2),
                            point.getY() / originalScale
                                    - (getTackTexture(mapPointsTypes.get(point)).getHeight() / 2),
                            null);
                }
            }
        }
        return floorToDrawOn;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (originalMap == null) return;
        Bitmap layer;
        fitMapToScreen(originalMap);
        layer = layerMap(originalMap);
        canvasMatrix.reset();
        canvas.concat(canvasMatrix);

        switch (mode) {

            case NONE:
                Log.i(TAG, "onDraw: NONE");
                break;

            case DRAG:
                Log.i(TAG, "onDraw: DRAG");
                canvasMatrix.postScale(scaleDetector,scaleDetector,measureWidth/2,measureHeight/2);
                canvasMatrix.postTranslate(deltaX+offsetX,deltaY+offsetY);
                Log.i(TAG, "onDraw: DRAG x = " + deltaX + " offset x = " +
                        offsetX + " y = " + deltaY + " offset x = " + offsetY);
                break;

            case ZOOM:
                Log.i(TAG, "onDraw: ZOOM");
                canvasMatrix.postScale(scaleDetector,scaleDetector,measureWidth / 2, measureHeight / 2);
                canvasMatrix.postTranslate(offsetX,offsetY);
                Log.i(TAG, "onDraw: ZOOM : scaleDetector = " + scaleDetector);
                Log.i(TAG, "onDraw: ZOOM OFFSET : X = " + offsetX + " Y = " + offsetY);
                break;
            case ZOOM_POINT:
                Log.i(TAG, "onDraw: ZOOM_POINT");
                pointToShow.x = (measureWidth / 2 - pointToShow.x / originalScale);
                pointToShow.y = (measureHeight / 2 - pointToShow.y / originalScale);
                pointToShow.x += (-measureWidth + desiredWidth) / 2;
                pointToShow.y += (-measureHeight + desiredHeight) / 2;
                Log.i(TAG, "onDraw: ZOOM_POINT point to show = " + pointToShow);
               canvasMatrix.postScale(scaleDetector,scaleDetector,measureWidth/2,measureHeight/2);
               canvasMatrix.postTranslate(pointToShow.x,pointToShow.y);
                offsetX = pointToShow.x;
                offsetY = pointToShow.y;
                Log.i(TAG, "onDraw: ZOOM_POINT : scaleFromDetector = " + scaleDetector);
        }
<<<<<<< HEAD

        canvas.translate((canvas.getWidth() - layer.getWidth()) / 2,
                (canvas.getHeight() - layer.getHeight()) / 2);
        layer = layerPathAndTacks(mapPoints, pathPoints, layer);
=======
        canvasMatrix.postTranslate((canvas.getWidth() - layer.getWidth()) / 2,(canvas.getHeight() - layer.getHeight()) / 2);
        canvas.concat(canvasMatrix);
        layer = layerTacks(mapPoints,layer);
>>>>>>> Proper position on map sent to listener
        canvas.drawBitmap(layer, 0, 0, null);
    }

    private void fitMapToScreen(Bitmap bitmap) {
        if ((measureWidth == 0) || (measureHeight == 0)) return;

        float originalBitmapWidth = bitmap.getWidth();
        float originalBitmapHeight = bitmap.getHeight();
        originalScale = Math.max(
                originalBitmapHeight / (float) measureHeight,
                originalBitmapWidth / (float) measureWidth);
        desiredWidth = (int) (originalBitmapWidth / originalScale);
        desiredHeight = (int) (originalBitmapHeight / originalScale);
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

        Log.i(TAG, "onMeasure: START");
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
        if (mode != ZOOM_POINT) {
            mode = NONE;
        }
    }

    private void loadTackTextures() {
        Bitmap texture;
        int tackWidth = 100;
        int tackHeight = 100;
        tackTextures = new ArrayList<>();
        try {
            for (int resourceName : resourceTacksId) {
                texture = BitmapDecoder.decodeSampledBitmapFromResource(context.getResources()
                        , resourceName
                        , tackWidth
                        , tackHeight);
                tackTextures.add(Bitmap.createScaledBitmap(texture, (tackWidth), (tackHeight), false));
            }
        } catch (Exception o) {
            Log.i(TAG, "loadTackTextures: No info about resources in .xml file. Use addTackResources() to" +
                    " avoid problems with rendering map");
        }
    }

    private Bitmap getTackTexture(int type) {
        return tackTextures.get(type);
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
     * @param type determines the texture set to given point basing on previously sent attributes
     *  in xml file or resources in method addTackResources
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

    /**
     * Set observer for MapDrawer to get ongoing
     * gestures in widget
     * @param mapDrawerGestureListener
     */
    public void setOnLongPressListener(MapDrawerGestureListener mapDrawerGestureListener){
        this.mMapDrawerGestureListener = mapDrawerGestureListener;
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

    private class GestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            invertedCanvasMatrix = new Matrix(canvasMatrix);
            invertedCanvasMatrix.invert(invertedCanvasMatrix);
            e.transform(invertedCanvasMatrix);
            int x = (int) e.getX();
            int y = (int) e.getY();
            mMapDrawerGestureListener.onLongPress(x,y);
        }

    }


}

