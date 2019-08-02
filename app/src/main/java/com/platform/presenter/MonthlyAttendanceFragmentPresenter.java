package com.platform.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.platform.listeners.MonthlyAttendaceListener;
import com.platform.models.attendance.MonthlyAttendance;
import com.platform.request.MonthlyAttendaceCall;
import com.platform.view.fragments.AttendancePlannerFragment;

import java.lang.ref.WeakReference;

public class MonthlyAttendanceFragmentPresenter implements MonthlyAttendaceListener {

    private WeakReference<AttendancePlannerFragment> fragmentWeakReference;
    public MonthlyAttendanceFragmentPresenter(AttendancePlannerFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    String TAG=MonthlyAttendanceFragmentPresenter.class.getSimpleName();

    @Override
    public void onSuccess(String response) {
        // parse a response and pass to activity
        Log.i(TAG,"AttendanceData"+response);

        String ValidString=response.replace("$","");
        try {
            MonthlyAttendance monthlyAttendance=new Gson().fromJson(ValidString, MonthlyAttendance.class);
            fragmentWeakReference.get().getAttendanceInfo(monthlyAttendance);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onError(String error) {
        Log.i(TAG,"AttendanceDataError"+error);
        fragmentWeakReference.get().showError(error);
    }

    @Override
    public void OnErrorResponse(String errorRes) {
        Log.i(TAG,errorRes);
    }

    // make a request call
    public void getMonthlyAttendance(int year,int month)
    {
        MonthlyAttendaceCall attendaceCall=new MonthlyAttendaceCall();
        attendaceCall.addListener(this);
        attendaceCall.getAllMonthDates(year,month);
    }
}
