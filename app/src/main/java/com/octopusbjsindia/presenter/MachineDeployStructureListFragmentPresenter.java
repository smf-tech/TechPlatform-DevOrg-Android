package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.StructureListAPIResponse;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.MachineDeployStructureListFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MachineDeployStructureListFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineDeployStructureListFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    public static final String GET_MACHINE_DEPLOY_STRUCTURE_LIST ="getMachineDeployStructuresList";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_TALUKA_ID = "taluka_id";
    private static final String KEY_VILLAGE_ID = "village_id";
    private static final String KEY_TYPE = "type";
    public static final String DEPLOY_MACHINE ="deployMachine";
    private static final String KEY_STRUCTURE_ID = "structure_id";
    private static final String KEY_MACHINE_ID = "machine_id";
    public static final String GET_DISTRICT = "getDistrict";
    public static final String GET_TALUKAS = "getTalukas";

    public MachineDeployStructureListFragmentPresenter(MachineDeployStructureListFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getDeployableStructuresList(String districtId, String talukaId, String villageId,
                                            String type, String currentStructureId){
        String paramjson = getStructuresListJson(districtId, talukaId, villageId, type, currentStructureId);
        final String getMachineDeployableStructuresUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_STRUCTURE_LIST);
        Log.d(TAG, "getMachineDeployableStructuresUrl: url" + getMachineDeployableStructuresUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MACHINE_DEPLOY_STRUCTURE_LIST, paramjson, getMachineDeployableStructuresUrl);
    }

    public void getDistrictDeployableStructuresList(String districtId, String type, String currentStructureId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_DISTRICT_ID, districtId);
        map.put(KEY_TYPE, type);
        map.put(KEY_STRUCTURE_ID, currentStructureId);

        final String getMachineDeployableStructuresUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_STRUCTURE_LIST);
        Log.d(TAG, "getMachineDeployableStructuresUrl: url" + getMachineDeployableStructuresUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MACHINE_DEPLOY_STRUCTURE_LIST, new JSONObject(map).toString(),
                getMachineDeployableStructuresUrl);
    }

    public void getTalukaDeployableStructuresList(String stateId, String districtId, String talukaId, String type, String currentStructureId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_STATE_ID, stateId);
        map.put(KEY_DISTRICT_ID, districtId);
        map.put(KEY_TALUKA_ID, talukaId);
        map.put(KEY_TYPE, type);
        map.put(KEY_STRUCTURE_ID, currentStructureId);

        final String getMachineDeployableStructuresUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_STRUCTURE_LIST);
        Log.d(TAG, "getMachineDeployableStructuresUrl: url" + getMachineDeployableStructuresUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MACHINE_DEPLOY_STRUCTURE_LIST, new JSONObject(map).toString(),
                getMachineDeployableStructuresUrl);
    }

    public void deployMachine(String structureId, String machineId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_STRUCTURE_ID, structureId);
        map.put(KEY_MACHINE_ID, machineId);

        final String machineDeployUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.DEPLOY_MACHINE);
        Log.d(TAG, "machineDeployUrl: url" + machineDeployUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(DEPLOY_MACHINE, new JSONObject(map).toString(), machineDeployUrl);
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
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICT, new JSONObject(map).toString(), getLocationUrl);
        }else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
            requestCall.postDataApiCall(GET_TALUKAS, new JSONObject(map).toString(), getLocationUrl);
        }
    }

    public String getStructuresListJson(String districtId, String talukaId, String villageId,
                                        String type, String currentStructureId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_DISTRICT_ID, districtId);
        map.put(KEY_TALUKA_ID, talukaId);
        map.put(KEY_VILLAGE_ID, villageId);
        map.put(KEY_TYPE, type);
        map.put(KEY_STRUCTURE_ID, currentStructureId);
        //JsonObject requestObject = new JsonObject(map);
        JSONObject jsonObject = new JSONObject(map);
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            requestObject.addProperty(key, value);
//        }
        return jsonObject.toString();
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
                if (requestID.equalsIgnoreCase(MachineDeployStructureListFragmentPresenter.GET_MACHINE_DEPLOY_STRUCTURE_LIST)) {
                    StructureListAPIResponse structureListData = PlatformGson.getPlatformGsonInstance().fromJson(response, StructureListAPIResponse.class);
                    if (structureListData.getStatus() == 200) {
                        fragmentWeakReference.get().populateStructureData(requestID, structureListData);
                    } else if(structureListData.getCode() == 400){
                        fragmentWeakReference.get().showNoDataMessage();
                    }
                }else if(requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_TALUKAS)){
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);
                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                Constants.JurisdictionLevelName.TALUKA_LEVEL);
                    }
                } else if (requestID.equalsIgnoreCase(MachineDeployStructureListFragmentPresenter.DEPLOY_MACHINE)) {
                    CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                    fragmentWeakReference.get().showResponse(responseOBJ.getMessage(),
                            MachineDeployStructureListFragmentPresenter.DEPLOY_MACHINE, responseOBJ.getStatus());
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
