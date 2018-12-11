/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.model.map;

/**
 * This class can be use to create MapPoint objects.
 */
public class MapPointFactory {


    /**
     * Create new MapPoint object with given parameters.
     * @param id
     * @param x
     * @param y
     * @param floor
     * @param locationId
     * @return MapPoint object
     */
    public static MapPoint create(int id, int x, int y, int floor, int locationId){
        return new mapPointImpl(id, x, y, floor, locationId);
    }


    /**
     * Create new MapPoint object with given parameters. ID and locationID will be set to -1.
     * @param x
     * @param y
     * @param floor
     * @return MapPoint object
     */
    public static MapPoint create(int x, int y, int floor){
        return create(-1, x, y, floor, -1);
    }

    private static class mapPointImpl implements MapPoint{
        private int id;
        private int x;
        private int y;
        private int floor;
        private int locationID;

        public mapPointImpl(int id, int x, int y, int floor, int locationID) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.floor = floor;
            this.locationID = locationID;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getFloor() {
            return floor;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public int getLocationID() {
            return locationID;
        }
    }

}
