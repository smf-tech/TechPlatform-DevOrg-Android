package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SELTrainingVideoActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class SELTrainingVideoActivityPresenter implements APIPresenterListener {

    private final String TAG = SELTrainingVideoActivityPresenter.class.getName();
    private WeakReference<SELTrainingVideoActivity> mContext;
    public static final String SEND_VIDEO_STATUS = "sendVideoStatus";

    public SELTrainingVideoActivityPresenter(SELTrainingVideoActivity activity) {
        mContext = new WeakReference<>(activity);
    }

    public void clearData() {
        mContext = null;
    }

    public void sendVideoStatus(String videoId, String status, int videoSeenTime) {
        mContext.get().showProgressBar();
        Gson gson = new GsonBuilder().create();
        Map<String, Object> map = new HashMap<>();
        map.put("videoId", videoId);
        map.put("status", status);
        map.put("videoSeenTime", videoSeenTime);
        String params = gson.toJson(map);

        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        final String sendVideoStatusUrl = BuildConfig.BASE_URL + String.format(Urls.SEL.SEND_VIDEO_STATUS);
        Log.d(TAG, "sendVideoStatusUrl: " + sendVideoStatusUrl);
        requestCall.postDataApiCall(SEND_VIDEO_STATUS, params, sendVideoStatusUrl);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContext.get().hideProgressBar();
        mContext.get().onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContext.get().hideProgressBar();
        mContext.get().onFailureListener(requestID, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContext.get().hideProgressBar();
        Gson gson = new Gson();
        CommonResponse responseData = gson.fromJson(response, CommonResponse.class);
        if (responseData.getStatus() == 1000) {
            Util.logOutUser(mContext.get());
            mContext.get().onFailureListener(requestID, responseData.getMessage());
            mContext.get().finish();
        } else if (responseData.getStatus() == 200) {
            mContext.get().onSuccessListener(requestID, responseData.getMessage());
        } else {
            mContext.get().onFailureListener(requestID, responseData.getMessage());
        }
    }
}
