package bean.pwr.imskamieskiego.MapGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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
    Bitmap currtackbitmap=null;
    Matrix canvasMatrix;
    ArrayList <TackObject> tackObjects;
    int [][] trackpoints;
    int measurewidth,measureheight;
    int bmwidth,bmheight;
    float scaleX,scaleY,ratio;
    int redundantSpaceX,redundantSpaceY;

    ScaleGestureDetector mScaleDetector;
    public GestureDetector mGestureDetector;

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
     */
    void SharedConstructing()
    {
        setClickable(true);
        paint=new Paint();
        setTacktexture();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector=new GestureDetector(context,new ScaleListener());
        setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                mScaleDetector.onTouchEvent(event);
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
    public void setMap(Bitmap bitmap)
    {

        if (bitmap != null) {
            this.bitmap=bitmap;
            Log.i(TAG, "setMap: Bitmap set");
            bmheight=bitmap.getHeight();
            bmwidth= bitmap.getWidth();
            setBitmapScale();
        }
        
    }




    private Bitmap rescaleBitmap(){


            Matrix matrix = new Matrix();

            float scale= Math.min(scaleX,scaleY);

            matrix.postScale(scale,scale);

            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0,
                    bmwidth, bmheight,
                    matrix, false);

            return resizedBitmap;

    }

    /**
     * Setting bitmap in center by adjusting to onMeasure params
     */
    private void centerBitmap(){

        bmheight=bitmap.getHeight();

        bmwidth=bitmap.getWidth();

        redundantSpaceX=(measurewidth-bmwidth)/2;

        redundantSpaceY=(measureheight-bmheight)/2;
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

    /** Main function to create one bitmap from path,
     * map and tacks
     * for later manipulations
     * caused by onTouch events
     * @return Bitmap
     */
    private Bitmap connectLayers(){

        Bitmap result=Bitmap.createBitmap(measurewidth,measureheight,bitmap.getConfig());
        Canvas canvas = new Canvas(result);
        bitmap = rescaleBitmap();
        centerBitmap();

        if (bitmap != null) {
            canvas.drawBitmap(bitmap,
                    redundantSpaceX,
                    redundantSpaceY,
                    null);

            Log.i(TAG, "onDraw: Map drawn");
        }

        // TO OVERRIDE with MAPPOINT and customized attributes to paint
        if (trackpoints != null) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10f);
            Path path = new Path();
            path.moveTo(trackpoints[0][0], trackpoints[0][1]);
            for (int i = 0; i < trackpoints.length; i++) {
                path.lineTo(trackpoints[i][0],trackpoints[i][1]);
            }
            canvas.drawPath(path,paint);
        }

        if (!tackObjects.isEmpty()){

            for (TackObject currtack:tackObjects){
                canvas.drawBitmap(currtackbitmap,
                        currtack.getX()-(currtackbitmap.getWidth()/2),
                        currtack.getY()-(currtackbitmap.getHeight()/2),
                        null);
            }

        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bitmap=connectLayers();
        canvas.drawBitmap(bitmap,0,0,null);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measurewidth = MeasureSpec.getSize(widthMeasureSpec);

        measureheight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(measurewidth,measureheight);
        setBitmapScale();
    }


    void setBitmapScale()
    {
        if ((measurewidth==0) || (measureheight==0)) return;
        scaleY=(float)measureheight/bmheight;
        scaleX=(float)measurewidth/bmwidth;
        ratio=(float)bmwidth/bmheight;
    }

    void setTacktexture() //This void is only for current tests
    {
        tacktexture=new Bitmap[2];
        tacktexture[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.blackcircle);
        tacktexture[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.blackcircle);
        tackObjects=new ArrayList<>();
        canvasMatrix=new Matrix();
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
    void removeAllTacks(){}
    void removeMapTack(){}
    //void setPath(ArrayList<MapPoint> path){};
    //void addMapPoint(MapPoint point, rodzajznacznika);
    /**
     * Show map
     * If floor exceeds size, try getting last floor
     * @param floor
     */
    public void showFloor(int floor){    }

}
