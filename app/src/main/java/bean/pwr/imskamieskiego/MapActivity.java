package bean.pwr.imskamieskiego;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;


import java.util.ArrayList;
import java.util.Collections;

import bean.pwr.imskamieskiego.GUI.InfoSheet;
import bean.pwr.imskamieskiego.GUI.NavigationWindow.NavigationSetupFragment;
import bean.pwr.imskamieskiego.GUI.QuickAccessFragment;
import bean.pwr.imskamieskiego.GUI.locationSearch.SearchFragment;
import bean.pwr.imskamieskiego.MapDrawer.MapDrawer;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.view_models.FloorViewModel;
import bean.pwr.imskamieskiego.view_models.LocationViewModel;


public class MapActivity extends AppCompatActivity
        implements QuickAccessFragment.QuickAccessListener,
        SearchFragment.SearchListener,
        InfoSheet.InfoSheetListener,
        NavigationView.OnNavigationItemSelectedListener,
        NavigationSetupFragment.NavigationSetupListener {

    private static final String TAG = "MapActivity";

    //Fragments
    private FragmentManager fragmentManager;
    private SearchFragment searchFragment;
    private QuickAccessFragment quickAccessFragment;

    //Fragment tags
    private final String infoSheetTag = "InfoSheet";
    private final String searchFragmentTag = "SearchFragment";
    private final String navigationSetupTag = "NavigationSetupFragment";


    private Button changeFloorButton;
    private LocationViewModel locationViewModel;
    private FloorViewModel floorViewModel;
    private MapDrawer mapDrawer;
    private PopupWindow floorListPopupWindow;
    private int currentFloor = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        searchFragment = SearchFragment.newInstance();
        quickAccessFragment = (QuickAccessFragment) fragmentManager.findFragmentById(R.id.quickAccessFragment);


        viewModelInit();
        floorViewModelInit();
        floorListPopupWindowInit();
        currentFloor = floorViewModel.getCurrentFloor();


        floorViewModel.setSelectedFloor(currentFloor);
        mapDrawer = findViewById(R.id.mapdrawer);

        mapDrawer.setOnLongPressListener(mapPoint -> {
            mapDrawer.removeAllMapPoints();
            locationViewModel.setMapPoint(mapPoint);
        });

        changeFloorButton = findViewById(R.id.floors_button);
        changeFloorButton.setText(Integer.toString(currentFloor));
        changeFloorButton.setOnClickListener(v -> floorListPopupWindow.showAsDropDown(v));


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
        } else if (quickAccessFragment.isAdded() && quickAccessFragment.isExpanded()) {
            quickAccessFragment.hideQuickAccessButtons();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.searchMenuItem) {
            displaySearchFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ap) {

        } else if (id == R.id.nav_authors) {

            Intent intent = new Intent(this, AuthorsActivity.class);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void viewModelInit() {
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        locationViewModel.getCurrentLocation().observe(this, location -> {
                    if (location != null) {
                        displayInfoSheet(location, false);
                    }
                }
        );
        locationViewModel.getNearestMapPoint().observe(this, mapPoint -> {
                    if (mapPoint != null) {
                        mapDrawer.zoomOnPoint(mapPoint);
                        Log.i(TAG, "x= " + mapPoint.getX() + "y: " + mapPoint.getY() + "floor: " + mapPoint.getFloor());
                        mapDrawer.addMapPoint(mapPoint, 1);
                    }
                }
        );

    }

    private void floorViewModelInit() {
        floorViewModel = ViewModelProviders.of(this).get(FloorViewModel.class);
        floorViewModel.getFloorBitmap().observe(this, bitmap -> {
                    if (bitmap != null) {
                        mapDrawer.showFloor(currentFloor, bitmap);

                    }
                }
        );

    }


    private void displaySearchFragment() {

        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        if (searchFragment.isAdded()) {
            fTransaction.show(searchFragment);
        } else {
            fTransaction.add(R.id.mainDrawerLayout, searchFragment, searchFragmentTag);
        }

        fTransaction.addToBackStack(null).commit();
    }

    private void displayInfoSheet(Location location, boolean asStartPoint) {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();

        fTransaction.replace(R.id.infoSheetStub, InfoSheet.newInstance(location, asStartPoint), infoSheetTag);

        if (quickAccessFragment.isAdded() && !quickAccessFragment.isHidden()) {
            fTransaction.hide(quickAccessFragment);
        }

        fTransaction.addToBackStack(null).commit();
    }

    private void displayNavigationSetup(String destinationLocationName) {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();

        fTransaction.setCustomAnimations(R.anim.slide_in_from_left, android.R.anim.slide_out_right,
                R.anim.slide_in_from_left, android.R.anim.slide_out_right);
        NavigationSetupFragment navigationSetupFragment = NavigationSetupFragment.newInstance(destinationLocationName);
        fTransaction.replace(R.id.toolBarHolder, navigationSetupFragment, navigationSetupTag);

        if (quickAccessFragment.isAdded() && !quickAccessFragment.isHidden()) {
            fTransaction.hide(quickAccessFragment);
        }

        Fragment infoSheetFragment = fragmentManager.findFragmentByTag(infoSheetTag);
        if (infoSheetFragment != null) {
            fTransaction.hide(infoSheetFragment);
        }

        navigationSetupFragment.setNavigationOnClickListener(view -> {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(navigationSetupTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        fTransaction.addToBackStack(navigationSetupTag).commit();
    }


    @Override
    public void onQAButtonClick(QuickAccessFragment.QuickAccessButtons button) {
        switch (button) {
            case WC:
                Log.d(TAG, "Quick access WC");
                displayNavigationSetup("potato");
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
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        displayInfoSheet(location, true);
    }


    @Override
    public void infoSheetAction(boolean asStartPoint) {
        if (asStartPoint) {
            Log.i(TAG, "infoSheetAction: SELECTED");
            displayNavigationSetup("Tomato");

        }
    }


    @Override
    public void startPointSearchRequest() {
        Log.i(TAG, "startPointSearchRequest: search start point");
        displaySearchFragment();
    }

    @Override
    public void startNavigation(boolean avoidStairs) {
        Log.i(TAG, "startNavigation: start navigation");
    }

    private void floorListPopupWindowInit() {


        floorListPopupWindow = new PopupWindow(MapActivity.this);
        View popupLayout = getLayoutInflater().inflate(R.layout.choose_floor_list, null);
        ListView floorListView = popupLayout.findViewById(R.id.floor_list);
        floorListView.setAdapter(floorsAdapter());
        floorListPopupWindow.setContentView(popupLayout);


        floorListView.setOnItemClickListener(

                (parent, view, position, id) -> {
                    if (currentFloor != position) {
                        currentFloor = position;
                        floorListPopupWindow.dismiss();
                        floorViewModel.setCurrentFloor(currentFloor);
                        floorViewModel.setSelectedFloor(currentFloor);
                        changeFloorButton.setText(Integer.toString(currentFloor));

                    }
                });


        floorListPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        floorListPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        floorListPopupWindow.setOutsideTouchable(true);
        floorListPopupWindow.setFocusable(true);

    }


    ArrayAdapter<String> floorsAdapter() {
        ArrayList<String> floors = new ArrayList<>();

        floorViewModel.getFloorList().observe(MapActivity.this, floorList -> {
            if (floorList != null) {

                Collections.addAll(floors, floorList);
            }
        });

        return new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1, floors) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View returnedView = super.getView(position, convertView, parent);

                if (position == currentFloor) {
                    returnedView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                } else {
                    returnedView.setBackgroundColor(getResources().getColor(R.color.fontColorWhite));
                }

                return returnedView;
            }

        };
    }

}
