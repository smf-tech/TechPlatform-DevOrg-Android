package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.listeners.TMFilterListRequestCallListener;
import com.octopusbjsindia.models.Operator.OperatorMachineData;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.request.OperatorMeterReadingRequestCall;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.OperatorActivity;
import com.octopusbjsindia.view.activities.OperatorMeterReadingActivity;

import java.lang.ref.WeakReference;

public class OperatorActivityPresenter implements TMFilterListRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<OperatorActivity> fragmentWeakReference;

    public OperatorActivityPresenter(OperatorActivity fragmentWeakReference) {
        this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);;
    }

    public void getAllFiltersRequests(String machineCode) {
        fragmentWeakReference.get().showProgressBar();
        OperatorMeterReadingRequestCall requestCall = new OperatorMeterReadingRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests(machineCode);
    }

    @Override
    public void onFilterListRequestsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();
        OperatorMachineData pendingRequestsResponse = new Gson().fromJson(response, OperatorMachineData.class);
        if(pendingRequestsResponse.getStatus() == 200){
            if (!TextUtils.isEmpty(response)) {
                if (pendingRequestsResponse != null && pendingRequestsResponse.getOperatorMachineCodeDataModel() != null) {
                    fragmentWeakReference.get().showPendingApprovalRequests(pendingRequestsResponse.getOperatorMachineCodeDataModel());
                }
            }
        } else  if(pendingRequestsResponse.getStatus() == 400){
            fragmentWeakReference.get().removeMachineid();
        } else if (pendingRequestsResponse.getStatus() == 1000) {
            Util.logOutUser(fragmentWeakReference.get());
            return;
        }
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

    }

    @Override
    public void onRequestStatusChanged(String response, int position) {

    }

    @Override
    public void onFailureListener(String message) {
        fragmentWeakReference.get().hideProgressBar();
        Log.e(TAG, "onErrorListener: "+ message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        fragmentWeakReference.get().hideProgressBar();
        Log.e(TAG, "onErrorListener: "+ error);
    }
}
