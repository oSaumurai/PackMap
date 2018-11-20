package edu.osu.cse5236.group10.packmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Based on code from Anurag Dhunna on Medium
 * https://medium.com/viithiisys/android-manage-user-session-using-shared-preferences-1187cb9c5cd8
 */
public class UserSharedPreference {

    public static final String LOGGED_IN_PREF = "logged_in_status";
    public static final String USER_ID = "user_id";

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean loggedIn, String userId) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static String getUserId(Context context) {
        return getPreferences(context).getString(USER_ID, null);
    }
}
