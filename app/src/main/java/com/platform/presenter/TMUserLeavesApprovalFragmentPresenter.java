package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.TMUserProfileApprovalRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.TMUserLeavesApprovalRequestsResponse;
import com.platform.request.TMUserAttendenceApprovalRequestCall;
import com.platform.request.TMUserLeavesApprovalRequestCall;
import com.platform.view.fragments.TMUserLeavesApprovalFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class TMUserLeavesApprovalFragmentPresenter implements TMUserProfileApprovalRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserLeavesApprovalFragment> fragmentWeakReference;

    public TMUserLeavesApprovalFragmentPresenter(TMUserLeavesApprovalFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests(JSONObject requestObject) {
        TMUserLeavesApprovalRequestCall requestCall = new TMUserLeavesApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests(requestObject);
    }

    public void approveRejectRequest(String requestStatus, int position) {
        TMUserLeavesApprovalRequestCall requestCall = new TMUserLeavesApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, position);
    }
    /*
    public void approveRejectRequest(String requestStatus, int position) {
    TMUserAttendenceApprovalRequestCall requestCall = new TMUserAttendenceApprovalRequestCall();
    requestCall.setListener(this);

    //fragmentWeakReference.get().showProgressBar();
    requestCall.approveRejectRequest(requestStatus,requestObject,position);
    }
    */

    @Override
    public void TMUserProfileApprovalRequestsFetched(String response) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            TMUserLeavesApprovalRequestsResponse pendingRequestsResponse
                    = new Gson().fromJson(response, TMUserLeavesApprovalRequestsResponse.class);
            if (pendingRequestsResponse != null && pendingRequestsResponse.getData() != null
                    && !pendingRequestsResponse.getData().getApplication().isEmpty()
                    && pendingRequestsResponse.getData().getApplication().size() > 0) {
                fragmentWeakReference.get().showFetchedUserProfileForApproval(pendingRequestsResponse.getData().getApplication());
            }
        }
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
        //   fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            //fragmentWeakReference.get().updateRequestStatus(response, pendingRequest);
        }
    }

    @Override
    public void onRequestStatusChanged(String response, int position) {
        //   fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            fragmentWeakReference.get().updateRequestStatus(response, position);
        }
    }

    @Override
    public void onFailureListener(String message) {
        //     fragmentWeakReference.get().hideProgressBar();
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

        //    fragmentWeakReference.get().hideProgressBar();
    }
}
