package com.octopusbjsindia.presenter;


import com.android.volley.VolleyError;
import com.octopusbjsindia.listeners.LeaveDataListener;
import com.octopusbjsindia.listeners.LeavePresenterListener;
import com.octopusbjsindia.models.leaves.LeaveData;
import com.octopusbjsindia.request.LeavesRequestCall;

import java.lang.ref.WeakReference;

public class LeavesPresenter implements LeavePresenterListener {

    private WeakReference<LeaveDataListener> fragmentWeakReference;

    public static final String GET_USER_LEAVE_DETAILS ="getUsersAllLeavesDetails";
    public static final String GET_LEAVE_BALANCE ="getLeavesBalance";
    public static final String POST_USER_DETAILS ="postUserLeave";
    public static final String REQUEST_USER_COMPOFF ="requestUserCompoff";
    public static final String DELETE_LEAVE ="deleteUserLeave";
    public static final String HOLIDAY_LIST ="holiday_list";

    public LeavesPresenter(LeaveDataListener tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);

    }

    public void clearData() {
        fragmentWeakReference = null;
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

    public void requestCompOff(LeaveData leaveData) {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.requestUserCompoff(POST_USER_DETAILS,leaveData);
    }

    public void deleteUserLeave(String leaveId) {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.deleteUserLeave(DELETE_LEAVE, leaveId);
    }

    public void getHolidayList() {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.getHolidayList(HOLIDAY_LIST);
    }

    public void getLeavesBalance() {
        LeavesRequestCall requestCall = new LeavesRequestCall();
        requestCall.setLeavePresenterListener(this);
        requestCall.getLeavesBalance(GET_LEAVE_BALANCE);
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
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
