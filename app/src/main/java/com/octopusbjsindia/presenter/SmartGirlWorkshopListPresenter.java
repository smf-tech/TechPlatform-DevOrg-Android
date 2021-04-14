package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.models.smartgirl.WorkshopBachListResponseModel;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SmartGirlWorkshopListActivity;

import java.lang.ref.WeakReference;

public class SmartGirlWorkshopListPresenter implements APIPresenterListener {


    private final String GET_CATEGORY = "getbatchlist";
    private final String CANCEL_EVENT_REQUEST = "CANCELEVENTREQUEST";
    private final String COMPLETE_EVENT_REQUEST = "COMPLETEEVENTREQUEST";
    private final String ADD_TRAINERS_TO_BATCH = "addtrainertobatch";
    private final String ADD_PRETEST_BATCH = "PRETESTFORBACTH";
    private final String ADD_POSTFEEDACK_TO_BATCH = "POSTFEEDBACK";
    private final String SEND_WORKSHOP_DATA_EMAIL = "WORKSHOPDATAEMAIL";

    private final String TAG = SmartGirlWorkshopListPresenter.class.getName();

    private final WeakReference<SmartGirlWorkshopListActivity> mContext;

    public SmartGirlWorkshopListPresenter(SmartGirlWorkshopListActivity mContext) {
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
                if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
                    Log.d("TAG", "BatchListResponse" + response);
                    WorkshopBachListResponseModel trainerBachListResponseModel
                            = new Gson().fromJson(response, WorkshopBachListResponseModel.class);
                    mContext.get().showReceivedBatchList(trainerBachListResponseModel);
                }else if (requestID.equalsIgnoreCase(ADD_TRAINERS_TO_BATCH)) {
                    CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                    mContext.get().showToastMessage(commonResponse.getMessage());
                    mContext.get().refreshData();

                    /*if(commonResponse.getStatus()==200){
                        Util.showToast(commonResponse.getMessage(),this);
                    } else {
                        Util.showToast(commonResponse.getMessage(),this);
                    }*/
                }else if (requestID.equalsIgnoreCase(CANCEL_EVENT_REQUEST)){
                    CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                    mContext.get().showToastMessage(commonResponse.getMessage());
                    mContext.get().refreshData();
                }else if (requestID.equalsIgnoreCase(SEND_WORKSHOP_DATA_EMAIL)){
                    CommonResponseStatusString commonResponse = new Gson().fromJson(response, CommonResponseStatusString.class);
                    Util.showToast(commonResponse.getMessage(), mContext);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //get Locations


    public void getBatchList() {
        mContext.get().showProgressBar();
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.CREATE_WORKSHOP_LIST_API);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_CATEGORY, getRoleAccessUrl);
    }

    //new api to get workshop list
    public void getBatchList(String requestJson, String apiUrl){
        //old
        /*final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.CREATE_WORKSHOP_LIST_API);*/
        //new dec 2020
        /*final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_DAHSBOARDS_LIST_API);*/

        Log.d("TAG", "getwoskshoplist: url" + apiUrl);
        Log.d("TAG", "requestjson" + requestJson);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_CATEGORY,requestJson,apiUrl);

    }

  /*  public void createBatch(String requestJson) {
        final String createBatchUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.CREATE_BATCH_API);
        Log.d("TAG", "getRoleAccessUrl: url" + createBatchUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_BATCH, requestJson, createBatchUrl);
    }*/


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

    }

    public void sendWorkshopDataEmail(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.SEND_FEEDBACK_EMAIL);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SEND_WORKSHOP_DATA_EMAIL,requestJson,url);

    }


}
