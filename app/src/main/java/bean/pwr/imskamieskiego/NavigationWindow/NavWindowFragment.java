package bean.pwr.imskamieskiego.NavigationWindow;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import javax.annotation.Nullable;

import bean.pwr.imskamieskiego.R;


public class NavWindowFragment extends Fragment {

    private TextView textViewStart;
    private TextView textViewDestination;
    private CheckBox checkStairs;
    private Button startButton;
    private Fragment searchFragment;
    private NavWindowListener navWindowListener;
    private ImageView nav_button;
    private float ClickX;
    private float ClickY;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_window, container, false);

        textViewStart = (TextView) view.findViewById(R.id.textViewStart);
        textViewDestination = (TextView) view.findViewById(R.id.textViewDestination);
        checkStairs = (CheckBox) view.findViewById(R.id.checkStairs);
        startButton = (Button) view.findViewById(R.id.startButton);
        nav_button = (ImageView) view.findViewById(R.id.nav_button);

        view.setOnTouchListener(myOnTouchListener());

        addSelectPlaceListeners();
        startButtonListener();
        navButtonListener();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        navWindowListener.onBack();
    }

    public View.OnTouchListener myOnTouchListener() {
        View.OnTouchListener onTouchListener = (new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    float nav_buttonX = nav_button.getX();
                    float nav_buttonY = nav_button.getY();

                    if (motionEvent.getX() != nav_buttonX && motionEvent.getY() != nav_buttonY) {
                        removeSearchMapFragment();
                    }
                }
                return false;
            }
        });
        return onTouchListener;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NavWindowListener) {
            navWindowListener = (NavWindowListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NavWindowListener");
        }
        navWindowListener.updateNavFragmentState();

    }

    public void addSelectPlaceListeners(){
        textViewDestination.setOnClickListener(view -> {
            removeSearchMapFragment();
            searchFragment = new SearchFragment();
            goToNextFragment(searchFragment,isDestinationBundle(true),null);

        });

        textViewStart.setOnClickListener(view -> {
            removeSearchMapFragment();
            searchFragment = new SearchFragment();
            goToNextFragment(searchFragment,isDestinationBundle(false),null);

        });
    }

    public void startButtonListener(){
        startButton.setOnClickListener(view -> navWindowListener.startNavigation());
    }

    public void removeSearchMapFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        Fragment searchMap = getFragmentManager().findFragmentByTag("SearchMap");

        if (searchMap != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    //.setCustomAnimations(R.anim.show_anim,R.anim.hide_anim)
                    .remove(searchMap)
                    .commit();
            fragmentManager.popBackStack();
        }
    }

    public Bundle isDestinationBundle(Boolean isDestination){

        Bundle bundle = new Bundle();
        bundle.putBoolean("isDestination",isDestination);

        return bundle;
    }

    /**
     * Set coordinates of clicked item, they will be send to SearchMap fragment
     * to set fragment's location
     *  @param X,Y coords of clicked item
     */

    public Bundle setCoordsBundle(float X, float Y){

        Log.i("CoordX",String.valueOf(X));
        Log.i("CoordY",String.valueOf(Y));

        Bundle bundle = new Bundle();
        bundle.putFloat("xCoord",X);
        bundle.putFloat("yCoord",Y);

        return bundle;
    }

    public void goToNextFragment(Fragment nextFragment, Bundle bundle, @Nullable String fragmentName){

        FragmentManager fragmentManager = getFragmentManager();

        nextFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment searchMap = getFragmentManager().findFragmentByTag("SearchMap");

        if (searchMap != null) {

            if (fragmentName == null){
                fragmentTransaction
                        .add(R.id.drawer_layout, nextFragment, fragmentName)
                        .addToBackStack(null)
                        .commit();
            }
            else if (fragmentName.equals("SearchMap")){
                fragmentTransaction
                        .remove(searchMap)
                        .add(R.id.drawer_layout, nextFragment, fragmentName)
                        .addToBackStack(null)
                        .commit();
                fragmentManager.popBackStack();
            }
        }
        else {
            fragmentTransaction
                    .add(R.id.drawer_layout, nextFragment, fragmentName)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public Boolean getGoDownTheStairs() {
        return checkStairs.isChecked();
    }


    //It has to be: on long press listener
    public void navButtonListener(){

            nav_button.setOnClickListener(view -> {

                ClickX = nav_button.getX() + nav_button.getWidth()/2;
                ClickY = nav_button.getY();
                Fragment searchMapFrag = new SearchMap();
                goToNextFragment(searchMapFrag,
                        setCoordsBundle(ClickX,ClickY),
                        "SearchMap");
            });

    }

}
