package bean.pwr.imskamieskiego.NavigationWindow;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import bean.pwr.imskamieskiego.R;



public class SearchMap extends Fragment{

    private LinearLayout popUpWindow;
    private LinearLayout popUpContent;
    private TextView selectedPlaceName;
    private Button fromButton;
    private Button toButton;
    private float xCoord;
    private float yCoord;
    private int screenWidth;
    private int screenHeight;


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
        popUpContent = view.findViewById(R.id.pop_up_content);
        fromButton = view.findViewById(R.id.fromButton);
        toButton = view.findViewById(R.id.toButton);

        selectedPlaceName.setText("X: "+String.valueOf(xCoord)+" Y: "+ String.valueOf(yCoord));

        getScreenDimensions();
        buttonsListener();
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
                int contentHeight = popUpContent.getMeasuredHeight();
                int winWidth = view.getMeasuredWidth();

                view.setX(screenWidth/2 - winWidth/2);
                view.setY(screenHeight/2 - contentHeight);

            }
        });
    }

    public void animateView(View view, int animationID){
        Animation animation = AnimationUtils.loadAnimation(getContext(),animationID);
        view.setAnimation(animation);
        view.animate();
    }

    public void buttonsListener (){
        fromButton.setOnClickListener(view1 -> {
            Log.i("SearchMapButton","fromButton");
        });
        toButton.setOnClickListener(view -> {
            Log.i("SearchMapButton","toButton");
        });
    }

    public void getScreenDimensions(){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

}
