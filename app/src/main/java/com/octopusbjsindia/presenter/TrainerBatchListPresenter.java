package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.smartgirl.SGTrainersListResponseModel;
import com.octopusbjsindia.models.smartgirl.TrainerBachListResponseModel;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;

import java.lang.ref.WeakReference;

public class TrainerBatchListPresenter implements APIPresenterListener {


    private final String GET_TRAINERS_LIST = "gettrainerslist";
    private final String GET_CATEGORY = "getbatchlist";
    private final String CANCEL_EVENT_REQUEST = "CANCELEVENTREQUEST";
    private final String COMPLETE_EVENT_REQUEST = "COMPLETEEVENTREQUEST";
    private final String ADD_TRAINERS_TO_BATCH = "addtrainertobatch";
    private final String GET_PROJECT_USER_PROFILE = "GETUSERPROFILE";
    private final String ADD_PRETEST_BATCH = "PRETESTFORBACTH";
    private final String ADD_POSTFEEDACK_TO_BATCH = "POSTFEEDBACK";

    private final String TAG = TrainerBatchListPresenter.class.getName();

    private final WeakReference<TrainerBatchListActivity> mContext;

    public TrainerBatchListPresenter(TrainerBatchListActivity mContext) {
        this.mContext = new WeakReference<>(mContext);
    }


    @Override
    public void onFailureListener(String requestID, String message) {
        if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
            if (mContext!=null) {
                mContext.get().showNoData();
            }
            mContext.get().hideProgressBar();
        }


    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
            if (mContext!=null) {
                mContext.get().showNoData();
            }
            mContext.get().hideProgressBar();
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
                if (requestID.equalsIgnoreCase(GET_CATEGORY)) {
                    Log.d("TAG", "BatchListResponse" + response);
                    TrainerBachListResponseModel trainerBachListResponseModel
                            = new Gson().fromJson(response, TrainerBachListResponseModel.class);
                    mContext.get().showReceivedBatchList(trainerBachListResponseModel);
                }else if (requestID.equalsIgnoreCase(ADD_TRAINERS_TO_BATCH)) {
                    CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                    mContext.get().showToastMessage(commonResponse.getMessage());
                    //mContext.get().refreshData();
                    /*if(commonResponse.getStatus()==200){
                        Util.showToast(commonResponse.getMessage(),this);
                    } else {
                        Util.showToast(commonResponse.getMessage(),this);
                    }*/
                }else if (requestID.equalsIgnoreCase(GET_TRAINERS_LIST)) {
                    Log.d("TAG", "TrainersListResponse" + response);
                    //mContext.get().showReceivedBatchList(trainerBachListResponseModel);
                    mContext.get().showReceivedTrainerListResponse(response);


                }else if (requestID.equalsIgnoreCase(CANCEL_EVENT_REQUEST)) {
                    CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                    mContext.get().showToastMessage(commonResponse.getMessage());
                }else if (requestID.equalsIgnoreCase(GET_PROJECT_USER_PROFILE)) {
                    /*CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
                    mContext.get().showToastMessage(commonResponse.getMessage());*/
                    mContext.get().showReceivedUserProfile(response);
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
                + String.format(Urls.SmartGirl.GET_BATCH_LIST_API);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_CATEGORY, getRoleAccessUrl);
    }

    //Get All Trainer list-
    public void getAllTrainerList() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_ALL_TRAINER_LIST_API);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_TRAINERS_LIST, getRoleAccessUrl);
    }

    public void getAllMasterTrainerList() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_ALL_MASTER_LIST_API);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_TRAINERS_LIST, getRoleAccessUrl);
    }

    public void getAllBeneficiaryList() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_ALL_BENEFICIARY_LIST_API);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_TRAINERS_LIST, getRoleAccessUrl);
    }



  //------------
    public void addTrainerToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.ADD_TRAINER_TO_BATCH);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_TRAINERS_TO_BATCH,requestJson,url);

    }
    public void addSelfTrainerToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.REGISTER_AS_TRAINER_TO_BATCH);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_TRAINERS_TO_BATCH,requestJson,url);

    }
    public void getSelectedUserProfile(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_USER_PROJECT_PROFILE_API);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_PROJECT_USER_PROFILE,requestJson,url);

    }
    public void cancelBatchAPI(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.CANCEL_BATCH_API);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CANCEL_EVENT_REQUEST,requestJson,url);

    }
    public void completeBatchAPI(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.COMPLETE_BATCH_API);
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
    public void submitMockTestFormToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.TRAINER_MOCKTEST_TEST);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_PRETEST_BATCH,requestJson,url);

    }
    public void submitFeedbsckToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.TRAINER_BATCH_FEEDBACK);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(ADD_POSTFEEDACK_TO_BATCH,requestJson,url);

    }

}
