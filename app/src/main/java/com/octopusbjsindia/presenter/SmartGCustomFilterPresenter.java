package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.models.smartgirl.SmartGirlCategoryResponseModel;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.SmartGirlDashboardListActivity;
import com.octopusbjsindia.view.customs.SmartGCustomFilterDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SmartGCustomFilterPresenter implements APIPresenterListener {

    public static final String GET_DISTRICT = "getDistrict";
    public static final String GET_TALUKAS = "getTalukas";
    public static final String GET_STATES = "getStates";
    private final String GET_CATEGORY = "getCategory";
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    public static final String GET_TRAINER_LIST = "gettrainerlist";

    private final String TAG = SmartGirlDashboardsListPresenter.class.getName();

    private final WeakReference<SmartGCustomFilterDialog> mContext;

    public SmartGCustomFilterPresenter(SmartGCustomFilterDialog mContext) {
        this.mContext = new WeakReference<>(mContext);
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if(requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_DISTRICT)){
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);
            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {
                mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                        Constants.JurisdictionLevelName.DISTRICT_LEVEL);
            }
        } else if(requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_TALUKAS)){
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);
            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {
                mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
            }
        } else if(requestID.equalsIgnoreCase(GET_STATES)){
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);
            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {
                mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                        Constants.JurisdictionLevelName.STATE_LEVEL);
            }
        } else if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
            SmartGirlCategoryResponseModel jurisdictionLevelResponse
                    = new Gson().fromJson(response, SmartGirlCategoryResponseModel.class);
            mContext.get().showReceivedCategories(jurisdictionLevelResponse);
        }else if (requestID.equalsIgnoreCase(GET_TRAINER_LIST)){
            Log.d(TAG, "TrainerList" + response);
            mContext.get().showReceivedTrainerList(response);
        }
    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        //mContext.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        //mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICT, new JSONObject(map).toString(), getLocationUrl);
        }else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
            requestCall.postDataApiCall(GET_TALUKAS, new JSONObject(map).toString(), getLocationUrl);
        }else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)){
            requestCall.postDataApiCall(GET_STATES, new JSONObject(map).toString(), getLocationUrl);
        }
    }

    public void getTrainerListData(String selectedStateId, String selectedDistrictId) {
        HashMap<String,String> map=new HashMap<>();
        map.put("state_id", selectedStateId);
        map.put("district_id", selectedDistrictId);


        //mContext.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_FILTER_TRAINER_LIST);
        Log.d(TAG, "gettrainerUrl: url" + getLocationUrl);
        //mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_TRAINER_LIST, new JSONObject(map).toString(), getLocationUrl);
    }

    public void getBatchCategory() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_BATCH_CATEGORY);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_CATEGORY, getRoleAccessUrl);
    }
}
