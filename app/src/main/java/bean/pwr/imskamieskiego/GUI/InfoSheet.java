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
import bean.pwr.imskamieskiego.R;

public class InfoSheet {
    private ConstraintLayout layoutInfoSheet;
    private Button guideToButton;
    private TextView placeName;
    private BottomSheetBehavior sheetBehavior;
    private ImageButton expandSheetButton;
    private ImageView pinButton;
    private Activity parent;
    private InfoSheetListener listener;

    public void setListener(InfoSheetListener listener) {
        this.listener = listener;
    }

    public InfoSheet(Activity parent) {
        this.listener = null;
        this.parent = parent;


        layoutInfoSheet = parent.findViewById(R.id.info_sheet_layout);
        guideToButton = parent.findViewById(R.id.guide_to_button);
        expandSheetButton = parent.findViewById(R.id.info_button);
        placeName = parent.findViewById(R.id.place_name);


        pinButton = parent.findViewById(R.id.pin_button);
        pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


        expandSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

        sheetBehavior = BottomSheetBehavior.from(layoutInfoSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {


            @Override
            public void onStateChanged(@NonNull View infoSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        layoutInfoSheet.setVisibility(View.GONE);
                        Log.i("Bottom sheet", "hidden");
                        listener.onSheetHidden();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.i("Bottom sheet", "expanded");
                        expandSheetButton.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

                        listener.onSheetExpanded();


                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        expandSheetButton.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                        layoutInfoSheet.setVisibility(View.VISIBLE);
                        listener.onSheetCollapsed();


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
        void showFull(String title, String description);

        void onSheetCollapsed();

        void onSheetExpanded();

        void onSheetHidden();


    }


}