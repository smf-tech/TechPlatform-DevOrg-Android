package com.octopusbjsindia.presenter;

import android.util.Log;

import com.octopusbjsindia.listeners.SubmitAttendanceListener;
import com.octopusbjsindia.models.attendance.AttendaceData;
import com.octopusbjsindia.request.SubmitAttendanceCall;
import com.octopusbjsindia.view.fragments.PlannerFragment;

import java.lang.ref.WeakReference;

public class SubmitAttendanceFragmentPresenter implements SubmitAttendanceListener {

    private WeakReference<PlannerFragment> fragmentWeakReference;


    public SubmitAttendanceFragmentPresenter(PlannerFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }


    @Override
    public void onSuccess(int id,String response, AttendaceData attendaceData) {
        Log.i("Response","111"+response);
        switch(id){
            case 1:
                fragmentWeakReference.get().checkInResponse(response, attendaceData);
                break;
            case 2:
                fragmentWeakReference.get().checkOutResponse(response, attendaceData);
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
    public void markAttendace(AttendaceData attendaceData)
    {
        SubmitAttendanceCall attendanceCall=new SubmitAttendanceCall();
        attendanceCall.addListener(this);
        attendanceCall.AttendanceCheckIn(attendaceData);

    }

    public void markOutAttendance(String att_id,String type,Long date,String lat,String log,String totalHrs)
    {
        SubmitAttendanceCall markOutAttendancCall=new SubmitAttendanceCall();
        markOutAttendancCall.addListener(this);
        markOutAttendancCall.AttendanceCheckOut(att_id,type,date,lat,log,totalHrs);

    }


}
