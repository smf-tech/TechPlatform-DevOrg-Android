package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.events.CommonResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Urls;
import com.platform.view.fragments.MachineMouFirstFragment;

import java.lang.ref.WeakReference;

public class MachineMouFragmentPresenter  implements APIPresenterListener {
    private WeakReference<MachineMouFirstFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();
    public static final String UPDATE_MACHINE_STATUS = "updateMachineStatus";
    public static final String UPDATE_STRUCTURE_STATUS = "updateStructureStatus";

    public MachineMouFragmentPresenter(MachineMouFirstFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void updateMachineStructureStatus(String machineId, String machineCode, int statusCode, String type) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);

        final String updateStructureMachineStatusUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.UPDATE_STRUCTURE_MACHINE_STATUS, machineId, machineCode, statusCode, type);
        Log.d(TAG, "updateStatus: url " + updateStructureMachineStatusUrl);
        fragmentWeakReference.get().showProgressBar();
        if (type.equals(Constants.SSModule.MACHINE_TYPE)) {
            requestCall.getDataApiCall(UPDATE_MACHINE_STATUS, updateStructureMachineStatusUrl);
        } else if (type.equals(Constants.SSModule.STRUCTURE_TYPE)) {
            requestCall.getDataApiCall(UPDATE_STRUCTURE_STATUS, updateStructureMachineStatusUrl);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID, error);
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
                CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                        MachineDetailsFragmentPresenter.UPDATE_MACHINE_STATUS, responseOBJ.getStatus());
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
