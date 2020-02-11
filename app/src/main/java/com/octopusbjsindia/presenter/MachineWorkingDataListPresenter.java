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
    private final String GET_WORKLOG_DETAILS = "getworklogdetails";
    private final String REQUEST_EDIT_WORKLOG = "REQUESTEDITWORKLOG";
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
        showProgressBar();
    }

    public void getMachineWorklogDetails(String requestJson){
        final String url = BuildConfig.BASE_URL + String.format(Urls.OperatorApi.MACHINE_WORKLOG__DETAILS);
        MachineWorkingDataListRequestCall requestCall = new MachineWorkingDataListRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_WORKLOG_DETAILS,requestJson ,url);
    }

    public void editMachineWorklog(String requestJson){
        final String url = BuildConfig.BASE_URL + String.format(Urls.OperatorApi.MACHINE_WORKLOG_EDIT);
        MachineWorkingDataListRequestCall requestCall = new MachineWorkingDataListRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(REQUEST_EDIT_WORKLOG,requestJson ,url);
    }


    @Override
    public void onFailureListener(String requestID, String message) {
        hideProgressBar();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        hideProgressBar();
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        hideProgressBar();
        if (requestID==REQUEST_EDIT_WORKLOG){
            Log.d("machineWorklog", requestID + " response Json : " + response);
            Gson gson = new Gson();
            CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
            mContext.ShowEditedMeterReading(requestID, response, commonResponse.getStatus());

        }else {
            Log.d("machineWorklog", requestID + " response Json : " + response);
        /*AppConfigResponseModel appConfigResponseModel
                = new Gson().fromJson(response, AppConfigResponseModel.class);*/
            Gson gson = new Gson();
            CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
            mContext.ShowReceivedWorkList(requestID, response, commonResponse.getStatus());
        }
    }

    @Override
    public void showProgressBar() {
        mContext.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        mContext.hideProgressBar();
    }

    @Override
    public void closeCurrentActivity() {

    }
}
