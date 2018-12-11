/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.path_search;

import android.support.annotation.NonNull;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public interface PathSearchAlgorithm {

    void startSearch();

    @NonNull List<MapPoint> getPath();

}
