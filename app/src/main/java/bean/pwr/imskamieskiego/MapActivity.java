package bean.pwr.imskamieskiego;

import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import bean.pwr.imskamieskiego.GUI.AnimationAdapter;
import bean.pwr.imskamieskiego.NavigationWindow.NavWindowListener;
import bean.pwr.imskamieskiego.NavigationWindow.NavWindowFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import bean.pwr.imskamieskiego.GUI.InfoSheet;

import static bean.pwr.imskamieskiego.GUI.InfoSheet.COLLAPSED;


public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavWindowListener {


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


    private static final String TAG = "MapActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        InfoSheet infoSheet = new InfoSheet(this);
        quickAccessButtonInit();
        changeFloorButtonInit();


        if (savedInstanceState != null){

            if (savedInstanceState.getBoolean("navFragIsAdd", false)) {
                quickAccessButton.setVisibility(View.GONE);
                changeFloorButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            }

        }


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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle hamburgerButton = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(hamburgerButton);
        hamburgerButton.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        if (getSupportFragmentManager().findFragmentById(R.id.drawer_layout ) != null){

            if (getSupportFragmentManager().getBackStackEntryCount() >= 1){
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

        Log.i("navFragIsAdd",String.valueOf(navFragmentIsAdd));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
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

        quickAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodButton.getVisibility() == View.VISIBLE
                        && (wcButton.getVisibility() == View.VISIBLE)
                        && (patientAssistantButton.getVisibility() == View.VISIBLE)) {
                    hideQuickAccessButtons();

                } else {
                    showQuickAccessButtons();

                }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        navWindowFragment = fragmentManager.findFragmentById(R.id.drawer_layout);


        if (navWindowFragment == null){
            navWindowFragment = new NavWindowFragment();

            fragmentTransaction
                    .setCustomAnimations(R.anim.slide_in_from_left,android.R.anim.slide_out_right,
                            R.anim.slide_in_from_left, android.R.anim.slide_out_right)
                    .add(R.id.drawer_layout,navWindowFragment)
                    .addToBackStack(null)
                    .commit();
            navFragmentIsAdd = true;
        }
    }



    @Override
    public void onBack() {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i("navFragIsAdd",String.valueOf(navFragmentIsAdd));
        outState.putBoolean("navFragIsAdd", navFragmentIsAdd);
    }

    private void changeFloorButtonInit() {
        changeFloorButton = findViewById(R.id.floors_button);
        changeFloorButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                PopupMenu floorSelect = new PopupMenu(MapActivity.this, changeFloorButton);
                floorSelect.getMenuInflater().inflate(R.menu.select_floor_menu, floorSelect.getMenu());
                floorSelect.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MapActivity.this, item.getTitle().toString(), Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
                floorSelect.show();
            }
        });
    }

}