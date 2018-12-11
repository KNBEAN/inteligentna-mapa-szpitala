/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.utils;

public class EventWrapper<T> {

    private boolean isHandled;
    private T data;

    /**
     * Create new event wrapper for data. Wrapper preserve information, was data read before.
     * @param data
     */
    public EventWrapper(T data){
        this.data = data;
        isHandled = false;
    }

    /**
     * Return information about handling status.
     * @return return true if data was handled in past. False if data is new.
     */
    public boolean isHandled() {
        return isHandled;
    }

    public T handleData(){
        if (isHandled){
            return null;
        }else {
            isHandled = true;
            return data;
        }
    }

    /**
     * Returns data
     * @return data contained in EventWrapper object
     */
    public T getData() {
        return data;
    }
}
