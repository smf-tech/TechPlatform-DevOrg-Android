package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.TMApprovedRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.PendingRequestsResponse;
import com.platform.request.TMApprovedRequestCall;
import com.platform.view.fragments.TMUserApprovedFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class ApprovedFragmentPresenter implements TMApprovedRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserApprovedFragment> fragmentWeakReference;

    public ApprovedFragmentPresenter(TMUserApprovedFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllApprovedRequests() {
        TMApprovedRequestCall requestCall = new TMApprovedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllApprovedRequests();
    }

    @Override
    public void onApprovedRequestsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            PendingRequestsResponse pendingRequestsResponse
                    = new Gson().fromJson(response, PendingRequestsResponse.class);

            if (pendingRequestsResponse != null && pendingRequestsResponse.getData() != null
                    && !pendingRequestsResponse.getData().isEmpty()
                    && pendingRequestsResponse.getData().size() > 0) {
                fragmentWeakReference.get().showPendingRequests(pendingRequestsResponse.getData());
            }
        }
    }

    public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        TMApprovedRequestCall requestCall = new TMApprovedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

    }

    @Override
    public void onFailureListener(String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
        }
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

        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
        }
    }
}
