package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.SubmitAttendanceListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubmitAttendanceCall  {



    public String KEY_TYPE="type";
    public String KEY_DATE="dates";
    public String KEY_CHEKINTIME="checkintime";
    public String KEY_CHEKOUTTIME="checkOutTime";
    public String KEY_LAT="lattitude";
    public String KEY_LONG="longitude";
    public String KEY_ADDRESS="address";
    private int MARK_IN=1;
    private int MARK_OUT=2;
    public String KEY_ATTID="attendanceId";



    private String TAG=SubmitAttendanceCall.class.getSimpleName();
    private SubmitAttendanceListener submitAttendanceListener;

    public void addListener(SubmitAttendanceListener submitAttendanceListener)
    {
        this.submitAttendanceListener=submitAttendanceListener;
    }

    public void AttendanceCheckIn(String type, Long d, String time, String chktype, String strLat, String strLong, String strAdd){
        Response.Listener<JSONObject> monthlyAttendancSuceessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "MonthlyAttendance - Resp: " + res);
                    submitAttendanceListener.onSuccess(MARK_IN,res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener monthlyAttendaceErrorLisetner=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "MonthlyAttendance - Error: " +error);
                submitAttendanceListener.onError(error.toString());
            }
        };



        Gson gson = new GsonBuilder().serializeNulls().create();

        final String USER_CHECKIN = BuildConfig.BASE_URL+ Urls.Attendance.SUBMIT_ATTENDANCE;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                USER_CHECKIN,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                monthlyAttendancSuceessListener,
                monthlyAttendaceErrorLisetner
        );

        // if we send token with ur

        try{
            gsonRequest.setHeaderParams(Util.requestHeader(true));
            gsonRequest.setShouldCache(false);
            gsonRequest.setBodyParams(getSubmitAttendaceJson(type,d
                    ,time,chktype,strLat,strLong,strAdd));
            Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
        }catch (Exception e){
            Log.i("NetworkException","111"+e.toString());
        }


    }

    public JsonObject getSubmitAttendaceJson(String type, Long d, String time, String chktype, String strLat, String strLong, String strAdd){
        String validType=type.replace(" ","");
        HashMap<String,String>map=new HashMap<>();
        map.put(KEY_TYPE,validType);
        map.put(KEY_LAT,strLat);
        map.put(KEY_LONG,strLong);
        map.put(KEY_DATE,String.valueOf(d));
        //map.put(KEY_CHEKINTIME,time);
        //map.put(KEY_CHEKOUTTIME,chktype);
        //map.put(KEY_LAT,strLat);
        //map.put(KEY_LONG,strLong);
        //map.put(KEY_ADDRESS,strAdd);

        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }

        return requestObject;

    }

    public void AttendanceCheckOut(String  att_id, String type, Long date, String lat, String log){
        Response.Listener<JSONObject> monthlyAttendancSuceessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "MonthlyAttendance - Resp: " + res);
                    submitAttendanceListener.onSuccess(MARK_OUT,res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener monthlyAttendaceErrorLisetner=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "MonthlyAttendance - Error: " +error);
                submitAttendanceListener.onError(error.toString());
            }
        };



        Gson gson = new GsonBuilder().serializeNulls().create();

        final String USER_CHECKOUT = BuildConfig.BASE_URL+ Urls.Attendance.SUBMIT_ATTENDANCE;

       GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
               USER_CHECKOUT,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                monthlyAttendancSuceessListener,
                monthlyAttendaceErrorLisetner
        );

        // if we send token with ur

        try{
            gsonRequest.setHeaderParams(Util.requestHeader(true));
            gsonRequest.setShouldCache(false);
            gsonRequest.setBodyParams(getJsonForMarkOut(att_id,type,date,lat,log));
            Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
        }catch (Exception e){
            Log.i("NetworkException","111"+e.toString());
        }



    }
    public JsonObject getJsonForMarkOut(String att_id, String type, Long date, String lat, String log){

        String validType=type.replace(" ","");
        HashMap<String,String>map=new HashMap<>();
        map.put(KEY_ATTID,att_id);
        map.put(KEY_TYPE,validType);
        map.put(KEY_LAT,lat);
        map.put(KEY_LONG,log);
        map.put(KEY_DATE,String.valueOf(date));


        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }
        return requestObject;
    }

}
