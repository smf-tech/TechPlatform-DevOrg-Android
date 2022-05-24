package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyMeet;
import com.octopusbjsindia.models.Matrimony.SubordinateResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.fragments.CreateMeetSecondFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class CreateMeetSecondFragmentPresenter implements APIPresenterListener {
    private WeakReference<CreateMeetSecondFragment> fragmentWeakReference;

    public static final String GET_MEET_USERS_LIST ="getMeetUsersList";
    public static final String SUBMIT_MEET ="submitMeet";
    private static final String KEY_PROJECT_ID = "project_id";
    private static final String KEY_COUNTRY_ID = "country_id";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_CITY_ID = "city_id";

    private final String TAG = CreateMeetFirstFragmentPresenter.class.getName();

    public CreateMeetSecondFragmentPresenter(CreateMeetSecondFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

//    public void getMatrimonyUsersList(String countryId, String stateId, String cityId){
//        Gson gson = new GsonBuilder().create();
//        String paramjson = gson.toJson(getMeetOrganizersJson(Util.getUserObjectFromPref().getProjectIds().get(0).getId(),
//                countryId, stateId, cityId));
//
//        final String getMatrimonyUsersUrl = BuildConfig.BASE_URL
//                + String.format(Urls.Matrimony.MATRIMONY_USERS_LIST);
//        Log.d(TAG, "getMatrimonyUsersListUrl: url" + getMatrimonyUsersUrl);
//        fragmentWeakReference.get().showProgressBar();
//        APIRequestCall requestCall = new APIRequestCall();
//        requestCall.setApiPresenterListener(this);
//        requestCall.postDataApiCall(GET_MEET_USERS_LIST, paramjson, getMatrimonyUsersUrl);
//    }

    public void getMatrimonySubordinatesList() {
        Gson gson = new GsonBuilder().create();

        final String getMatrimonyUsersUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MATRIMONY_SUBORDINATE_USERS);
        Log.d(TAG, "getMatrimonySubordinatesListUrl: url" + getMatrimonyUsersUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_MEET_USERS_LIST, getMatrimonyUsersUrl);
    }

    public void submitMeet(MatrimonyMeet matrimonyMeet){
        Gson gson = new GsonBuilder().create();
        String paramjson = gson.toJson(matrimonyMeet);
        final String getMatrimonyMeetTypesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.SUBMIT_MEET);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMatrimonyMeetTypesUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SUBMIT_MEET, paramjson, getMatrimonyMeetTypesUrl);
    }

    public JsonObject getMeetOrganizersJson(String projectId, String countryId, String stateId, String cityId){
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_PROJECT_ID, projectId);
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
                if(requestID.equalsIgnoreCase(CreateMeetSecondFragmentPresenter.GET_MEET_USERS_LIST)){
                    SubordinateResponse subordinateResponse = new Gson().fromJson(response, SubordinateResponse.class);
                    if (subordinateResponse.getStatus() == 200) {
                        fragmentWeakReference.get().setMatrimonyUsers(subordinateResponse.getData());
                    } else {
                        fragmentWeakReference.get().showDataMessage(subordinateResponse.getMessage());
                    }

                } else if (requestID.equalsIgnoreCase(CreateMeetSecondFragmentPresenter.SUBMIT_MEET)) {
                    try {
                        fragmentWeakReference.get().onSuccessListener(requestID, response);
                    } catch (Exception e) {
                        Log.e("TAG", "Exception");
                    }
                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
