/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * View model for navigation setup fragment.
 */
public class NavigationSetupViewModel extends AndroidViewModel {

    private static final String TAG = "NavSetupViewModel";

    private String startLocationName;
    private String targetLocationName;

    /**
     * Custom ViewModel for navigation setup view model.
     * @param application application context
     * @param targetName name of target location
     * @param startLocationName name of start location. If can be null.
     */
    public NavigationSetupViewModel(@NonNull Application application, String targetName, String startLocationName) {
        super(application);

        this.targetLocationName = targetName;
        this.startLocationName = startLocationName;
    }

    /**
     * Sets target location name
     * @param targetLocationName name of target location
     */
    public void setTargetLocationName(String targetLocationName){
        this.targetLocationName = targetLocationName;
    }

    /**
     * Returns target location name as LiveData
     * @return String with target location name
     */
    public String getTargetLocationName(){
        return targetLocationName;
    }

    /**
     * Returns the selected starting location name.
     * @return selected location as LiveData
     */
    public String getStartLocationName() {
        return startLocationName;
    }

    /**
     * Sets start location name
     * @param startLocationName location name
     */
    public void setStartLocation(String startLocationName){
        this.startLocationName = startLocationName;
    }

}
