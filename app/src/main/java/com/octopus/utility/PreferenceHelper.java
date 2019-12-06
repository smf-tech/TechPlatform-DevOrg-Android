package com.octopus.utility;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings({"unused", "CanBeFinal"})
public class PreferenceHelper {

    public static final String PREFER_NAME = "Platform";
    public static final String TOKEN = "Token";
    public static final String IS_PENDING = "IS_PENDING";
    public static final String NON_PENDING = "NON_PENDING";

    private SharedPreferences pref;

    public PreferenceHelper(Context context) {
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
    }

    public void insertString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void totalHours(String key,Boolean value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key,value);
        editor.apply();

    }
    public boolean showTotalHours(String key)
    {
        return pref.getBoolean(key,false);
    }


    public String getString(String key) {
        return pref.getString(key, "");
    }


    public void saveCheckInTime(String key,CharSequence value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,(String) value);
        editor.apply();
    }
    public String getCheckInTime(String key){

       return pref.getString(key," ");
    }


    public void saveCheckInButtonText(String keyCheckintext, String checkInText) {
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(keyCheckintext,checkInText);
        editor.apply();
    }

    public String getCheckInButtonText(String textKey){
        return pref.getString(textKey,"CheckIn");

    }

    public void saveCheckOutText(String keyCheckouttext, String checkOutText) {
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(keyCheckouttext,checkOutText);
        editor.apply();
    }

    public String getCheckOutText(String key){
        return pref.getString(key,"CheckOut");
    }

    public void insertTotalHoursAfterCheckOut(String key,String value){
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(key,value);
    }

    public String getTotalHoursAfterCheckOut(String key){
        return pref.getString(key,"");
    }


    public void isCheckOut(String keyIscheckout, boolean b) {
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean(keyIscheckout,b);
        editor.apply();
    }
    public boolean getCheckOutStatus(String key){
        return  pref.getBoolean(key,false);
    }
}
