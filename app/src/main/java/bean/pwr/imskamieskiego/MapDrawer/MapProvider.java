package bean.pwr.imskamieskiego.MapDrawer;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;


public class MapProvider {
    final private String MAP_PROVIDER = "map_provider";
    private BitmapRegionDecoder bitmapRegionDecoder;
    private MutableLiveData<RegionBitmap> regionBitmap;
    final private Observer<InputStream> floorMapObserver;
    private Rect currentRect;
    private Rect mapRect;
    private boolean isDecoding;
    private Thread decodeThread;

    public MapProvider(Context context, LiveData<InputStream> inputStream){
        isDecoding = false;
        floorMapObserver = new Observer<InputStream>() {
            @Override
            public void onChanged(@Nullable InputStream inputStream) {
                Log.i(MAP_PROVIDER, "MapProvider: inputStream changed!");
                try {
                   bitmapRegionDecoder= bitmapRegionDecoder.newInstance(inputStream,true);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bitmapRegionDecoder!= null){
                        currentRect = new Rect();
                        mapRect = new Rect();
                        currentRect.right = bitmapRegionDecoder.getWidth();
                        currentRect.bottom = bitmapRegionDecoder.getHeight();
                        mapRect.set(currentRect);
                        RegionBitmap emptyRegion = new RegionBitmap(null,currentRect,false);
                        regionBitmap.postValue(emptyRegion);
                    }
                }
            }
        };
        inputStream.observe((LifecycleOwner) context,floorMapObserver);

    }

    private class BitmapDecoderTask extends AsyncTask<Rect,Void,RegionBitmap> {

        BitmapRegionDecoder bitmapRegionDecoder;

        public BitmapDecoderTask(BitmapRegionDecoder bitmapRegionDecoder){
            this.bitmapRegionDecoder = bitmapRegionDecoder;
        }

        @Override
        protected RegionBitmap doInBackground(Rect... rects) {
            Log.i(MAP_PROVIDER, "DECODE_REGION: START");
            Rect rect = rects[0];
            Bitmap bitmap;
            try {
                bitmap = bitmapRegionDecoder.decodeRegion(rect, null);
            } catch (NullPointerException e) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } catch (IllegalArgumentException e) {
                return null;
            }
            RegionBitmap regionBitmap = new RegionBitmap(bitmap, rect, true);
            return regionBitmap;
        }

        @Override
        protected void onPostExecute(RegionBitmap regionBitmap) {
            MapProvider.this.regionBitmap.postValue(regionBitmap);
        }
    }



    private Rect matchRectToDecoder(Rect rect,int width, int height){
        if (rect.bottom>height) rect.bottom =height;
        if (rect.top<0) rect.top = 0;
        if (rect.left<0) rect.left = 0;
        if (rect.right>width) rect.right = width;
        return rect;
    }

    public LiveData<RegionBitmap> getRegionBitmap() {
        if (regionBitmap == null){
            regionBitmap = new MutableLiveData<>();
        }
        return regionBitmap;
    }


    public void setRegionBitmap(Rect rect){
            currentRect.set(rect);
            BitmapDecoderTask bitmapDecoderTask = new BitmapDecoderTask(this.bitmapRegionDecoder);
            bitmapDecoderTask.execute(currentRect);

    }

    public Rect getCurrentRect(){
        return currentRect;
    }

}
