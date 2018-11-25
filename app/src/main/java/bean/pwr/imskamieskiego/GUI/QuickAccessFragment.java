/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bean.pwr.imskamieskiego.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuickAccessListener} interface
 * to handle interaction events.
 * Use the {@link QuickAccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickAccessFragment extends Fragment {

    private QuickAccessListener listener;

    public enum QuickAccessButtons {
        WC,
        FOOD,
        ASSISTANT
    }

    private FloatingActionButton wcButton;
    private FloatingActionButton patientAssistantButton;
    private FloatingActionButton foodButton;
    private FloatingActionButton quickAccessButton;

    private TextView patientAssistantButtonLabel;
    private TextView foodButtonLabel;
    private TextView wcButtonLabel;

    private boolean isExpanded;

    public QuickAccessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuickAccessFragment.
     */
    public static QuickAccessFragment newInstance() {
        QuickAccessFragment fragment = new QuickAccessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quick_access_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        wcButton = view.findViewById(R.id.wc_button);
        patientAssistantButton = view.findViewById(R.id.patient_assistant_button);
        foodButton = view.findViewById(R.id.food_button);
        quickAccessButton = view.findViewById(R.id.tools_button);

        patientAssistantButtonLabel = view.findViewById(R.id.qa_asistant_label);
        foodButtonLabel = view.findViewById(R.id.qa_food_label);
        wcButtonLabel = view.findViewById(R.id.qa_wc_label);

        isExpanded = false;
        quickAccessButton.setOnClickListener(view1 -> toggleQuickAccessButtons());


        wcButton.setOnClickListener(view1 -> {
            listener.onQAButtonClick(QuickAccessButtons.WC);
            hideQuickAccessButtons();
        });
        patientAssistantButton.setOnClickListener(view1 -> {
            listener.onQAButtonClick(QuickAccessButtons.ASSISTANT);
            hideQuickAccessButtons();
        });
        foodButton.setOnClickListener(view1 -> {
            listener.onQAButtonClick(QuickAccessButtons.FOOD);
            hideQuickAccessButtons();
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuickAccessListener) {
            listener = (QuickAccessListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement QuickAccessListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void toggleQuickAccessButtons() {
        if (isExpanded){
            hideQuickAccessButtons();
        }else {
            showQuickAccessButtons();
        }
    }

    public void hideQuickAccessButtons() {

        AnimationAdapter animationRotateHide = new AnimationAdapter(this.getContext() ,R.anim.rotate_hide);
        AnimationAdapter animationHide = new AnimationAdapter(this.getContext(), R.anim.quick_access_hide_anim);

        AnimationAdapter.AnimationEndListener animationEndListener = view -> {
            view.setVisibility(View.GONE);
        };

        wcButton.setClickable(false);
        foodButton.setClickable(false);
        patientAssistantButton.setClickable(false);
        animationRotateHide.startAnimation(quickAccessButton,null);
        animationHide.startAnimation(wcButton, animationEndListener);
        animationHide.startAnimation(foodButton, animationEndListener);
        animationHide.startAnimation(patientAssistantButton, animationEndListener);

        animationHide.startAnimation(wcButtonLabel, animationEndListener);
        animationHide.startAnimation(foodButtonLabel, animationEndListener);
        animationHide.startAnimation(patientAssistantButtonLabel, animationEndListener);


        isExpanded = false;

    }

    public void showQuickAccessButtons() {

        AnimationAdapter animationRotateShow = new AnimationAdapter(this.getContext(), R.anim.rotate_show);
        animationRotateShow.startAnimation(quickAccessButton,null);

        AnimationAdapter showAnimation = new AnimationAdapter(this.getContext(), R.anim.quick_access_show_anim);
        showAnimation.startAnimation(wcButton, view -> view.setClickable(true));
        showAnimation.startAnimation(foodButton, view -> view.setClickable(true));
        showAnimation.startAnimation(patientAssistantButton, view -> view.setClickable(true));

        showAnimation.startAnimation(wcButtonLabel, null);
        showAnimation.startAnimation(foodButtonLabel, null);
        showAnimation.startAnimation(patientAssistantButtonLabel, null);

        wcButton.setVisibility(View.VISIBLE);
        foodButton.setVisibility(View.VISIBLE);
        patientAssistantButton.setVisibility(View.VISIBLE);
        patientAssistantButtonLabel.setVisibility(View.VISIBLE);
        foodButtonLabel.setVisibility(View.VISIBLE);
        wcButtonLabel.setVisibility(View.VISIBLE);

        isExpanded = true;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public interface QuickAccessListener {
        void onQAButtonClick(QuickAccessButtons button);
    }
}
