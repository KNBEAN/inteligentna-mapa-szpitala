package bean.pwr.imskamieskiego.NavigationWindow;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


import bean.pwr.imskamieskiego.R;



public class SearchMap extends Fragment {

    private LinearLayout popUpWindow;
    private TextView selectedPlaceName;
    private float xCoord;
    private float yCoord;

    public SearchMap() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            xCoord = getArguments().getFloat("xCoord");
            yCoord = getArguments().getFloat("yCoord");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_map, container, false);

        popUpWindow = view.findViewById(R.id.pop_up_window);
        selectedPlaceName = view.findViewById(R.id.selected_place_name);

        selectedPlaceName.setText("X: "+String.valueOf(xCoord)+" Y: "+ String.valueOf(yCoord));

        setViewCoords(popUpWindow);
        animateView(popUpWindow,R.anim.show_anim);


        return view;
    }


    public void setViewCoords(View view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int winWidth = view.getMeasuredWidth();
                int winHeight = view.getMeasuredHeight();

                view.setX(xCoord - winWidth/2);
                view.setY(yCoord - winHeight);

            }
        });
    }

    public void animateView(View view, int animationID){
        Animation animation = AnimationUtils.loadAnimation(getContext(),animationID);
        view.setAnimation(animation);
        view.animate();
    }


}
