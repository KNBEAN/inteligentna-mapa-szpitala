/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.showcase;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;

import org.jetbrains.annotations.NotNull;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.utils.Preferences;

/**
 * This class provides methods which shows user tutorial steps
 */
public class ShowcaseController {
    private static final int WELCOME = 0;
    private static final int USER_LOCATION = 1;
    private static final int TARGET_SELECTION = 2;
    private static final int TARGET_INFO = 3;
    private static final int END = 4;

    private static int tutorialStage = 0;

    /**
     * Creates sequence of first stage of user tutorial. If this stage was finished, next call of
     * this method will return empty sequence.
     * @param activity
     * @return sequence of tutorial
     */
    public static ShowCaseSequence welcomeStage(Activity activity) {
        if (tutorialStage > WELCOME || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = WELCOME;
        BubbleShowCaseBuilder helloMessage = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_welcome_title))
                .description(activity.getString(R.string.tutorial_welcome_message));
        BubbleShowCaseBuilder userLocationMessage = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_user_location_title))
                .description(activity.getString(R.string.tutorial_user_location_message))
                .targetView(activity.findViewById(R.id.my_position_button));

        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(helloMessage))
                .addItem(new ShowCaseSequenceItem(userLocationMessage) {
                    @Override
                    public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {
                    }

                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onTargetClick(bubbleShowCase);
                        activity.findViewById(R.id.my_position_button).callOnClick();
                    }
                })
                .setCloseListener(() -> closeMessage(activity));
    }

    /**
     * Creates sequence of second stage of user tutorial. If this stage was finished, next call of
     * this method will return empty sequence.
     * @param activity
     * @return sequence of tutorial
     */
    public static ShowCaseSequence userLocationStage(Activity activity) {
        if (tutorialStage > USER_LOCATION || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = USER_LOCATION;
        BubbleShowCaseBuilder mapTouch = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_touch_map_title))
                .description(activity.getString(R.string.tutorial_touch_map_message));


        BubbleShowCaseBuilder search = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_search_title))
                .description(activity.getString(R.string.tutorial_search_message))
                .targetView(activity.findViewById(R.id.searchButton));

        BubbleShowCaseBuilder qrCodeScanner = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_qr_code_title))
                .description(activity.getString(R.string.tutorial_qr_code_message))
                .targetView(activity.findViewById(R.id.cameraButton));

        BubbleShowCaseBuilder selectOwnPosition = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_your_turn_title))
                .description(activity.getString(R.string.tutorial_your_turn_message));
        BubbleShowCaseBuilder floorChange = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_floor_change_title))
                .description(activity.getString(R.string.tutorial_floor_change_message))
                .targetView(activity.findViewById(R.id.floors_button))
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
                .textColorResourceId(R.color.fontColorBlack);

        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(mapTouch))
                .addItem(new ShowCaseSequenceItem(search))
                .addItem(new ShowCaseSequenceItem(qrCodeScanner))
                .addItem(new ShowCaseSequenceItem(floorChange))
                .addItem(new ShowCaseSequenceItem(selectOwnPosition))
                .setCloseListener(() -> closeMessage(activity));
    }

    /**
     * Creates sequence of third stage of user tutorial. If this stage was finished, next call of
     * this method will return empty sequence.
     * @param activity
     * @return sequence of tutorial
     */
    public static ShowCaseSequence targetSelectStage(Activity activity) {
        if (tutorialStage > TARGET_SELECTION || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = TARGET_SELECTION;
        BubbleShowCaseBuilder selectTarget = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_target_title))
                .description(activity.getString(R.string.tutorial_target_message));

        BubbleShowCaseBuilder fastTravelButtons = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_quick_access_title))
                .description(activity.getString(R.string.tutorial_quick_access_message))
                .targetView(activity.findViewById(R.id.quickAccessFragment))
                .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE);

        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(selectTarget))
                .addItem(new ShowCaseSequenceItem(fastTravelButtons))
                .setCloseListener(() -> closeMessage(activity));
    }

    /**
     * Creates sequence of fourth stage of user tutorial. If this stage was finished, next call of
     * this method will return empty sequence.
     * @param activity
     * @return sequence of tutorial
     */
    public static ShowCaseSequence infoSheetStage(Activity activity) {
        if (tutorialStage > TARGET_INFO || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = TARGET_INFO;
        BubbleShowCaseBuilder infoSheet = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_info_sheet_title))
                .description(activity.getString(R.string.tutorial_info_sheet_message))
                .targetView(activity.findViewById(R.id.info_button));

        BubbleShowCaseBuilder pathOptions = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_path_settings_title))
                .description(activity.getString(R.string.tutorial_path_settings_message))
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .textColorResourceId(R.color.fontColorBlack)
                .targetView(getNavigationButton(((Toolbar) activity.findViewById(R.id.toolbar))));

        BubbleShowCaseBuilder startPathSearch = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_start_path_title))
                .description(activity.getString(R.string.tutorial_start_path_message))
                .targetView(activity.findViewById(R.id.guide_to_button));


        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(infoSheet) {
                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onTargetClick(bubbleShowCase);
                        activity.findViewById(R.id.info_button).callOnClick();
                    }
                })
                .addItem(new ShowCaseSequenceItem(pathOptions))
                .addItem(new ShowCaseSequenceItem(startPathSearch) {
                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onTargetClick(bubbleShowCase);
                        activity.findViewById(R.id.guide_to_button).callOnClick();
                    }
                })
                .setCloseListener(() -> closeMessage(activity));
    }

    /**
     * Creates sequence of end stage of user tutorial. If this stage was finished, next call of
     * this method will return empty sequence.
     * @param activity
     * @return sequence of tutorial
     */
    public static ShowCaseSequence finishStage(Activity activity) {
        if (tutorialStage > END || tutorialStage < TARGET_INFO || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = END;
        BubbleShowCaseBuilder end_message = new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_finish_title))
                .description(activity.getString(R.string.tutorial_finish_message));
        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(end_message){
                    @Override
                    public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onBubbleClick(bubbleShowCase);
                        Preferences.setRunTutorial(activity, false);
                    }
                });
    }

    /**
     * Reset tutorial and show first stage.
     * @param activity
     */
    public static void resetTutorial(Activity activity) {
        Preferences.setRunTutorial(activity, true);
        tutorialStage = WELCOME;
        welcomeStage(activity).start();
    }

    private static void closeMessage(Activity activity) {
        new BubbleShowCaseBuilder(activity)
                .title(activity.getString(R.string.tutorial_skip_title))
                .description(activity.getString(R.string.tutorial_skip_message))
                .listener(new ShowCaseListenerWrapper())
                .show();
        Preferences.setRunTutorial(activity, false);
    }

    private static View getNavigationButton(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return toolbar.getChildAt(i);
        return null;
    }

}
