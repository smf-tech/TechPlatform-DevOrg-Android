package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.models.Matrimony.MatrimonyRoleUsersAPIResponse;
import com.platform.models.leaves.LeaveData;
import com.platform.request.MatrimonyMeetRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.view.fragments.CreateMeetSecondFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class CreateMeetSecondFragmentPresenter implements APIPresenterListener {
    private WeakReference<CreateMeetSecondFragment> fragmentWeakReference;

    public static final String GET_MEET_USERS_LIST ="getMeetUsersList";
    public static final String GET_MEET_REFERENCES_LIST ="getMeetReferncesList";
    public static final String SUBMIT_MEET ="submitMeet";
//    private static final String KEY_STATE_ID = "state_id";
//    private static final String KEY_CITY_ID = "city_id";
//    private static final String KEY_CHAPTER_ID = "chapter_id";
//    private static final String KEY_TYPE_ID = "type";

    private final String TAG = CreateMeetFirstFragmentPresenter.class.getName();

    public CreateMeetSecondFragmentPresenter(CreateMeetSecondFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getMeetReferencesList(){
        final String applyLeaveUrl = BuildConfig.BASE_URL + String.format(Urls.Matrimony.MEET_REFERENCES_LIST);
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_MEET_REFERENCES_LIST, applyLeaveUrl);
    }

    public void getMatrimonyUsersList(){
        Util.getUserObjectFromPref().getProjectIds().get(0).getId();
        final String getMatrimonyUsersUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MATRIMONY_USERS_LIST, "5d4129345dda7642c4094b62");
                //Util.getUserObjectFromPref().getProjectIds().get(0).getId());
        Log.d(TAG, "getMatrimonyUsersListUrl: url" + getMatrimonyUsersUrl);
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_MEET_USERS_LIST, getMatrimonyUsersUrl);
    }

    public void submitMeet(MatrimonyMeet matrimonyMeet){
        Gson gson = new GsonBuilder().create();
        String paramjson = gson.toJson(matrimonyMeet);
        final String getMatrimonyMeetTypesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.SUBMIT_MEET);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMatrimonyMeetTypesUrl);
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(SUBMIT_MEET, paramjson, getMatrimonyMeetTypesUrl);
    }

//    public JsonObject getMeetOrganizersJson(String stateId, String cityId, String chapterId, String type){
//        //String validType=type.replace(" ","");
//        HashMap<String,String> map=new HashMap<>();
//        map.put(KEY_STATE_ID, stateId);
//        map.put(KEY_CITY_ID, cityId);
//        map.put(KEY_CHAPTER_ID, chapterId);
//        map.put(KEY_TYPE_ID, type);
//
//        JsonObject requestObject = new JsonObject();
//
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            requestObject.addProperty(key, value);
//        }
//
//        return requestObject;
//
//    }

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
                    MatrimonyRoleUsersAPIResponse matrimonyRoleUsers = PlatformGson.getPlatformGsonInstance().fromJson(response, MatrimonyRoleUsersAPIResponse.class);
                    fragmentWeakReference.get().setMatrimonyUsers(matrimonyRoleUsers.getData());
                }
            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
