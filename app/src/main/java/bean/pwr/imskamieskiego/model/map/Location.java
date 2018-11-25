/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.model.map;

import javax.annotation.Nullable;

/**
 * This interface represents object of location. Location on the map represents real existing place,
 * for ex. room, lobby or coffee shop ect.
 */
public interface Location {

    /**
     * ID of the place on the map.
     * @return ID
     */
    int getId();

    /**
     * The short name of the place on the map. For example "Room 404".
     * @return location name
     */
    String getName();

    /**
     * The description of the place. This can be Null.
     * @return location description.
     */
    @Nullable
    String getDescription();
}
