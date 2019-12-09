package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.CatchmentVillagesResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.CommunityMobilizationActivity;

import java.util.HashMap;
import java.util.Map;

public class CommunityMobilizationActivityPresenter implements APIPresenterListener {

    final String GET_DISTRICT = "getDistrict";
    final String GET_TALUKA = "getTaluka";
    final String GET_CATACHMENT_VILLAGE = "getCatchmentvillage";
    
    CommunityMobilizationActivity mContext;

    public CommunityMobilizationActivityPresenter(CommunityMobilizationActivity activity) {
        this.mContext = activity;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (mContext != null && mContext != null) {
            mContext.hideProgressBar();
            mContext.onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (mContext != null && mContext != null) {
            mContext.hideProgressBar();
            if (error != null) {
                mContext.onErrorListener(requestID,error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (mContext == null) {
            return;
        }
        mContext.hideProgressBar();
        try {
            if (response != null) {
               if(requestID.equalsIgnoreCase(GET_CATACHMENT_VILLAGE)){
                   CatchmentVillagesResponse response1 = new Gson().fromJson(response, CatchmentVillagesResponse.class);
                   if(response1.getStatus()==200){
                       mContext.showCattachmentVileges(response1.getData());
                   } else {
                       mContext.onFailureListener(GET_CATACHMENT_VILLAGE,response1.getMessage());
                   }

                }
            }
        }catch (Exception e) {
            mContext.onFailureListener(requestID,e.getMessage());
        }
    }

    public void getCatchmentVillage(String structId) {
        Gson gson = new GsonBuilder().create();

        Map<String,String> map = new HashMap<>();
        map.put("structure_id",structId);
        String params = gson.toJson(map);

        final String url = BuildConfig.BASE_URL + Urls.SSModule.CATCHMENT_VILLAGES;
        Log.d(GET_CATACHMENT_VILLAGE, " url: " + url);
        mContext.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_CATACHMENT_VILLAGE, params, url);
    }


}
