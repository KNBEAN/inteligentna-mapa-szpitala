package bean.pwr.imskamieskiego.GUI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.pwr.imskamieskiego.R;

public class FloorListWindow {

    private Context context;
    private PopupWindow floorListPopupWindow;
    private List<String> floorList;
    private int selectedFloor;
    private FloorSelectListener listener;

    /**
     * Create new popup menu with floor list
     * @param context context of list
     * @param popupLayout layout with floors list view
     */
    public FloorListWindow(Context context, View popupLayout) {
        floorList = new ArrayList<>();

        this.context = context;
        floorListPopupWindow = new PopupWindow(context);
        ListView floorListView = popupLayout.findViewById(R.id.floor_list);
        floorListView.setAdapter(floorsAdapter());
        floorListView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    listener.onFloorSelect(position);
                    selectedFloor = position;
                });

        floorListPopupWindow.setContentView(popupLayout);
        floorListPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        floorListPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        floorListPopupWindow.setOutsideTouchable(true);
        floorListPopupWindow.setFocusable(true);
    }

    /**
     * Show popup window with list of floors
     * @param view the view on which to pin the popup window
     */
    public void showList(View view){
        floorListPopupWindow.showAsDropDown(view);
    }

    /**
     * Hide popup window with floors list
     */
    public void dismissList(){
        floorListPopupWindow.dismiss();
    }

    /**
     * Set floor from list as selected
     * @param selectedFloor position of floor
     */
    public void setSelectedFloor(int selectedFloor) {
        this.selectedFloor = selectedFloor;
    }

    /**
     * Set list of floor names
     * @param floorList array with floor names
     */
    public void setFloorList(String[] floorList) {
        this.floorList.clear();
        this.floorList.addAll(Arrays.asList(floorList));
    }

    /**
     * Set listener of list selection
     * @param listener selection listener
     */
    public void setFloorSelectListener(FloorSelectListener listener){
        this.listener = listener;
    }

    public interface FloorSelectListener{
        /**
         * Called, when select floor from list
         * @param floor floor position from list
         */
        void onFloorSelect(int floor);
    }

    private ArrayAdapter<String> floorsAdapter() {
        return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, floorList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View returnedView = super.getView(position, convertView, parent);
                if (position == selectedFloor) {
                    returnedView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                } else {
                    returnedView.setBackgroundColor(context.getResources().getColor(R.color.fontColorWhite));
                }
                return returnedView;
            }
        };
    }
}
