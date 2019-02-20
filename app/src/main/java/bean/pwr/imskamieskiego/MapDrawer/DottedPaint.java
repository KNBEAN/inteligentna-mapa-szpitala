/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.MapDrawer;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

public class DottedPaint extends Paint {

    private DashPathEffect dotsEffect = new DashPathEffect(new float[]{1, 30, 1}, 0);

    public DottedPaint() {
        setStyle(Style.STROKE);
        setPathEffect(dotsEffect);
        setStrokeJoin(Join.ROUND);
        setStrokeCap(Cap.ROUND);
        setAntiAlias(true);
        setDither(true);
    }
}
