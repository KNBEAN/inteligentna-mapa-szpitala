package bean.pwr.imskamieskiego.GUI.NavigationWindow;

import android.arch.lifecycle.ViewModelProviders;
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
import android.support.v7.widget.Toolbar;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.view_models.NavigationSetupViewModel;
import bean.pwr.imskamieskiego.view_models.NavigationSetupViewModelFactory;


public class NavigationSetupFragment extends Fragment {

    private static final String TAG = "NavSetupFragment";
    private static final String TARGET_LOCATION_PARAM = "targetLocationName";

    private String targetLocationName;

    private NavigationSetupViewModel viewModel;

    private Toolbar toolbar;
    private TextView textViewStart;
    private TextView textViewDestination;
    private CheckBox checkStairs;
    private Button startButton;
    private NavigationSetupListener listener;


    public NavigationSetupFragment() {
        // Required empty public constructor
    }

    public static NavigationSetupFragment newInstance(String targetLocationName) {
        NavigationSetupFragment fragment = new NavigationSetupFragment();
        Bundle args = new Bundle();
        args.putString(TARGET_LOCATION_PARAM, targetLocationName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getStartLocation().observe(this, location -> {
            if (location != null) {
                textViewStart.setText(location.getName());
                startButton.setEnabled(true);
            }
        });

        viewModel.getTargetLocationName().observe(this, textViewDestination::setText);

        viewModel.getStartPoint().observe(this, listener::onStartPointSelected);

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

        if (getArguments() != null) {
            targetLocationName = getArguments().getString(TARGET_LOCATION_PARAM);
        }

        viewModel = ViewModelProviders.of(
                this,
                new NavigationSetupViewModelFactory(getActivity().getApplication(), targetLocationName))
                .get(NavigationSetupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_window, container, false);
        toolbar = view.findViewById(R.id.navi_setup_toolbar);
        textViewStart = view.findViewById(R.id.textViewStart);
        textViewDestination = view.findViewById(R.id.textViewDestination);
        checkStairs = view.findViewById(R.id.checkStairs);
        startButton = view.findViewById(R.id.startButton);

        startButton.setOnClickListener(view1 -> listener.startNavigation(checkStairs.isSelected()));
        textViewStart.setOnClickListener(view1 -> listener.startPointSearchRequest());
        toolbar.setNavigationOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Snackbar.make(view.getRootView(), R.string.navigation_setup_snackbar_hint, Snackbar.LENGTH_LONG).show();
    }

    public void setStartLocation(Location startLocation) {
        viewModel.setStartLocation(startLocation);
    }

    public void setStartPoint(MapPoint startPoint) {
        viewModel.setStartPoint(startPoint);
    }

    public MapPoint getStartPoint() {
        return viewModel.getStartPoint().getValue();
    }

    public interface NavigationSetupListener {
        void startPointSearchRequest();

        void startNavigation(boolean avoidStairs);

        void onStartPointSelected(MapPoint selectedStartPoint);
    }

}
