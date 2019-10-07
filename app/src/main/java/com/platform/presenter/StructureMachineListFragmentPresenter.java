package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.BuildConfig;
import com.platform.R;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.MachineListAPIResponse;
import com.platform.models.events.CommonResponse;
import com.platform.models.profile.JurisdictionLevelResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.view.fragments.StructureMachineListFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class StructureMachineListFragmentPresenter implements APIPresenterListener {

    private static final String KEY_STATE_ID = "state";
    private static final String KEY_DISTRICT_ID = "district";
    private static final String KEY_TALUKA_ID = "taluka";
    private WeakReference<StructureMachineListFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();
    public static final String GET_STRUCTURE_LIST ="getStructureList";
    public static final String GET_MACHINE_LIST = "getMachineList";
    public static final String GET_TALUKAS = "getTalukas";
    private static final String KEY_MACHINE_ID = "machine_id";
    private static final String KEY_MACHINE_CODE = "machine_code";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DEPLOY_TALUKA = "deploy_taluka";
    private static final String KEY_TERMINATE_REASON = "reason";
    public static final String TERMINATE_DEPLOY = "terminateDeployMachine";

    public StructureMachineListFragmentPresenter(StructureMachineListFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getStrucuresList(String stateId, String districtId, String talukaId){
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        String paramjson = gson.toJson(getSSDataJson(stateId, districtId,talukaId));
        final String getStructuresListUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_STRUCTURE_LIST);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getStructuresListUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_STRUCTURE_LIST, paramjson, getStructuresListUrl);
    }

    public void getMachinesList(String stateId, String districtId, String talukaId){
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        String paramjson = gson.toJson(getSSDataJson(stateId,districtId,talukaId));
        final String getMachinesListUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_MACHINE_LIST);
        Log.d(TAG, "getMachineListUrl: url" + getMachinesListUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MACHINE_LIST, paramjson, getMachinesListUrl);
    }

    public void getDistrictMachinesList(String stateId, String districtId){
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        String paramjson = gson.toJson(getDistrictMachineDataJson(stateId,districtId));
        final String getMachinesListUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_MACHINE_LIST);
        Log.d(TAG, "getDistrictMachineListUrl: url" + getMachinesListUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MACHINE_LIST, paramjson, getMachinesListUrl);
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

    public void terminateSubmitMou(String machineId, String machineCode, int status, String deployTaluka_terminateReason){
        Gson gson = new GsonBuilder().create();
        fragmentWeakReference.get().showProgressBar();
        String paramjson = gson.toJson(terminateDeployDataJson(machineId, machineCode, status, deployTaluka_terminateReason));
        final String getTerminateDeployUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.MOU_TERMINATE_DEPLOY);
        Log.d(TAG, "getTerminateDeployUrl: url" + getTerminateDeployUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(TERMINATE_DEPLOY, paramjson, getTerminateDeployUrl);
    }

    public JsonObject getSSDataJson(String stateId, String districtId, String talukaId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_STATE_ID, stateId);
        map.put(KEY_DISTRICT_ID, districtId);
        map.put(KEY_TALUKA_ID, talukaId);

        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }
        return requestObject;
    }

    public JsonObject getDistrictMachineDataJson(String stateId, String districtId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_STATE_ID, stateId);
        map.put(KEY_DISTRICT_ID, districtId);

        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }
        return requestObject;
    }

    public JsonObject terminateDeployDataJson(String machineId, String machineCode, int status, String deployTaluka_terminateReason){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_MACHINE_ID, machineId);
        map.put(KEY_MACHINE_CODE, machineCode);
        map.put(KEY_STATUS, String.valueOf(status));
        if(status == Constants.SSModule.MACHINE_DEPLOYED_STATUS_CODE) {
            map.put(KEY_DEPLOY_TALUKA, deployTaluka_terminateReason);
        } else if(status == Constants.SSModule.MACHINE_MOU_TERMINATED_STATUS_CODE) {
            map.put(KEY_TERMINATE_REASON, deployTaluka_terminateReason);
        }
        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }
        return requestObject;
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
                if (requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_STRUCTURE_LIST)) {

                } else if (requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_MACHINE_LIST)) {
                    //fragmentWeakReference.get().populateAnalyticsData(requestID, machineListData);
                    MachineListAPIResponse machineListData = PlatformGson.getPlatformGsonInstance().fromJson(response, MachineListAPIResponse.class);
                    if (machineListData.getCode() == 200) {
                            fragmentWeakReference.get().populateMachineData(requestID, machineListData);
                    }
                } else if(requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_TALUKAS)){
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);
                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                Constants.JurisdictionLevelName.TALUKA_LEVEL);
                    }
                } else if(requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.TERMINATE_DEPLOY)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage(), responseOBJ.getStatus());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
