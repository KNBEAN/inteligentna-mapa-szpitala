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
import bean.pwr.imskamieskiego.R;
import static android.content.ContentValues.TAG;


public class MapDrawer extends View {

    Bitmap origbitmap;
    Paint paint;
    MapModel model;
    Context context;
    Bitmap [] tacktexture;
    PointF pointFlast = new PointF();
    PointF pointFstart = new PointF();
    ArrayList <TackObject> tackObjects;
    ArrayList <Integer> tackTypes;
    boolean horizontal;
    int [][] trackpoints;
    int measurewidth,measureheight;
    int bmwidth,bmheight,origbmwidth,origbmheight;
    float origScale,scaleX,scaleY;
    float deltaX,deltaY;
    float offsetX,offsetY;
    ScaleGestureDetector mScaleDetector;

    float scaleFDetector;
    float scaleFDetectorXcenter;
    float scaleFDetectorYcenter;
    float scaleFDetectorMAX = 3.f;
    float scaleFDetectorMIN = 0.5f;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static int MODE;


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

        setTacktexture();

        paint=new Paint();

        origScale = 1;

        scaleX = 1 ;

        scaleY = 1 ;

        offsetX = 0;

        offsetY = 0;

        scaleFDetector = 1.f;

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mScaleDetector.onTouchEvent(event);

                if (mScaleDetector.isInProgress()) return true;

                PointF pointF=new PointF(event.getX(),event.getY());

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN :

                        pointFlast=pointF;

                        pointFstart=pointF;

                        MODE = DRAG;

                        offsetX += deltaX;

                        offsetY += deltaY;

                        break;

                    case MotionEvent.ACTION_MOVE :

