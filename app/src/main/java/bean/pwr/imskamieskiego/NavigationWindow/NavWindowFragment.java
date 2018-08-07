package bean.pwr.imskamieskiego.NavigationWindow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import bean.pwr.imskamieskiego.R;


public class NavWindowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView textViewStart, textViewCel;
    private CheckBox checkStairs;
    private int index;
    private StringReciver stringReciver;
    private Boolean goDownTheStairs;
    private Button startButton;
    private Button button;
    private Fragment searchFragment;




    // TODO: Rename and change types and number of parameters
    public static NavWindowFragment newInstance(String param1, String param2) {
        NavWindowFragment fragment = new NavWindowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
                replaceFragment(searchFragment);
            }
        });

        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Start",Toast.LENGTH_LONG).show();

                searchFragment = new SearchFragment();
                replaceFragment(searchFragment);
            }
        });
    }

    public void replaceFragment(Fragment nextFragment){

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container,nextFragment)
                .addToBackStack(null)
                .commit();

    }




}
