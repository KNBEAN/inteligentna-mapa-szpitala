/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bean.pwr.imskamieskiego.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationRouteFragment extends Fragment {

    private Toolbar toolbar;

    public NavigationRouteFragment() {
        // Required empty public constructor
    }

    public static NavigationRouteFragment newInstance() {
        return new NavigationRouteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.routeToolbar);
        toolbar.setNavigationOnClickListener(view1 -> {
            if (getActivity() != null){
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle(R.string.route_fragment_title);
        toolbar.setSubtitle(R.string.route_fragment_subtitle);
    }
}
