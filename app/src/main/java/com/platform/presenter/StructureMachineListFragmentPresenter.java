package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.MachineListAPIResponse;
import com.platform.request.APIRequestCall;
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

    public StructureMachineListFragmentPresenter(StructureMachineListFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getStrucuresList(String stateId, String districtId, String talukaId){
        Gson gson = new GsonBuilder().create();
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
        String paramjson = gson.toJson(getSSDataJson(stateId,districtId,talukaId));
        final String getMachinesListUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_MACHINE_LIST);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMachinesListUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MACHINE_LIST, paramjson, getMachinesListUrl);
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
                    MachineListAPIResponse machineListData = PlatformGson.getPlatformGsonInstance().fromJson(response, MachineListAPIResponse.class);
                    if (machineListData.getCode() == 200) {
                        if (requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_STRUCTURE_LIST)) {
                            fragmentWeakReference.get().populateMachineData(requestID, machineListData);
                        }
                    }
                }  else if (requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_MACHINE_LIST)) {
                    //fragmentWeakReference.get().populateAnalyticsData(requestID, machineListData);
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
