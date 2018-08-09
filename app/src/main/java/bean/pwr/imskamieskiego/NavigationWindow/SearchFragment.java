package bean.pwr.imskamieskiego.NavigationWindow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import bean.pwr.imskamieskiego.R;


public class SearchFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String listViewPlace;
    private StringReciver stringReciver;
    private Boolean logicSD;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        stringReciver = new StringReciver();
        logicSD = getArguments().getBoolean("Bool",true);
        stringReciver.StartOrDest(logicSD);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar2);
        listView = (ListView) view.findViewById(R.id.destinations_list);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        setHasOptionsMenu(true);
        setPlacesArray(view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setListViewPlace(listView.getItemAtPosition(i).toString());

                Intent intent = new Intent();
                intent.putExtra("Key",getListViewPlace());
                intent.putExtra("Key2",logicSD);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);

                Toast.makeText(getContext(),getListViewPlace(),Toast.LENGTH_LONG).show();

                getFragmentManager().popBackStack();

            }
        });
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.findItem(R.id.search_item);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setIconified(false);                             //You can type to searchView, no click required
        searchView.setQueryHint(stringReciver.getHintText());


        searchView.setOnQueryTextListener(
                new android.support.v7.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        getFragmentManager().popBackStack();
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
