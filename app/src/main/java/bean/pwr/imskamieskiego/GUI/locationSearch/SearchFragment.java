package bean.pwr.imskamieskiego.GUI.locationSearch;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import bean.pwr.imskamieskiego.R;



public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private final String TAG = "Search Fragment";

    private SearchViewModel searchViewModel;

    private ListView suggestionsListView;
    private SearchView searchView;

    // Creates a new fragment
    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getSuggestionsList().observe(this, this::createSuggestions);
        searchViewModel.getSubmitQueryResult().observe(this, location -> {
            if (location == null) return;
            Toast.makeText(SearchFragment.this.getContext(), "Searched place "+location.getName(),
                    Toast.LENGTH_LONG).show();
            Log.i(TAG, "Searched location name: "+location.getName());
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        suggestionsListView = view.findViewById(R.id.suggestionList);
        searchView = view.findViewById(R.id.searchField);

        searchView.setQueryHint(getString(R.string.search_view_hint));
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void createSuggestions(List<String> locationNames){
        Log.d(TAG, "Create suggestions.");

        ArrayAdapter adapter = new ArrayAdapter<String>(this.getContext(), R.layout.suggestion_item, locationNames){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                String name = locationNames.get(position);
                View row = getLayoutInflater().inflate(R.layout.suggestion_item, parent, false);
                ((TextView)row.findViewById(R.id.locationName)).setText(name);

                row.setOnClickListener(view -> {
                    //take next action based user selected item
                    String selectedLocation = ((TextView)view.findViewById(R.id.locationName)).getText().toString();
                    Toast.makeText(this.getContext(), "Selected suggestion "+selectedLocation,
                            Toast.LENGTH_LONG).show();
                    searchView.setQuery(selectedLocation, true);
                });

                return row;
            }
        };

        suggestionsListView.setAdapter(adapter);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchViewModel.submitQuery(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 2) {
            searchViewModel.suggestionsQuery(newText);
        }else {
            searchViewModel.suggestionsQuery(null);
        }
        return true;
    }
}
