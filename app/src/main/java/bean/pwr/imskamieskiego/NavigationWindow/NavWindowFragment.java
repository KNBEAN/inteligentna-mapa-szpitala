package bean.pwr.imskamieskiego.NavigationWindow;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import bean.pwr.imskamieskiego.R;


public class NavWindowFragment extends Fragment{

    private TextView textViewStart, textViewCel;
    private CheckBox checkStairs;
    private Boolean goDownTheStairs;
    private Button startButton;
    private Fragment searchFragment;
    private InteractionListener interactionListener;
    private int reqCode = 123;
    private Boolean placeLogic = false;
    private String placeName = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_window, container, false);

        textViewStart = (TextView) view.findViewById(R.id.textViewStart);
        textViewCel = (TextView) view.findViewById(R.id.textViewCel);
        checkStairs = (CheckBox) view.findViewById(R.id.checkStairs);
        startButton = (Button) view.findViewById(R.id.startButton);

        setTextPlace(getPlaceName(),getPlaceLogic());

        typeNav();
        checkStairsFunction();
        startNavigation();


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("onPauseFragment","Fragment have paused");
        interactionListener.onFragmentPause(true);
    }
    @Override
    public void onStop() {
        super.onStop();

        Log.i("onStop","Stop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResumeFragment","Fragment has resumed");


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof InteractionListener) {
            //init the listener
            interactionListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    public void typeNav(){
        textViewCel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Cel",Toast.LENGTH_LONG).show();
                searchFragment = new SearchFragment();
                replaceFragment(searchFragment,true);
            }
        });

        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Start",Toast.LENGTH_LONG).show();
                searchFragment = new SearchFragment();
                replaceFragment(searchFragment,false);
            }
        });
    }

    public void checkStairsFunction(){
        checkStairs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    setGoDownTheStairs(true);
                    Log.i("Stairs: ",getGoDownTheStairs().toString());
                }
                else {
                    setGoDownTheStairs(false);
                    Log.i("Stairs: ",getGoDownTheStairs().toString());
                }
            }
        });
    }

    public void startNavigation(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Things doing after click START
            }
        });
    }

    public void replaceFragment(Fragment nextFragment,Boolean logic){


        nextFragment.setTargetFragment(this,reqCode);

        FragmentManager fragmentManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putBoolean("Bool",logic);
        nextFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction

                .replace(R.id.drawer_layout,nextFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == reqCode && resultCode == Activity.RESULT_OK){
            if (data != null){
                String place = data.getStringExtra("Key");
                Boolean placeL = data.getBooleanExtra("Key2",true);
                if (place != null){
                    setPlaceName(place);
                    setPlaceLogic(placeL);
                    Log.v("HY", "Data passed from Child fragment = " + place);
                }

            }
        }
    }



    public void setTextPlace (String placeName, Boolean placeLogic){

        if(placeLogic){
            textViewCel.setText(placeName);
        }
        else if (!placeLogic){
            textViewStart.setText(placeName);
        }

    }

    public Boolean getGoDownTheStairs() {
        return goDownTheStairs;
    }

    public void setGoDownTheStairs(Boolean goDownTheStairs) {
        this.goDownTheStairs = goDownTheStairs;
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }


    public Boolean getPlaceLogic() {
        return placeLogic;
    }

    public void setPlaceLogic(Boolean placeLogic) {
        this.placeLogic = placeLogic;
    }





}
