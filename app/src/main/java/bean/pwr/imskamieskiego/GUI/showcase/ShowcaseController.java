/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.GUI.showcase;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;

import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bean.pwr.imskamieskiego.R;
import bean.pwr.imskamieskiego.utils.Preferences;

public class ShowcaseController {
    private static final int WELCOME = 0;
    private static final int USER_LOCATION = 1;
    private static final int TARGET_SELECTION = 2;
    private static final int TARGET_INFO = 3;
    private static final int END = 4;

    private static int tutorialStage = 0;

    public static ShowCaseSequence welcomeStage(Activity activity) {
        if (tutorialStage > WELCOME || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = WELCOME;
        BubbleShowCaseBuilder helloMessage = new BubbleShowCaseBuilder(activity)
                .title("Witaj!")
                .description("Witamy w interaktywnej mapie szpitala. W tym samouczku chcielibyśmy przybliżyć Ci działanie aplikacji.\n" +
                        "Aby pominąć samouczek naciśnij X w górnym prawym rogu.\nAby kontynuować, naciśnij tą wiadomość.")
                .imageResourceId(R.drawable.splash_icon);

        BubbleShowCaseBuilder userLocationMessage = new BubbleShowCaseBuilder(activity)
                .title("Twoja lokalizacja")
                .description("Na początek warto zaznaczyć gdzie aktualnie się znajdujesz. Aby to zrobić, nacisnąć wskazany przycisk")
                .imageResourceId(R.drawable.ic_my_location_white_24dp)
                .targetView(activity.findViewById(R.id.my_position_button))
                .backgroundColorResourceId(R.color.colorPrimaryDark);

        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(helloMessage))
                .addItem(new ShowCaseSequenceItem(userLocationMessage) {
                    @Override
                    public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {
                    }

                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onTargetClick(bubbleShowCase);
                        super.dismiss();
                        activity.findViewById(R.id.my_position_button).callOnClick();
                    }
                })
                .setCloseListener(() -> closeMessage(activity));
    }

    public static ShowCaseSequence userLocationStage(Activity activity) {
        if (tutorialStage > USER_LOCATION || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = USER_LOCATION;
        BubbleShowCaseBuilder mapTouch = new BubbleShowCaseBuilder(activity)
                .title("Twoja lokalizacja")
                .description("Swoją lokalizację można zaznaczyć na kilka sposobów. Jednym z nich jest naciśnięcie i przytrzymanie miejsca na mapie")
                .imageResourceId(R.drawable.ic_touch_app_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimary);

        BubbleShowCaseBuilder floorChange = new BubbleShowCaseBuilder(activity)
                .title("Wyświetlane piętro")
                .description("Klikając tutaj, możesz zmienić aktualnie wyświetlane piętro")
                .imageResourceId(R.drawable.ic_layers_black_24dp)
                .targetView(activity.findViewById(R.id.floors_button))
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
                .textColorResourceId(R.color.fontColorBlack);

        BubbleShowCaseBuilder search = new BubbleShowCaseBuilder(activity)
                .title("Wyszukiwanie")
                .description("Możesz również wyznaczyć swoją pozycję wyszukując miejsce po nazwie")
                .imageResourceId(R.drawable.ic_search_black_24dp)
                .backgroundColorResourceId(R.color.colorAccent)
                .targetView(activity.findViewById(R.id.searchButton));

        BubbleShowCaseBuilder qrCodeScanner = new BubbleShowCaseBuilder(activity)
                .title("Skaner kodów")
                .description("Możesz także zeskanować kod z najbliższego plakatu.")
                .imageResourceId(R.drawable.ic_photo_camera_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimaryDark)
                .targetView(activity.findViewById(R.id.cameraButton));

        BubbleShowCaseBuilder selectOwnPosition = new BubbleShowCaseBuilder(activity)
                .title("Twoja lokalizacja")
                .description("Teraz Twoja kolej! Zaznacz swoją pozycję.")
                .imageResourceId(R.drawable.start_point)
                .backgroundColorResourceId(R.color.colorPrimary);


        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(mapTouch))
                .addItem(new ShowCaseSequenceItem(floorChange))
                .addItem(new ShowCaseSequenceItem(search))
                .addItem(new ShowCaseSequenceItem(qrCodeScanner))
                .addItem(new ShowCaseSequenceItem(selectOwnPosition))
                .setCloseListener(() -> closeMessage(activity));
    }

    public static ShowCaseSequence targetSelectStage(Activity activity) {
        if (tutorialStage > TARGET_SELECTION || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = TARGET_SELECTION;
        BubbleShowCaseBuilder selectTarget = new BubbleShowCaseBuilder(activity)
                .title("Cel trasy")
                .description("Teraz musimy wyznaczyć cel naszej trasy. Podobnie jak wcześniej, cel możemy zaznaczyć na mapie lub wyszukać po nazwie.")
                .imageResourceId(R.drawable.destination_point)
                .backgroundColorResourceId(R.color.colorAccent);

        BubbleShowCaseBuilder fastTravelButtons = new BubbleShowCaseBuilder(activity)
                .title("Szybki dostęp")
                .description("Klikając tutaj, możesz zaznaczyć \"szybkie lokacje\", takie jak najbliższe toalety, stanowisko asystenta pacjęta czy bufet.")
                .imageResourceId(R.drawable.ic_widgets_black_24dp)
                .targetView(activity.findViewById(R.id.quickAccessFragment))
                .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .textColorResourceId(R.color.fontColorBlack);

        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(selectTarget))
                .addItem(new ShowCaseSequenceItem(fastTravelButtons))
                .setCloseListener(() -> closeMessage(activity));
    }

    public static ShowCaseSequence infoSheetStage(Activity activity) {
        if (tutorialStage > TARGET_INFO || !Preferences.isRunTutorial(activity)) {
            return new ShowCaseSequence();
        }
        tutorialStage = TARGET_INFO;
        BubbleShowCaseBuilder infoSheet = new BubbleShowCaseBuilder(activity)
                .title("Szczegóły lokacji")
                .description("Możesz zobaczyć szczegóły dotyczące wybranej lokacji, klikając na ten przycisk.")
                .imageResourceId(R.drawable.ic_pin_drop_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimaryDark)
                .targetView(activity.findViewById(R.id.info_button));

        BubbleShowCaseBuilder pathOptions = new BubbleShowCaseBuilder(activity)
                .title("Ustawienia")
                .description("W bocznym menu możesz znaleźć przydatne opcje takie jak unikanie schodów podcza nawigacji.")
                .imageResourceId(R.drawable.ic_path_setting_black_24dp)
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .textColorResourceId(R.color.fontColorBlack)
                .targetView(getNavigationButton(((Toolbar) activity.findViewById(R.id.toolbar))));

        BubbleShowCaseBuilder startPathSearch = new BubbleShowCaseBuilder(activity)
                .title("Trasa")
                .description("Aby wyznaczyć trasę, wystarczy kliknąć na wskazany przycisk")
                .imageResourceId(R.drawable.ic_run_path_search_white_24dp)
                .targetView(activity.findViewById(R.id.guide_to_button))
                .backgroundColorResourceId(R.color.colorPrimaryDark);


        return new ShowCaseSequence()
                .addItem(new ShowCaseSequenceItem(infoSheet) {
                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onTargetClick(bubbleShowCase);
                        super.dismiss();
                        activity.findViewById(R.id.info_button).callOnClick();
                    }
                })
                .addItem(new ShowCaseSequenceItem(pathOptions))
                .addItem(new ShowCaseSequenceItem(startPathSearch) {
                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onTargetClick(bubbleShowCase);
                        super.dismiss();
                        activity.findViewById(R.id.guide_to_button).callOnClick();
                    }
                })
                .setCloseListener(() -> closeMessage(activity));
    }

    public static void finishStage(Activity activity) {
        if (tutorialStage > END || !Preferences.isRunTutorial(activity)) {
            return;
        }
        tutorialStage = END;
        new BubbleShowCaseBuilder(activity)
                .title("Gratulacje!")
                .description("Trasa została wyznaczona! To już koniec naszego samouczka. Samouczek możesz uruchomić ponownie w menu bocznym.\nWszystkiego dobrego i powodzenia!")
                .imageResourceId(R.drawable.ic_face_smile_white_24dp)
                .backgroundColorResourceId(R.color.colorAccent)
                .disableCloseAction(true)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onBubbleClick(bubbleShowCase);
                        Preferences.setRunTutorial(activity, false);
                    }
                })
                .show();
    }

    private static void closeMessage(Activity activity) {
        new BubbleShowCaseBuilder(activity)
                .title("Samouczek")
                .description("Możesz ponownie uruchomić samouczek w menu bocznym.")
                .imageResourceId(R.drawable.ic_help_outline_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimary)
                .disableCloseAction(true)
                .listener(new ShowCaseListenerWrapper())
                .show();
        Preferences.setRunTutorial(activity, false);
    }

    public static void resetTutorial(Activity activity) {
        Preferences.setRunTutorial(activity, true);
        tutorialStage = WELCOME;
        welcomeStage(activity);
    }

    private static View getNavigationButton(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return toolbar.getChildAt(i);
        return null;
    }

}
