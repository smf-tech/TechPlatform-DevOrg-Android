package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.TMPendingRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.PendingRequestsResponse;
import com.platform.request.TMPendingRequestCall;
import com.platform.view.fragments.NotificationsFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class NotificationsFragmentPresenter implements TMPendingRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<NotificationsFragment> fragmentWeakReference;

    public NotificationsFragmentPresenter(NotificationsFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests() {
        TMPendingRequestCall requestCall = new TMPendingRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests();
    }

    @Override
    public void onPendingRequestsFetched(String response) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
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
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();

            if (!TextUtils.isEmpty(response)) {
                fragmentWeakReference.get().updateRequestStatus(response, pendingRequest);
            }
        }
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

    @SuppressWarnings("ThrowableNotThrown")
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
