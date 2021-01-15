package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.sel_content.VideoContentAPIResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.SELFragment;

import java.lang.ref.WeakReference;

public class SELFragmentPresenter implements APIPresenterListener {

    private WeakReference<SELFragment> fragmentWeakReference;
    private final String TAG = SELFragmentPresenter.class.getName();
    public static final String GET_SEL_CONTENT = "getSELContent";

    public SELFragmentPresenter(SELFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getSelContentData() {
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String getSELContentUrl = BuildConfig.BASE_URL
                + String.format(Urls.SEL.GET_SEL_CONTENT);
        Log.d(TAG, "getSELContentUrl: url" + getSELContentUrl);
        requestCall.getDataApiCall(GET_SEL_CONTENT, getSELContentUrl);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID, error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        try {
            if (response != null) {
                VideoContentAPIResponse videoContentAPIResponse = PlatformGson.getPlatformGsonInstance().fromJson(response, VideoContentAPIResponse.class);
                if (videoContentAPIResponse.getStatus() == 200) {
                    fragmentWeakReference.get().populateData(videoContentAPIResponse);
                } else if (videoContentAPIResponse.getStatus() == 400) {
                    //fragmentWeakReference.get().emptyResponse();
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
