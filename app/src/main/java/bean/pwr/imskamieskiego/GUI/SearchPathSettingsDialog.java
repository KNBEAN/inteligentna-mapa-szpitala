/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.view_models.PathSearchMode;

import static bean.pwr.imskamieskiego.view_models.PathSearchMode.*;

public class SearchPathSettingsDialog {

    private AlertDialog dialog;
    private final View dialogView;
    private ChangeListener listener;

    public SearchPathSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_path_search_settings, null);
        builder.setTitle(R.string.path_search_options);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.path_settings_confirm_button, (dialog, which) -> {
            RadioGroup modePathButtonGroup = dialogView.findViewById(R.id.modePathButtonGroup);
            switch (modePathButtonGroup.getCheckedRadioButtonId()) {
                case R.id.fastPathButton:
                    listener.onChange(FAST_PATH);
                    break;
                case R.id.optimalPathButton:
                    listener.onChange(OPTIMAL_PATH);
                    break;
                case R.id.comfortPathButton:
                    listener.onChange(COMFORTABLE_PATH);
                    break;
            }
        });
        dialog = builder.create();
    }

    public void show(PathSearchMode mode) {
        RadioButton modeButton;
        switch (mode) {
            case FAST_PATH:
                modeButton = dialogView.findViewById(R.id.fastPathButton);
                break;
            case OPTIMAL_PATH:
                modeButton = dialogView.findViewById(R.id.optimalPathButton);
                break;
            case COMFORTABLE_PATH:
                modeButton = dialogView.findViewById(R.id.comfortPathButton);
                break;
            default:
                modeButton = dialogView.findViewById(R.id.optimalPathButton);
        }
        modeButton.setChecked(true);
        dialog.show();
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange(PathSearchMode searchMode);
    }
}
