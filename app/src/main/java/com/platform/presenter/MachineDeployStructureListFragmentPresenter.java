package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.StructureListAPIResponse;
import com.platform.models.events.CommonResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.view.fragments.MachineDeployStructureListFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MachineDeployStructureListFragmentPresenter implements APIPresenterListener {
    private WeakReference<MachineDeployStructureListFragment> fragmentWeakReference;
    private final String TAG = StructureMachineListFragmentPresenter.class.getName();
    public static final String GET_MACHINE_DEPLOY_STRUCTURE_LIST ="getMachineDeployStructuresList";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_TALUKA_ID = "taluka_id";
    private static final String KEY_VILLAGE_ID = "village_id";
    private static final String KEY_TYPE = "type";
    public static final String DEPLOY_MACHINE ="deployMachine";
    private static final String KEY_STRUCTURE_ID = "structure_id";
    private static final String KEY_MACHINE_ID = "machine_id";

    public MachineDeployStructureListFragmentPresenter(MachineDeployStructureListFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getDeployableStructuresList(String districtId, String talukaId, String villageId,
                                            String type, String currentStructureId){
        //Gson gson = new GsonBuilder().create();
        String paramjson = getStructuresListJson(districtId, talukaId, villageId, type, currentStructureId);
        final String getMachineDeployableStructuresUrl = BuildConfig.BASE_URL
                + String.format(Urls.SSModule.GET_SS_STRUCTURE_LIST);
        Log.d(TAG, "getMachineDeployableStructuresUrl: url" + getMachineDeployableStructuresUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MACHINE_DEPLOY_STRUCTURE_LIST, paramjson, getMachineDeployableStructuresUrl);
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
                    }
                }
                if (requestID.equalsIgnoreCase(MachineDeployStructureListFragmentPresenter.DEPLOY_MACHINE)) {
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
