package bean.pwr.imskamieskiego.GUI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.Location;

public class InfoSheet extends Fragment {

    private static final String TAG = "InfoSheet";
    private static final String LOCATION_NAME = "location_name";
    private static final String LOCATION_DESC = "location_desc";

    private ImageButton expandSheetButton;
    private InfoSheetListener listener;

    private String locationName;
    private String locationDesc;

    private TextView placeInfo;
    private boolean descriptionIsVisible = false;



    public InfoSheet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param location
     * @return A new instance of fragment InfoSheet.
     */
    public static InfoSheet newInstance(Location location) {
        InfoSheet fragment = new InfoSheet();
        Bundle args = new Bundle();
        args.putString(LOCATION_NAME, location.getName());
        args.putString(LOCATION_DESC, location.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationName = getArguments().getString(LOCATION_NAME);
            locationDesc = getArguments().getString(LOCATION_DESC);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InfoSheetListener) {
            listener = (InfoSheetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InfoSheetListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.info_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConstraintLayout layoutInfoSheet = view.findViewById(R.id.info_sheet_layout);
        Button guideToButton = view.findViewById(R.id.guide_to_button);
        expandSheetButton = view.findViewById(R.id.info_button);
        TextView placeName = view.findViewById(R.id.place_name);
        placeInfo = view.findViewById(R.id.place_info);

        placeName.setText(locationName);
        placeInfo.setText(locationDesc != null ? locationDesc : "");


        guideToButton.setOnClickListener(v -> listener.infoSheetClose(true));
        expandSheetButton.setOnClickListener(v -> toggleBottomSheet());
    }

    private void toggleBottomSheet() {
        if (!descriptionIsVisible) {
            placeInfo.setVisibility(View.VISIBLE);
            descriptionIsVisible = true;
        } else {
            placeInfo.setVisibility(View.GONE);
            descriptionIsVisible = false;
        }
    }

    public interface InfoSheetListener {
        void infoSheetClose(boolean actionClicked);
    }

}