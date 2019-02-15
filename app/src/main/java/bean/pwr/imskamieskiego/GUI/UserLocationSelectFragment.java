/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */


package bean.pwr.imskamieskiego.GUI;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import bean.pwr.imskamieskiego.GUI.locationSearch.LocationSearchInterface;
import bean.pwr.imskamieskiego.QRCodeReader.QRCodeReaderActivity;
import bean.pwr.imskamieskiego.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserLocationSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLocationSelectFragment extends Fragment {

    private Toolbar toolbar;
    private ImageButton searchButton;
    private LocationSearchInterface searchListener;


    public UserLocationSelectFragment() {
        // Required empty public constructor
    }

    public static UserLocationSelectFragment newInstance() {
        return new UserLocationSelectFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LocationSearchInterface) {
            searchListener = (LocationSearchInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NavWindowListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_location_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.userLocationToolbar);
        toolbar.setNavigationOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle(R.string.user_location_selection_title);

        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view1 -> searchListener.startSearch());

        FloatingActionButton cameraButton = view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), QRCodeReaderActivity.class);
            startActivity(intent);
        });
    }
}
