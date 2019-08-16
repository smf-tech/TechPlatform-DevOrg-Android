package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.models.leaves.LeaveData;
import com.platform.request.MatrimonyMeetRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Urls;
import com.platform.view.fragments.CreateMeetSecondFragment;

import java.lang.ref.WeakReference;

public class CreateMeetSecondFragmentPresenter implements APIPresenterListener {
    private WeakReference<CreateMeetSecondFragment> fragmentWeakReference;

    public static final String GET_MEET_ORGANIZERS_LIST ="getMeetOrganizersList";
    public static final String GET_MEET_REFERENCES_LIST ="getMeetReferncesList";
    public static final String SUBMIT_MEET ="submitMeet";
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

    public void getMeetOrganizersList(){
        final String getMatrimonyMeetTypesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MEET_ORGANIZERS_LIST);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMatrimonyMeetTypesUrl);
        MatrimonyMeetRequestCall requestCall = new MatrimonyMeetRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_MEET_ORGANIZERS_LIST, getMatrimonyMeetTypesUrl);
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

            }
        }catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
