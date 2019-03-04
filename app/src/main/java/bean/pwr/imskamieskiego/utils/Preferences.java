/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFERENCES_NAME = "user_settings";

    private static final String SEARCH_MODE_PREFERENCE = "path_search_mode";
    private static final String RUN_TUTORIAL = "run_tutorial";

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setSearchMode(Context context, int searchMode){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(SEARCH_MODE_PREFERENCE, searchMode);
        editor.apply();
    }

    public static int getSearchMode(Context context){
        return getPreferences(context).getInt(SEARCH_MODE_PREFERENCE, 1);
    }

    public static void setRunTutorial(Context context, Boolean runTutorial){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(RUN_TUTORIAL, runTutorial);
        editor.apply();
    }

    public static boolean isRunTutorial(Context context){
        return getPreferences(context).getBoolean(RUN_TUTORIAL, true);
    }
}
