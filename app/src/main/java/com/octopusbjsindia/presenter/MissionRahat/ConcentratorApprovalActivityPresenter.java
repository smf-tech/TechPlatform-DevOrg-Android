package com.octopusbjsindia.presenter.MissionRahat;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.MissionRahat.RequirementDetailsResponse;
import com.octopusbjsindia.models.MissionRahat.SearchListResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.ConcentratorApprovalActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ConcentratorApprovalActivityPresenter implements APIPresenterListener {

    private WeakReference<ConcentratorApprovalActivity> mContext;

    public ConcentratorApprovalActivityPresenter(ConcentratorApprovalActivity context) {
        mContext = new WeakReference<ConcentratorApprovalActivity>(context);
    }


    @Override
    public void onFailureListener(String requestID, String message) {
        if (mContext != null && mContext.get() != null) {
            mContext.get().hideProgressBar();
            mContext.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (mContext != null && mContext.get() != null) {
            mContext.get().hideProgressBar();
            if (error != null) {
                mContext.get().onErrorListener(requestID, error);
            }
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
                CommonResponseStatusString commonResponse = new Gson().fromJson(response, CommonResponseStatusString.class);
                if (commonResponse.getCode() == 200) {
                    if (requestID.equalsIgnoreCase("REQUEST_DETAILS")) {

                        RequirementDetailsResponse responseData = new Gson().fromJson(response, RequirementDetailsResponse.class);
                        mContext.get().setData(responseData.getData());
                    } else if (requestID.equalsIgnoreCase("MACHINE_LIST")) {

                        SearchListResponse responseData = new Gson().fromJson(response, SearchListResponse.class);
                        mContext.get().setMachineList(responseData.getData());
                    } else if (requestID.equalsIgnoreCase("CONCENTRATOR_REQUEST_ACTION")) {
                        mContext.get().onSuccessListener(requestID, commonResponse.getMessage());
                    }
                } else {
                    if (commonResponse.getCode() == 1000) {
                        Util.logOutUser(mContext.get());
                    } else {
                        mContext.get().onFailureListener(requestID, commonResponse.getMessage());
                    }

                }
            }
        } catch (Exception e) {
            mContext.get().onFailureListener(requestID, e.getMessage());
        }
    }

    public void getDetails(String requestId) {
        mContext.get().showProgressBar();
        Gson gson = new GsonBuilder().create();
        HashMap request = new HashMap<String, String>();
        request.put("requirmentId", requestId);
        String params = gson.toJson(request);
        final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.CONCENTRATOR_REQUEST_DETAILS;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("REQUEST_DETAILS", params, Url);
    }

    public void submitRequest(HashMap request) {
        mContext.get().showProgressBar();
        Gson gson = new GsonBuilder().create();
        String params = gson.toJson(request);
        final String url = BuildConfig.BASE_URL + Urls.MissionRahat.CONCENTRATOR_REQUEST_ACTION;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("CONCENTRATOR_REQUEST_ACTION", params, url);
    }

    public void getMachines() {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.MACHINE_LIST;
        requestCall.getDataApiCall("MACHINE_LIST", Url);
    }
}
