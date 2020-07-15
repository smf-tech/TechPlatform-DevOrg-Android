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
import com.octopusbjsindia.view.activities.CreateTrainerWorkshop;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class CreateTrainerWorkshopPresenter implements APIPresenterListener {

    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    final String STRUCTURE_MASTER = "structureMasterData";
    final String CREATE_STRUCTURE = "createStructure";
    private final String GET_DISTRICT = "getDistrict";
    private final String GET_TALUKA = "getTaluka";
    private final String GET_STATE = "getState";
    private final String GET_CATEGORY = "getCategory";
    private final String CREATE_BATCH = "createBatch";
    private final String EDIT_BATCH = "createBatch";
    private final String GET_MASTER_TRAINERS = "GET_MASTER_TRAINERS";
    private final String TAG = CreateStructureActivityPresenter.class.getName();

    private final WeakReference<CreateTrainerWorkshop> mContext;

    public CreateTrainerWorkshopPresenter(CreateTrainerWorkshop mContext) {
        this.mContext = new WeakReference<>(mContext);
    }


    @Override
    public void onFailureListener(String requestID, String message) {
        if (requestID.equalsIgnoreCase(EDIT_BATCH)) {

        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (requestID.equalsIgnoreCase(EDIT_BATCH)) {

        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (mContext == null) {
            return;
        }
        mContext.get().hideProgressBar();
        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(GET_DISTRICT) ||
                        requestID.equalsIgnoreCase(GET_TALUKA) ||
                        requestID.equalsIgnoreCase(GET_STATE)) {
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);

                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        if (requestID.equalsIgnoreCase(GET_DISTRICT)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_TALUKA)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else if (requestID.equalsIgnoreCase(GET_STATE)) {
                            mContext.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL);
                        }
                    }
                } else if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
                    SmartGirlCategoryResponseModel jurisdictionLevelResponse
                            = new Gson().fromJson(response, SmartGirlCategoryResponseModel.class);
                    mContext.get().showReceivedCategories(jurisdictionLevelResponse);
                } else if (requestID.equalsIgnoreCase(GET_MASTER_TRAINERS)) {
                    mContext.get().showTrainerList(response);
                } else if (requestID.equalsIgnoreCase(CREATE_BATCH)) {
                    mContext.get().batchCreatedSuccess(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //get Locations
    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        // mContext.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        //mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICT, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(GET_TALUKA, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.postDataApiCall(GET_STATE, new JSONObject(map).toString(), getLocationUrl);
        }
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

    public void createBatch(String requestJson) {
        final String createBatchUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.CREATE_BATCH_API);
        Log.d("TAG", "getRoleAccessUrl: url" + createBatchUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_BATCH, requestJson, createBatchUrl);
    }

    public void editBatch(String requestJson) {
        final String createBatchUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.EDIT_BATCH_API);
        Log.d("TAG", "getRoleAccessUrl: url" + createBatchUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(EDIT_BATCH, requestJson, createBatchUrl);
    }


    public void getAdditionalTrainer(String requestJson) {
        final String createBatchUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_MASTER_TRAINERS_SG);
        Log.d("TAG", "getRoleAccessUrl: url" + createBatchUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MASTER_TRAINERS, requestJson, createBatchUrl);
    }

    /*public void createBatch(String requestJson){
        final String url = BuildConfig.BASE_URL + String.format(Urls.OperatorApi.MACHINE_DATA_WORKLOG);
        MachineWorkingDataListRequestCall requestCall = new MachineWorkingDataListRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_BATCH,requestJson ,url);

    }*/
}
