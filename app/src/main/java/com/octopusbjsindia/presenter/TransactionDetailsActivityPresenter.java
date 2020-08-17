package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.TransactionDetailsActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class TransactionDetailsActivityPresenter implements APIPresenterListener {

    TransactionDetailsActivity mContext;

    public TransactionDetailsActivityPresenter(TransactionDetailsActivity mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContext.hideProgressBar();
        mContext.onFailureListener(requestID,message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContext.hideProgressBar();
        mContext.onFailureListener(requestID,error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContext.hideProgressBar();
        mContext.onSuccessListener(requestID,response);
    }


    public void getTransactionDetails(String meetId) {
        mContext.showProgressBar();

        HashMap<String, String> map = new HashMap<>();
//        map.put("meet_id", "5f2956d64c7ce7386f090c42");
        map.put("meet_id",meetId);
        String paramJson = new JSONObject(map).toString();

        final String getRoleAccessUrl = BuildConfig.BASE_URL + Urls.Matrimony.MEET_TRANSECTION_DETAILS;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("MEET_TRANSECTION_DETAILS", paramJson, getRoleAccessUrl);
    }
}
