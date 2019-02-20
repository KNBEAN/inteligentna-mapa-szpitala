package bean.pwr.imskamieskiego.MapDrawer;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class RegionBitmap {
    private Bitmap bitmap;
    private Rect regionRect;
    private boolean isDecodedAlready;

    public boolean isDecodedAlready() {
        return isDecodedAlready;
    }

    public void setDecodedAlready(boolean decodedAlready) {
        isDecodedAlready = decodedAlready;
    }



    public RegionBitmap (Bitmap regionBitmap, Rect regionRect){
        this.bitmap = regionBitmap;
        this.regionRect = regionRect;
    }


    public RegionBitmap (Bitmap regionBitmap, Rect regionRect, boolean isDecodedAlready){
        this.bitmap = regionBitmap;
        this.regionRect = regionRect;
        this.isDecodedAlready = isDecodedAlready;
    }


    public Rect getRect() {
        return regionRect;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setRect(Rect rect){
        this.regionRect = rect;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
