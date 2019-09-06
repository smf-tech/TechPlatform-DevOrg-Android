package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.BuildConfig;
import com.platform.R;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.Matrimony.AllMatrimonyMeetsAPIResponse;
import com.platform.models.events.CommonResponse;
import com.platform.request.MatrimonyMeetRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.view.fragments.MatrimonyFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MatrimonyFragmentPresenter implements APIPresenterListener {

    private WeakReference<MatrimonyFragment> fragmentWeakReference;
    public static final String GET_MATRIMONY_MEETS ="getMatrimonyMeets";
    public static final String PUBLISH_SAVED_MEET ="publishSavedMeet";
    private final String TAG = MatrimonyFragmentPresenter.class.getName();
    private static final String KEY_COUNTRY_ID = "country_id";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_CITY_ID = "city_id";
    private static final String KEY_MEET_ID = "_id";
    private static final String KEY_IS_PUBLISH = "is_published";

    public MatrimonyFragmentPresenter(MatrimonyFragment mFragment){
        fragmentWeakReference = new WeakReference<>(mFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getMatrimonyMeets(){
        Gson gson = new GsonBuilder().create();
        String paramjson = gson.toJson(getMeetOrganizersJson(
                "5d68c8645dda765a5f17f9d3", "5c66989ec7982d31cc6b86c3", "5d6640745dda763fa96a3416"));
        final String getMatrimonyMeetsUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MATRIMONY_MEETS);
        Log.d(TAG, "getMatrimonyMeetsUrl: url" + getMatrimonyMeetsUrl);
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MATRIMONY_MEETS, paramjson, getMatrimonyMeetsUrl);
    }

    public void publishSavedMeet(String meetId, boolean isPublish) {
        Gson gson = new GsonBuilder().create();
        String paramJson = gson.toJson(getMeetPublishJson(meetId, isPublish));
        final String publishSavedMeetUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.PUBLISH_SAVED_MEET);
        Log.d(TAG, "getMatrimonyMeetsUrl: url" + publishSavedMeetUrl);
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(PUBLISH_SAVED_MEET, paramJson, publishSavedMeetUrl);
    }

    public JsonObject getMeetOrganizersJson(String countryId, String stateId, String cityId){
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

    public JsonObject getMeetPublishJson(String meetId, boolean isPublish){
        HashMap<String,Object> map=new HashMap<>();
        map.put(KEY_MEET_ID, meetId);
        map.put(KEY_IS_PUBLISH, isPublish);

        JsonObject requestObject = new JsonObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            boolean value = (boolean) entry.getValue();
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
                if(requestID.equalsIgnoreCase(MatrimonyFragmentPresenter.GET_MATRIMONY_MEETS)){
                    AllMatrimonyMeetsAPIResponse allMeets = PlatformGson.getPlatformGsonInstance().fromJson(response, AllMatrimonyMeetsAPIResponse.class);
                    fragmentWeakReference.get().setMatrimonyMeets(allMeets.getData());
                }
                if(requestID.equalsIgnoreCase(MatrimonyFragmentPresenter.PUBLISH_SAVED_MEET)){
                    try {
                        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                        fragmentWeakReference.get().showResponse(responseOBJ.getMessage());
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
