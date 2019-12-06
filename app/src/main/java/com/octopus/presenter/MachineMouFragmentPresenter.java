package com.octopus.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopus.BuildConfig;
import com.octopus.listeners.APIPresenterListener;
import com.octopus.models.SujalamSuphalam.MachineDetailData;
import com.octopus.models.events.CommonResponse;
import com.octopus.models.profile.JurisdictionLevelResponse;
import com.octopus.request.APIRequestCall;
import com.octopus.utility.Constants;
import com.octopus.utility.Urls;
import com.octopus.view.fragments.MachineMouFirstFragment;

import java.lang.ref.WeakReference;

public class MachineMouFragmentPresenter  implements APIPresenterListener {
    private WeakReference<MachineMouFirstFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();
    public static final String CREATE_MACHINE = "cretaeMachine";
    public static final String UPDATE_MACHINE_STATUS = "updateMachineStatus";
    public static final String UPDATE_STRUCTURE_STATUS = "updateStructureStatus";
    public static final String GET_TALUKAS = "getTalukas";

    public MachineMouFragmentPresenter(MachineMouFirstFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void createMachine(MachineDetailData machineDetailData) {
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        String paramjson = gson.toJson(machineDetailData);
        final String createMachineUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.CREATE_MACHINE);
        Log.d(TAG, "createMachineUrl: url" + createMachineUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_MACHINE, paramjson, createMachineUrl);
    }

    public void updateMachineStructureStatus(String machineId, String machineCode, int statusCode, String type) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
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

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        fragmentWeakReference.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        fragmentWeakReference.get().showProgressBar();

        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
            requestCall.getDataApiCall(GET_TALUKAS, getLocationUrl);
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
                if (requestID.equalsIgnoreCase(MachineMouFragmentPresenter.UPDATE_MACHINE_STATUS)) {
                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                            MachineMouFragmentPresenter.UPDATE_MACHINE_STATUS, responseOBJ.getStatus());
                } else if (requestID.equalsIgnoreCase(MachineMouFragmentPresenter.CREATE_MACHINE)) {
                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                            MachineMouFragmentPresenter.UPDATE_MACHINE_STATUS, responseOBJ.getStatus());
                } else if (requestID.equalsIgnoreCase(MachineMouFragmentPresenter.GET_TALUKAS)) {
                    JurisdictionLevelResponse jurisdictionLevelResponse
                        = new Gson().fromJson(response, JurisdictionLevelResponse.class);
                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                        && !jurisdictionLevelResponse.getData().isEmpty()
                        && jurisdictionLevelResponse.getData().size() > 0) {
                    fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
            }
        }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
