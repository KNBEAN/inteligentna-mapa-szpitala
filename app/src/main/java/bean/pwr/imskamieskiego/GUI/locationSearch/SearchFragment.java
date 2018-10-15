package bean.pwr.imskamieskiego.GUI.locationSearch;

import android.arch.lifecycle.LifecycleOwner;
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
import java.util.ArrayList;
import java.util.List;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.repository.IMapRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;



public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private final String TAG = "Search Fragment";

    private static final String LAST_SEARCH_QUERY = "LAST_SEARCH_QUERY";

    private ListView suggestionsListView;
    private SearchView searchView;

    private String lastQuery = null;

    private IMapRepository mapRepository;

    // Creates a new fragment
    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            lastQuery = savedInstanceState.getString(LAST_SEARCH_QUERY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

//        listView = (ListView) view.findViewById(R.id.suggestion_list);


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                setListViewPlace(listView.getItemAtPosition(i).toString());
//                Toast.makeText(getContext(),getListViewPlace(),Toast.LENGTH_LONG).show();
//                getFragmentManager().popBackStack();
//
//            }
//        });
        suggestionsListView = view.findViewById(R.id.suggestionList);
        searchView = view.findViewById(R.id.searchField);

        searchView.setQueryHint(getString(R.string.search_view_hint));
        searchView.setOnQueryTextListener(this);

        mapRepository = new MapRepository(LocalDB.getDatabase(view.getContext()));

        if(lastQuery != null && !lastQuery.isEmpty()) {
            Log.i(TAG, "Search query restoring: "+lastQuery);
            searchView.setQuery(lastQuery, false);
            searchView.clearFocus();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(LAST_SEARCH_QUERY, lastQuery);
        super.onSaveInstanceState(outState);
    }

    private void createSuggestions(List<Location> locations){
        Log.d(TAG, "Create suggestions.");
        ArrayList<String> names = new ArrayList<>();

        for (Location location : locations){
            names.add(location.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this.getContext(), R.layout.suggestion_item, names){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                String name = names.get(position);
                View row = getLayoutInflater().inflate(R.layout.suggestion_item, parent, false);
                ((TextView)row.findViewById(R.id.locationName)).setText(name);

                row.setOnClickListener(view -> {
                    //take next action based user selected item
                    String selectedLocation = ((TextView)view.findViewById(R.id.locationName)).getText().toString();
                    Toast.makeText(this.getContext(), "Selected suggestion "+selectedLocation,
                            Toast.LENGTH_LONG).show();
                    searchView.setQuery(selectedLocation, true);
                });

                return row;//super.getView(position, convertView, parent);
            }
        };

        suggestionsListView.setAdapter(adapter);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO in this place should be called method of viewModel
        //for test
        mapRepository.getLocationsListByName(query, 1)
                .observe(SearchFragment.this, locations -> {
                    if (locations != null){
                        for (Location location:locations) {
                            Toast.makeText(SearchFragment.this.getContext(), "Searched place "+location.getName(),
                                    Toast.LENGTH_LONG).show();
                            Log.i(TAG, "Searched location name: "+location.getName());
                        }
                    }
                });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 2) {
            mapRepository.getLocationsListByName("%" + newText + "%", 5).observe(
                    (LifecycleOwner) searchView.getContext(),
                    this::createSuggestions);
        }else {
            this.createSuggestions(new ArrayList<Location>());
        }
        return true;
    }
}
