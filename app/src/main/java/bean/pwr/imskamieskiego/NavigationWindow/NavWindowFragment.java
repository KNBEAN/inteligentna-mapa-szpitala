package bean.pwr.imskamieskiego.NavigationWindow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import bean.pwr.imskamieskiego.R;


public class NavWindowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final Boolean hintText = null;

    private TextView textViewStart, textViewCel;
    private CheckBox checkStairs;
    private int index;
    private StringReciver stringReciver;
    private Boolean goDownTheStairs;
    private Button startButton;
    private Button button;
    private Fragment searchFragment;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_window, container, false);

        button = getActivity().findViewById(R.id.button);


        textViewStart = (TextView) view.findViewById(R.id.textViewStart);
        textViewCel = (TextView) view.findViewById(R.id.textViewCel);
        checkStairs = (CheckBox) view.findViewById(R.id.checkStairs);
        startButton = (Button) view.findViewById(R.id.startButton);

        stringReciver = new StringReciver();

        typeNav();
        checkStairsFunction();
        startNavigation();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("onPauseFragment","Fragment have paused");
        buttonsVisibility(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResumeFragment","Fragment have been resumed");
        buttonsVisibility(false);
    }

    public void buttonsVisibility(boolean visible){
        //Add next buttons
        if(visible){
            button.setVisibility(View.VISIBLE);
        }
        else{
            button.setVisibility(View.GONE);
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

        FragmentManager mFragmentManager;

        Bundle bundle = new Bundle();
        bundle.putBoolean("Bool",logic);

        nextFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction

                .replace(R.id.fragment_container,nextFragment)
                .addToBackStack(null)
                .commit();

    }

    public Boolean getGoDownTheStairs() {
        return goDownTheStairs;
    }

    public void setGoDownTheStairs(Boolean goDownTheStairs) {
        this.goDownTheStairs = goDownTheStairs;
    }



}
