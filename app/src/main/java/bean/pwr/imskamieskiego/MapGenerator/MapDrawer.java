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

    Bitmap bitmap;
    Paint paint;
    Context context;
    Bitmap [] tacktexture;
    Bitmap connectedBitmap;
    Bitmap currtackbitmap;
    Bitmap mapbitmap;
    Matrix canvasMatrix;
    Matrix bitmapmatrix;
    PointF pointFlast = new PointF();
    PointF pointFstart = new PointF();
    ArrayList <TackObject> tackObjects;

    int [][] trackpoints;
    int measurewidth,measureheight;
    int bmwidth,bmheight,origbmwidth,origbmheight;
    float origScaleX,origScaleY,scaleX,scaleY,ratio;
    int redundantSpaceX,redundantSpaceY;

    ScaleGestureDetector mScaleDetector;

    float scaleFDetector;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static int MODE;

    enum Types {
        START(1),
        FINISH(2);
        private int typo;
        Types(int i) {
            this.typo=i;
        }
    }


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
        paint=new Paint();
        setTacktexture();

        origScaleX=0;

        origScaleY=0;

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mScaleDetector.onTouchEvent(event);

                PointF pointF=new PointF(event.getX(),event.getY());

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN :

                        pointFlast=pointF;

                        pointFstart=pointF;

                        MODE = DRAG;
                        Log.i(TAG, "onTouch: MODE == DRAG");
                        break;

                    case MotionEvent.ACTION_MOVE :
                        if (MODE == DRAG){

                            float deltaX = pointF.x - pointFlast.x;

                            float deltaY = pointF.y - pointFlast.y;

                            invalidate();
                        }

                }
                return true;
            }
        });
    }

    /**
     * Setting map to be drawn on canvas
     * and getting dimensions from bitmap
     * to set up scale parameters
     * @param bitmap
     */
    public void setMap(Bitmap bitmap){

        if (bitmap != null) {
            this.bitmap=bitmap;
            Log.i(TAG, "setMap: Bitmap set");
            bmheight=bitmap.getHeight();
            bmwidth= bitmap.getWidth();
            origbmwidth=bmwidth;
            origbmheight=bmheight;
        }
        
    }


    /* Scale bitmap to scaleY and scaleX */
    private Bitmap rescaleBitmap(){

            if ((origScaleY==scaleY) || (origScaleX==scaleX)) return mapbitmap;

            bitmapmatrix = new Matrix();

            float scale= Math.min(scaleX,scaleY);

            bitmapmatrix.postScale(scale,scale);

            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0,
                    bmwidth, bmheight,
                    bitmapmatrix, false);

                origScaleX=scaleX;

                origScaleY=scaleY;

            return resizedBitmap;

    }

    /**
     * Setting bitmap in center by adjusting to onMeasure params
     */
    private void centerBitmap(Bitmap bitmap){

        int  bmheight=bitmap.getHeight();

        int  bmwidth=bitmap.getWidth();

        redundantSpaceX=(measurewidth-bmwidth)/2;

        redundantSpaceY=(measureheight-bmheight)/2;
    }



    /** Main function to create one bitmap from path,
     * map and tacks
     * for later manipulations
     * caused by onTouch events
     * @return Bitmap
     */
    private Bitmap connectLayers(){

        Bitmap result=Bitmap.createBitmap(measurewidth,measureheight,bitmap.getConfig());
        Canvas canvas = new Canvas(result);
        mapbitmap = rescaleBitmap();
        centerBitmap(mapbitmap);

        if (mapbitmap != null) {
            canvas.drawBitmap(mapbitmap,
                    redundantSpaceX,
                    redundantSpaceY,
                    null);

            Log.i(TAG, "connectLayers: Map drawn");
        }
        else return bitmap;

        // TO OVERRIDE with MAPPOINT and customized attributes to paint
        if (trackpoints != null) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10f);
            Path path = new Path();
            path.moveTo(trackpoints[0][0]+redundantSpaceX, trackpoints[0][1]+redundantSpaceY);
            for (int i = 0; i < trackpoints.length; i++) {
                path.lineTo(trackpoints[i][0]+redundantSpaceX,trackpoints[i][1]+redundantSpaceY);
            }
            canvas.drawPath(path,paint);

            Log.i(TAG, "connectLayers: Path drawn");
        }

        if (!tackObjects.isEmpty()){

            for (TackObject currtack:tackObjects){
                canvas.drawBitmap(currtackbitmap,
                        currtack.getX()-(currtackbitmap.getWidth()/2)+redundantSpaceX,
                        currtack.getY()-(currtackbitmap.getHeight()/2)+redundantSpaceY,
                        null);
            }

            Log.i(TAG, "connectLayers: Tacks drawn");

        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {

            if (bitmap==null) return;
        switch (MODE) {

            case NONE:

            Log.i(TAG, "onDraw: NONE");
            setBitmapScale(bitmap);
            connectedBitmap = connectLayers();
            canvas.drawBitmap(connectedBitmap, 0, 0, null);
            break;

            case DRAG:
                Log.i(TAG, "onDraw: DRAG");

                break;

            case ZOOM:
                setScale(scaleFDetector);
                connectedBitmap = connectLayers();
                canvas.drawBitmap(connectedBitmap, 0, 0, null);
                Log.i(TAG, "onDraw: ZOOM");
                break;
        }






    }

    void setBitmapScale(Bitmap bitmap)
    {
        if ((measurewidth==0) || (measureheight==0) ) return;
            scaleY = (float) measureheight / bitmap.getHeight();
            scaleX = (float) measurewidth / bitmap.getWidth();
            ratio = (float) bitmap.getWidth() / bitmap.getHeight();

    }

    void setScale(float scalefactor)
    {
        scaleX=scaleX*scalefactor;
        Log.i(TAG, "setScale: scaleX: "+scaleX);
        scaleY=scaleY*scalefactor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.i(TAG, "onMeasure: START");

        MODE = NONE;

        measurewidth = MeasureSpec.getSize(widthMeasureSpec);

        measureheight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(measurewidth,measureheight);

        setBitmapScale(bitmap);

    }



    void setTacktexture() //This void is only for current tests
    {
        tacktexture=new Bitmap[2];
        tacktexture[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.blackcircle);
        tacktexture[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.blackcircle);
        tackObjects=new ArrayList<>();
    }

    public void setModel(MapModel model){

    }
    public void addMapPoint(TackObject tack, int rodzaj){
        tackObjects.add(tack);
        if (rodzaj==Types.START.typo)
        {
         currtackbitmap= tacktexture[1];
        }
        else {
            currtackbitmap=tacktexture[2];
        }

        invalidate();
    }

    void removeAllTacks(){
        tackObjects.removeAll(tackObjects);
        invalidate();
    }

    void removeMapTack(){
        tackObjects.remove(tackObjects.size()-1);
        invalidate();
    }
    /**
     * Set trace to be drawn as path to destined place
     * @param trace
     */
    public void setTrace(int [][] trace){
        trackpoints=trace;
        invalidate();
    }
    /**
     * Clear trace to be drawn as path to destined place
     */
    public void removeTrace(){
        trackpoints=null;
        invalidate();
    }
    //void setPath(ArrayList<MapPoint> path){};
    //void addMapPoint(MapPoint point, rodzajznacznika);
    /**
     * Show map
     * If floor exceeds size, try getting last floor
     * @param floor
     */
    public void showFloor(int floor){    }



    /**
    * INNER CLASS WITH ALL METHODS
    * TO RESPONSE TO MOTIONEVENTS
    */

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFDetector=detector.getScaleFactor();
        invalidate();
        Log.i(TAG, "onScale: "+ scaleFDetector);
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
        //Log.i(TAG, "onScroll: TRUE");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress: TRUE");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Log.i(TAG, "onFling: TRUE");
        return false;
    }
}


}

