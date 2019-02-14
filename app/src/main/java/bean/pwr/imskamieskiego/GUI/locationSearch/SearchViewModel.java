/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.locationSearch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.IMapRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;

public class SearchViewModel extends AndroidViewModel {
    private final String TAG = "Search View Model";
    private IMapRepository mapRepository;

    private MutableLiveData<String> suggestionQuery;
    private MutableLiveData<String> submitLocationQuery;
    private MutableLiveData<Integer> submitCodeQuery;

    private LiveData<List<String>> suggestionsList;
    private LiveData<Location> locationQueryResult;
    private LiveData<MapPoint> codeQueryResult;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        LocalDB db = LocalDB.getDatabase(application.getApplicationContext());
        mapRepository = new MapRepository(db);

        suggestionQuery = new MutableLiveData<>();
        submitLocationQuery = new MutableLiveData<>();
        submitCodeQuery = new MutableLiveData<>();

        LiveData<List<Location>> suggestedLocationsList = Transformations.switchMap(suggestionQuery,
                input -> mapRepository.getLocationsListByName("%" + input + "%", 5));

        suggestionsList = Transformations.map(suggestedLocationsList,
                locations -> {
                    List<String> locationNames = new ArrayList<>();
                    for (Location location : locations) {
                        locationNames.add(location.getName());
                    }
                    return locationNames;
                });

        LiveData<List<Location>> rawSubmitQueryResult = Transformations.switchMap(submitLocationQuery,
                input -> mapRepository.getLocationsListByName(input, 1));

        locationQueryResult = Transformations.map(rawSubmitQueryResult,
                locationsList -> locationsList != null && locationsList.size() != 0 ? locationsList.get(0) : null);

        codeQueryResult = Transformations.switchMap(submitCodeQuery,
                mapRepository::getPointByID);

    }

    void suggestionsQuery(String queryString) {
        suggestionQuery.postValue(queryString);
    }

    LiveData<List<String>> getSuggestionsList() {
        return suggestionsList;
    }

    void submitQuery(String query) {
        if (query.matches("#[0-9]+")) {
            Log.i(TAG, "Regex match");
            int pointCode = Integer.valueOf(query.substring(1));
            submitCodeQuery.postValue(pointCode);
        } else {
            submitLocationQuery.postValue(query);
        }
    }

    LiveData<Location> getLocationQueryResult() {
        return locationQueryResult;
    }

    LiveData<MapPoint> getCodeQueryResult() {
        return codeQueryResult;
    }
}
