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

    public void showList(View view){
        floorListPopupWindow.showAsDropDown(view);
    }

    public void dismissList(){
        floorListPopupWindow.dismiss();
    }

    public void setSelectedFloor(int selectedFloor) {
        this.selectedFloor = selectedFloor;
    }

    public void setFloorList(String[] floorList) {
        this.floorList.clear();
        this.floorList.addAll(Arrays.asList(floorList));
    }

    public void setFloorSelectListener(FloorSelectListener listener){
        this.listener = listener;
    }


    public interface FloorSelectListener{
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
