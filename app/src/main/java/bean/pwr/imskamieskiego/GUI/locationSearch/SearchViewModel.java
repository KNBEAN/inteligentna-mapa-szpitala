package bean.pwr.imskamieskiego.GUI.locationSearch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.repository.IMapRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;

public class SearchViewModel extends AndroidViewModel {

    private IMapRepository mapRepository;

    private MutableLiveData<String> suggestionQuery;
    private MutableLiveData<String> submitQuery;

    private LiveData<List<String>> suggestionsList;
    private LiveData<Location> queryResult;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        LocalDB db = LocalDB.getDatabase(application.getApplicationContext());
        mapRepository = new MapRepository(db);

        suggestionQuery = new MutableLiveData<>();
        submitQuery = new MutableLiveData<>();

        LiveData<List<Location>> suggestedLocationsList = Transformations.switchMap(suggestionQuery,
                input -> mapRepository.getLocationsListByName("%"+input+"%", 5));

        suggestionsList = Transformations.map(suggestedLocationsList,
                locations -> {
                    List<String> locationNames = new ArrayList<>();
                    for (Location location: locations) {
                        locationNames.add(location.getName());
                    }
                    return locationNames;
                });

        LiveData<List<Location>> rawSubmitQueryResult = Transformations.switchMap(submitQuery,
                input -> mapRepository.getLocationsListByName(input, 1));

        queryResult = Transformations.map(rawSubmitQueryResult,
                locationsList -> locationsList != null && locationsList.size() != 0 ? locationsList.get(0) : null);

    }

    void suggestionsQuery(String queryString){
        suggestionQuery.postValue(queryString);
    }

    LiveData<List<String>> getSuggestionsList(){
        return suggestionsList;
    }

    void submitQuery(String query){
        submitQuery.postValue(query);
    }

    LiveData<Location> getSubmitQueryResult(){
        return queryResult;
    }

}
