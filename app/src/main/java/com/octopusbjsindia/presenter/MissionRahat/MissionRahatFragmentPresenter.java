package com.octopusbjsindia.presenter.MissionRahat;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.presenter.HomeActivityPresenter;
import com.octopusbjsindia.presenter.SujalamSuphalamFragmentPresenter;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.MissionRahat.MissionRahatFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MissionRahatFragmentPresenter implements APIPresenterListener {

    private WeakReference<MissionRahatFragment> fragmentWeakReference;
    private final String TAG = SujalamSuphalamFragmentPresenter.class.getName();
    public static final String GET_MR_ANALYTICS = "getMRAnalytics";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_DISTRICT_ID = "district_id";
    public static final String GET_ROLE_ACCESS ="getRoleAccesss";

    public MissionRahatFragmentPresenter(MissionRahatFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getMRAnalyticsData(String stateIds, String districtIds) {

        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_STATE_ID, stateIds);
        map.put(KEY_DISTRICT_ID, districtIds);

        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);

        String getMRAnalyticsUrl = BuildConfig.BASE_URL
                + String.format(Urls.MissionRahat.GET_MR_ANALYTICS);
        Log.d(TAG, "getMRAnalyticsUrl: url" + getMRAnalyticsUrl);
        requestCall.postDataApiCall(GET_MR_ANALYTICS, new JSONObject(map).toString(), getMRAnalyticsUrl);
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
                if (requestID.equalsIgnoreCase(GET_ROLE_ACCESS)) {
                    RoleAccessAPIResponse roleAccessAPIResponse = new Gson().fromJson(response, RoleAccessAPIResponse.class);
                    if (roleAccessAPIResponse.getStatus() == 200 && roleAccessAPIResponse.getData() != null) {
                        Util.saveRoleAccessObjectInPref(response);
                        fragmentWeakReference.get().refreshAccess();
                    }
                }
                if (requestID.equalsIgnoreCase(GET_MR_ANALYTICS)) {
                    SSAnalyticsAPIResponse mrAnalyticsData = PlatformGson.getPlatformGsonInstance().fromJson(response, SSAnalyticsAPIResponse.class);
                    if (mrAnalyticsData.getCode() == 200) {
                        fragmentWeakReference.get().populateAnalyticsData(mrAnalyticsData);
                    } else if (mrAnalyticsData.getCode() == 400) {
                        fragmentWeakReference.get().emptyResponse();
                    }
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }

    public void getRoleAccess() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.Home.GET_ROLE_ACCESS);
        Log.d(TAG, "getRoleAccessUrl: url" + getRoleAccessUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_ROLE_ACCESS, getRoleAccessUrl);
    }
}
