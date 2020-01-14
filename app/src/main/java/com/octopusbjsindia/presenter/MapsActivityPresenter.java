package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.StructureBoundaryData;
import com.octopusbjsindia.models.stories.CommentResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.MapsActivity;

public class MapsActivityPresenter implements APIPresenterListener {

    MapsActivity activity;

    public MapsActivityPresenter(MapsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        activity.hideProgressBar();
        activity.onFailureListener(requestID,message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        activity.hideProgressBar();
        activity.onErrorListener(requestID,error);;
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        activity.hideProgressBar();
        activity.onSuccessListener(requestID,response);
    }

    public void submitBoundatys(StructureBoundaryData requestData){
        activity.showProgressBar();
        Gson gson = new GsonBuilder().create();
        String paramjson =gson.toJson(requestData);

       final String url = BuildConfig.BASE_URL
                + Urls.SSModule.STRUCTURE_BOUNDARY;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("STRUCTURE_BOUNDARY",paramjson,url);
    }

}
