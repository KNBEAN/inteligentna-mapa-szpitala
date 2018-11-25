/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class NavigationSetupViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String targetLocationName;

    /**
     * The constructor of the NavigationSetupViewModel factory. This factory is used to pass a
     * custom parameter to the NavigationSetupViewModel constructor.
     * @param application application context
     * @param targetLocationName name of target location
     */
    public NavigationSetupViewModelFactory(Application application, String targetLocationName){
        this.application = application;
        this.targetLocationName = targetLocationName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NavigationSetupViewModel(application, targetLocationName);
    }
}
