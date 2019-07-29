package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.TMUserProfileApprovalRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.TMLandingPageRequestsResponse;
import com.platform.models.tm.TMUserAttendanceApprovalRequestsResponse;
import com.platform.request.TMUserAttendenceApprovalRequestCall;
import com.platform.request.TMUserLeavesApprovalRequestCall;
import com.platform.request.TMUserProfileApprovalRequestCall;
import com.platform.utility.Util;
import com.platform.view.fragments.TMUserAttendanceApprovalFragment;
import com.platform.view.fragments.TMUserProfileApprovalFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class TMUserAttendenceApprovalFragmentPresenter implements TMUserProfileApprovalRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserAttendanceApprovalFragment> fragmentWeakReference;

    public TMUserAttendenceApprovalFragmentPresenter(TMUserAttendanceApprovalFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests(JSONObject requestObject) {
        TMUserAttendenceApprovalRequestCall requestCall = new TMUserAttendenceApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests(requestObject);
    }

    public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        TMUserAttendenceApprovalRequestCall requestCall = new TMUserAttendenceApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }
    public void approveRejectRequest(String requestStatus, int position) {
        TMUserAttendenceApprovalRequestCall requestCall = new TMUserAttendenceApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, position);
    }



    @Override
    public void TMUserProfileApprovalRequestsFetched(String response) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            TMUserAttendanceApprovalRequestsResponse pendingRequestsResponse
                    = new Gson().fromJson(response, TMUserAttendanceApprovalRequestsResponse.class);
            if (pendingRequestsResponse != null && pendingRequestsResponse.getData() != null
                    && !pendingRequestsResponse.getData().isEmpty()
                    && pendingRequestsResponse.getData().size() > 0) {
                fragmentWeakReference.get().showFetchedUserProfileForApproval(pendingRequestsResponse.getData());
            }
        }
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
     //   fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
         //   fragmentWeakReference.get().updateRequestStatus(response, pendingRequest);
        }
    }

    @Override
    public void onRequestStatusChanged(String response, int position) {
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
