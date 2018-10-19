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

    private MutableLiveData<String> suggestionQuery = new MutableLiveData<>();
    private MutableLiveData<String> submitQuery = new MutableLiveData<>();


    private LiveData<List<Location>> locationsList = Transformations.switchMap(suggestionQuery,
            input -> mapRepository.getLocationsListByName("%"+input+"%", 5));

    private LiveData<List<String>> suggestionsList = Transformations.map(locationsList,
            locations -> {
                List<String> locationNames = new ArrayList<>();
                for (Location location: locations) {
                    locationNames.add(location.getName());
                }
                return locationNames;
            });

    private LiveData<List<Location>> rawSubmitQuery = Transformations.switchMap(submitQuery,
            input -> mapRepository.getLocationsListByName(input, 1));

    private LiveData<Location> queryResult = Transformations.map(rawSubmitQuery,
            locationsList -> locationsList != null && locationsList.size() != 0 ? locationsList.get(0) : null);

    public SearchViewModel(@NonNull Application application) {
        super(application);
        LocalDB db = LocalDB.getDatabase(application.getApplicationContext());
        mapRepository = new MapRepository(db);
    }

    public void suggestionsQuery(String queryString){
        suggestionQuery.postValue(queryString);
    }

    public LiveData<List<String>> getSuggestionsList(){
        return suggestionsList;
    }

    public void submitQuery(String query){
        submitQuery.postValue(query);
    }

    public LiveData<Location> getSubmitQueryResult(){
        return queryResult;
    }

}
