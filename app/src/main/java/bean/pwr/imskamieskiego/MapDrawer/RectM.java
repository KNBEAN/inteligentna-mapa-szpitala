package bean.pwr.imskamieskiego.MapDrawer;

import android.graphics.Rect;

public class RectM {
    static private int additionalSpace = 0;

    static public void setAdditionalSpace(int space){additionalSpace = space;}

    static public Rect insetRect(Rect rect, boolean makeWider){
        Rect broadenRect = new Rect();
        broadenRect.set(rect);
        if (makeWider) {
            broadenRect.inset(-additionalSpace,-additionalSpace);
        } else {
            broadenRect.inset(additionalSpace,additionalSpace);
        }
        return broadenRect;
    }

    static public Rect fitRectToScreen(Rect rect, int measureWidth, int measureHeight){
        Rect fitRect = new Rect();
        fitRect.left = Math.round((rect.right - measureWidth )/2);
        fitRect.top = Math.round((rect.bottom - measureHeight) / 2);
        fitRect.right = fitRect.left + measureWidth;
        fitRect.bottom = fitRect.top + measureHeight;
        return fitRect;
    }
}
