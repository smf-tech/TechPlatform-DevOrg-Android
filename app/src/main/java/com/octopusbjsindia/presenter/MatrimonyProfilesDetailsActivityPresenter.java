package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.ProfileDetailRequestCallListener;
import com.octopusbjsindia.matrimonyregistration.model.ProfileDetailResponse;
import com.octopusbjsindia.models.Matrimony.AllMatrimonyMeetsAPIResponse;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.MatrimonyProfileDetailRequestCall;
import com.octopusbjsindia.request.MatrimonyProfileListRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MatrimonyProfileDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfilesDetailsActivityPresenter implements ProfileDetailRequestCallListener,
        APIPresenterListener {

    private static final String GET_MATRIMONY_MEETS ="getMatrimonyMeets";
    private static final String KEY_COUNTRY_ID = "country_id";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_CITY_ID = "city_id";

    private final String TAG = this.getClass().getName();
    private WeakReference<MatrimonyProfileDetailsActivity> fragmentWeakReference;

    public MatrimonyProfilesDetailsActivityPresenter(MatrimonyProfileDetailsActivity tmFiltersListActivity) {
        fragmentWeakReference = new WeakReference<>(tmFiltersListActivity);
    }

    public void markAttendanceRequest(JSONObject requestObject, int position, String requestType) {
        MatrimonyProfileDetailRequestCall requestCall = new MatrimonyProfileDetailRequestCall();
        requestCall.setListener(this);

        requestCall.approveRejectRequest(requestObject, position, requestType);
    }

    @Override
    public void onRequestStatusChanged(String response, int position, String requestType) {
        fragmentWeakReference.get().updateRequestStatus(response, position, requestType);
    }

    @Override
    public void onFailureListener(String message) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError volleyError) {

        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            String message = error.getMessage();
            Log.i(TAG, "Error: " + message);
        }

        //fragmentWeakReference.get().hideProgressBar();
    }


    //---------Approve Reject Request-----------

    public JSONObject createBodyParams(String meetid, String userid, String approval_type) {
        JSONObject requestObject = new JSONObject();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson("");
        Log.d("JsonObjRequestfilter", "SubmitRequest: " + json);

        try {
            requestObject.put("meet_id", meetid);
            requestObject.put("type", approval_type);
            requestObject.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return requestObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void userAction(String paramjson) {
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String url = BuildConfig.BASE_URL + Urls.Matrimony.BLOCK_UNBLOCK_USER;
        requestCall.postDataApiCall("BLOCK_UNBLOCK_USER", paramjson, url);
    }

    public void approveRejectRequest(String paramjson) {
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String url = BuildConfig.BASE_URL + Urls.Matrimony.USER_APPROVAL_API;
        requestCall.postDataApiCall("APPROVE_REJECT_USER", paramjson, url);
    }

    public void approveRejectDocumentsRequest(String paramjson, int type) {

        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        String url = BuildConfig.BASE_URL + Urls.Matrimony.USER_DOC_VERIFY_API;
        if (type ==3){
            requestCall.postDataApiCall("VERIFY_PROFILE", paramjson, url);
        }
        else if (type==1) {
            requestCall.postDataApiCall("APPROVE_REJECT_ID", paramjson, url);
        }else {
            requestCall.postDataApiCall("APPROVE_REJECT_EDU", paramjson, url);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        fragmentWeakReference.get().hideProgressBar();
        fragmentWeakReference.get().onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        fragmentWeakReference.get().hideProgressBar();
        fragmentWeakReference.get().onFailureListener(requestID, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (requestID.equalsIgnoreCase("GET_PROFILE_DETAILS")) {
            ProfileDetailResponse profileResponse = new Gson().fromJson(response, ProfileDetailResponse.class);
            fragmentWeakReference.get().onProfileReceived(response);
        }else {
        CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
        if (requestID.equals("BLOCK_UNBLOCK_USER")) {
            if (commonResponse.getStatus() == 200) {
                fragmentWeakReference.get().updateBlockUnblock(commonResponse.getMessage());
            } else {
                fragmentWeakReference.get().onFailureListener(requestID, commonResponse.getMessage());
            }
        } else if (requestID.equals("APPROVE_REJECT_USER")) {
            fragmentWeakReference.get().updateRequestStatus(response);
        } else if (requestID.equals("APPROVE_REJECT_ID")) {
            fragmentWeakReference.get().updateVerificationStatus(1, commonResponse.getMessage());
        } else if (requestID.equals("APPROVE_REJECT_EDU")) {
            fragmentWeakReference.get().updateVerificationStatus(2, commonResponse.getMessage());
        } else if (requestID.equals("VERIFY_PROFILE")) {
            fragmentWeakReference.get().updateVerificationStatus(3, commonResponse.getMessage());
        }else if(requestID.equalsIgnoreCase(GET_MATRIMONY_MEETS)){
            AllMatrimonyMeetsAPIResponse allMeets = PlatformGson.getPlatformGsonInstance().fromJson(response,
                    AllMatrimonyMeetsAPIResponse.class);
            if(allMeets.getStatus() == 200){
                //earliestMeetId will never be null. If no value available at backend it will send empty string.
                fragmentWeakReference.get().setMatrimonyMeets(allMeets.getData(), allMeets.getEarliestMeetId());
            } else {
                fragmentWeakReference.get().onFailureListener(GET_MATRIMONY_MEETS,allMeets.getMessage());
            }
        } else if (requestID.equalsIgnoreCase("CHECK_USER_CREATED")) {
            try {
                CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
                fragmentWeakReference.get().showResponseRegisterUser(responseOBJ.getMessage(), responseOBJ.getStatus());
            } catch (Exception e) {
                Log.e("TAG", "Exception");
            }
        }
    }

    }


    public void getProfile(String id, String meetId) {
        fragmentWeakReference.get().showProgressBar();
        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
        if (!TextUtils.isEmpty(meetId) && meetId.length() > 0) {
            params.put("meet_id", meetId);
        }
        String bodyParams = new Gson().toJson(params);
        final String url = BuildConfig.BASE_URL + String.format(Urls.Matrimony.GET_PROFILE_DETAILS);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("GET_PROFILE_DETAILS", bodyParams, url);
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
                + String.format(Urls.Matrimony.MATRIMONY_MEETS_UPCOMING);
        Log.d("getMatrimonyMeetsUrl: ", getMatrimonyMeetsUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall(GET_MATRIMONY_MEETS, paramjson, getMatrimonyMeetsUrl);
    }

    public void RegisterUserInMeet(String mobilenumber,String userId, String meetId) {
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
}
