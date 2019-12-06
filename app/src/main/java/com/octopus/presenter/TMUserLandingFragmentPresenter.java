package com.octopus.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopus.listeners.TMUserLandingRequestCallListener;
import com.octopus.models.tm.PendingRequest;
import com.octopus.models.tm.TMLandingPageRequestsResponse;
import com.octopus.request.TMUserLandingRequestCall;
import com.octopus.view.fragments.TMUserLandingFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class TMUserLandingFragmentPresenter implements TMUserLandingRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserLandingFragment> fragmentWeakReference;

    public TMUserLandingFragmentPresenter(TMUserLandingFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests() {
        TMUserLandingRequestCall requestCall = new TMUserLandingRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests();
    }

    public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        TMUserLandingRequestCall requestCall = new TMUserLandingRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }



    @Override
    public void onTMUserLandingRequestsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            TMLandingPageRequestsResponse pendingRequestsResponse
                    = new Gson().fromJson(response, TMLandingPageRequestsResponse.class);
            if (pendingRequestsResponse != null && pendingRequestsResponse.getData() != null
                    && !pendingRequestsResponse.getData().isEmpty()
                    && pendingRequestsResponse.getData().size() > 0) {
                fragmentWeakReference.get().showLandingPageRequests(pendingRequestsResponse.getData());
            }
        }
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            fragmentWeakReference.get().updateRequestStatus(response, pendingRequest);
        }
    }

    @Override
    public void onFailureListener(String message) {
        fragmentWeakReference.get().hideProgressBar();
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

        fragmentWeakReference.get().hideProgressBar();
    }
}
