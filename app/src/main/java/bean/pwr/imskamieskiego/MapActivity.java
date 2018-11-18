package bean.pwr.imskamieskiego;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import bean.pwr.imskamieskiego.GUI.MapFragment;
import bean.pwr.imskamieskiego.GUI.NavigationRouteFragment;
import bean.pwr.imskamieskiego.GUI.NavigationSetupFragment;
import bean.pwr.imskamieskiego.GUI.QuickAccessFragment;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bean.pwr.imskamieskiego.GUI.InfoSheet;
import bean.pwr.imskamieskiego.GUI.locationSearch.SearchFragment;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.utils.EventWrapper;
import bean.pwr.imskamieskiego.view_models.FloorViewModel;
import bean.pwr.imskamieskiego.view_models.LocationViewModel;
import bean.pwr.imskamieskiego.view_models.PathSearchViewModel;


public class MapActivity extends AppCompatActivity
        implements QuickAccessFragment.QuickAccessListener,
        SearchFragment.SearchListener,
        InfoSheet.InfoSheetListener,
        NavigationView.OnNavigationItemSelectedListener,
        NavigationSetupFragment.NavigationSetupListener,
        MapFragment.OnMapInteractionListener {

    private static final String TAG = "MapActivity";

    //Fragments
    private FragmentManager fragmentManager;
    private SearchFragment searchFragment;
    private QuickAccessFragment quickAccessFragment;
    private MapFragment mapFragment;

    //Fragment tags
    private final String infoSheetTag = "InfoSheet";
    private final String searchFragmentTag = "SearchFragment";
    private final String navigationSetupTag = "NavigationSetupFragment";
    private final String navigationRouteTag = "NavigationRouteFragment";


    private Toolbar toolbar;
    private Button changeFloorButton;

    private LocationViewModel locationViewModel;
    private PathSearchViewModel pathSearchViewModel;
    private FloorViewModel floorViewModel;
    private PopupWindow floorListPopupWindow;

    private int currentFloor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        searchFragment = SearchFragment.newInstance();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        quickAccessFragment = (QuickAccessFragment) fragmentManager.findFragmentById(R.id.quickAccessFragment);

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        pathSearchViewModel = ViewModelProviders.of(this).get(PathSearchViewModel.class);

        locationViewModel.getTargetLocation().observe(this, locationEvent -> {
            Location location = locationEvent != null ? locationEvent.handleData() : null;
            if (location != null && !navigationSetupIsShown()) {
                Log.i(TAG, String.format("target location: %s", location.getId()));

                if (fragmentManager.findFragmentByTag(navigationSetupTag) == null){
                    displayInfoSheet(location);
                }
            }
        });

        locationViewModel.getTargetPoint().observe(this, mapPoints -> {

            if (mapPoints != null){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("target points: ");
                for (MapPoint mapPoint:mapPoints) {
                    stringBuilder.append(String.format("%d ", mapPoint.getId()));
                }
                Log.d(TAG, stringBuilder.toString());
                mapFragment.setTargetPoints(mapPoints);
            }
        });

        pathSearchViewModel.getSearchedTrace().observe(this, mapPoints -> {
            if (mapPoints != null) {
                Log.i(TAG, "trace: new trace ready");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("trace: ");
                for (MapPoint mapPoint:mapPoints) {
                    stringBuilder.append(String.format("%n%d", mapPoint.getId()));
                }
                Log.d(TAG, stringBuilder.toString());
                mapFragment.setTrace(mapPoints);
            }
        });


        floorViewModel = ViewModelProviders.of(this).get(FloorViewModel.class);
        floorListPopupWindowInit();
        currentFloor = floorViewModel.getCurrentFloor();
        floorViewModel.setSelectedFloor(currentFloor);
        floorViewModel.getFloorBitmap().observe(this, bitmap -> {
                    if (bitmap != null) {
                        mapFragment.showFloor(currentFloor, bitmap);
                    }
                }
        );

        floorMenuInit();

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

    private void floorMenuInit(){
        changeFloorButton = findViewById(R.id.floors_button);
        changeFloorButton.setText(Integer.toString(currentFloor));
        PopupMenu floorSelect = new PopupMenu(MapActivity.this, changeFloorButton);
        changeFloorButton.setOnClickListener(v -> floorListPopupWindow.showAsDropDown(v));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.mainDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (quickAccessFragment.isAdded() && quickAccessFragment.isExpanded()){
            quickAccessFragment.hideQuickAccessButtons();
            return;
        }

        mapPointsDrawingBack();

        super.onBackPressed();

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void displaySearchFragment(){

        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        if(searchFragment.isAdded()){
            fTransaction.show(searchFragment);
        }else {
            fTransaction.add(R.id.mainDrawerLayout, searchFragment, searchFragmentTag);
        }

        fTransaction.addToBackStack(null).commit();
    }


    private boolean navigationSetupIsShown() {
        NavigationSetupFragment navSetupFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        return navSetupFragment != null && navSetupFragment.isAdded();
    }



    private void displayInfoSheet(Location location){
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        InfoSheet infoSheetFragment = (InfoSheet) fragmentManager.findFragmentByTag(infoSheetTag);

        if (infoSheetFragment == null) {
            infoSheetFragment = InfoSheet.newInstance(location);
            if (quickAccessFragment.isAdded() && !quickAccessFragment.isHidden()){fTransaction.hide(quickAccessFragment);}
            fTransaction.replace(R.id.infoSheetStub, infoSheetFragment, infoSheetTag);
            fTransaction.addToBackStack(null);
            fTransaction.commit();
        } else {
            infoSheetFragment.setLocation(location);
        }
    }

    private void displayNavigationSetup(String destinationLocationName){
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        Fragment infoSheetFragment = fragmentManager.findFragmentByTag(infoSheetTag);

        fTransaction.setCustomAnimations(R.anim.slide_in_from_left,android.R.anim.slide_out_right,
                R.anim.slide_in_from_left, android.R.anim.slide_out_right);
        NavigationSetupFragment navigationSetupFragment = NavigationSetupFragment.newInstance(destinationLocationName);
        fTransaction.replace(R.id.toolBarHolder, navigationSetupFragment, navigationSetupTag);

        if (quickAccessFragment.isAdded() && !quickAccessFragment.isHidden()){fTransaction.hide(quickAccessFragment);}

        if (infoSheetFragment != null){
            fTransaction.hide(infoSheetFragment);
        }

        fTransaction.addToBackStack(navigationSetupTag).commit();
    }


    @Override
    public void onQAButtonClick(QuickAccessFragment.QuickAccessButtons button) {
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
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }

        NavigationSetupFragment navigationSetupFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        if (navigationSetupFragment != null ){
            mapFragment.clearStartPoint();
            navigationSetupFragment.setStartLocation(location);
        }else {
            locationViewModel.setTargetLocation(location);
        }
    }

    @Override
    public void infoSheetAction() {
        Log.i(TAG, "infoSheetAction: SELECTED");
        EventWrapper<Location> locationEvent = locationViewModel.getTargetLocation().getValue();
        Location targetLocation = locationEvent != null ? locationEvent.getData() : null;
        if (targetLocation != null) {
            displayNavigationSetup(targetLocation.getName());
        } else {
            displayNavigationSetup(getString(R.string.default_place_name));
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
        NavigationSetupFragment navigationFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        if (navigationFragment != null){
            List<MapPoint> targets = locationViewModel.getTargetPoint().getValue();
            MapPoint startPoint = navigationFragment.getStartPoint();
            for(MapPoint target : targets){
                if (target.getId() == startPoint.getId()){
                    Snackbar.make(findViewById(R.id.map_fragment), R.string.start_is_target_warning, Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            pathSearchViewModel.startPathSearch(startPoint, targets);

            FragmentTransaction fTransaction = fragmentManager.beginTransaction();
            NavigationRouteFragment routeFragment = NavigationRouteFragment.newInstance();
            fTransaction.replace(R.id.toolBarHolder, routeFragment, navigationRouteTag);
            fTransaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onStartPointSelected(MapPoint selectedStartPoint) {
        if (selectedStartPoint != null){
            Log.i(TAG, String.format("onStartPointSelected: x: %d y: %d floor: %d", selectedStartPoint.getX(), selectedStartPoint.getY(), selectedStartPoint.getFloor()));
            mapFragment.setStartPoint(selectedStartPoint);
        }
    }

    @Override
    public void onMapClick(MapPoint clickPoint) {
        NavigationSetupFragment navSetupFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        NavigationRouteFragment navRouteFragment = (NavigationRouteFragment) fragmentManager.findFragmentByTag(navigationRouteTag);
        if (navRouteFragment != null && navRouteFragment.isVisible()){
            //Do nothing
        } else if (navSetupFragment != null && navSetupFragment.isVisible()) {
            Log.i(TAG, "onCreate: Navigation setup is visible? :" + navSetupFragment.isVisible());
            navSetupFragment.setStartPoint(clickPoint);
        }else {
            locationViewModel.setTargetPoint(clickPoint);
        }
    }

    private void mapPointsDrawingBack(){
        if (mapFragment.isTraceSet()){
            pathSearchViewModel.clearTrace();
            mapFragment.clearTrace();
            return;
        }

        NavigationSetupFragment navigationFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        if (navigationFragment != null){
            mapFragment.clearStartPoint();
            return;
        }

        locationViewModel.clearTargetPointSelection();
        mapFragment.clearTargetPoints();
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
        floorViewModel.getFloorsList().observe(MapActivity.this, floorList -> {
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