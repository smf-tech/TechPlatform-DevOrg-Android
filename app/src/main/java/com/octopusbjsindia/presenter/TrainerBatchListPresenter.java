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


    /*public void createBatch(String requestJson){
        final String url = BuildConfig.BASE_URL + String.format(Urls.OperatorApi.MACHINE_DATA_WORKLOG);
        MachineWorkingDataListRequestCall requestCall = new MachineWorkingDataListRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(CREATE_BATCH,requestJson ,url);

    }*/
}