package bean.pwr.imskamieskiego.view_models;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.MapPoint;

public class NavigationSetupViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String targetLocationName;

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
