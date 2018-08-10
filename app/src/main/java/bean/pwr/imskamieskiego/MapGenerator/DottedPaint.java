package bean.pwr.imskamieskiego.MapGenerator;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

public class DottedPaint extends Paint {

    DashPathEffect dots = new DashPathEffect(new float[] {1,30,1}, 0);

    public DottedPaint() {
        setStyle(Paint.Style.STROKE);
        setPathEffect(dots);
        setStrokeJoin(Paint.Join.ROUND);
        setAntiAlias(true);
        setDither(true);
        setStrokeCap(Paint.Cap.ROUND);
    }
}
