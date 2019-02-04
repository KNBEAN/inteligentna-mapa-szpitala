/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bean.pwr.imskamieskiego.R;

public class UserLocationButtonFragment extends Fragment {

    private UserLocationButtonListener listener;

    public UserLocationButtonFragment() {
        // Required empty public constructor
    }

    public static UserLocationButtonFragment newInstance() {
        return new UserLocationButtonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_location_button, container, false);
        FloatingActionButton userNavigationButton = view.findViewById(R.id.my_position_button);
        userNavigationButton.setOnClickListener(this::onButtonClick);
        return view;
    }


    private void onButtonClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setButtonListener(UserLocationButtonListener listener){
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface UserLocationButtonListener {
        void onClick(View view);
    }
}
