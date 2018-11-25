/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.model.map;

import java.util.Objects;

import javax.annotation.Nullable;

/**
 * This class can be use to create Location objects.
 */
public class LocationFactory {

    /**
     * Create new Location object with given parameters.
     * @param id
     * @param name
     * @param description
     * @return
     */
    public static Location create(int id, String name, String description){
        return new locationImpl(id, name, description);
    }

    /**
     * Create new Location object with given parameters. ID of this object will be set to -1.
     * @param name
     * @param description
     * @return
     */
    public static Location create(String name, String description){
        return create(-1, name, description);
    }

    static private class locationImpl implements Location{
        private  int id;
        private String name;
        private String description;

        public locationImpl(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Nullable
        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            locationImpl location = (locationImpl) o;
            return id == location.id &&
                    Objects.equals(name, location.name) &&
                    Objects.equals(description, location.description);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id, name, description);
        }
    }


}
