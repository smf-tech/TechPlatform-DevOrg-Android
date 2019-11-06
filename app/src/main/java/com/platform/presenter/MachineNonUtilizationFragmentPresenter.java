package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.MachineDetailData;
import com.platform.models.events.CommonResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.Urls;
import com.platform.view.fragments.MachineNonUtilizationFragment;
import com.platform.view.fragments.SiltTransportationRecordFragment;

import java.lang.ref.WeakReference;

public class MachineNonUtilizationFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineNonUtilizationFragment> fragmentWeakReference;
    public static final String NON_UTILIZATION = "non_utilization";
    private final String TAG = MachineNonUtilizationFragmentPresenter.class.getName();
    public MachineNonUtilizationFragmentPresenter(MachineNonUtilizationFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void submitNonUtilization(String machineId, String selectedReason, String otherReason){
        fragmentWeakReference.get().showProgressBar();
        final String submitNonUtilizationUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.NON_UTILIZATION_URL, machineId, selectedReason, otherReason);
        Log.d(TAG, "submitNonUtilizationUrl: url" + submitNonUtilizationUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(NON_UTILIZATION, submitNonUtilizationUrl);
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
                if (requestID.equalsIgnoreCase(MachineNonUtilizationFragmentPresenter.NON_UTILIZATION)) {
                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                            MachineNonUtilizationFragmentPresenter.NON_UTILIZATION, responseOBJ.getStatus());
                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
