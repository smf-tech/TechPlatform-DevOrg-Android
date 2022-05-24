package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.Matrimony.AllMatrimonyMeetsAPIResponse;
import com.octopusbjsindia.models.Matrimony.NewRegisteredUserResponse;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.MatrimonyFragment;

import java.util.HashMap;

public class MatrimonyFragmentPresenter implements APIPresenterListener {

    private static final String GET_MATRIMONY_MEETS ="getMatrimonyMeets";
    private static final String GET_NEW_USER = "getNewUsers";
    private static final String GET_UNVERIFED_USER = "getUnVerifiedUsers";
    private static final String KEY_COUNTRY_ID = "country_id";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_CITY_ID = "city_id";
    private MatrimonyFragment mContext;
    
    public MatrimonyFragmentPresenter(MatrimonyFragment matrimonyFragment) {
        mContext = matrimonyFragment;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContext.onFailureListener(requestID,message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContext.onFailureListener(requestID,error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContext.hideProgressBar();
        try {
            if (response != null) {
                if(requestID.equalsIgnoreCase(GET_MATRIMONY_MEETS)){
                    AllMatrimonyMeetsAPIResponse allMeets = PlatformGson.getPlatformGsonInstance().fromJson(response,
                            AllMatrimonyMeetsAPIResponse.class);
                    if(allMeets.getStatus() == 1000){
                        Util.logOutUser(mContext.getActivity());
                    }
                    if(allMeets.getStatus() == 200){
                        //earliestMeetId will never be null. If no value available at backend it will send empty string.
                        mContext.setMatrimonyMeets(allMeets.getData(), allMeets.getEarliestMeetId());
                    } else {
                        mContext.onFailureListener(GET_MATRIMONY_MEETS,allMeets.getMessage());
                    }
                } else if(requestID.equals(GET_NEW_USER)){
                    NewRegisteredUserResponse newUserResponse = new Gson().fromJson(response, NewRegisteredUserResponse.class);
                    mContext.onNewProfileFetched(requestID, newUserResponse);
                    //mContext.onUnverifiedProfileFetched(requestID, newUserResponse);// temprery
                }else if(requestID.equals(GET_UNVERIFED_USER)){
                    NewRegisteredUserResponse newUserResponse = new Gson().fromJson(response, NewRegisteredUserResponse.class);
                    mContext.onUnverifiedProfileFetched(requestID, newUserResponse);
                }
            }
        } catch (Exception e) {
            mContext.onFailureListener(requestID,e.getMessage());
        }
    }

    public void getMatrimonyMeets() {
        Gson gson = new GsonBuilder().create();
        String countries = "";
        if(Util.getUserObjectFromPref().getUserLocation().getCountryId()!=null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCountryId().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getCountryId().get(i);
                if (i == 0) {
                    countries = j.getId();
                } else {
                    countries = countries + "," + j.getId();
                }
            }
        }
        String states = "";
        if(Util.getUserObjectFromPref().getUserLocation().getStateId()!=null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                if (i == 0) {
                    states = j.getId();
                } else {
                    states = states + "," + j.getId();
                }
            }
        }
        String cities = "";
        if(Util.getUserObjectFromPref().getUserLocation().getCityIds()!=null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCityIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getCityIds().get(i);
                if (i == 0) {
                    cities = j.getId();
                } else {
                    cities = cities + "," + j.getId();
                }
            }
        }

        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_COUNTRY_ID, countries);
        map.put(KEY_STATE_ID, states);
        map.put(KEY_CITY_ID, cities);

        String paramjson = gson.toJson(map);
        
        final String getMatrimonyMeetsUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MATRIMONY_MEETS);
        Log.d("getMatrimonyMeetsUrl: ", getMatrimonyMeetsUrl);
        mContext.showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MATRIMONY_MEETS, paramjson, getMatrimonyMeetsUrl);
    }

    public void getRecentelyJoinedUsers() {
        mContext.showProgressBar();
        final String getNewUserMatrimonyUrl = BuildConfig.BASE_URL + String.format(Urls.Matrimony.MATRIMONY_RECENTELY_JOINED_USERS);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_NEW_USER, getNewUserMatrimonyUrl);
    }

    public void getUnVerifiedUsers() {
        mContext.showProgressBar();
        final String getUnverifiedUersUrl = BuildConfig.BASE_URL + String.format(Urls.Matrimony.MATRIMONY_VERIFICATION_PENDING_USERS);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_UNVERIFED_USER, getUnverifiedUersUrl);
    }
}
