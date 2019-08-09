package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.APIPresenterListener;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.models.profile.JurisdictionLevelResponse;
import com.platform.request.MatrimonyMeetRequestCall;
import com.platform.request.ProfileRequestCall;
import com.platform.utility.Constants;
import com.platform.view.activities.CreateMatrimonyMeetActivity;

import java.lang.ref.WeakReference;

public class MatrimonyMeetPresenter implements APIPresenterListener {

    private WeakReference<APIDataListener> fragmentWeakReference;

    public static final String GET_MATRIMONY_MEET_TYPES ="getMatrimonyMeetTypes";
    public static final String GET_STATES ="getStates";
    public static final String GET_DISTRICTS ="getDistricts";

    public MatrimonyMeetPresenter(APIDataListener tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getMeetTypes(){
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getMatrimonyMeetTypes(GET_MATRIMONY_MEET_TYPES);
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);

        fragmentWeakReference.get().showProgressBar();
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName, GET_STATES);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)){
            requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName, GET_DISTRICTS);
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
                fragmentWeakReference.get().onSuccessListener(requestID, response);
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
