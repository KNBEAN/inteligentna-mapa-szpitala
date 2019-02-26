package bean.pwr.imskamieskiego.MapDrawer;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Rect Wrapper class which provides dedicated methods for Map handling in application,
 * such as inset, scaling. TODO Rotating
 */
public class MapRect {

    private Rect rect;

    public MapRect(int left, int top, int right, int bottom) {
        this.rect = new Rect(left,top,right,bottom);
    }


    public void inset(int additionalSpace, boolean makeWider){
        if (makeWider) {
            rect.inset(-additionalSpace,-additionalSpace);
        } else {
            insetRectCheckingAdditionalSpaceBefore(additionalSpace);
        }

    }

    public void scaleCentrally(float scale){
        int rectCenterX = rect.centerX();
        int rectCenterY = rect.centerY();
        rect.bottom *=scale;
        rect.left *=scale;
        rect.top *= scale;
        rect.right *= scale;
        rect.offset(rectCenterX-rect.centerX(),rectCenterY-rect.centerY());
    }

    private void insetRectCheckingAdditionalSpaceBefore(int additionalSpace) {
        if (additionalSpace*2>rect.width() || additionalSpace*2>rect.height())
            throw new ArithmeticException("Additional space is to big to subtract");
        else  rect.inset(additionalSpace,additionalSpace);
    }

    public Point getCenter(){
        return new Point(rect.centerX(),rect.centerY());
    }

    public Rect getRect() {
        return rect;
    }
;
}
