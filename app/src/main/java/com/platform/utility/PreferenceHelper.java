package com.platform.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings({"unused", "CanBeFinal"})
public class PreferenceHelper {

    private static final String PREFER_NAME = "Platform";
    public static final String TOKEN = "Token";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PreferenceHelper(Context context) {
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void insertString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }
}
