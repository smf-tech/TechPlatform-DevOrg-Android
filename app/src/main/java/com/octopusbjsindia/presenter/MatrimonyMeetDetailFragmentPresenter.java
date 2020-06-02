package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.Matrimony.AllMatrimonyMeetsAPIResponse;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.MatrimonyMeetDetailFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MatrimonyMeetDetailFragmentPresenter implements APIPresenterListener {

    private WeakReference<MatrimonyMeetDetailFragment> fragmentWeakReference;
    public static final String GET_MATRIMONY_MEETS ="getMatrimonyMeets";
    public static final String PUBLISH_SAVED_MEET ="publishSavedMeet";

    private final String TAG = MatrimonyMeetDetailFragmentPresenter.class.getName();
    private static final String KEY_COUNTRY_ID = "country_id";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_CITY_ID = "city_id";
    private static final String KEY_MEET_ID = "_id";
    private static final String KEY_IS_PUBLISH = "is_published";

    public MatrimonyMeetDetailFragmentPresenter(MatrimonyMeetDetailFragment mFragment){
        fragmentWeakReference = new WeakReference<>(mFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getMatrimonyMeets(){
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

        String paramjson = gson.toJson(getMeetJson(
                countries,
                states,
                cities));

        final String getMatrimonyMeetsUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MATRIMONY_MEETS);
        Log.d(TAG, "getMatrimonyMeetsUrl: url" + getMatrimonyMeetsUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MATRIMONY_MEETS, paramjson, getMatrimonyMeetsUrl);
    }

    public void publishSavedMeet(String meetId) {
        final String publishSavedMeetUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.PUBLISH_SAVED_MEET, meetId);
        Log.d(TAG, "getMatrimonyMeetsUrl: url" + publishSavedMeetUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(PUBLISH_SAVED_MEET, publishSavedMeetUrl);
    }

    public void VerifyUserProfile(String mobilenumber,String userId, String meetId) {
        Gson gson = new GsonBuilder().create();

        String paramjson =gson.toJson(getCheckProfileJson(meetId,userId,mobilenumber));

        final String checkProfileUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.REGISTER_USER_TO_MEET);//, mobilenumber,meetId);
        Log.d(TAG, "getMatrimonyMeetsUrl: url" + checkProfileUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("CHECK_USER_CREATED",paramjson,checkProfileUrl);
    }

    public JsonObject getMeetJson(String countryId, String stateId, String cityId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_COUNTRY_ID, countryId);
        map.put(KEY_STATE_ID, stateId);
        map.put(KEY_CITY_ID, cityId);

        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }

        return requestObject;

    }
    public JsonObject getCheckProfileJson(String meetId, String userId, String mobilenumber){

        HashMap<String,String> map=new HashMap<>();
        map.put("meet_id", meetId);
        map.put("user_id", userId);
        map.put("mobile", mobilenumber);

        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }
        return requestObject;
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
                if(requestID.equalsIgnoreCase(MatrimonyMeetDetailFragmentPresenter.GET_MATRIMONY_MEETS)){
                    AllMatrimonyMeetsAPIResponse allMeets = PlatformGson.getPlatformGsonInstance().fromJson(response,
                            AllMatrimonyMeetsAPIResponse.class);
                    if(allMeets.getStatus() == 1000){
                        fragmentWeakReference.get().logOutUser();
                    }
                    if(allMeets.getStatus() == 200){
                        //earliestMeetId will never be null. If no value available at backend it will send empty string.
                    fragmentWeakReference.get().setMatrimonyMeets(allMeets.getData(), allMeets.getEarliestMeetId());
                    } else {
                        fragmentWeakReference.get().showResponse(allMeets.getMessage());
                    }
                }
                if(requestID.equalsIgnoreCase(MatrimonyMeetDetailFragmentPresenter.PUBLISH_SAVED_MEET)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage(), responseOBJ.getStatus());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
                if(requestID.equalsIgnoreCase("CHECK_USER_CREATED"))
                {
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponseVerifyUser(responseOBJ.getMessage(), responseOBJ.getStatus());
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
            }
        } catch (Exception e) {
                fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
            }
    }
}
