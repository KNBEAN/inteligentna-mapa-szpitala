package bean.pwr.imskamieskiego.NavigationWindow;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import bean.pwr.imskamieskiego.R;


public class SearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String listViewPlace;
    private StringReciver stringReciver;
    private Toolbar toolbar;




    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.inflateMenu(R.menu.menu_search);
        setHasOptionsMenu(true);

        setPlacesArray(view);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setListViewPlace(listView.getItemAtPosition(i).toString());

                Toast.makeText(getContext(),getListViewPlace(),Toast.LENGTH_LONG).show();

               // getFragmentManager().popBackStack();
            }
        });

        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.i("OncreateMenu","Create?");

        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setIconified(false);                             //You can type to searchView, no click required
        //searchView.setQueryHint(stringReciver.getHintText());


        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter.getFilter().filter(s);
                        return false;
                    }
                });
        super.onCreateOptionsMenu(menu,inflater);
    }



    public void setPlacesArray(View view){
        listView = (ListView) view.findViewById(R.id.destinations_list);
        ArrayList<String> DestArray = new ArrayList<>();
        DestArray.addAll(Arrays.asList(getResources().getStringArray(R.array.dest)));

        adapter = new ArrayAdapter<>(
                getContext(),android.R.layout.simple_list_item_1,DestArray
        );
        listView.setAdapter(adapter);
    }

    public String getListViewPlace() {
        return listViewPlace;
    }

    public void setListViewPlace(String listViewPlace) {
        this.listViewPlace = listViewPlace;
    }

}
