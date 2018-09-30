package bean.pwr.imskamieskiego.GUI.locationSearch;

import android.arch.lifecycle.LifecycleOwner;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.annotation.Nullable;

import bean.pwr.imskamieskiego.MapActivity;
import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.Location;

public class SearchHandler implements SearchView.OnQueryTextListener{

    private final String TAG = "SearchHandler";

    private SearchListener searchListener;
    private SearchSuggestionProvider suggestionProvider;
    private SearchView searchView;


    public SearchHandler(SearchView searchView, @Nullable SearchSuggestionProvider suggestionProvider, SearchListener searchListener) {
        this.suggestionProvider = suggestionProvider;
        this.searchView = searchView;
        this.searchListener = searchListener;
        searchView.setOnQueryTextListener(this);
    }

    private void createSuggestions(List<Location> locations){
        Log.d(TAG, "Create suggestions.");
        searchView.setSuggestionsAdapter(new LocationSuggestionAdapter(searchView.getContext(), locations, view -> {
            //take next action based user selected item
            TextView nameText = (TextView) view.findViewById(R.id.locationName);
            Toast.makeText(view.getContext(), "Selected suggestion "+nameText.getText(),
                    Toast.LENGTH_LONG).show();
            searchView.setQuery(nameText.getText(), true);
        }));
    }


    public String getLastQuery() {
        return searchView.getQuery().toString();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchListener.searchSubmit(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (suggestionProvider != null) {
            suggestionProvider.searchLocationsListByName(newText).observe(
                    (LifecycleOwner) searchView.getContext(),
                    this::createSuggestions);
        }
        return false;
    }


    public interface SearchListener{

        void searchSubmit(String query);

    }
}
