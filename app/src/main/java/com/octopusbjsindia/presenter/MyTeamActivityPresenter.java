package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.MyTeamActivity;

public class MyTeamActivityPresenter implements APIPresenterListener {

    MyTeamActivity mContext;

    public MyTeamActivityPresenter(MyTeamActivity mContext) {
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


    public void getTeamList() {
        mContext.showProgressBar();
        final String getRoleAccessUrl = BuildConfig.BASE_URL + Urls.Matrimony.MATRIMONY_SUBORDINATE_USERS;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("MATRIMONY_SUBORDINATE_USERS", getRoleAccessUrl);
    }
}
