package com.octopusbjsindia.presenter.MissionRahat;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.MissionRahat.RequirementsListActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class RequirementsListActivityPresenter implements APIPresenterListener {
    private WeakReference<RequirementsListActivity> mContext;

    public RequirementsListActivityPresenter(RequirementsListActivity context) {
        mContext = new WeakReference<RequirementsListActivity>(context);
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
        mContext.get().onSuccessListener(requestID,response);

    }

    public void getRequirementsList(String Url) {
        mContext.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("RequirementsList", Url);
    }

    public void getMailMOU(HashMap<String, String> map) {
        mContext.get().showProgressBar();
        String url = BuildConfig.BASE_URL + Urls.MissionRahat.MOU_ON_MAIL;
        Gson gson = new GsonBuilder().create();
        String params = gson.toJson(map);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("MOU_ON_MAIL", params, url);
    }
}
