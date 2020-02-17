package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.SujalamSufalamFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SujalamSuphalamFragmentPresenter implements APIPresenterListener {

    private WeakReference<SujalamSufalamFragment> fragmentWeakReference;
    private final String TAG = SujalamSuphalamFragmentPresenter.class.getName();
    public static final String GET_STRUCTURE_ANALYTICS ="getStructureAnalytics";
    public static final String GET_MACHINE_ANALYTICS = "getMachineAnalytics";
    public static final String GET_SS_MASTER_DATA = "getSSMasterData";
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    public static final String GET_DISTRICT = "getDistrict";
    public static final String GET_TALUKAS = "getTalukas";

    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_TALUKA_ID = "taluka_id";

    public SujalamSuphalamFragmentPresenter(SujalamSufalamFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getAnalyticsData(String type, String stateIds, String districtIds, String talukaIds) {
        String getSSAnalyticsUrl = "";

        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_STATE_ID, stateIds);
        map.put(KEY_DISTRICT_ID, districtIds);
        map.put(KEY_TALUKA_ID, talukaIds);

        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);

        if(type.equals(GET_STRUCTURE_ANALYTICS)) {
            getSSAnalyticsUrl = BuildConfig.BASE_URL
                    + String.format(Urls.SSModule.GET_SS_STRUCTURE_ANALYTICS);
            Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getSSAnalyticsUrl);
            requestCall.postDataApiCall(GET_STRUCTURE_ANALYTICS, new JSONObject(map).toString(), getSSAnalyticsUrl);
        }
        if(type.equals(GET_MACHINE_ANALYTICS)){
            getSSAnalyticsUrl = BuildConfig.BASE_URL
                    + String.format(Urls.SSModule.GET_SS_MACHINE_ANALYTICS);
            Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getSSAnalyticsUrl);
            requestCall.postDataApiCall(GET_MACHINE_ANALYTICS, new JSONObject(map).toString(), getSSAnalyticsUrl);
        }
    }

    public void getSSMasterData(){
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String getSSMasterDaraUrl = BuildConfig.BASE_URL
                    + String.format(Urls.SSModule.GET_SS_MASTER_DATA);
        Log.d(TAG, "getSSMasterDaraUrl: url" + getSSMasterDaraUrl);
        requestCall.getDataApiCall(GET_SS_MASTER_DATA, getSSMasterDaraUrl);
    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String, String> map = new HashMap<>();
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
        if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICT, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(GET_TALUKAS, new JSONObject(map).toString(), getLocationUrl);
        }
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
                if (requestID.equals(SujalamSuphalamFragmentPresenter.GET_STRUCTURE_ANALYTICS) ||
                        requestID.equals(SujalamSuphalamFragmentPresenter.GET_MACHINE_ANALYTICS)) {
                    SSAnalyticsAPIResponse ssAnalyticsData = PlatformGson.getPlatformGsonInstance().fromJson(response, SSAnalyticsAPIResponse.class);
                    if (ssAnalyticsData.getCode() == 200) {
                        fragmentWeakReference.get().populateAnalyticsData(requestID, ssAnalyticsData);
                    } else if (ssAnalyticsData.getCode() == 400) {
                        fragmentWeakReference.get().emptyResponse(requestID);
                    }
                } else if (requestID.equals(SujalamSuphalamFragmentPresenter.GET_SS_MASTER_DATA)) {
                    MasterDataResponse masterDataResponse = new Gson().fromJson(response, MasterDataResponse.class);
                    fragmentWeakReference.get().setMasterData(masterDataResponse);
                } else if (requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_DISTRICT)) {
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);
                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                    }
                } else if (requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_TALUKAS)) {
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
