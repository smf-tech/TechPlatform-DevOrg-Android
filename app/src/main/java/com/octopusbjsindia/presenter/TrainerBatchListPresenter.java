package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.smartgirl.TrainerBachListResponseModel;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;

import java.lang.ref.WeakReference;

public class TrainerBatchListPresenter implements APIPresenterListener {


    private final String GET_CATEGORY = "getbatchlist";
    private final String ADD_TRAINERS_TO_BATCH = "addtrainertobatch";
    private final String ADD_PRETEST_BATCH = "PRETESTFORBACTH";
    private final String ADD_POSTFEEDACK_TO_BATCH = "POSTFEEDBACK";

    private final String TAG = TrainerBatchListPresenter.class.getName();

    private final WeakReference<TrainerBatchListActivity> mContext;

    public TrainerBatchListPresenter(TrainerBatchListActivity mContext) {
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

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //get Locations


    public void getBatchList() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_BATCH_LIST_API);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_CATEGORY, getRoleAccessUrl);
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

    public void submitPreTestFormToBatch(String requestJson){
        final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.TRAINER_PRE_TEST);
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
