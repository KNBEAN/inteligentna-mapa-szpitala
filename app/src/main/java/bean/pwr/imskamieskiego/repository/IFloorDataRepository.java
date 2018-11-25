/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.repository;

import android.arch.lifecycle.LiveData;

import java.io.InputStream;

/**
 * This interface represents the floor data repository of the building. Provides access to
 * the list of floors (including floor names) and floor pictures.
 */
public interface IFloorDataRepository {

    /**
     * Returns table of floors names. Table is returned as LiveData.
     * @return table of floors names
     */
    LiveData<String[]> getFloorNames();

    /**
     * Returns InputStream to floor image file.
     * @param floor floor number
     * @return LiveData with InputStream of floor image. It can be null, when image for given floor
     * non exist.
     */
    LiveData<InputStream> getMapImage(int floor);

}
