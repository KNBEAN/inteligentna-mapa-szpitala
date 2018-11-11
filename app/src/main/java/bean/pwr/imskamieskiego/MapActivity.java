package bean.pwr.imskamieskiego;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import bean.pwr.imskamieskiego.GUI.NavigationRouteFragment;
import bean.pwr.imskamieskiego.GUI.NavigationWindow.NavigationSetupFragment;
import bean.pwr.imskamieskiego.GUI.QuickAccessFragment;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;


import java.util.List;

import bean.pwr.imskamieskiego.GUI.InfoSheet;
import bean.pwr.imskamieskiego.GUI.locationSearch.SearchFragment;
import bean.pwr.imskamieskiego.MapDrawer.MapDrawer;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.utils.EventWrapper;
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
    //    private InfoSheet infoSheetFragment;
    private NavigationSetupFragment navigationSetupFragment;

    //Fragment tags
    private final String infoSheetTag = "InfoSheet";
    private final String searchFragmentTag = "SearchFragment";
    private final String navigationSetupTag = "NavigationSetupFragment";


    private Toolbar toolbar;
    private ImageButton changeFloorButton;

    private LocationViewModel locationViewModel;
    private FloorViewModel floorViewModel;
    private MapDrawer mapDrawer;


    private int currentFloor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        searchFragment = SearchFragment.newInstance();
        quickAccessFragment = (QuickAccessFragment) fragmentManager.findFragmentById(R.id.quickAccessFragment);

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        locationViewModel.getTargetLocation().observe(this, locationEvent -> {
            Location location = locationEvent != null ? locationEvent.handleData() : null;
            if (location != null && !navigationSetupIsShown()) {
                Log.i(TAG, String.format("target location: %s", location.getId()));

                if (fragmentManager.findFragmentByTag(navigationSetupTag) == null){
                    displayInfoSheet(location);
                }
            }
        });

        locationViewModel.getStartLocation().observe(this, locationEvent -> {
            Location location = locationEvent != null ? locationEvent.handleData() : null;
            if (location != null) {
                Log.i(TAG, String.format("start location: %s", location.getId()));
                NavigationSetupFragment navSetupFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
                if (navSetupFragment != null){
                    navSetupFragment.setStartLocationName(location.getName());
                }
            }
        });


        locationViewModel.getTargetPoint().observe(this, mapPoints -> {

            if (mapPoints != null){
                clearTargetPoint();
//              FIXME zoomOnPoint method is unusable on this moment.
//              mapDrawer.zoomOnPoint(point);
//              Log.i(TAG, "x= " + point.getX() + "y: " + point.getY() + "floor: " + point.getFloor());
                showTargetOnMap(mapPoints);
            }
        });

        locationViewModel.getStartPoint().observe(this, mapPoint -> {
            if (mapPoint != null){
                Log.i(TAG, "x= " + mapPoint.getX() + "y: " + mapPoint.getY() + "floor: " + mapPoint.getFloor());
                clearStartPoint();
                //FIXME zoomOnPoint method is unusable on this moment.
                //mapDrawer.zoomOnPoint(mapPoint);
                Log.i(TAG, "x= " + mapPoint.getX() + "y: " + mapPoint.getY() + "floor: " + mapPoint.getFloor());
                showStartPointOnMap(mapPoint);
            }
        });


        locationViewModel.getTrace().observe(this, mapPoints -> {
            Log.i(TAG, "trace: new trace ready");
            if (mapPoints != null) {
                for (MapPoint mapPoint:mapPoints) {
                    Log.d(TAG, "trace: " + mapPoint.getId());
                }
                showTraceOnMap(mapPoints);
            }
        });


        floorViewModel = ViewModelProviders.of(this).get(FloorViewModel.class);
        currentFloor = floorViewModel.getCurrentFloor();
        floorViewModel.setSelectedFloor(currentFloor);
        floorViewModel.getFloorBitmap().observe(this, bitmap -> {
                    if (bitmap != null) {
                        mapDrawer.showFloor(currentFloor, bitmap);
                    }
                }
        );

        floorMenuInit();


        mapDrawer = findViewById(R.id.mapdrawer);

        mapDrawer.setOnLongPressListener(mapPoint -> {
            if (fragmentManager.findFragmentByTag(navigationSetupTag) != null) {
                locationViewModel.setStartPoint(mapPoint);
            }else {
                locationViewModel.setTargetPoint(mapPoint);
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

    private void floorMenuInit(){
        changeFloorButton = findViewById(R.id.floors_button);

        PopupMenu floorSelect = new PopupMenu(MapActivity.this, changeFloorButton);
        floorViewModel.getFloorsList().observe(MapActivity.this, floorList -> {
            if (floorList != null) {
                for (int i = 0; i < floorList.length; i++) {
                    floorSelect.getMenu().add(1,i,i,floorList[i]);
                }
            }
        });

        changeFloorButton.setOnClickListener(v -> floorSelect.show());

        floorSelect.setOnMenuItemClickListener(item -> {
            if (currentFloor != item.getItemId()) {
                currentFloor = item.getItemId();
                floorViewModel.setCurrentFloor(currentFloor);
                floorViewModel.setSelectedFloor(currentFloor);
            }
            else
                Toast.makeText(MapActivity.this, item.getTitle().toString(), Toast.LENGTH_LONG).show();
            return false;
        });
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

        navigationSetupFragment.setNavigationOnClickListener(view -> {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(navigationSetupTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                locationViewModel.clearStartPointSelection();
                clearStartPoint();
            }
        });

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

        if (fragmentManager.findFragmentByTag(navigationSetupTag) != null ){
            clearStartPoint();
            locationViewModel.setStartLocation(location);
        }else {
            clearTargetPoint();
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
        locationViewModel.searchPatch();
        Fragment navigationFragment = fragmentManager.findFragmentByTag(navigationSetupTag);
        if (navigationFragment != null){
            FragmentTransaction fTransaction = fragmentManager.beginTransaction();

            NavigationRouteFragment routeFragment = NavigationRouteFragment.newInstance();

            fTransaction.replace(R.id.toolBarHolder, routeFragment);
            routeFragment.setNavigationOnClickListener(view -> {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    clearTraceOnMap();
                }
            });
            fTransaction.addToBackStack(null).commit();
        }
    }










    // management of drawing points on map

    private List<MapPoint> targetPoints;
    private MapPoint startPoint;
    private boolean traceIsPresent = false;

    private void showTargetOnMap(List<MapPoint> targetPoints){
        this.targetPoints = targetPoints;
        for (MapPoint mapPoint:targetPoints) {
            mapDrawer.addMapPoint(mapPoint, 0);
        }
    }

    private void showStartPointOnMap(MapPoint startPoint){
        this.startPoint = startPoint;
        mapDrawer.addMapPoint(startPoint, 1);
    }

    private boolean clearStartPoint(){
        if (startPoint == null) return false;
        mapDrawer.removeMapPoint(startPoint);
        startPoint = null;
        return true;
    }

    private boolean clearTargetPoint(){
        if (targetPoints == null) return false;
        for (MapPoint mapPoint:targetPoints) {
            mapDrawer.removeMapPoint(mapPoint);
        }
        targetPoints = null;
        return true;
    }

    private void showTraceOnMap(List<MapPoint> trace){
        traceIsPresent = true;
        mapDrawer.setTrace(trace);
    }

    private boolean clearTraceOnMap(){
        if (traceIsPresent) {
            mapDrawer.removeTrace();
            traceIsPresent = false;
            return true;
        }
        return false;
    }

    private void mapPointsDrawingBack(){
        if (clearTraceOnMap()) return;

        locationViewModel.clearStartPointSelection();
        if (clearStartPoint()) return;

        locationViewModel.clearTargetPointSelection();
        clearTargetPoint();
    }

}