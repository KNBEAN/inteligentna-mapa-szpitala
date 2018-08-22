package bean.pwr.imskamieskiego.MapDrawer;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

public class DottedPaint extends Paint {

    private DashPathEffect dotsEffect = new DashPathEffect(new float[]{1, 30, 1}, 0);

    public DottedPaint() {
        setStyle(Paint.Style.STROKE);
        setPathEffect(dotsEffect);
        setStrokeJoin(Paint.Join.ROUND);
        setStrokeCap(Paint.Cap.ROUND);
        setAntiAlias(true);
        setDither(true);
    }
}
