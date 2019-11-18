package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.Matrimony.MeetTypesAPIResponse;
import com.platform.models.profile.JurisdictionLevelResponse;
import com.platform.request.APIRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.view.fragments.CreateMeetFirstFragment;

import java.lang.ref.WeakReference;

public class CreateMeetFirstFragmentPresenter implements APIPresenterListener {

    private WeakReference<CreateMeetFirstFragment> fragmentWeakReference;

    public static final String GET_MATRIMONY_MEET_TYPES ="getMatrimonyMeetTypes";
    public static final String GET_COUNTRIES = "getCountries";
    public static final String GET_STATES = "getStates";
    public static final String GET_CITIES = "getCities";
    private final String TAG = CreateMeetFirstFragmentPresenter.class.getName();

    public CreateMeetFirstFragmentPresenter(CreateMeetFirstFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getMeetTypes(){
        final String getMatrimonyMeetTypesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MATRIMONY_MEET_TYPES);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMatrimonyMeetTypesUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_MATRIMONY_MEET_TYPES, getMatrimonyMeetTypesUrl);
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);

        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA,
                orgId,
                jurisdictionTypeId, levelName);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        fragmentWeakReference.get().showProgressBar();

        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.COUNTRY_LEVEL)) {
            //requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName, GET_STATES);
            requestCall.getDataApiCall(GET_COUNTRIES, getLocationUrl);

        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            //requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName, GET_STATES);
            requestCall.getDataApiCall(GET_STATES, getLocationUrl);

        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.CITY_LEVEL)){
            requestCall.getDataApiCall(GET_CITIES, getLocationUrl);
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
                if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_MATRIMONY_MEET_TYPES)){
                    MeetTypesAPIResponse meetTypes = PlatformGson.getPlatformGsonInstance().fromJson(response, MeetTypesAPIResponse.class);
                    fragmentWeakReference.get().setMatrimonyMeetTypes(meetTypes.getData());
                }
                if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_COUNTRIES) ||
                        requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_STATES) ||
                        requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_CITIES)){
                    JurisdictionLevelResponse jurisdictionLevelResponse
                            = new Gson().fromJson(response, JurisdictionLevelResponse.class);

                    if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                            && !jurisdictionLevelResponse.getData().isEmpty()
                            && jurisdictionLevelResponse.getData().size() > 0) {
                        if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_COUNTRIES)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.COUNTRY_LEVEL);
                        }else if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_STATES)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL);
                        } else if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_CITIES)) {
                            fragmentWeakReference.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(),
                                    Constants.JurisdictionLevelName.CITY_LEVEL);
                        }
                    }
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
