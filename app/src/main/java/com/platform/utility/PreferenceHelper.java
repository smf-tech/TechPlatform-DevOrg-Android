package com.platform.utility;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings({"unused", "CanBeFinal"})
public class PreferenceHelper {

    private static final String PREFER_NAME = "Platform";
    public static final String TOKEN = "Token";

    private SharedPreferences pref;

    public PreferenceHelper(Context context) {
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
    }

    public void insertString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key) {
        return pref.getString(key, "");
    }
}
