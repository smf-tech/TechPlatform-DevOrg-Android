package com.octopusbjsindia.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.Matrimony.MissingInfoMasterResponse;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissingInfoActivity;

public class MissingInfoActivityPresenter implements APIPresenterListener {
    MissingInfoActivity mContext;

    public MissingInfoActivityPresenter(MissingInfoActivity mContext) {
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
        Gson gson = new Gson();
//        AllMatrimonyMeetsAPIResponse allMeets = PlatformGson.getPlatformGsonInstance().fromJson(response,
//                AllMatrimonyMeetsAPIResponse.class);

        if(requestID.equals("MASTER_DATA")) {
            MissingInfoMasterResponse master = gson.fromJson(response, MissingInfoMasterResponse.class);
            if (master.getCode() == 200) {
                mContext.onMasterFatched(master.getData());
            }
        } else if(requestID.equals("MISSING_FIELD_REQ")){
            CommonResponse common = gson.fromJson(response, CommonResponse.class);
            if(common.getStatus()==200){
                Util.showToast(common.getMessage(),mContext);
                mContext.finish();
            } else {
                Util.showToast(common.getMessage(),mContext);
            }
        }
    }

    public void getMaster() {
        mContext.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String url = BuildConfig.BASE_URL + Urls.Matrimony.MATRIMONY_FIELD_MASTER;
        requestCall.getDataApiCall("MASTER_DATA", url);
    }

    public void sentRequest(String paramjson) {
        mContext.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String url = BuildConfig.BASE_URL + Urls.Matrimony.MISSING_FIELD_REQ;
        requestCall.postDataApiCall("MISSING_FIELD_REQ", paramjson,url);
    }
}