                        if (MODE == DRAG){

                            deltaX = pointF.x - pointFlast.x;

                            deltaY = pointF.y - pointFlast.y;

                                invalidate();

                                //temporary statement to stop map getting out of sight
                                // adding animation for smoother back

                                if ((Math.abs(deltaX+offsetX)>origScale*bmwidth*scaleFDetector/3) || (Math.abs(deltaY+offsetY)>origScale*bmheight*scaleFDetector/3))
                            {
                                deltaX = 0;
                                deltaY = 0;
                                return true;
                            }
                        }

                        
                }
                return true;
            }
        });
    }

    /** Setting map to be drawn on canvas
     * and getting dimensions from bitmap
     * to set up scale parameters
     */
    public void setMap(Bitmap bitmap){

        if (bitmap != null) {

            origbitmap=bitmap;

            Log.i(TAG, "setMap: Bitmap for layer map set");

            bmheight=bitmap.getHeight();

            bmwidth= bitmap.getWidth();

            origbmwidth=bmwidth;

            origbmheight=bmheight;
        }
        
    }

    private Bitmap LayerMap(Bitmap origbitmap,int xfrom, int yfrom,float scale){

        Matrix tempmatrix=new Matrix();

        Bitmap result;

        tempmatrix.postScale(scale,scale);
        try {
            result=Bitmap.createBitmap(origbitmap,xfrom,yfrom,origbmwidth,origbmheight,tempmatrix,true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }
    private Bitmap LayerPath(){

        Bitmap result=Bitmap.createBitmap(measurewidth,measureheight, Bitmap.Config.ARGB_8888);

        if (trackpoints != null) {

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



    private Bitmap LayerTacks(ArrayList <TackObject> tackObjects,float scale){

        Bitmap result = Bitmap.createBitmap(measurewidth,measureheight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);

        if (!tackObjects.isEmpty())
        {
            for (TackObject tack:tackObjects)
            canvas.drawBitmap(tacktexture[tackObjects.indexOf(tack)],tack.getX()*scale,tack.getY()*scale,null);
        }

     return result;
    }




    @Override
    protected void onDraw(Canvas canvas) {

        if (origbitmap==null) return;

        Bitmap layer;

        switch (MODE) {

            case NONE:

                Log.i(TAG, "onDraw: NONE");

                fitBitmapToScreen(origbitmap);

                break;

            case DRAG:

                Log.i(TAG, "onDraw: DRAG");

                canvas.scale(scaleFDetector,scaleFDetector,scaleFDetectorXcenter,scaleFDetectorYcenter);

                canvas.translate(deltaX + offsetX ,deltaY + offsetY);

                Log.i(TAG, "onDraw: DRAG x = " + deltaX + " offset x = " + offsetX + " y = " + deltaY + " offset x = " + offsetY);

                break;

            case ZOOM:

                Log.i(TAG, "onDraw: ZOOM");

                canvas.scale(scaleFDetector,scaleFDetector,scaleFDetectorXcenter,scaleFDetectorYcenter);

                canvas.translate(offsetX,offsetY);

                break;

        }

        layer = LayerMap(origbitmap,0,0,origScale);

        canvas.translate((canvas.getWidth()-layer.getWidth())/2,(canvas.getHeight()-layer.getHeight())/2);

        canvas.drawBitmap(layer,0,0,null);

        layer = LayerTacks(tackObjects,origScale);

        canvas.drawBitmap(layer,0,0,null);

        layer= LayerPath();

        canvas.drawBitmap(layer,0,0,null);


    }

    void fitBitmapToScreen(Bitmap bitmap)
    {
        if ((measurewidth==0) || (measureheight==0) ) return;
            float origScaleX = (float) measureheight / bitmap.getHeight();
            float origScaleY= (float) measurewidth / bitmap.getWidth();
            origScale =  Math.min(origScaleX,origScaleY);

    }

    /**
     * Setting max scale.
     * Have in mind that too big
     * scale can cause problems with rendering
     * on canvas.
     */
    void setMaxScale(float maxScale)
    {
        scaleFDetectorMAX=maxScale;
    }

    /**
     * Setting min scale
     */
    void setMinScale(float minScale)
    {
        scaleFDetectorMIN=minScale;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.i(TAG, "onMeasure: START");

        measurewidth = MeasureSpec.getSize(widthMeasureSpec);

        measureheight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(measurewidth,measureheight);

        if (measureheight>measurewidth) horizontal=false;

        if (measurewidth>measureheight) horizontal=true;

        MODE = NONE;
    }



    void setTacktexture() //This void is only for current tests
    {
        tacktexture=new Bitmap[2];
        tacktexture[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.small);
        tacktexture[1]= Bitmap.createBitmap(tacktexture[1],0,0,50,50);
        tacktexture[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.small);
        tacktexture[0]= Bitmap.createBitmap(tacktexture[0],0,0,50,50);
        tackObjects=new ArrayList<>();
        tackTypes= new ArrayList<>();
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
    public void addMapPoint(TackObject tack, Integer type){

        tackObjects.add(tack);

        tackTypes.add(type);

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
        for (TackObject x:tackObjects)
        {
            Log.i(TAG, "addMapPoint: Current Point : " +tackObjects.indexOf(x)+" x: " + x.getX() +" and y: "+x.getY());
        }
        for (Integer x:tackTypes)
        {
            Log.i(TAG, "addMapPoint: Current Type : " + tackTypes.indexOf(x)+" type: " + x.toString());
        }
    }

    /**
     * Clear mappoint arraylist
     */
    public void removeAllTacks(){

        tackObjects.clear();

        tackTypes.clear();

        invalidate();
    }

    /**
     * Remove object from mappoint arraylist
     * with given index
     */
    public void removeMapTack(int index){
        if (index<tackObjects.size())
        {
            tackObjects.remove(index);
            tackTypes.remove(index);
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
    public void setTrace(int [][] trace){
        trackpoints = trace;
        invalidate();
    }
    /**
     * Clear trace to be drawn as path to destined place
     */
    public void removeTrace(){
        trackpoints = null;
        invalidate();
    }

    /**
     * Show map
     * If floor exceeds size, try getting last floor
     */
    public void showFloor(int floor){    }



    /**
    * INNER CLASS WITH ALL METHODS
    * TO RESPONSE TO MOTIONEVENTS
    */

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {


    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        if ((scaleFDetectorMAX<scaleFDetector*detector.getScaleFactor())||(scaleFDetectorMIN>scaleFDetector*detector.getScaleFactor())) return true;

            scaleFDetector=scaleFDetector*detector.getScaleFactor();

        scaleFDetectorXcenter=detector.getFocusX();

        scaleFDetectorYcenter=detector.getFocusY();

        Log.i(TAG, "onScale: scaleFdetector: " + scaleFDetector);
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

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}


}

