package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.listeners.TMUserProfileApprovalRequestCallListener;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.models.tm.TMUserProfileApprovalRequestsResponse;
import com.octopusbjsindia.request.TMUserProfileApprovalRequestCall;
import com.octopusbjsindia.view.fragments.TMUserProfileApprovalFragment;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class TMUserProfileApprovalFragmentPresenter implements TMUserProfileApprovalRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserProfileApprovalFragment> fragmentWeakReference;

    public TMUserProfileApprovalFragmentPresenter(TMUserProfileApprovalFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests(JSONObject requestObject) {
        TMUserProfileApprovalRequestCall requestCall = new TMUserProfileApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests(requestObject);
    }

    public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        TMUserProfileApprovalRequestCall requestCall = new TMUserProfileApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }
    public void approveRejectRequest(String requestStatus, int position) {
        TMUserProfileApprovalRequestCall requestCall = new TMUserProfileApprovalRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, position);
    }




    @Override
    public void TMUserProfileApprovalRequestsFetched(String response) {
        //fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            TMUserProfileApprovalRequestsResponse pendingRequestsResponse
                    = new Gson().fromJson(response, TMUserProfileApprovalRequestsResponse.class);
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
