package bean.pwr.imskamieskiego.GUI;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.model.map.Location;

public class LocationSuggestionAdapter extends CursorAdapter{

    private View.OnClickListener selectItemListener;

    public LocationSuggestionAdapter(Context context, Cursor cursor, View.OnClickListener selectListener){
        super(context, cursor, false);
        this.selectItemListener = selectListener;
    }

    public LocationSuggestionAdapter(Context context, List<Location> locations, View.OnClickListener selectListener){
        super(context, listToCursor(locations), false);
        this.selectItemListener = selectListener;
    }

    private static Cursor listToCursor(List<Location> locations){
        String[] columns = new String[] { "_id", "name"};
        MatrixCursor cursor = new MatrixCursor(columns);
        for (Location location:locations) {
            cursor.addRow(new Object[]{location.getId(), location.getName()});
        }
        return cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.suggestion_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

        TextView locationName = view.findViewById(R.id.locationName);
        locationName.setText(name);

        view.setOnClickListener(selectItemListener);

//        view.setOnClickListener(view1 -> {
//            //take next action based user selected item
//            TextView nameText = view1.findViewById(R.id.locationName);
//            Toast.makeText(context, "Selected suggestion "+nameText.getText(),
//                    Toast.LENGTH_LONG).show();
//        });
    }
}
