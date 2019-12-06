package com.octopus.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopus.BuildConfig;
import com.octopus.Platform;
import com.octopus.listeners.MonthlyAttendaceListener;
import com.octopus.utility.GsonRequestFactory;
import com.octopus.utility.Urls;
import com.octopus.utility.Util;

import org.json.JSONObject;

import java.util.Date;

public class MonthlyAttendaceCall {

    private String TAG = MonthlyAttendaceCall.class.getSimpleName();

    private MonthlyAttendaceListener attendaceListener;

    public void addListener(MonthlyAttendaceListener attendaceListener) {
        this.attendaceListener = attendaceListener;
    }

    public void getAllMonthDates(int year, int month) {

        Response.Listener<JSONObject> monthlyAttendancSuceessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "MonthlyAttendance - Resp: " + res);
                    attendaceListener.onSuccess(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener monthlyAttendaceErrorLisetner = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "MonthlyAttendance - Error: " + error);
                attendaceListener.onError(error.toString());
            }
        };


        Gson gson = new GsonBuilder().serializeNulls().create();

        final String GET_USER_MONTHLY_ATTENDANCE = BuildConfig.BASE_URL + Urls.Attendance.GET_ALL_MONTH + year + "/" + month;
        Log.d(TAG, "MonthlyAttendance - Url: " + GET_USER_MONTHLY_ATTENDANCE);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                GET_USER_MONTHLY_ATTENDANCE,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                monthlyAttendancSuceessListener,
                monthlyAttendaceErrorLisetner
        );

        // if we send token with url

        try {
            gsonRequest.setHeaderParams(Util.requestHeader(true));
            gsonRequest.setShouldCache(false);
            Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
        } catch (Exception e) {
            Log.i("NetworkException", "111" + e.toString());
        }

    }

    public void getTeamAttendance(Date selectedDate) {
        Response.Listener<JSONObject> monthlyAttendancSuceessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "GET_TEAM_ATTENDANCE - Resp: " + res);
                    attendaceListener.onTeamAttendance(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener monthlyAttendaceErrorLisetner = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "GET_TEAM_ATTENDANCE - Error: " + error);
                attendaceListener.onError(error.toString());
            }
        };
        Gson gson = new GsonBuilder().serializeNulls().create();
        final String URL = BuildConfig.BASE_URL + Urls.Attendance.GET_TEAM_ATTENDANCE + "/" + selectedDate.getTime();
        Log.d(TAG, "GET_TEAM_ATTENDANCE - URL: " + URL);


        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                URL,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                monthlyAttendancSuceessListener,
                monthlyAttendaceErrorLisetner
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);

    }
}
