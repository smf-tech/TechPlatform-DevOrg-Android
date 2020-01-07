package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineDetailData;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.MachineMouFirstFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MachineMouFragmentPresenter  implements APIPresenterListener {
    private WeakReference<MachineMouFirstFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
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

//    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
//        APIRequestCall requestCall = new APIRequestCall();
//        requestCall.setApiPresenterListener(this);
//        fragmentWeakReference.get().showProgressBar();
//        final String getLocationUrl = BuildConfig.BASE_URL
//                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);
//        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
//        fragmentWeakReference.get().showProgressBar();
//
//        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
//            requestCall.getDataApiCall(GET_TALUKAS, getLocationUrl);
//        }
//    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        fragmentWeakReference.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(GET_TALUKAS, new JSONObject(map).toString(), getLocationUrl);
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
