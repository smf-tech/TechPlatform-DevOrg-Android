package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.MasterDataResponse;
import com.platform.models.profile.JurisdictionLevelResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Urls;
import com.platform.view.activities.CommunityMobilizationActivity;

public class CommunityMobilizationActivityPresenter implements APIPresenterListener {

    final String GET_DISTRICT = "getDistrict";
    final String GET_TALUKA = "getTaluka";
    final String GET_VILLAGE = "getvillage";
    
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
               if(requestID.equalsIgnoreCase(GET_DISTRICT) ||
                        requestID.equalsIgnoreCase(GET_TALUKA) ||
                        requestID.equalsIgnoreCase(GET_VILLAGE)){
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);

                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        if(requestID.equalsIgnoreCase(GET_DISTRICT)) {
                            mContext.showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        }else if(requestID.equalsIgnoreCase(GET_TALUKA)) {
                            mContext.showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else if(requestID.equalsIgnoreCase(GET_VILLAGE)) {
                            mContext.showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    }
                }
            }
        }catch (Exception e) {
            mContext.onFailureListener(requestID,e.getMessage());
        }
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);

        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);
        Log.d("getLocationUrl", " url: " + getLocationUrl);
        mContext.showProgressBar();

        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)){
            requestCall.getDataApiCall(GET_DISTRICT, getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.getDataApiCall(GET_TALUKA, getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
            requestCall.getDataApiCall(GET_VILLAGE, getLocationUrl);
        }
    }


}
