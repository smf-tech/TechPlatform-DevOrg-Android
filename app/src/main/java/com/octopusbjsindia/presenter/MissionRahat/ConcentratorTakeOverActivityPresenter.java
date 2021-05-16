package com.octopusbjsindia.presenter.MissionRahat;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.MissionRahat.SearchListResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.ConcentratorTakeOverActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ConcentratorTakeOverActivityPresenter implements APIPresenterListener {
    private WeakReference<ConcentratorTakeOverActivity> mContext;

    public static final String KEY_CREATE_MACHINE_MASTER_DATA = "getMasterData";

    public ConcentratorTakeOverActivityPresenter(ConcentratorTakeOverActivity context) {
        mContext = new WeakReference<ConcentratorTakeOverActivity>(context);
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
                    if (requestID.equalsIgnoreCase("TAKEOVER_REQUEST")) {

                        SearchListResponse responseData = new Gson().fromJson(response, SearchListResponse.class);
                        //mContext.get().setHostpitalList("TAKEOVER_REQUEST", responseData.getData());
                        mContext.get().onSuccessListener(requestID, response);
                    }
                    else if (requestID.equalsIgnoreCase(KEY_CREATE_MACHINE_MASTER_DATA)) {

                        // Please note, here we have used same model class for data parsing as SS master data model class.
                        // So, dont do any change in model class for Mission Rahat project as it can affect to SS.
                        // If need change, create another model class for Mission Rahat.
                        MasterDataResponse masterDataResponse = new Gson().fromJson(response, MasterDataResponse.class);
                        {
                            mContext.get().setMasterData(masterDataResponse);
                        }

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

    //take over hand over request
    public void submitRequest(HashMap request) {
        mContext.get().showProgressBar();
        Gson gson = new GsonBuilder().create();
        String params = gson.toJson(request);
        final String takeOverUrl = BuildConfig.BASE_URL + Urls.MissionRahat.MACHINE_HANDTAKEOVER_FORM;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("TAKEOVER_REQUEST", params, takeOverUrl);
    }
    public void getMasterData() {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        final String Url = BuildConfig.BASE_URL + Urls.MissionRahat.GET_CREATE_MACHINE_MASTER_DATA;
        requestCall.getDataApiCall(KEY_CREATE_MACHINE_MASTER_DATA, Url);
    }
}
