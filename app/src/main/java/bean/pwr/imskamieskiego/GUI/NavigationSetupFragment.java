/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.GUI.locationSearch.LocationSearchInterface;
import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.view_models.NavigationSetupViewModel;
import bean.pwr.imskamieskiego.view_models.NavigationSetupViewModelFactory;

/**
 * This fragment is responsible for selecting the start point for navigation.
 */
public class NavigationSetupFragment extends Fragment {

    private static final String TAG = "NavSetupFragment";
    private static final String TARGET_LOCATION_PARAM = "targetLocationName";

    private String targetLocationName;

    private NavigationSetupViewModel viewModel;

    private Toolbar toolbar;
    private TextView textViewStart;
    private TextView textViewDestination;
    private ImageButton settingButton;
    private Button startButton;
    private NavigationSetupListener listener;
    private ViewGroup settingsLayout;
    private RadioGroup modePathButtonGroup;


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
        settingButton = view.findViewById(R.id.settingButton);
        startButton = view.findViewById(R.id.startButton);
        settingsLayout = view.findViewById(R.id.settingsLayout);
        settingButton.setOnClickListener(view1 -> {
            if (settingsLayout.getVisibility() == View.GONE) {
                settingsLayout.setVisibility(View.VISIBLE);
            } else {
                settingsLayout.setVisibility(View.GONE);
            }
        });
        modePathButtonGroup = view.findViewById(R.id.pathModeButtonGroup);
        startButton.setOnClickListener(view1 -> {
            switch (modePathButtonGroup.getCheckedRadioButtonId()) {
                case R.id.fastPathButton:
                    listener.startNavigation(NavigationSetupListener.FAST_PATH);
                    break;
                case R.id.optimalPathButton:
                    listener.startNavigation(NavigationSetupListener.OPTIMAL_PATH);
                    break;
                case R.id.comfortPathButton:
                    listener.startNavigation(NavigationSetupListener.COMFORT_PATH);
                    break;
            }
        });
        textViewStart.setOnClickListener(view1 -> listener.startSearch());
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
        loadUserPreferences();
        Snackbar.make(view.getRootView(), R.string.navigation_setup_snackbar_hint, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        int pathSearchModeButtonID = modePathButtonGroup.getCheckedRadioButtonId();

        SharedPreferences userSettings = getContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putInt("path_search_mode", pathSearchModeButtonID);
        editor.apply();

        super.onDestroyView();
    }

    public void loadUserPreferences() {
        SharedPreferences userSettings = getContext().getSharedPreferences("user_settings", Context.MODE_PRIVATE);
        int pathSearchMode = userSettings.getInt("path_search_mode", -1);
        List<Integer> optionButtons = Arrays.asList(R.id.fastPathButton, R.id.optimalPathButton, R.id.comfortPathButton);
        if (optionButtons.contains(pathSearchMode)) {
            modePathButtonGroup.check(pathSearchMode);
        }
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

    public interface NavigationSetupListener extends LocationSearchInterface {
        int FAST_PATH = 1;
        int OPTIMAL_PATH = 2;
        int COMFORT_PATH = 3;

        /**
         * Called, when navigation setup fragment starts navigation
         *
         * @param pathSearchMode
         */
        void startNavigation(int pathSearchMode);

        /**
         * Called, when navigation setup fragment selects start point
         *
         * @param selectedStartPoint
         */
        void onStartPointSelected(MapPoint selectedStartPoint);
    }

}
