package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.request.MachineWorkingDataListRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.MachineWorkingDataListActivity;

public class MachineWorkingDataListPresenter implements APIDataListener {


    private final String GET_APP_CONFIG = "getappconfig";

    private MachineWorkingDataListActivity mContext;

    public MachineWorkingDataListPresenter(MachineWorkingDataListActivity mContext) {
        this.mContext = mContext;
    }

    /*@Override
    public void onFailureListener(String requestID, String message) {
        //mContext.hideProgressBar();
        mContext.showMessage(requestID, message,300 );
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        //mContext.hideProgressBar();
        mContext.showMessage(requestID, error.getMessage(), 400);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        //mContext.hideProgressBar();
        Gson gson = new Gson();
        CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
        if(commonResponse.getCode()==1000){
            Utils.signOut(mContext);
            mContext.showMessage(requestID, commonResponse.getMessage(), commonResponse.getCode());
            mContext.finish();
            return;
        } else if (commonResponse.getCode() == 200) {
            switch (requestID){
                case GET_APP_CONFIG:
                    mContext.showMessage(requestID,response, commonResponse.getCode());
                    break;
            }

        } else {
            mContext.showMessage(requestID, commonResponse.getMessage(), 100);
        }
    }*/

    public void getAppConfig(String mobileNumber) {

        //mContext.showProgressBar();
        final String url = BuildConfig.BASE_URL + String.format(Urls.Configuration.API_CONFIG);
        MachineWorkingDataListRequestCall requestCall = new MachineWorkingDataListRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getApiCall(GET_APP_CONFIG, url);

    }
    public void getMachineWorkData(String requestJson){
        final String url = BuildConfig.BASE_URL + String.format(Urls.OperatorApi.MACHINE_DATA_WORKLOG);
        MachineWorkingDataListRequestCall requestCall = new MachineWorkingDataListRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_APP_CONFIG,requestJson ,url);
    }


    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        Log.d("machineWorklog", requestID + " response Json : " + response);
        /*AppConfigResponseModel appConfigResponseModel
                = new Gson().fromJson(response, AppConfigResponseModel.class);*/
        Gson gson = new Gson();
        CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
        mContext.ShowReceivedWorkList(requestID,response, commonResponse.getStatus());
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }
}
