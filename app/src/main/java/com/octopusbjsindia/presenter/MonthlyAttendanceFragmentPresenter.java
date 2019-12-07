package com.octopusbjsindia.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.octopusbjsindia.listeners.MonthlyAttendaceListener;
import com.octopusbjsindia.models.attendance.MonthlyAttendance;
import com.octopusbjsindia.models.attendance.TeamAttendanceResponse;
import com.octopusbjsindia.request.MonthlyAttendaceCall;
import com.octopusbjsindia.view.fragments.AttendancePlannerFragment;

import java.lang.ref.WeakReference;
import java.util.Date;

public class MonthlyAttendanceFragmentPresenter implements MonthlyAttendaceListener {

    private WeakReference<AttendancePlannerFragment> fragmentWeakReference;
    public MonthlyAttendanceFragmentPresenter(AttendancePlannerFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    String TAG=MonthlyAttendanceFragmentPresenter.class.getSimpleName();


    // make a request call
    public void getMonthlyAttendance(int year,int month)
    {
        fragmentWeakReference.get().showProgressBar();
        MonthlyAttendaceCall attendaceCall=new MonthlyAttendaceCall();
        attendaceCall.addListener(this);
        attendaceCall.getAllMonthDates(year,month);
    }
    //get TeamAttendance
    public void getTeamAttendance(Date selectedDate) {
        fragmentWeakReference.get().showProgressBar();
        MonthlyAttendaceCall attendaceCall=new MonthlyAttendaceCall();
        attendaceCall.addListener(this);
        attendaceCall.getTeamAttendance(selectedDate);
    }

    @Override
    public void onTeamAttendance(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if(response!=null){
            try {
                TeamAttendanceResponse res=new Gson().fromJson(response, TeamAttendanceResponse.class);
                if (res.getData()!=null && res.getData().size()>0){
                    fragmentWeakReference.get().setTeamAttendanceData(res.getData());
                } else {
                    onError(res.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(String response) {
        // parse a response and pass to activity
        fragmentWeakReference.get().hideProgressBar();
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
        fragmentWeakReference.get().hideProgressBar();
        fragmentWeakReference.get().showError(error);
    }

    @Override
    public void OnErrorResponse(String errorRes) {
        fragmentWeakReference.get().hideProgressBar();
        Log.i(TAG,errorRes);
    }

}
