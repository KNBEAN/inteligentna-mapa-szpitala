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

import bean.pwr.imskamieskiego.R;

public class ShowcaseController {

    public static void welcomeStage(Activity activity) {
        BubbleShowCaseBuilder helloMessage = new BubbleShowCaseBuilder(activity)
                .title("Witaj!")
                .description("Witamy w interaktywnej mapie szpitala. W tym samouczku chcielibyśmy przybliżyć Ci działanie aplikacji.\n" +
                        "Aby pominąć samouczek naciśnij X w górnym prawym rogu.\nAby kontynuować, naciśnij tą wiadomość.")
                .imageResourceId(R.drawable.splash_icon)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });

        BubbleShowCaseBuilder userLocationMessage = new BubbleShowCaseBuilder(activity)
                .title("Twoja lokalizacja")
                .description("Na początek warto zaznaczyć gdzie aktualnie się znajdujesz. Aby to zrobić, nacisnąć wskazany przycisk")
                .imageResourceId(R.drawable.ic_my_location_white_24dp)
                .targetView(activity.findViewById(R.id.my_position_button))
                .backgroundColorResourceId(R.color.colorPrimaryDark)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {
                    }

                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        activity.findViewById(R.id.my_position_button).callOnClick();
                    }

                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });


        new BubbleShowCaseSequence()
                .addShowCase(helloMessage)
                .addShowCase(userLocationMessage)
                .show();
    }

    public static void userLocationStage(Activity activity) {
        BubbleShowCaseBuilder mapTouch = new BubbleShowCaseBuilder(activity)
                .title("Twoja lokalizacja")
                .description("Swoją lokalizację można zaznaczyć na kilka sposobów. Jednym z nich jest naciśnięcie i przytrzymanie miejsca na mapie")
                .imageResourceId(R.drawable.ic_touch_app_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimary)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });

        BubbleShowCaseBuilder floorChange = new BubbleShowCaseBuilder(activity)
                .title("Wyświetlane piętro")
                .description("Klikając tutaj, możesz zmienić aktualnie wyświetlane piętro")
                .imageResourceId(R.drawable.ic_layers_black_24dp)
                .targetView(activity.findViewById(R.id.floors_button))
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
                .textColorResourceId(R.color.fontColorBlack)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });
        BubbleShowCaseBuilder search = new BubbleShowCaseBuilder(activity)
                .title("Wyszukiwanie")
                .description("Możesz również wyznaczyć swoją pozycję wyszukując miejsce po nazwie")
                .imageResourceId(R.drawable.ic_search_black_24dp)
                .backgroundColorResourceId(R.color.colorAccent)
                .targetView(activity.findViewById(R.id.searchButton))
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });

        BubbleShowCaseBuilder qrCodeScanner = new BubbleShowCaseBuilder(activity)
                .title("Skaner kodów")
                .description("Możesz także zeskanować kod z najbliższego plakatu.")
                .imageResourceId(R.drawable.ic_photo_camera_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimaryDark)
                .targetView(activity.findViewById(R.id.cameraButton))
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });

        BubbleShowCaseBuilder selectOwnPosition = new BubbleShowCaseBuilder(activity)
                .title("Twoja lokalizacja")
                .description("Teraz Twoja kolej! Zaznacz swoją pozycję.")
                .imageResourceId(R.drawable.start_point)
                .backgroundColorResourceId(R.color.colorPrimary)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });


        new BubbleShowCaseSequence()
                .addShowCase(mapTouch)
                .addShowCase(floorChange)
                .addShowCase(search)
                .addShowCase(qrCodeScanner)
                .addShowCase(selectOwnPosition)
                .show();
    }

    public static void targetSelectStage(Activity activity) {
        BubbleShowCaseBuilder selectTarget = new BubbleShowCaseBuilder(activity)
                .title("Cel trasy")
                .description("Teraz musimy wyznaczyć cel naszej trasy. Podobnie jak wcześniej, cel możemy zaznaczyć na mapie lub wyszukać po nazwie.")
                .imageResourceId(R.drawable.destination_point)
                .backgroundColorResourceId(R.color.colorAccent)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });

        BubbleShowCaseBuilder fastTravelButtons = new BubbleShowCaseBuilder(activity)
                .title("Szybki dostęp")
                .description("Klikając tutaj, możesz zaznaczyć \"szybkie lokacje\", takie jak najbliższe toalety, stanowisko asystenta pacjęta czy bufet.")
                .imageResourceId(R.drawable.ic_widgets_black_24dp)
                .targetView(activity.findViewById(R.id.quickAccessFragment))
                .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .textColorResourceId(R.color.fontColorBlack)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });


        new BubbleShowCaseSequence()
                .addShowCase(selectTarget)
                .addShowCase(fastTravelButtons)
                .show();
    }

    public static void infoSheetStage(Activity activity) {
        BubbleShowCaseBuilder infoSheet = new BubbleShowCaseBuilder(activity)
                .title("Szczegóły lokacji")
                .description("Możesz zobaczyć szczegóły dotyczące wybranej lokacji, klikając na ten przycisk.")
                .imageResourceId(R.drawable.ic_pin_drop_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimaryDark)
                .targetView(activity.findViewById(R.id.info_button))
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }

                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        activity.findViewById(R.id.info_button).callOnClick();
                    }
                });

        BubbleShowCaseBuilder pathOptions = new BubbleShowCaseBuilder(activity)
                .title("Ustawienia")
                .description("W bocznym menu możesz znaleźć przydatne opcje takie jak unikanie schodów podcza nawigacji.")
                .imageResourceId(R.drawable.ic_path_setting_black_24dp)
                .backgroundColorResourceId(R.color.colorPrimaryLight)
                .textColorResourceId(R.color.fontColorBlack)
                .targetView(getNavigationButton(((Toolbar) activity.findViewById(R.id.toolbar))))
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                });

        BubbleShowCaseBuilder startPathSearch = new BubbleShowCaseBuilder(activity)
                .title("Trasa")
                .description("Aby wyznaczyć trasę, wystarczy kliknąć na wskazany przycisk")
                .imageResourceId(R.drawable.ic_run_path_search_white_24dp)
                .targetView(activity.findViewById(R.id.guide_to_button))
                .backgroundColorResourceId(R.color.colorPrimaryDark)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }

                    @Override
                    public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {
                        activity.findViewById(R.id.guide_to_button).callOnClick();
                    }
                });


        new BubbleShowCaseSequence()
                .addShowCase(infoSheet)
                .addShowCase(pathOptions)
                .addShowCase(startPathSearch)
                .show();
    }

    public static void finishStage(Activity activity) {
        new BubbleShowCaseBuilder(activity)
                .title("Gratulacje!")
                .description("Trasa została wyznaczona! To już koniec naszego samouczka. Samouczek możesz uruchomić ponownie w menu bocznym.\nWszystkiego dobrego i powodzenia!")
                .imageResourceId(R.drawable.ic_face_smile_white_24dp)
                .backgroundColorResourceId(R.color.colorAccent)
                .listener(new ShowCaseListenerWrapper() {
                    @Override
                    public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {
                        super.onCloseActionImageClick(bubbleShowCase);
                        closeMessage(activity);
                    }
                })
                .show();
    }

    private static void closeMessage(Activity activity){
        new BubbleShowCaseBuilder(activity)
                .title("Samouczek")
                .description("Możesz ponownie uruchomić samouczek w menu bocznym.")
                .imageResourceId(R.drawable.ic_help_outline_white_24dp)
                .backgroundColorResourceId(R.color.colorPrimary)
                .listener(new ShowCaseListenerWrapper())
                .show();
    }


    private static View getNavigationButton(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return toolbar.getChildAt(i);
        return null;
    }

}
