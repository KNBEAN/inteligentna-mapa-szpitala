package bean.pwr.imskamieskiego;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton wcButton;
    private FloatingActionButton patientAssistantButton;
    private FloatingActionButton foodButton;
    private FloatingActionButton quickAccessButton;
    private Button patientAssistantButtonDescription;
    private Button foodButtonDescription;
    private Button wcButtonDescription;
    private ImageButton changeFloorButton;
    private ConstraintLayout layoutBottomSheet;
    private Button guideToButton;
    private TextView placeName;
    private BottomSheetBehavior sheetBehavior;
    private FloatingActionButton infoButton;
    private ImageView pinButton;
    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        quickAccessButtonInit();
        changeFloorButtonInit ();
        bottomSheetInit();



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle hamburgerButton = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(hamburgerButton);
        hamburgerButton.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pinButton = findViewById(R.id.pin_button);
        pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    public void rotateRight(View v) {
        final Animation rotateRightAnimation = AnimationUtils.loadAnimation(
                MapActivity.this, R.anim.rotate_show);
        v.startAnimation(rotateRightAnimation);


    }

    public void rotateLeft(View v) {
        final Animation rotateLeftAnimation = AnimationUtils.loadAnimation(
                MapActivity.this, R.anim.rotate_hide);
        v.startAnimation(rotateLeftAnimation);

    }

    public void animate_hide(View v) {
        final Animation hideAnimation = AnimationUtils.loadAnimation(
                MapActivity.this, R.anim.hide_anim);
        v.startAnimation(hideAnimation);


    }

    public void animate_show(View v) {
        final Animation showAnimation = AnimationUtils.loadAnimation(
                MapActivity.this, R.anim.show_anim);
        v.startAnimation(showAnimation);


    }

    public void hideQuickAccessButtons() {

        // animate_hide(wcButton);
        // animate_hide(foodButton);
        // animate_hide(patientAssistantButton);
        rotateLeft(quickAccessButton);

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

         //animate_show(wcButton);
         //animate_show(foodButton);
        // animate_show(patientAssistantButton);
        rotateRight(quickAccessButton);

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
        wcButtonDescription =findViewById(R.id.wc_button_description);

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
        wcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideQuickAccessButtons();

            }
        });
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideQuickAccessButtons();
            }
        });
        patientAssistantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideQuickAccessButtons();
            }
        });

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
    private void bottomSheetInit() {
        layoutBottomSheet = findViewById(R.id.bottom_sheet_layout);
        guideToButton = findViewById(R.id.guide_to_button);
        infoButton = findViewById(R.id.info_button);
        placeName = findViewById(R.id.place_name);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        quickAccessButton.setVisibility(View.VISIBLE);
                        quickAccessButton.setClickable(true);
                        Log.i("Bottom sheet","hidden");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.i("Bottom sheet","expanded");

                        hideQuickAccessButtons();
                        quickAccessButton.setVisibility(View.GONE);
                        infoButton.setImageResource(android.R.drawable.ic_delete);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        hideQuickAccessButtons();
                        quickAccessButton.setVisibility(View.GONE);
                        quickAccessButton.setClickable(false);

                        infoButton.setImageResource(android.R.drawable.ic_dialog_info);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                         break;
                    case BottomSheetBehavior.STATE_SETTLING:
                         break;
                }
            }


            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            infoButton.setImageResource(android.R.drawable.ic_delete);

        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            infoButton.setImageResource(android.R.drawable.ic_dialog_info);
        }
    }



}
