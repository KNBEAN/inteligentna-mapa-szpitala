package bean.pwr.imskamieskiego;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import bean.pwr.imskamieskiego.GUI.AnimationAdapter;
import bean.pwr.imskamieskiego.GUI.NavigationWindow.NavWindowListener;
import bean.pwr.imskamieskiego.GUI.NavigationWindow.NavWindowFragment;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;


import bean.pwr.imskamieskiego.GUI.InfoSheet;
import bean.pwr.imskamieskiego.GUI.locationSearch.SearchFragment;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.repository.IMapRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;


public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavWindowListener {

    private FragmentManager fragmentManager;

    private FloatingActionButton wcButton;
    private FloatingActionButton patientAssistantButton;
    private FloatingActionButton foodButton;
    private FloatingActionButton quickAccessButton;
    private Button patientAssistantButtonDescription;
    private Button foodButtonDescription;
    private Button wcButtonDescription;
    private ImageButton changeFloorButton;
    private Fragment navWindowFragment;
    private Toolbar toolbar;
    private Boolean navFragmentIsAdd = false;


    private SearchFragment searchFragment;

    private IMapRepository mapRepository;


    private static final String TAG = "MapActivity";

    private static final String NAV_FRAG_FLAG = "navFragIsAdd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        searchFragment = SearchFragment.newInstance();

        InfoSheet infoSheet = new InfoSheet(this);
        quickAccessButtonInit();
        changeFloorButtonInit();

        if (savedInstanceState != null){

            if (savedInstanceState.getBoolean(NAV_FRAG_FLAG, false)) {
                quickAccessButton.setVisibility(View.GONE);
                changeFloorButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            }
        }

        //TODO It's for test. You should remove this when view model will be ready!
        mapRepository = new MapRepository(LocalDB.getDatabase(this));


        infoSheet.setListener(new InfoSheet.InfoSheetListener() {
            @Override
            public void guideTo() {

            }


            @Override
            public void onSheetCollapsed() {
                hideQuickAccessButtons();
                quickAccessButton.setVisibility(View.GONE);
                quickAccessButton.setClickable(false);
            }

            @Override
            public void onSheetExpanded() {
                hideQuickAccessButtons();
                quickAccessButton.setVisibility(View.GONE);

            }

            @Override
            public void onSheetHidden() {
                quickAccessButton.setVisibility(View.VISIBLE);
                quickAccessButton.setClickable(true);
            }
        });


        DrawerLayout drawerLayout = findViewById(R.id.mainDrawerLayout);
        ActionBarDrawerToggle hamburgerButton = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(hamburgerButton);
        hamburgerButton.syncState();



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.mainDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (fragmentManager.findFragmentById(R.id.mainDrawerLayout ) != null){

            if (fragmentManager.getBackStackEntryCount() >= 1){
                navFragmentIsAdd = true;
                quickAccessButton.setVisibility(View.GONE);
                changeFloorButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            }
            else{
                navFragmentIsAdd = false;
                quickAccessButton.setVisibility(View.VISIBLE);
                changeFloorButton.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
            }
        }
        else {
            navFragmentIsAdd = false;
            quickAccessButton.setVisibility(View.VISIBLE);
            changeFloorButton.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }

        Log.i(NAV_FRAG_FLAG, String.valueOf(navFragmentIsAdd));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.searchMenuItem){
            displaySearchFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        menuSearchItem = menu.findItem(R.id.action_search);
//        searchView = (SearchView) menuSearchItem.getActionView();
//
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setQueryHint(getString(R.string.search_view_hint));
//
//        SuggestionProvider suggestionProvider = new SuggestionProvider(mapRepository);
//
//        searchHandler = new SearchHandler(searchView, suggestionProvider, query -> {
//
//            //TODO in this place should be called method of viewModel
//            //for test
//            mapRepository.getLocationsListByName(query, 1)
//                    .observe(MapActivity.this, locations -> {
//                        if (locations != null){
//                            for (Location location:locations) {
//                                Toast.makeText(MapActivity.this, "Searched place "+location.getName(),
//                                        Toast.LENGTH_LONG).show();
//                                Log.i(TAG, "Searched location name: "+location.getName());
//                            }
//                        }
//                    });
//            //end for test
//        });
//
//        if(lastQuery != null && !lastQuery.isEmpty()) {
//            Log.i(TAG, "Search query restoring: "+lastQuery);
//            searchView.setIconified(false);
//            menuSearchItem.expandActionView();
//            searchView.setQuery(lastQuery, false);
//            searchView.clearFocus();
//        }
//
//        return super.onCreateOptionsMenu(menu);
//    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ap) {

        } else if (id == R.id.nav_authors) {

            Intent intent = new Intent(this,AuthorsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_search) {


        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = findViewById(R.id.mainDrawerLayout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void changeFloorButtonInit() {
        changeFloorButton = findViewById(R.id.floors_button);
        changeFloorButton.setOnClickListener(v -> {

            PopupMenu floorSelect = new PopupMenu(MapActivity.this, changeFloorButton);
            floorSelect.getMenuInflater().inflate(R.menu.select_floor_menu, floorSelect.getMenu());
            floorSelect.setOnMenuItemClickListener(item -> {
                Toast.makeText(MapActivity.this, item.getTitle().toString(), Toast.LENGTH_LONG).show();
                return false;
            });
            floorSelect.show();
        });
    }

    private void displaySearchFragment(){

        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        if(searchFragment.isAdded()){
            fTransaction.show(searchFragment);
        }else {
            fTransaction.add(R.id.mainDrawerLayout, searchFragment, "SearchFragment");
        }

        if(navWindowFragment != null && navWindowFragment.isAdded()){
            fTransaction.hide(navWindowFragment);
        }
        fTransaction.addToBackStack(null);

        hideQuickAccessButtons();

        fTransaction.commit();
    }



    public void hideQuickAccessButtons() {

        AnimationAdapter animationRotateHide = new AnimationAdapter(MapActivity.this,R.anim.rotate_hide);
        animationRotateHide.startAnimation(quickAccessButton,null);

        wcButton.setVisibility(View.GONE);
        foodButton.setVisibility(View.GONE);
        patientAssistantButton.setVisibility(View.GONE);
        patientAssistantButtonDescription.setVisibility(View.GONE);
        foodButtonDescription.setVisibility(View.GONE);
        wcButtonDescription.setVisibility(View.GONE);


        wcButton.setClickable(false);
        foodButton.setClickable(false);
        patientAssistantButton.setClickable(false);


    }

    public void showQuickAccessButtons() {

        AnimationAdapter animationRotateShow = new AnimationAdapter(MapActivity.this,R.anim.rotate_show);
        animationRotateShow.startAnimation(quickAccessButton,null);

        wcButton.setVisibility(View.VISIBLE);
        foodButton.setVisibility(View.VISIBLE);
        patientAssistantButton.setVisibility(View.VISIBLE);
        patientAssistantButtonDescription.setVisibility(View.VISIBLE);
        foodButtonDescription.setVisibility(View.VISIBLE);
        wcButtonDescription.setVisibility(View.VISIBLE);


        wcButton.setClickable(true);
        foodButton.setClickable(true);
        patientAssistantButton.setClickable(true);


    }

    public void quickAccessButtonInit() {
        wcButton = findViewById(R.id.wc_button);
        patientAssistantButton = findViewById(R.id.patient_assistant_button);
        foodButton = findViewById(R.id.food_button);
        quickAccessButton = findViewById(R.id.tools_button);

        patientAssistantButtonDescription = findViewById(R.id.ap_button_description);
        foodButtonDescription = findViewById(R.id.food_button_description);
        wcButtonDescription = findViewById(R.id.wc_button_description);

        quickAccessButton.setOnClickListener(view -> {
            if (foodButton.getVisibility() == View.VISIBLE
                    && (wcButton.getVisibility() == View.VISIBLE)
                    && (patientAssistantButton.getVisibility() == View.VISIBLE)) {
                hideQuickAccessButtons();

            } else {
                showQuickAccessButtons();

            }
        });
        AnimationAdapter animationHide = new AnimationAdapter(MapActivity.this, R.anim.hide_anim);
        AnimationAdapter.AnimationEndListener animationEndListener = view -> view.setVisibility(View.GONE);

        View.OnClickListener quickButtonsOnClick = view -> {
            animationHide.startAnimation(changeFloorButton, animationEndListener);
            animationHide.startAnimation(toolbar, animationEndListener);
            setNewNavWindowFragment();
            hideQuickAccessButtons();
            animationHide.startAnimation(quickAccessButton, animationEndListener);
        };

        wcButton.setOnClickListener(quickButtonsOnClick);
        foodButton.setOnClickListener(quickButtonsOnClick);
        patientAssistantButton.setOnClickListener(quickButtonsOnClick);

    }

    public void setNewNavWindowFragment(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        navWindowFragment = fragmentManager.findFragmentById(R.id.mainDrawerLayout);


        if (navWindowFragment == null){
            navWindowFragment = new NavWindowFragment();

            fragmentTransaction
                    .setCustomAnimations(R.anim.slide_in_from_left,android.R.anim.slide_out_right,
                            R.anim.slide_in_from_left, android.R.anim.slide_out_right)
                    .add(R.id.mainDrawerLayout,navWindowFragment)
                    .addToBackStack(null)
                    .commit();
            navFragmentIsAdd = true;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(NAV_FRAG_FLAG,String.valueOf(navFragmentIsAdd));

        outState.putBoolean(NAV_FRAG_FLAG, navFragmentIsAdd);

        super.onSaveInstanceState(outState);
    }















    @Override
    public void onNavWindowBack() {
        AnimationAdapter animationShow = new AnimationAdapter(MapActivity.this, R.anim.show_anim);
        AnimationAdapter.AnimationEndListener animationEndListener = view -> view.setVisibility(View.VISIBLE);

        navFragmentIsAdd = true;

        animationShow.startAnimation(changeFloorButton,animationEndListener);
        animationShow.startAnimation(toolbar,animationEndListener);
        animationShow.startAnimation(quickAccessButton,animationEndListener);

    }

    @Override
    public void startNavigation() {

    }

    @Override
    public void updateNavFragmentState() {
        navFragmentIsAdd = true;

    }

}