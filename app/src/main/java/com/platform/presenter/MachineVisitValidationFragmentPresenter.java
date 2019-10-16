package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.request.APIRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Urls;
import com.platform.view.fragments.MachineVisitValidationFragment;

import java.lang.ref.WeakReference;

public class MachineVisitValidationFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineVisitValidationFragment> fragmentWeakReference;
    private final String TAG = MachineVisitValidationFragmentPresenter.class.getName();
    public static final String GET_WORKING_HOURS_RECORD ="getWorkingHoursRecord";
    public static final String SUBMIT_MACHINE_VISIT_VALIDATION_FORM ="submitMachineShiftingForm";

    public MachineVisitValidationFragmentPresenter(MachineVisitValidationFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getWorkingHourDetails(long selectedDate, String machineId){
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        final String getWorkingHoursRecordUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_MACHINE_WORKING_HOURS_RECORD, selectedDate, machineId);
        Log.d(TAG, "getWorkingHoursRecordUrl: url " + getWorkingHoursRecordUrl);
        fragmentWeakReference.get().showProgressBar();
            requestCall.getDataApiCall(GET_WORKING_HOURS_RECORD, getWorkingHoursRecordUrl);
    }

    public void submitWorkingHours(){
//        APIRequestCall requestCall = new APIRequestCall();
//        requestCall.setApiPresenterListener(this);
//        fragmentWeakReference.get().showProgressBar();
//        final String getWorkingHoursRecordUrl = BuildConfig.BASE_URL
//                + String.format(Urls.SSModule.UPDATE_STRUCTURE_MACHINE_STATUS, selectedDate);
//        Log.d(TAG, "getWorkingHoursRecordUrl: url " + getWorkingHoursRecordUrl);
//        fragmentWeakReference.get().showProgressBar();
//        requestCall.getDataApiCall(GET_WORKING_HOURS_RECORD, getWorkingHoursRecordUrl);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID,error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(MachineVisitValidationFragmentPresenter.SUBMIT_MACHINE_VISIT_VALIDATION_FORM)) {
//                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
//                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
//                            MachineVisitValidationFragmentPresenter.SUBMIT_MACHINE_VISIT_VALIDATION_FORM, responseOBJ.getStatus());
                } else if(requestID.equalsIgnoreCase(MachineVisitValidationFragmentPresenter.GET_WORKING_HOURS_RECORD)) {
                    fragmentWeakReference.get().setData();
                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
