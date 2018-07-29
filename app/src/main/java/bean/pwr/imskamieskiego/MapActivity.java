package bean.pwr.imskamieskiego;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
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
import android.widget.LinearLayout;

import static android.view.View.GONE;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Animations declaration
        final Animation rotateShow = AnimationUtils.loadAnimation(MapActivity.this, R.anim.rotate_show);
        final Animation rotateHide = AnimationUtils.loadAnimation(MapActivity.this, R.anim.rotate_hide);
        final Animation hideButtonAnimation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.hide_anim);
        final Animation showButtonAnimation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.show_anim);


        // Initialization of buttons from content_map.xml
        final FloatingActionButton wcButton = findViewById(R.id.wc_button);
        final FloatingActionButton patientAssistantButton = findViewById(R.id.patient_assistant_button);
        final FloatingActionButton foodButton = findViewById(R.id.food_button);
        final FloatingActionButton toolsButton = findViewById(R.id.tools_button);
        toolsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodButton.getVisibility() == View.VISIBLE &&
                        (wcButton.getVisibility() == View.VISIBLE)) {
                    wcButton.setVisibility(GONE);
                    foodButton.setVisibility(GONE);
                    patientAssistantButton.setVisibility(GONE);
                    toolsButton.startAnimation(rotateHide);
                    wcButton.startAnimation(showButtonAnimation);
                    foodButton.startAnimation(showButtonAnimation);
                    patientAssistantButton.startAnimation(showButtonAnimation);
                }
                else {
                    wcButton.setVisibility(View.VISIBLE);
                    foodButton.setVisibility(View.VISIBLE);
                    patientAssistantButton.setVisibility(View.VISIBLE);
                    toolsButton.startAnimation(rotateShow);
                    wcButton.startAnimation(hideButtonAnimation);
                   foodButton.startAnimation(hideButtonAnimation);
                   patientAssistantButton.startAnimation(hideButtonAnimation);
                }
            }
        });








        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_item) {
            return true;
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

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_search) {


        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
