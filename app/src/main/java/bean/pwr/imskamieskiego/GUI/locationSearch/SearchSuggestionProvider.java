package bean.pwr.imskamieskiego.GUI.locationSearch;

import android.arch.lifecycle.LiveData;

import java.util.List;

import bean.pwr.imskamieskiego.model.map.Location;

public interface SearchSuggestionProvider {

//    LiveData<List<Location>> getLocationsData();

    LiveData<List<Location>> searchLocationsListByName(String name);

}
