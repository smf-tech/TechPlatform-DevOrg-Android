package com.octopusbjsindia.presenter;

import android.graphics.Bitmap;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.StructurePripretionData;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.StructurePreparationActivity;

import java.util.HashMap;

public class StructurePripretionsActivityPresenter implements APIPresenterListener {

    StructurePreparationActivity activity;

    public StructurePripretionsActivityPresenter(StructurePreparationActivity activity) {
        this.activity=activity;
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
        MasterDataResponse masterDataResponse = new Gson().fromJson(response, MasterDataResponse.class);
//        activity.success(masterDataResponse);
    }

    public void submitPripretion(StructurePripretionData requestData, HashMap<String, Bitmap> imageHashmap){
        activity.showProgressBar();
        Gson gson = new GsonBuilder().create();
        String paramjson =gson.toJson(requestData);

        final String checkProfileUrl = BuildConfig.BASE_URL
                + Urls.SSModule.STRUCTURE_PREPARATION;//, mobilenumber,meetId);
        activity.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
//        drawable = new BitmapDrawable(activity.getResources(), imageHashmap.get(key));
        requestCall.multipartApiCall("STRUCTURE_PREPARATION",paramjson,imageHashmap,
                activity.getResources(),checkProfileUrl);
    }

}
