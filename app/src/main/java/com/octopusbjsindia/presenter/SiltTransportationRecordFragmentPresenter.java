package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.SujalamSuphalam.SiltTransportApiResponse;
import com.octopusbjsindia.models.SujalamSuphalam.SiltTransportRecord;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.SiltTransportationRecordFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SiltTransportationRecordFragmentPresenter implements APIPresenterListener {
    private WeakReference<SiltTransportationRecordFragment> fragmentWeakReference;
    private final String TAG = SiltTransportationRecordFragmentPresenter.class.getName();
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    public static final String GET_STATE = "getState";
    public static final String GET_DISTRICTS = "getDistricts";
    public static final String GET_TALUKAS = "getTalukas";
    public static final String GET_VILLAGES = "getVillages";
    public static final String KEY_MOBILE = "mobile";
    public static final String GET_B_INFO = "getBeneficiaryInfo";

    public SiltTransportationRecordFragmentPresenter(SiltTransportationRecordFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        fragmentWeakReference.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.postDataApiCall(GET_STATE, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(GET_DISTRICTS, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(GET_TALUKAS, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
            requestCall.postDataApiCall(GET_VILLAGES, new JSONObject(map).toString(), getLocationUrl);
        }
    }

    public void getBeneficiaryDetails(String mobileNumber) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_MOBILE, mobileNumber);

        fragmentWeakReference.get().showProgressBar();
        final String getBeneficiaryInfoUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_BENEFICIARY_INFO);
        Log.d(TAG, "getBeneficiaryInfoUrl: url" + getBeneficiaryInfoUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_B_INFO, new JSONObject(map).toString(), getBeneficiaryInfoUrl);
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
                if (requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_STATE) ||
                        requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_DISTRICTS) ||
                        requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_TALUKAS) ||
                        requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_VILLAGES)) {
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);
                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        if (requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_STATE)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL);
                        } else if (requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_DISTRICTS)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        } else if (requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_TALUKAS)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else if (requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_VILLAGES)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    }
                } else if(requestID.equalsIgnoreCase(SiltTransportationRecordFragmentPresenter.GET_B_INFO)) {
                    SiltTransportApiResponse record
                            = new Gson().fromJson(response, SiltTransportApiResponse.class);
                    if(record.getStatus() == 200) {
                        fragmentWeakReference.get().updateBeneficiaryDetails(record.getData());
                    } else {
                        fragmentWeakReference.get().onFailureListener(SiltTransportationRecordFragmentPresenter.GET_B_INFO,
                                record.getMessage());
                    }
                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
