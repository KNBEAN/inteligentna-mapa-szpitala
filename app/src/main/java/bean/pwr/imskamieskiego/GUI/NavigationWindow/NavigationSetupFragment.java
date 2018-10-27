package bean.pwr.imskamieskiego.GUI.NavigationWindow;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import bean.pwr.imskamieskiego.R;


public class NavigationSetupFragment extends Fragment{

    private static final String TAG = "NavigationSetupFragment";
    private static final String DESTINATION_NAME = "destinationName";
    private TextView textViewStart;
    private TextView textViewDestination;
    private CheckBox checkStairs;
    private Button startButton;
    private NavigationSetupListener listener;


    public NavigationSetupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InfoSheet.
     */
    public static NavigationSetupFragment newInstance(String destination_name) {
        NavigationSetupFragment fragment = new NavigationSetupFragment();
        Bundle args = new Bundle();
        args.putString(DESTINATION_NAME, destination_name);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavigationSetupListener) {
            listener = (NavigationSetupListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NavWindowListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_window, container, false);
        textViewStart = (TextView) view.findViewById(R.id.textViewStart);
        textViewDestination = (TextView) view.findViewById(R.id.textViewDestination);
        checkStairs = (CheckBox) view.findViewById(R.id.checkStairs);
        startButton = (Button) view.findViewById(R.id.startButton);

        if (getArguments() != null) {
            String destinationName = getArguments().getString(DESTINATION_NAME);
            textViewDestination.setText(destinationName);
        }

        startButton.setOnClickListener(view1 -> listener.startNavigation(checkStairs.isSelected()));
        textViewStart.setOnClickListener(view1 -> listener.startPointSearchRequest());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Snackbar.make(view.getRootView(), R.string.navigation_setup_snackbar_hint, Snackbar.LENGTH_LONG).show();
    }

    public void setStartLocationName(String locationName){
        textViewStart.setText(locationName);
    }

    public void setDestinationLocationName(String locationName){
        textViewDestination.setText(locationName);
    }

    public interface NavigationSetupListener {
        void startPointSearchRequest();
        void startNavigation(boolean avoidStairs);
    }

}
