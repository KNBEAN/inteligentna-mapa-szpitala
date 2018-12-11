/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.locationSearch;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.Location;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private final String TAG = "Search Fragment";

    private SearchViewModel searchViewModel;
    private ListView suggestionsListView;
    private SearchView searchView;
    private SearchListener listener;

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getSuggestionsList().observe(this, this::createSuggestions);
        searchViewModel.getSubmitQueryResult().observe(this, this::processSearchResult);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchListener) {
            listener = (SearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        suggestionsListView = view.findViewById(R.id.suggestionList);
        searchView = view.findViewById(R.id.searchField);

        searchView.setQueryHint(getString(R.string.search_view_hint));
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
                row.setOnClickListener(SearchFragment.this::selectSuggestionItem);
                return row;
            }

        };
        suggestionsListView.setAdapter(adapter);
    }

    private void selectSuggestionItem(View view){
        String selectedLocation = ((TextView)view.findViewById(R.id.locationName)).getText().toString();
        Log.d(TAG, "selectSuggestionItem: Selected suggestion: " + selectedLocation);
        searchView.setQuery(selectedLocation, true);
    }

    private void processSearchResult(Location location){
        if (location != null){
            Log.d(TAG, String.format("processSearchResult: %d, %s", location.getId(), location.getName()));
            searchView.clearFocus();
            listener.onLocationSearched(location);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchViewModel.submitQuery(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        int minQueryLength = 2;
        Log.d(TAG, "onQueryTextChange: "+newText);
        if (newText.length() >= minQueryLength) {
            searchViewModel.suggestionsQuery(newText);
        }else {
            searchViewModel.suggestionsQuery(null);
        }
        return true;
    }


    public interface SearchListener {
        void onLocationSearched(Location location);
    }
}
