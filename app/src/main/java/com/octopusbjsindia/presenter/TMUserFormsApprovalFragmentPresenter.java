package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.listeners.TMUserProfileApprovalRequestCallListener;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.models.tm.TMUserFormsApprovalRequest;
import com.octopusbjsindia.request.TMUserFormsApprovalRequestCall;
import com.octopusbjsindia.view.fragments.TMUserFormsApprovalFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class TMUserFormsApprovalFragmentPresenter implements TMUserProfileApprovalRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserFormsApprovalFragment> fragmentWeakReference;

    public TMUserFormsApprovalFragmentPresenter(TMUserFormsApprovalFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests(JSONObject requestObject) {
        TMUserFormsApprovalRequestCall requestCall = new TMUserFormsApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests(requestObject);
    }

    public void approveRejectRequest(String requestStatus, int position) {
        TMUserFormsApprovalRequestCall requestCall = new TMUserFormsApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, position);
    }




    @Override
    public void TMUserProfileApprovalRequestsFetched(String response) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            TMUserFormsApprovalRequest pendingRequestsResponse
                    = new Gson().fromJson(response, TMUserFormsApprovalRequest.class);
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
