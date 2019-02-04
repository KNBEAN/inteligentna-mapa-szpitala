/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import bean.pwr.imskamieskiego.GUI.FloorListWindow;
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
import android.widget.Button;

import java.util.List;

import bean.pwr.imskamieskiego.GUI.InfoSheet;
import bean.pwr.imskamieskiego.GUI.UserLocationSelectFragment;
import bean.pwr.imskamieskiego.GUI.locationSearch.LocationSearchInterface;
import bean.pwr.imskamieskiego.GUI.locationSearch.SearchFragment;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.nav_window_activity.AboutApp;
import bean.pwr.imskamieskiego.nav_window_activity.AboutHospitalActivity;
import bean.pwr.imskamieskiego.nav_window_activity.AboutPatientAssistantActivity;
import bean.pwr.imskamieskiego.utils.EventWrapper;
import bean.pwr.imskamieskiego.view_models.FloorViewModel;
import bean.pwr.imskamieskiego.view_models.NavigationPointsViewModel;
import bean.pwr.imskamieskiego.view_models.PathSearchViewModel;


public class MapActivity extends AppCompatActivity
        implements QuickAccessFragment.QuickAccessListener,
        SearchFragment.SearchListener, LocationSearchInterface,
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
    private final String userLocationSelectFragmentTag = "UserLocationSelection";


    private Toolbar toolbar;
    private Button changeFloorButton;

    private NavigationPointsViewModel navigationPointsViewModel;
    private PathSearchViewModel pathSearchViewModel;
    private FloorViewModel floorViewModel;
    private FloorListWindow floorListWindow;
    private final int firstDrawerMenuItemId = 0;


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

        navigationPointsViewModel = ViewModelProviders.of(this).get(NavigationPointsViewModel.class);
        pathSearchViewModel = ViewModelProviders.of(this).get(PathSearchViewModel.class);

        navigationPointsViewModel.getTargetLocation().observe(this, locationEvent -> {
            Location location = locationEvent != null ? locationEvent.handleData() : null;
            if (location != null && !navigationSetupIsShown()) {
                Log.i(TAG, String.format("target location: %s", location.getId()));

                if (fragmentManager.findFragmentByTag(navigationSetupTag) == null) {
                    displayInfoSheet(location);
                }
            }
        });

        navigationPointsViewModel.getTargetPoint().observe(this, mapPoints -> {

            if (mapPoints != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("target points: ");
                for (MapPoint mapPoint : mapPoints) {
                    stringBuilder.append(String.format("%d ", mapPoint.getId()));
                }
                Log.d(TAG, stringBuilder.toString());
                mapFragment.setTargetPoints(mapPoints);
            }
        });

        navigationPointsViewModel.getStartLocation().observe(this, location -> {
            if (location != null && navigationSetupIsShown()) {
                NavigationSetupFragment navSetupFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
                navSetupFragment.setStartLocationName(location.getName());
            }
        });

        navigationPointsViewModel.getStartPoint().observe(this, mapPoint -> {
            if (mapPoint != null) {
                mapFragment.setStartPoint(mapPoint);
            }
        });


        pathSearchViewModel.getSearchedRoute().observe(this, mapPoints -> {
            if (mapPoints != null) {
                Log.i(TAG, "route: new route ready");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("route: ");
                for (MapPoint mapPoint : mapPoints) {
                    stringBuilder.append(String.format("%n%d", mapPoint.getId()));
                }
                Log.d(TAG, stringBuilder.toString());
                mapFragment.setRoute(mapPoints);
            }
        });


        floorViewModel = ViewModelProviders.of(this).get(FloorViewModel.class);
        floorViewModel.setSelectedFloor(floorViewModel.getCurrentFloor());
        floorViewModel.getFloorBitmap().observe(this, bitmap -> {
                    if (bitmap != null) {
                        mapFragment.showFloor(floorViewModel.getCurrentFloor(), bitmap);
                    }
                }
        );

        floorListWindow = new FloorListWindow(this, getLayoutInflater().inflate(R.layout.choose_floor_list, null));
        floorListWindow.setFloorSelectListener(floor -> {
            if (floorViewModel.getCurrentFloor() != floor) {
                floorListWindow.dismissList();
                floorViewModel.setSelectedFloor(floor);
                changeFloorButton.setText(String.valueOf(floor));
            }
        });

        floorListWindow.setSelectedFloor(floorViewModel.getCurrentFloor());
        floorViewModel.getFloorsList().observe(this, floorListWindow::setFloorList);

        changeFloorButton = findViewById(R.id.floors_button);
        changeFloorButton.setText(String.valueOf(floorViewModel.getCurrentFloor()));
        changeFloorButton.setOnClickListener(floorListWindow::showList);

        FloatingActionButton userLocationButton = findViewById(R.id.my_position_button);
        userLocationButton.setOnClickListener(view -> {
            if (fragmentManager.findFragmentByTag(userLocationSelectFragmentTag) == null) {
                FragmentTransaction fTransaction = fragmentManager.beginTransaction();
                UserLocationSelectFragment userLocationFragment = UserLocationSelectFragment.newInstance();
                fTransaction.replace(R.id.toolBarHolder, userLocationFragment, userLocationSelectFragmentTag);
                fTransaction.addToBackStack(null).commit();
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawerLayout = findViewById(R.id.mainDrawerLayout);
        ActionBarDrawerToggle hamburgerButton = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(hamburgerButton);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset < 0.1) {
                    navigationView.getMenu().getItem(firstDrawerMenuItemId).setChecked(true);
                }

            }
        });
        hamburgerButton.syncState();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.mainDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (fragmentManager.findFragmentByTag(searchFragmentTag) == null) {
            if (quickAccessFragment.isVisible() && quickAccessFragment.isExpanded()) {
                quickAccessFragment.hideQuickAccessButtons();
                return;
            }
            mapPointsDrawingBack();
        }

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
        if (itemId == R.id.searchMenuItem) {
            startSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ap) {
            Intent intent = new Intent(this, AboutPatientAssistantActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_authors) {
            Intent intent = new Intent(this, AboutApp.class);
            startActivity(intent);

        } else if (id == R.id.nav_about_hospital_info) {
            Intent intent = new Intent(this, AboutHospitalActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_search) {

        }

        DrawerLayout drawer = findViewById(R.id.mainDrawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private boolean navigationSetupIsShown() {
        NavigationSetupFragment navSetupFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        return navSetupFragment != null && navSetupFragment.isAdded();
    }


    private void displayInfoSheet(Location location) {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        InfoSheet infoSheetFragment = (InfoSheet) fragmentManager.findFragmentByTag(infoSheetTag);

        if (infoSheetFragment == null) {
            infoSheetFragment = InfoSheet.newInstance(location);
            if (quickAccessFragment.isAdded() && !quickAccessFragment.isHidden()) {
                fTransaction.hide(quickAccessFragment);
            }
            fTransaction.replace(R.id.infoSheetStub, infoSheetFragment, infoSheetTag);
            fTransaction.addToBackStack(null);
            fTransaction.commit();
        } else {
            infoSheetFragment.setLocation(location);
        }
    }

    private void displayNavigationSetup() {
        EventWrapper<Location> locationEvent = navigationPointsViewModel.getTargetLocation().getValue();
        String targetLocationName = locationEvent != null ? locationEvent.getData().getName() : null;
        Location startLocation = navigationPointsViewModel.getStartLocation().getValue();
        String startLocationName = startLocation != null? startLocation.getName() : null;


        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        Fragment infoSheetFragment = fragmentManager.findFragmentByTag(infoSheetTag);

        fTransaction.setCustomAnimations(R.anim.slide_in_from_left, android.R.anim.slide_out_right,
                R.anim.slide_in_from_left, android.R.anim.slide_out_right);
        NavigationSetupFragment navigationSetupFragment = NavigationSetupFragment.newInstance(startLocationName, targetLocationName);
        fTransaction.replace(R.id.toolBarHolder, navigationSetupFragment, navigationSetupTag);

        if (quickAccessFragment.isAdded() && !quickAccessFragment.isHidden()) {
            fTransaction.hide(quickAccessFragment);
        }

        if (infoSheetFragment != null) {
            fTransaction.hide(infoSheetFragment);
        }

        fTransaction.addToBackStack(navigationSetupTag).commit();
    }

    @Override
    public void onQAButtonClick(QuickAccessFragment.QuickAccessButtons button) {
        switch (button) {
            case WC:
                Log.d(TAG, "Quick access WC");
                navigationPointsViewModel.getQuickAccessTarget(NavigationPointsViewModel.TOILET_QA);
                break;
            case FOOD:
                Log.d(TAG, "Quick access FOOD");
                navigationPointsViewModel.getQuickAccessTarget(NavigationPointsViewModel.FOOD_QA);
                break;
            case ASSISTANT:
                Log.d(TAG, "Quick access ASSISTANT");
                navigationPointsViewModel.getQuickAccessTarget(NavigationPointsViewModel.PATIENT_ASSISTANT_QA);
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
        if (navigationSetupFragment != null) {
            mapFragment.clearStartPoint();
            navigationPointsViewModel.setStartLocation(location);
        } else {
            navigationPointsViewModel.setTargetLocation(location);
        }
    }

    @Override
    public void onSearchByCode(MapPoint point) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        onMapClick(point);
    }

    @Override
    public void infoSheetAction() {
        Log.i(TAG, "infoSheetAction: SELECTED");
        displayNavigationSetup();
    }

    @Override
    public void startSearch() {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        if (searchFragment.isAdded()) {
            fTransaction.show(searchFragment);
        } else {
            fTransaction.add(R.id.mainDrawerLayout, searchFragment, searchFragmentTag);
        }

        fTransaction.addToBackStack(null).commit();
    }

    @Override
    public void startNavigation(int pathSearchMode) {
        Log.i(TAG, "startNavigation: start navigation");
        NavigationSetupFragment navigationFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        if (navigationFragment != null) {
            List<MapPoint> targets = navigationPointsViewModel.getTargetPoint().getValue();
            MapPoint startPoint = navigationPointsViewModel.getStartPoint().getValue();
            for (MapPoint target : targets) {
                if (target.getId() == startPoint.getId()) {
                    Snackbar.make(findViewById(R.id.map_fragment), R.string.start_is_target_warning, Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            PathSearchViewModel.SearchMode searchMode;
            switch (pathSearchMode) {
                case NavigationSetupFragment.NavigationSetupListener.FAST_PATH:
                    searchMode = PathSearchViewModel.SearchMode.FAST_PATH;
                    Log.i(TAG, "Fast path mode");
                    break;
                case NavigationSetupFragment.NavigationSetupListener.OPTIMAL_PATH:
                    searchMode = PathSearchViewModel.SearchMode.OPTIMAL_PATH;
                    Log.i(TAG, "Optimal path mode");
                    break;
                case NavigationSetupFragment.NavigationSetupListener.COMFORT_PATH:
                    searchMode = PathSearchViewModel.SearchMode.COMFORTABLE_PATH;
                    Log.i(TAG, "Comfort path mode");
                    break;
                default:
                    searchMode = PathSearchViewModel.SearchMode.FAST_PATH;
            }

            pathSearchViewModel.startPathSearch(startPoint, targets, searchMode);
            FragmentTransaction fTransaction = fragmentManager.beginTransaction();
            NavigationRouteFragment routeFragment = NavigationRouteFragment.newInstance();
            fTransaction.replace(R.id.toolBarHolder, routeFragment, navigationRouteTag);
            fTransaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onMapClick(MapPoint clickPoint) {
        NavigationSetupFragment navSetupFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
        NavigationRouteFragment navRouteFragment = (NavigationRouteFragment) fragmentManager.findFragmentByTag(navigationRouteTag);
        if (navRouteFragment != null && navRouteFragment.isVisible()) {
            //Do nothing
        } else if (navSetupFragment != null && navSetupFragment.isVisible()) {
            Log.i(TAG, "onCreate: Navigation setup is visible? :" + navSetupFragment.isVisible());
            navigationPointsViewModel.setStartPoint(clickPoint);
        } else {
            navigationPointsViewModel.setTargetPoint(clickPoint);
        }
    }

    private void mapPointsDrawingBack() {
        if (mapFragment.isRouteSet()) {
            pathSearchViewModel.clearRoute();
            mapFragment.clearRoute();
            return;
        }

//        NavigationSetupFragment navigationFragment = (NavigationSetupFragment) fragmentManager.findFragmentByTag(navigationSetupTag);
//        if (navigationFragment != null) {
//            mapFragment.clearStartPoint();
//            return;
//        }

        navigationPointsViewModel.clearTargetPointSelection();
        mapFragment.clearTargetPoints();
    }
}