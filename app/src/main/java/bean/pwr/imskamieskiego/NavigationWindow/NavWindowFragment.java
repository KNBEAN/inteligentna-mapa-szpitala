package bean.pwr.imskamieskiego.NavigationWindow;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;


import bean.pwr.imskamieskiego.R;


public class NavWindowFragment extends Fragment{

    private TextView textViewStart, textViewDestination, textViewFrom, textViewTo;
    private CheckBox checkStairs;
    private Button startButton;
    private Fragment searchFragment;
    private NavWindowListener navWindowListener;
    private ImageButton oppositeArrowsButton;
    private float[] startTxtCoords, destTxtCoords, fromTxtCoords, toTxtCoords;
    private int clickCounter = 0;



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
        oppositeArrowsButton = (ImageButton) view.findViewById(R.id.oppositeArrowsButton);
        textViewFrom = (TextView) view.findViewById(R.id.textViewFrom);
        textViewTo = (TextView) view.findViewById(R.id.textViewTo);


        setViewsCoords(view);
        selectPlace();
        startButtonListener();
        changeTextViews();

        return view;
    }



    @Override
    public void onPause() {
        super.onPause();
        navWindowListener.onBack();
        Log.i("onPause","Pause");
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
    }

    public void setViewsCoords(View view){
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                startTxtCoords = getViewCoords(textViewStart);
                destTxtCoords = getViewCoords(textViewDestination);
                fromTxtCoords = getViewCoords(textViewFrom);
                toTxtCoords = getViewCoords(textViewTo);

            }
        });
    }
    public void changeTextViews(){
        oppositeArrowsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCounter++;
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    translateYAnimationView(textViewStart, startTxtCoords, destTxtCoords, clickCounter);
                    translateYAnimationView(textViewDestination, destTxtCoords, startTxtCoords, clickCounter);
                    translateYAnimationView(textViewFrom, fromTxtCoords, toTxtCoords, clickCounter);
                    translateYAnimationView(textViewTo, toTxtCoords, fromTxtCoords, clickCounter);
                }
                else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

                    translateXAnimationView(textViewStart, startTxtCoords, destTxtCoords, clickCounter);
                    translateXAnimationView(textViewDestination, destTxtCoords, startTxtCoords, clickCounter);
                    translateXAnimationView(textViewFrom, fromTxtCoords, toTxtCoords, clickCounter);
                    translateXAnimationView(textViewTo, toTxtCoords, fromTxtCoords, clickCounter);
                }

            }
        });
    }
    public float[] getViewCoords(View view){
        float[] viewCoords = new float[2];

        viewCoords[0] = view.getX();
        viewCoords[1] = view.getY();

        return viewCoords;
    }

    public void translateYAnimationView(View view, float[] startCoord, float[] endCoord, int countClick){

        float move;

        if (countClick%2 == 0)  //It represents state when you want go back to first position
            move = 0;
        else
            move = endCoord[1] - startCoord[1];

        ObjectAnimator translateAnimation =
               ObjectAnimator.ofFloat(view,View.TRANSLATION_Y , move);
        translateAnimation.setDuration(500);
        translateAnimation.start();


    }

    public void translateXAnimationView(View view, float[] startCoord, float[] endCoord, int countClick){

        float move;

        if (countClick%2 == 0)   //It represents state when you want go back to first position
            move = 0;
        else
            move = endCoord[0] - startCoord[0];

        ObjectAnimator translateAnimation =
                ObjectAnimator.ofFloat(view,View.TRANSLATION_X , move);
        translateAnimation.setDuration(500);
        translateAnimation.start();

    }


    public void selectPlace(){
        textViewDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragment = new SearchFragment();
                goToNextFragment(searchFragment,true);
            }
        });

        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragment = new SearchFragment();
                goToNextFragment(searchFragment,false);
            }
        });
    }

    public void startButtonListener(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navWindowListener.startNavigation();
            }
        });
    }

    public void goToNextFragment(Fragment nextFragment, Boolean isDestination){

        FragmentManager fragmentManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isDestination",isDestination);
        nextFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction

                .add(R.id.drawer_layout,nextFragment)
                .addToBackStack(null)
                .commit();

    }

    public Boolean getGoDownTheStairs() {
        return checkStairs.isChecked();
    }

}
