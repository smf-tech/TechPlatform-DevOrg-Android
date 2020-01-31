package com.octopusbjsindia.presenter;


import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.attendance.MonthlyAttendance;
import com.octopusbjsindia.models.attendance.TeamAttendanceResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.LoginActivity;
import com.octopusbjsindia.view.fragments.AttendanceSummeryFragment;

import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.remoteconfig.FirebaseRemoteConfig.TAG;

public class AttendanceSummeryFragmentPresenter implements APIPresenterListener {
    AttendanceSummeryFragment mContext;

    public AttendanceSummeryFragmentPresenter(AttendanceSummeryFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContext.hideProgressBar();
        mContext.onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContext.hideProgressBar();
        mContext.onErrorListener(requestID, error);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        try {
            TeamAttendanceResponse data = new Gson().fromJson(response, TeamAttendanceResponse.class);
            if (data.getStatus() == 1000) {
                Util.saveLoginObjectInPref("");
                try {
                    Intent startMain = new Intent(mContext.getActivity(), LoginActivity.class);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.getActivity().startActivity(startMain);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            } else if (data.getStatus() == 200) {
                if (data.getData() != null && data.getData().size() > 0) {
                    mContext.onSuccessUserAtendance(data);
                }
            } else {
                onFailureListener(requestID, data.getMessage());
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }
//        activity.success(masterDataResponse);
    }

    public void getAttendanceSummery(String userId, int year, int month) {
        mContext.showProgressBar();
        Gson gson = new GsonBuilder().create();

        Map<String, String> map = new HashMap<>();
        map.put("year", Integer.toString(year));
        map.put("month", Integer.toString(month));
        map.put("userId", userId);

        String paramjson = gson.toJson(map);

        final String checkProfileUrl = BuildConfig.BASE_URL
                + Urls.Attendance.GET_TEAM_USER_ATTENDANCE;//, mobilenumber,meetId);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
//        drawable = new BitmapDrawable(activity.getResources(), imageHashmap.get(key));
        requestCall.postDataApiCall("AttendanceSummery", paramjson, checkProfileUrl);
    }

}
