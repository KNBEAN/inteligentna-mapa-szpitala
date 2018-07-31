package bean.pwr.imskamieskiego.MapGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.app.WindowDecorActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import java.util.ArrayList;
import java.util.Map;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.MapPoint;

import static android.content.ContentValues.TAG;


public class MapDrawer extends View {

    private Bitmap originalMap;
    private Paint paint;
    private MapModel model;
    private Context context;
    private Bitmap [] tackTexture;
    private PointF pointFlast = new PointF();
    private PointF pointFstart = new PointF();
    private ArrayList <MapPoint> mapObjects;
    private ArrayList <Integer> mapObjectTypes;
    private boolean horizontal;
    private ArrayList<MapPoint> pathPoints;
    private int measureWidth,measureHeight;
    private int bitmapWidth,bitmapHeight,originalBitmapWidth,originalBitmapHeight;
    private float originalScale,scaleX,scaleY;
    private float deltaX,deltaY;
    private float offsetX,offsetY;
    private ScaleGestureDetector mScaleDetector;

    float scaleFromDetector;
    float scaleFromDetectorXcenter;
    float scaleFromDetectorYcenter;
    float scaleFromDetectorMAX = 3.f;
    float scaleFromDetectorMIN = 0.5f;

   final int NONE = 0;
   final int DRAG = 1;
   final int ZOOM = 2;
   int MODE;


    public MapDrawer(Context context) {
        super(context);
        this.context=context;
        SharedConstructing();
    }

