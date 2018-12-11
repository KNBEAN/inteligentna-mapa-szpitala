/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.MapDrawer;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public interface MapDrawerGestureListener {
    void onLongPress(MapPoint mapPoint);
}
