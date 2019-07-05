package com.platform.presenter;


import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.platform.listeners.LeaveDataListener;
import com.platform.listeners.LeavePresenterListener;
import com.platform.models.leaves.LeaveData;
import com.platform.request.LeavesRequestCall;

import java.lang.ref.WeakReference;

public class LeavesPresenter implements LeavePresenterListener {

    private WeakReference<LeaveDataListener> fragmentWeakReference;

    public static final String GET_USER_LEAVE_DETAILS ="getUsersAllLeavesDetails";
    public static final String GET_LEAVE_DETAILS ="getLeavesData";
    public static final String POST_USER_DETAILS ="postUserLeave";
    public static final String DELETE_USER_LEAVE ="deleteUserLeave";

    public LeavesPresenter(LeaveDataListener tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);

    }

    public void clearData() {
        fragmentWeakReference = null;
    }



    public void getLeavesData() {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.getLeavesData(GET_LEAVE_DETAILS);
    }

    public void getUsersAllLeavesDetails(String year, String month) {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.getUsersAllLeavesDetails(GET_USER_LEAVE_DETAILS, year, month);
    }

    public void postUserLeave(LeaveData leaveData) {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.postUserLeave(POST_USER_DETAILS,leaveData);
    }

    public void deleteUserLeave(String leaveId) {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.deleteUserLeave(DELETE_USER_LEAVE, leaveId);
    }

    @Override
    public void onFailureListener(String requestID,String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID,VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID,error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID,String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        try {
            if (response != null) {
                fragmentWeakReference.get().onSuccessListener(requestID, response);
                //fragmentWeakReference.get().closeCurrentActivity();
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
