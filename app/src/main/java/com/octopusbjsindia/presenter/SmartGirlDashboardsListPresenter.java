package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.models.smartgirl.DashboardItemListResponseModel;
import com.octopusbjsindia.models.smartgirl.WorkshopBachListResponseModel;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.SmartGirlDashboardListActivity;
import com.octopusbjsindia.view.activities.SmartGirlWorkshopListActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SmartGirlDashboardsListPresenter implements APIPresenterListener {

    public static final String GET_DISTRICT = "getDistrict";
    public static final String GET_TALUKAS = "getTalukas";

    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";

    private final String GET_CATEGORY = "getbatchlist";
    private final String GET_DATA_LIST = "getdatalist";
    /*private final String CANCEL_EVENT_REQUEST = "CANCELEVENTREQUEST";
    */

    private final String TAG = SmartGirlDashboardsListPresenter.class.getName();

    private final WeakReference<SmartGirlDashboardListActivity> mContext;

    public SmartGirlDashboardsListPresenter(SmartGirlDashboardListActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }


    @Override
    public void onFailureListener(String requestID, String message) {
        if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
            mContext.get().showNoData();
        }
        mContext.get().hideProgressBar();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
            mContext.get().showNoData();
        }
        mContext.get().hideProgressBar();
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (mContext == null) {
            return;
        }
        mContext.get().hideProgressBar();
        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(GET_DATA_LIST)) {
                    Log.d("TAG", "BatchListResponse" + response);
                    DashboardItemListResponseModel dashboardItemListResponseModel
                            = new Gson().fromJson(response, DashboardItemListResponseModel.class);
                    mContext.get().showReceivedDataList(dashboardItemListResponseModel);
                }else
                if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
                    Log.d("TAG", "BatchListResponse" + response);
                    DashboardItemListResponseModel dashboardItemListResponseModel
                            = new Gson().fromJson(response, DashboardItemListResponseModel.class);
                    mContext.get().showReceivedDataList(dashboardItemListResponseModel);
                }else if(requestID.equalsIgnoreCase(StructureMachineListFragmentPresenter.GET_DISTRICT)){
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void getRequestedList(String url) {
        mContext.get().showProgressBar();
        final String getRoleAccessUrl = url;
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_CATEGORY, getRoleAccessUrl);
    }*/

  /*  public void createBatch(String requestJson) {
        final String createBatchUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.CREATE_BATCH_API);
        Log.d("TAG", "getRoleAccessUrl: url" + createBatchUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_BATCH, requestJson, createBatchUrl);
    }*/

/*
    public void addTrainerToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.ADD_BENEFICIARY_TO_WORKSHOP);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_TRAINERS_TO_BATCH,requestJson,url);

    }

    public void addSelfTrainerToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.REGISTER_TO_WORKSHOP);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_TRAINERS_TO_BATCH,requestJson,url);

    }
    public void cancelWorkshopRequest(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.CANCEL_WORKSHOP_API);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CANCEL_EVENT_REQUEST,requestJson,url);

    }
    public void completeWorkshopRequest(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.COMPLETE_WORKSHOP_API);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(COMPLETE_EVENT_REQUEST,requestJson,url);

    }

    public void submitPreTestFormToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.TRAINER_PRE_TEST);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_PRETEST_BATCH,requestJson,url);

    }
    public void submitFeedbsckToWorkshop(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.BENEFICIARY_WORKSHOP_FEEDBACK_API);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_POSTFEEDACK_TO_BATCH,requestJson,url);

    }*/

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        mContext.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICT, new JSONObject(map).toString(), getLocationUrl);
        }else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
            requestCall.postDataApiCall(GET_TALUKAS, new JSONObject(map).toString(), getLocationUrl);
        }
    }


    //get  list data
    public void getRequestedDataList(String paramsJson){
        Gson gson = new GsonBuilder().create();
        mContext.get().showProgressBar();
/*
        HashMap<String,String> map=new HashMap<>();
        map.put("state_id", state_id);
        if(!TextUtils.isEmpty(district_id)){
            map.put("district_id", district_id);
        }
        if(!TextUtils.isEmpty(taluka_id)){
            map.put("taluka_id", taluka_id);
        }
        if(!TextUtils.isEmpty(taluka_id)){
            map.put("taluka_id", taluka_id);
        }
        if(!TextUtils.isEmpty(taluka_id)){
            map.put("category_id", categoryId);
        }
        if(!TextUtils.isEmpty(taluka_id)){
            map.put("type", type);
        }

        String paramjson = gson.toJson(map);*/
        final String getStructuresListUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_DAHSBOARDS_LIST_API);
        Log.d(TAG, "getworkshoplistUrl: url" + getStructuresListUrl);
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_DATA_LIST, paramsJson, getStructuresListUrl);
    }
}
