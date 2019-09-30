package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.view.fragments.SujalamSufalamFragment;

import java.lang.ref.WeakReference;

public class SujalamSuphalamFragmentPresenter implements APIPresenterListener {

    private WeakReference<SujalamSufalamFragment> fragmentWeakReference;
    private final String TAG = SujalamSuphalamFragmentPresenter.class.getName();
    public static final String GET_STRUCTURE_ANALYTICS ="getStructureAnalytics";
    public static final String GET_MACHINE_ANALYTICS = "getMachineAnalytics";

    public SujalamSuphalamFragmentPresenter(SujalamSufalamFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getAnalyticsData(String type){
        String getSSAnalyticsUrl = "";
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(type.equals(GET_STRUCTURE_ANALYTICS)) {
            getSSAnalyticsUrl = BuildConfig.BASE_URL
                    + String.format(Urls.SSModule.GET_SS_STRUCTURE_ANALYTICS);
            Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getSSAnalyticsUrl);
            requestCall.getDataApiCall(GET_STRUCTURE_ANALYTICS, getSSAnalyticsUrl);
        }
        if(type.equals(GET_MACHINE_ANALYTICS)){
            getSSAnalyticsUrl = BuildConfig.BASE_URL
                    + String.format(Urls.SSModule.GET_SS_MACHINE_ANALYTICS);
            Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getSSAnalyticsUrl);
            requestCall.getDataApiCall(GET_MACHINE_ANALYTICS, getSSAnalyticsUrl);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID,error);
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
                SSAnalyticsAPIResponse ssAnalyticsData = PlatformGson.getPlatformGsonInstance().fromJson(response, SSAnalyticsAPIResponse.class);
                if (ssAnalyticsData.getCode() == 200) {
                    if (requestID.equalsIgnoreCase(SujalamSuphalamFragmentPresenter.GET_STRUCTURE_ANALYTICS)) {
                        fragmentWeakReference.get().populateAnalyticsData(requestID, ssAnalyticsData);
                    } else if (requestID.equalsIgnoreCase(SujalamSuphalamFragmentPresenter.GET_MACHINE_ANALYTICS)) {
                        fragmentWeakReference.get().populateAnalyticsData(requestID, ssAnalyticsData);
                    }
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
