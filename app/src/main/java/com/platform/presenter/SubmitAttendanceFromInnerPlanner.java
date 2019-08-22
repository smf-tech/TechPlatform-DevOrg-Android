package com.platform.presenter;

import android.util.Log;

import com.platform.listeners.SubmitAttendanceListener;
import com.platform.request.SubmitAttendanceCall;
import com.platform.view.fragments.AttendancePlannerFragment;
import com.platform.view.fragments.PlannerFragment;

import java.lang.ref.WeakReference;

public class SubmitAttendanceFromInnerPlanner implements SubmitAttendanceListener {

    private WeakReference<AttendancePlannerFragment> fragmentWeakReference;


    public SubmitAttendanceFromInnerPlanner(AttendancePlannerFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }


    @Override
    public void onSuccess(int id,String response) {
        Log.i("Response","111"+response);
        switch(id){
            case 1:
                fragmentWeakReference.get().checkInResponse(response);
                break;
            case 2:
                fragmentWeakReference.get().checkOutResponse(response);
                break;
            default:
        }

    }

    @Override
    public void onError(int id,String error) {
        Log.i("Error","111");
        //fragmentWeakReference.get().showError(error);
        switch(id){
            case 1:
                fragmentWeakReference.get().checkInError(error);
                break;
            case 2:
                fragmentWeakReference.get().checkOutError(error);
                break;
            default:
        }
    }
    // make a request call
    public void markAttendace(String type, Long d, String time, String chktype , String strLat, String strLong, String strAdd)
    {
        SubmitAttendanceCall attendanceCall=new SubmitAttendanceCall();
        attendanceCall.addListener(this);
        attendanceCall.AttendanceCheckIn(type,d,strLat,strLong);

    }

    public void markOutAttendance(String att_id,String type,Long date,String lat,String log,String totalHrs)
    {
        SubmitAttendanceCall markOutAttendancCall=new SubmitAttendanceCall();
        markOutAttendancCall.addListener(this);
        markOutAttendancCall.AttendanceCheckOut(att_id,type,date,lat,log,totalHrs);

    }



}
