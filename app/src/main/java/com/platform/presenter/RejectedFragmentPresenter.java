package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.TMRejectedRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.PendingRequestsResponse;
import com.platform.request.TMRejectedRequestCall;
import com.platform.view.fragments.TMUserRejectedFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class RejectedFragmentPresenter implements TMRejectedRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserRejectedFragment> fragmentWeakReference;

    public RejectedFragmentPresenter(TMUserRejectedFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllRejectedRequests() {
        TMRejectedRequestCall requestCall = new TMRejectedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllRejectedRequests();
    }

    @Override
    public void onRejectedRequestsFetched(String response) {
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
        TMRejectedRequestCall requestCall = new TMRejectedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

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
