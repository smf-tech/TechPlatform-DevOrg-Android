package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.formComponents.LocationFragment;

import org.json.JSONObject;

import java.util.HashMap;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class LocationFragmentPresenter implements APIPresenterListener {

    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";

    LocationFragment mContext;

    public LocationFragmentPresenter(LocationFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        JurisdictionLevelResponse jurisdictionLevelResponse
                = new Gson().fromJson(response, JurisdictionLevelResponse.class);
        if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                && !jurisdictionLevelResponse.getData().isEmpty()
                && jurisdictionLevelResponse.getData().size() > 0) {
            mContext.showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                    requestID);
        }
    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        mContext.showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        mContext.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(levelName, new JSONObject(map).toString(), getLocationUrl);
    }

    public void getAllLocationData(String selectedId, String jurisdictionTypeId, String levelName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        mContext.showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ALL_LOCATION_DATA);
        mContext.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("AllLocation", new JSONObject(map).toString(), getLocationUrl);
    }
}
