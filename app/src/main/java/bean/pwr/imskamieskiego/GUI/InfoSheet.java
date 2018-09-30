package bean.pwr.imskamieskiego.GUI;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.chrono.HijrahChronology;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.Location;
import bean.pwr.imskamieskiego.model.map.LocationFactory;

public class InfoSheet {
    private ConstraintLayout layoutInfoSheet;
    private Button guideToButton;
    private TextView placeName;
    private TextView placeInfo;
    private BottomSheetBehavior sheetBehavior;
    private ImageButton expandSheetButton;
    private ImageView pinButton;
    private Activity parent;
    private InfoSheetListener listener;
    public static int COLLAPSED = BottomSheetBehavior.STATE_COLLAPSED;
    public static int EXPANDED = BottomSheetBehavior.STATE_EXPANDED;
    public static int HIDDEN = BottomSheetBehavior.STATE_HIDDEN;





    public void setListener(InfoSheetListener listener) {
        this.listener = listener;
    }

    public InfoSheet(Activity parent) {
        this.listener = null;
        this.parent = parent;

        infoSheetComponentsInit();


        sheetBehavior = BottomSheetBehavior.from(layoutInfoSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {


            @Override
            public void onStateChanged(@NonNull View infoSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        layoutInfoSheet.setVisibility(View.GONE);
                        Log.i("Bottom sheet", "hidden");
                        if (listener!=null){
                            listener.onSheetHidden();
                        }

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.i("Bottom sheet", "expanded");
                        expandSheetButton.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                        if (listener!=null){
                            listener.onSheetExpanded();
                        }




                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        expandSheetButton.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                        layoutInfoSheet.setVisibility(View.VISIBLE);
                        if (listener!=null){
                            listener.onSheetCollapsed();
                        }




                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }


            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }


    private void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            expandSheetButton.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);

        }
    }


    public interface InfoSheetListener {
        void guideTo();

        void onSheetCollapsed();

        void onSheetExpanded();

        void onSheetHidden();


    }

    public int getBottomSheetState() {
        return sheetBehavior.getState();
    }

    public void setBottomSheetState(int bottomSheetState) {
        sheetBehavior.setState(bottomSheetState);
    }
    public void showInfoSheet(Location location, int state){
        String name = location.getName();
        String description = location.getDescription();
        placeName.setText(name);
        placeInfo.setText(description);
        sheetBehavior.setState(state);


    }
    public void showInfoSheet(Location location){
        String name = location.getName();
        String description = location.getDescription();
        placeName.setText(name);
        placeInfo.setText(description);
        sheetBehavior.setState(COLLAPSED);


    }
    private void infoSheetComponentsInit () {
        layoutInfoSheet = parent.findViewById(R.id.info_sheet_layout);
        guideToButton = parent.findViewById(R.id.guide_to_button);
        expandSheetButton = parent.findViewById(R.id.info_button);
        placeName = parent.findViewById(R.id.place_name);
        placeInfo = parent.findViewById(R.id.place_info);


        guideToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                listener.guideTo();
                }

            }
        });
        pinButton = parent.findViewById(R.id.pin_button);
        pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = LocationFactory.create("SOR", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ");
                showInfoSheet(location);
            }
        });


        expandSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

    }




}