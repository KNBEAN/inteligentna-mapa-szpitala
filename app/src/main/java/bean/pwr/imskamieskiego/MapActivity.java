package bean.pwr.imskamieskiego;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import bean.pwr.imskamieskiego.GUI.AnimationAdapter;
import bean.pwr.imskamieskiego.GUI.QuickAccessFragment;
import bean.pwr.imskamieskiego.GUI.NavigationWindow.NavWindowListener;
import bean.pwr.imskamieskiego.GUI.NavigationWindow.NavWindowFragment;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;


import bean.pwr.imskamieskiego.GUI.InfoSheet;
import bean.pwr.imskamieskiego.GUI.locationSearch.SearchFragment;
import bean.pwr.imskamieskiego.data.LocalDB;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.repository.IMapRepository;
import bean.pwr.imskamieskiego.repository.MapRepository;


public class MapActivity extends AppCompatActivity
        implements QuickAccessFragment.QuickAccessListener,
        SearchFragment.SearchListener,
        NavigationView.OnNavigationItemSelectedListener,
        NavWindowListener {

    private FragmentManager fragmentManager;

    private Fragment navWindowFragment;
    private Toolbar toolbar;
    private Boolean navFragmentIsAdd = false;


    private ImageButton changeFloorButton;


    private SearchFragment searchFragment;
    private QuickAccessFragment quickAccessFragment;


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
        quickAccessFragment = (QuickAccessFragment) fragmentManager.findFragmentById(R.id.quickAccessFragment);


        changeFloorButton = findViewById(R.id.floors_button);
        changeFloorButton.setOnClickListener(v -> {

            PopupMenu floorSelect = new PopupMenu(this, changeFloorButton);
            floorSelect.getMenuInflater().inflate(R.menu.select_floor_menu, floorSelect.getMenu());
            floorSelect.setOnMenuItemClickListener(item -> {
                Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_LONG).show();
                return false;
            });
            floorSelect.show();
        });


        InfoSheet infoSheet = new InfoSheet(this);
//        changeFloorButtonInit();

        if (savedInstanceState != null){

            if (savedInstanceState.getBoolean(NAV_FRAG_FLAG, false)) {
//                changeFloorButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            }
        }


        infoSheet.setListener(new InfoSheet.InfoSheetListener() {
            @Override
            public void guideTo() {

            }


            @Override
            public void onSheetCollapsed() {
//                hideQuickAccessButtons();
//                quickAccessButton.setVisibility(View.GONE);
//                quickAccessButton.setClickable(false);
            }

            @Override
            public void onSheetExpanded() {
//                hideQuickAccessButtons();
//                quickAccessButton.setVisibility(View.GONE);

            }

            @Override
            public void onSheetHidden() {
//                quickAccessButton.setVisibility(View.VISIBLE);
//                quickAccessButton.setClickable(true);
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
//                quickAccessButton.setVisibility(View.GONE);
//                changeFloorButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            }
            else{
                navFragmentIsAdd = false;
//                quickAccessButton.setVisibility(View.VISIBLE);
//                changeFloorButton.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
            }
        }
        else {
            navFragmentIsAdd = false;
//            quickAccessButton.setVisibility(View.VISIBLE);
//            changeFloorButton.setVisibility(View.VISIBLE);
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

        if (quickAccessFragment != null && quickAccessFragment.isAdded()){
            fTransaction.hide(quickAccessFragment);
        }
        fTransaction.addToBackStack(null);

//        hideQuickAccessButtons();

        fTransaction.commit();
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

//        animationShow.startAnimation(changeFloorButton,animationEndListener);
        animationShow.startAnimation(toolbar,animationEndListener);
//        animationShow.startAnimation(quickAccessButton,animationEndListener);

    }

    @Override
    public void startNavigation() {

    }

    @Override
    public void updateNavFragmentState() {
        navFragmentIsAdd = true;

    }

    @Override
    public void onButtonClick(QuickAccessFragment.QuickAccessButtons button) {
        switch (button){
            case WC:
                Log.d(TAG, "Quick access WC");
                break;
            case FOOD:
                Log.d(TAG, "Quick access FOOD");
                break;
            case ASSISTANT:
                Log.d(TAG, "Quick access ASSISTANT");
                break;
            default:
                Log.d(TAG, "Quick access undefined");
        }
    }

    @Override
    public void onLocationSearched(Location location) {
        Snackbar.make(changeFloorButton, "SelectedLocation: "+location.getName(), Snackbar.LENGTH_SHORT).show();
    }
}