    public MapDrawer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        SharedConstructing();
    }

    /**
     * Operations common to both constructors
     * -SET TEXTURES FOR TACKS
     * -CREATE DETECTORS
     * -CREATE PAINT
     * -SEND MOTIONEVENTS TO LISTENER
     */
    void SharedConstructing() {

        setClickable(true);
        setTackTexture();
        paint=new Paint();

        originalScale = 1;
        scaleX = 1 ;
        scaleY = 1 ;
        offsetX = 0;
        offsetY = 0;
        scaleFromDetector = 1.f;
        pathPoints = new ArrayList<MapPoint>();

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mScaleDetector.onTouchEvent(event);

                if (mScaleDetector.isInProgress()) return true;

                PointF pointF=new PointF(event.getX(),event.getY());

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN :

                        pointFlast = pointF;
                        pointFstart = pointF;
                        offsetX += deltaX;
                        offsetY += deltaY;
                        MODE = DRAG;
                        break;

                    case MotionEvent.ACTION_MOVE :

                        if (MODE == DRAG){

                            deltaX = pointF.x - pointFlast.x;
                            deltaY = pointF.y - pointFlast.y;

                                invalidate();

                                //temporary statement to stop map getting out of sight
                                // adding animation for smoother back

                                if ((Math.abs(deltaX+offsetX)>originalScale*bitmapWidth*scaleFromDetector/3) || (Math.abs(deltaY+offsetY)>originalScale*bitmapHeight*scaleFromDetector/3))
                            {
                                deltaX = 0;
                                deltaY = 0;
                                return true;
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    /** Setting map to be drawn on canvas
     * and getting dimensions from bitmap
     * to set up scale parameters
     * If @floor exceeds size of
     * floors list method gets the last one floor
     * map possible
     */
    public void showFloor(int floor){
        try {
            originalMap=model.getFloorMap(floor);
        }
        catch (Exception e)
        {
            originalMap = model.getFloorMap(model.getFloorCount()-1);
        }
            Log.i(TAG, "setMap: Bitmap for layer map set");
            bitmapHeight = originalMap.getHeight();
            bitmapWidth = originalMap.getWidth();
            originalBitmapWidth = bitmapWidth;
            originalBitmapHeight = bitmapHeight;
    }

    private Bitmap layerMap(Bitmap origbitmap,int xfrom, int yfrom,float scale){

        Matrix tempMatrix=new Matrix();
        Bitmap result;
        tempMatrix.postScale(scale,scale);
        try {
            result=Bitmap.createBitmap(origbitmap,xfrom,yfrom,originalBitmapWidth,originalBitmapHeight,tempMatrix,true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return result;
    }
    private Bitmap layerPath(){
        Bitmap result=Bitmap.createBitmap(measureWidth,measureHeight, Bitmap.Config.ARGB_8888);
        if (pathPoints != null) {
            Path path = new Path();
            /*
            ...
            */
        }
        else
        {
            return result;
        }
        return result;
    }



    private Bitmap layerTacks(ArrayList <MapPoint> tackObjects,float scale){

        Bitmap result = Bitmap.createBitmap(measureWidth,measureHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        if (!tackObjects.isEmpty())
        {
            for (MapPoint tack:tackObjects)
            canvas.drawBitmap(tackTexture[tackObjects.indexOf(tack)],tack.getX()*scale,tack.getY()*scale,null);
        }
     return result;
    }




    @Override
    protected void onDraw(Canvas canvas) {

        if (originalMap == null) return;
        Bitmap layer;
        switch (MODE) {

            case NONE:
                Log.i(TAG, "onDraw: NONE");
                fitBitmapToScreen(originalMap);
                break;

            case DRAG:
                Log.i(TAG, "onDraw: DRAG");
                canvas.scale(scaleFromDetector,scaleFromDetector,scaleFromDetectorXcenter,scaleFromDetectorYcenter);
                canvas.translate(deltaX + offsetX ,deltaY + offsetY);
                Log.i(TAG, "onDraw: DRAG x = " + deltaX + " offset x = " + offsetX + " y = " + deltaY + " offset x = " + offsetY);
                break;

            case ZOOM:
                Log.i(TAG, "onDraw: ZOOM");
                canvas.scale(scaleFromDetector,scaleFromDetector,scaleFromDetectorXcenter,scaleFromDetectorYcenter);
                canvas.translate(offsetX,offsetY);
                break;

        }

        layer = layerMap(originalMap,0,0,originalScale);
        canvas.translate((canvas.getWidth()-layer.getWidth())/2,(canvas.getHeight()-layer.getHeight())/2);
        canvas.drawBitmap(layer,0,0,null);

        layer = layerTacks(mapObjects,originalScale);
        canvas.drawBitmap(layer,0,0,null);

        layer= layerPath();
        canvas.drawBitmap(layer,0,0,null);


    }

    void fitBitmapToScreen(Bitmap bitmap)
    {
        if ((measureWidth==0) || (measureHeight==0) ) return;
            float origScaleX = (float) measureHeight / bitmap.getHeight();
            float origScaleY = (float) measureWidth / bitmap.getWidth();
            originalScale =  Math.min(origScaleX,origScaleY);

    }

    /**
     * Setting max scale.
     * Have in mind that too big
     * scale can cause problems with rendering
     * on canvas.
     */
    void setMaxScale(float maxScale)
    {
        scaleFromDetectorMAX = maxScale;
    }

    /**
     * Setting min scale
     */
    void setMinScale(float minScale)
    {
        scaleFromDetectorMIN = minScale;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.i(TAG, "onMeasure: START");
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth,measureHeight);

        if (measureHeight>measureWidth) horizontal=false;
        if (measureWidth>measureHeight) horizontal=true;

        MODE = NONE;
    }



    void setTackTexture() //This void is only for current tests
    {
        tackTexture=new Bitmap[2];
        tackTexture[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.small);
        tackTexture[1]= Bitmap.createBitmap(tackTexture[1],0,0,50,50);
        tackTexture[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.small);
        tackTexture[0]= Bitmap.createBitmap(tackTexture[0],0,0,50,50);
        mapObjects=new ArrayList<>();
        mapObjectTypes= new ArrayList<>();
    }

    /** Set object that implements or extends mapmodel interface
     */
    public void setModel(MapModel model){
        this.model=model;
    }

    /**
     * Add mappoint which will be drawn
     * immediately with previous added tacks.
     * Add type to determine image for tack.
     * Method's first argument has to be changed to
     * MapPoint class
     */
    public void addMapPoint(MapPoint tack, Integer type){

        mapObjects.add(tack);
        mapObjectTypes.add(type);
        Log.i(TAG, "addMapPoint: Point Added");
        invalidate();

    }

    /**
     * Return all currently saved tacks with their
     * types
     */
    public void showCurrentTacks()
    {
        Log.i(TAG, "showCurrentTacks: SHOWING");
        for (MapPoint x:mapObjects)
        {
            Log.i(TAG, "addMapPoint: Current Point : " +mapObjects.indexOf(x)+" x: " + x.getX() +" and y: "+x.getY());
        }
        for (Integer x:mapObjectTypes)
        {
            Log.i(TAG, "addMapPoint: Current Type : " + mapObjectTypes.indexOf(x)+" type: " + x.toString());
        }
    }

    /**
     * Clear mappoint arraylist
     */
    public void removeAllTacks(){

        mapObjects.clear();
        mapObjectTypes.clear();
        invalidate();
    }

    /**
     * Remove object from mappoint arraylist
     * with given index
     */
    public void removeMapTack(int index){
        if (index<mapObjects.size())
        {
            mapObjects.remove(index);
            mapObjectTypes.remove(index);
            invalidate();
        }
        else 
        {
            Log.e(TAG, "removeMapTack: index exceeds size");
        }
        
    }
    /**
     * Set trace to be drawn as path to destined place
     */
    public void setTrace(ArrayList<MapPoint> trace){
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
    * INNER CLASS WITH ALL METHODS
    * TO RESPONSE TO MOTIONEVENTS
    */

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener  {


    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        if ((scaleFromDetectorMAX<scaleFromDetector*detector.getScaleFactor())||(scaleFromDetectorMIN>scaleFromDetector*detector.getScaleFactor())) return true;

        scaleFromDetector=scaleFromDetector*detector.getScaleFactor();
        scaleFromDetectorXcenter=detector.getFocusX();
        scaleFromDetectorYcenter=detector.getFocusY();
        Log.i(TAG, "onScale: scaleFdetector: " + scaleFromDetector);
        invalidate();
        return true;

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.i(TAG, "onScaleBegins");
        MODE = ZOOM;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.i(TAG, "onScaleEnds");
    }

}


}

