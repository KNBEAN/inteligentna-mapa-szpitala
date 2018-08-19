package bean.pwr.imskamieskiego;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;


public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavWindowListener {


    private FloatingActionButton wcButton;
    private FloatingActionButton patientAssistantButton;
    private FloatingActionButton foodButton;
    private FloatingActionButton quickAccessButton;
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

        changeFloorButton = findViewById(R.id.floors_button);
        quickAccessButtonInit();

        if (savedInstanceState != null){

                if (savedInstanceState.getBoolean("navFragIsAdd", false)) {
                    quickAccessButton.setVisibility(View.GONE);
                    changeFloorButton.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                }
        }

        changeFloorButton.setOnClickListener(v -> {

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

        if (getFragmentManager().findFragmentById(R.id.drawer_layout ) != null){
            setNavFragmentIsAdd(true);
            quickAccessButton.setVisibility(View.GONE);
            changeFloorButton.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);

        }
        else {
            setNavFragmentIsAdd(false);
            quickAccessButton.setVisibility(View.VISIBLE);
            changeFloorButton.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }

        Log.i("onBackPressed",String.valueOf(getNavFragmentIsAdd()));

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

        // animate_hide(wcButton);
        // animate_hide(foodButton);
        // animate_hide(patientAssistantButton);
        //rotateLeft(quickAccessButton);

        AnimationAdapter animationRotateHide = new AnimationAdapter(MapActivity.this,R.anim.rotate_hide);
        animationRotateHide.startAnimation(quickAccessButton,null);

        wcButton.setVisibility(View.GONE);
        foodButton.setVisibility(View.GONE);
        patientAssistantButton.setVisibility(View.GONE);

        wcButton.setClickable(false);
        foodButton.setClickable(false);
        patientAssistantButton.setClickable(false);


    }

    public void showQuickAccessButtons() {

        // animate_show(wcButton);
        // animate_show(foodButton);
        // animate_show(patientAssistantButton);
        //rotateRight(quickAccessButton);

        AnimationAdapter animationRotateShow = new AnimationAdapter(MapActivity.this,R.anim.rotate_show);
        animationRotateShow.startAnimation(quickAccessButton,null);

        wcButton.setVisibility(View.VISIBLE);
        foodButton.setVisibility(View.VISIBLE);
        patientAssistantButton.setVisibility(View.VISIBLE);

        wcButton.setClickable(true);
        foodButton.setClickable(true);
        patientAssistantButton.setClickable(true);


    }

    public void quickAccessButtonInit() {
        wcButton = findViewById(R.id.wc_button);
        patientAssistantButton = findViewById(R.id.patient_assistant_button);
        foodButton = findViewById(R.id.food_button);
        quickAccessButton = findViewById(R.id.tools_button);

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
        wcButton.setOnClickListener(view -> {

            animationHide.startAnimation(changeFloorButton, animatedView -> animatedView.setVisibility(View.GONE));
            animationHide.startAnimation(toolbar,animatedView -> animatedView.setVisibility(View.GONE));
            setNewNavWindowFragment();
            hideQuickAccessButtons();
            animationHide.startAnimation(quickAccessButton,animatedView -> animatedView.setVisibility(View.GONE));
        });
        foodButton.setOnClickListener(view -> {

            animationHide.startAnimation(changeFloorButton, animatedView -> animatedView.setVisibility(View.GONE));
            animationHide.startAnimation(toolbar,animatedView -> animatedView.setVisibility(View.GONE));
            setNewNavWindowFragment();
            hideQuickAccessButtons();
            animationHide.startAnimation(quickAccessButton,animatedView -> animatedView.setVisibility(View.GONE));
        });
        patientAssistantButton.setOnClickListener(view -> {

            animationHide.startAnimation(changeFloorButton, animatedView -> animatedView.setVisibility(View.GONE));
            animationHide.startAnimation(toolbar,animatedView -> animatedView.setVisibility(View.GONE));
            setNewNavWindowFragment();
            hideQuickAccessButtons();
            animationHide.startAnimation(quickAccessButton,animatedView -> animatedView.setVisibility(View.GONE));
        });

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
            setNavFragmentIsAdd(true);
        }
    }


    @Override
    public void onBack() {
        AnimationAdapter animationShow = new AnimationAdapter(MapActivity.this, R.anim.show_anim);

        setNavFragmentIsAdd(true);

        animationShow.startAnimation(changeFloorButton,animatedView -> animatedView.setVisibility(View.VISIBLE));
        animationShow.startAnimation(toolbar,animatedView -> animatedView.setVisibility(View.VISIBLE));
        animationShow.startAnimation(quickAccessButton,animatedView -> animatedView.setVisibility(View.VISIBLE));

    }

    @Override
    public void startNavigation() {

    }

    @Override
    public void updateNavFragmentState() {
        setNavFragmentIsAdd(true);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i("navFragIsAdd",String.valueOf(getNavFragmentIsAdd()));
        outState.putBoolean("navFragIsAdd", getNavFragmentIsAdd());
    }


    public Boolean getNavFragmentIsAdd() {
        return navFragmentIsAdd;
    }

    public void setNavFragmentIsAdd(Boolean navFragmentIsAdd) {
        this.navFragmentIsAdd = navFragmentIsAdd;
    }

}
