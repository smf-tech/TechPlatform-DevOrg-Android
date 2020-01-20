package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.PlannerFragmetListener;
import com.octopusbjsindia.models.attendance.AttendaceData;
import com.octopusbjsindia.models.attendance.AttendanceResponse;
import com.octopusbjsindia.models.planner.PlannerSummerResopnse;
import com.octopusbjsindia.models.planner.SubmoduleData;
import com.octopusbjsindia.request.PlannerDashboardCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.PlannerFragment;

import java.util.ArrayList;

public class PlannerFragmentPresenter implements PlannerFragmetListener {

    PlannerFragment plannerFragment;

    public PlannerFragmentPresenter(PlannerFragment plannerFragment) {
        this.plannerFragment=plannerFragment;
    }

    public void getPlannerData() {
        PlannerDashboardCall requestCall = new PlannerDashboardCall();
        requestCall.setPlannerFragmentListener(this);
        plannerFragment.showProgressBar();
        requestCall.getPlannerData();
    }

    public void submitAttendance(AttendaceData data) {
        PlannerDashboardCall requestCall = new PlannerDashboardCall();
        requestCall.setPlannerFragmentListener(this);
        plannerFragment.showProgressBar();
        requestCall.submitAttendance(data);
    }

    @Override
    public void onPlannerDashboardDataFetched(String response) {
        plannerFragment.hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            PlannerSummerResopnse data = PlatformGson.getPlatformGsonInstance().fromJson(response,PlannerSummerResopnse.class);
            if(data.getStatus() == 1000){
                plannerFragment.logOutUser();
            }
            if (data != null && data.getData() != null
                    && !data.getData().isEmpty()
                    && data.getData().size() > 0) {
                plannerFragment.showPlannerSummary((ArrayList<SubmoduleData>) data.getData());
            }
        }
    }

    @Override
    public void onAttendanceSubmited(String response) {
        plannerFragment.hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            AttendanceResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, AttendanceResponse.class);
            if(data.getStatus() == 1000){
                plannerFragment.logOutUser();
            } else {
                DatabaseManager.getDBInstance(Platform.getInstance()).getAttendaceSchema()
                        .updateUserAttendace(data.getData().getAttendanceId(),
                                true,
                                data.getData().getData().getAttendaceDate(),
                                data.getData().getData().getAttendanceType());
                Util.showToast(data.getMessage(),plannerFragment);
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (plannerFragment != null) {
            plannerFragment.hideProgressBar();
            plannerFragment.showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (plannerFragment != null) {
            plannerFragment.hideProgressBar();
            if (error != null) {
                plannerFragment.showErrorMessage(error.getLocalizedMessage());
            }
        }
    }


}
